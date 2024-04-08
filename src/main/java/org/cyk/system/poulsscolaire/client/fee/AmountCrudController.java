package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountClient;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDto;
import org.cyk.system.poulsscolaire.server.api.fee.AmountService;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link AmountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AmountCrudController extends AbstractController {

  @Inject
  AmountClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  DeadlineClient deadlineClient;

  @Getter
  @Setter
  List<SelectItem> deadlines;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Groupe d'échéances";

    listController.setEntityClass(AmountDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AmountService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, AmountDto.JSON_VALUE_AS_STRING,
        AmountDto.JSON_REGISTRATION_VALUE_PART_AS_STRING, AmountDto.JSON_OPTIONAL_AS_STRING,
        AmountDto.JSON_RENEWABLE_AS_STRING, AmountDto.JSON_DEADLINE_AS_STRING,
        AmountDto.JSON_PAYMENT_ORDER_NUMBER_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((AmountDto) entity).getValue(),
            ((AmountDto) entity).getRegistrationValuePart(), ((AmountDto) entity).getOptional(),
            ((AmountDto) entity).getPaymentOrderNumber(), ((AmountDto) entity).getRenewable(),
            ((AmountDto) entity).getDeadlineIdentifier(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((AmountDto) entity).getIdentifier(),
            ((AmountDto) entity).getValue(), ((AmountDto) entity).getRegistrationValuePart(),
            ((AmountDto) entity).getOptional(), ((AmountDto) entity).getPaymentOrderNumber(),
            ((AmountDto) entity).getRenewable(), ((AmountDto) entity).getDeadlineIdentifier(),
            userIdentifier, null));

    listController.setUpdateProjection(new ProjectionDto().addNames(
        AbstractIdentifiableDto.JSON_IDENTIFIER, AmountDto.JSON_VALUE,
        AmountDto.JSON_REGISTRATION_VALUE_PART, AmountDto.JSON_OPTIONAL, AmountDto.JSON_RENEWABLE,
        AmountDto.JSON_DEADLINE_IDENTIFIER, AmountDto.JSON_PAYMENT_ORDER_NUMBER));

    deadlines = new ActionExecutor<>(this, DeadlineService.GET_MANY_IDENTIFIER,
        () -> deadlineClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}
