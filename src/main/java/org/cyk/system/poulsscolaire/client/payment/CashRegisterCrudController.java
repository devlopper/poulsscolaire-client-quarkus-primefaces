package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolService;
import org.cyk.system.poulsscolaire.server.api.payment.CashRegisterClient;
import org.cyk.system.poulsscolaire.server.api.payment.CashRegisterDto;
import org.cyk.system.poulsscolaire.server.api.payment.CashRegisterService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link CashRegisterDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class CashRegisterCrudController extends AbstractController {

  @Inject
  CashRegisterClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  SchoolClient schoolClient;

  @Getter
  @Setter
  List<SelectItem> schools;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Modes de paiement";

    listController.setEntityClass(CashRegisterDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(CashRegisterService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        CashRegisterDto.JSON_SCHOOL_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((CashRegisterDto) entity).getCode(),
            ((CashRegisterDto) entity).getName(), ((CashRegisterDto) entity).getSchoolIdentifier(),
            userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((CashRegisterDto) entity).getIdentifier(),
            ((CashRegisterDto) entity).getCode(), ((CashRegisterDto) entity).getName(),
            ((CashRegisterDto) entity).getSchoolIdentifier(), userIdentifier, null));

    schools = new ActionExecutor<>(this, SchoolService.GET_MANY_IDENTIFIER,
        () -> schoolClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}
