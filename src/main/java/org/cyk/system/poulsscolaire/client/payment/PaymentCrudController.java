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
import org.cyk.system.poulsscolaire.server.api.payment.PaymentClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeService;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentService;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link PaymentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentCrudController extends AbstractController {

  @Inject
  PaymentClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  RegistrationClient registrationClient;

  @Getter
  @Setter
  List<SelectItem> registrations;

  @Inject
  PaymentModeClient modeClient;

  @Getter
  @Setter
  List<SelectItem> modes;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = PaymentDto.NAME;

    listController.setEntityClass(PaymentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(PaymentService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, PaymentDto.JSON_MODE_AS_STRING,
        PaymentDto.JSON_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((PaymentDto) entity).getRegistrationIdentifier(),
            ((PaymentDto) entity).getModeIdentifier(), ((PaymentDto) entity).getAmount(),
            userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((PaymentDto) entity).getIdentifier(),
            ((PaymentDto) entity).getModeIdentifier(), ((PaymentDto) entity).getAmount(),
            userIdentifier, null));

    registrations = new ActionExecutor<>(this, RegistrationService.GET_MANY_IDENTIFIER,
        () -> registrationClient
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                AbstractIdentifiableCodableDto.JSON_CODE),
                null, null, userIdentifier, null)
            .getDatas().stream()
            .map(dto -> new SelectItem(dto.getIdentifier(),
                dto.getCode()))
            .toList()).execute();

    modes = new ActionExecutor<>(this, PaymentModeService.GET_MANY_IDENTIFIER,
        () -> modeClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}
