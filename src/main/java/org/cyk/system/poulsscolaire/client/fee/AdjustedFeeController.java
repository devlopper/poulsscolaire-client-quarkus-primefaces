package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneBooleanRadio;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeService;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeService.AdjustedFeeCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeService.AdjustedFeeUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;

/**
 * Cette classe représente le contrôleur de {@link AdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AdjustedFeeController extends AbstractController {

  @Inject
  AdjustedFeeClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  FeeClient feeClient;

  @Getter
  @Setter
  String amountValueTotalAsString;

  @Getter
  @Setter
  String amountValuePaidTotalAsString;

  @Getter
  @Setter
  String amountValuePayableTotalAsString;

  @Getter
  @Setter
  String amountRegistrationValuePartTotalAsString;

  @Getter
  @Setter
  List<SelectItem> fees;

  @Inject
  RegistrationClient registrationClient;

  @Getter
  @Setter
  List<SelectItem> registrations;

  @Inject
  DeadlineClient deadlineClient;

  @Getter
  @Setter
  List<SelectItem> deadlines;

  @Inject
  @Getter
  AdjustedFeeFilterController filterController;

  @Getter
  SelectOneBooleanRadio amountValuePayableEqualsZeroSelectOneRadio;

  @Getter
  SelectOneBooleanRadio amountDeadlineDateOverSelectOneRadio;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AdjustedFeeDto.NAME;
    amountValuePayableEqualsZeroSelectOneRadio = new SelectOneBooleanRadio();
    amountValuePayableEqualsZeroSelectOneRadio.outputLabel().setValue("Soldé");
    amountValuePayableEqualsZeroSelectOneRadio.addTrueOrFalseChoices(true)
        .addValueConsumer(value -> filterController.getFilter()
            .setAmountValuePayableEqualsZero(value));

    amountDeadlineDateOverSelectOneRadio = new SelectOneBooleanRadio().addTrueOrFalseChoices(true);
    amountDeadlineDateOverSelectOneRadio.outputLabel().setValue("En retard");
    amountDeadlineDateOverSelectOneRadio.addValueConsumer(value -> filterController.getFilter()
        .setAmountDeadlineDateOver(value));
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(AdjustedFeeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AdjustedFeeService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    if (Core.isStringBlank(filterController.getFilter().getRegistrationIdentifier())) {
      projection.addNames(AdjustedFeeDto.JSON_REGISTRATION_AS_STRING);
    }
    if (filterController.getFilter().getAmountOptional() == null) {
      projection.addNames(AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL_AS_STRING);
    } else if (Boolean.FALSE.equals(filterController.getFilter().getAmountOptional())) {
      projection.addNames(AdjustedFeeDto.JSON_AMOUNT_VALUE_PAID_AS_STRING,
          AdjustedFeeDto.JSON_AMOUNT_VALUE_PAYABLE,
          AdjustedFeeDto.JSON_AMOUNT_VALUE_PAYABLE_AS_STRING,
          AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER_AS_STRING,
          AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_OVER);
    }

    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, AdjustedFeeDto.JSON_FEE_AS_STRING,
        AdjustedFeeDto.JSON_FEE_OPTIONAL, AbstractAmountContainerDto.JSON_AMOUNT_VALUE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> ((AdjustedFeeDto) entity)
        .setRegistrationIdentifier(filterController.getFilter().getRegistrationIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      AdjustedFeeCreateRequestDto request = new AdjustedFeeCreateRequestDto();
      request.setFeeIdentifier(((AdjustedFeeDto) entity).getFeeIdentifier());
      request.setRegistrationIdentifier(((AdjustedFeeDto) entity).getRegistrationIdentifier());
      request.setValue(((AdjustedFeeDto) entity).getAmountValue());
      request.setDeadlineIdentifier(((AdjustedFeeDto) entity).getAmountDeadlineIdentifier());
      request.setRegistrationValuePart(((AdjustedFeeDto) entity).getAmountRegistrationValuePart());
      request.setOptional(((AdjustedFeeDto) entity).getAmountOptional());
      request.setPaymentOrderNumber(((AdjustedFeeDto) entity).getAmountPaymentOrderNumber());
      request.setRenewable(((AdjustedFeeDto) entity).getAmountRenewable());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      AdjustedFeeUpdateRequestDto request = new AdjustedFeeUpdateRequestDto();
      request.setIdentifier(((AdjustedFeeDto) entity).getIdentifier());
      request.setFeeIdentifier(((AdjustedFeeDto) entity).getFeeIdentifier());
      request.setRegistrationIdentifier(
          Optional.ofNullable(filterController.getFilter().getRegistrationIdentifier())
              .orElse(((AdjustedFeeDto) entity).getRegistrationIdentifier()));
      request.setValue(((AdjustedFeeDto) entity).getAmountValue());
      request.setDeadlineIdentifier(((AdjustedFeeDto) entity).getAmountDeadlineIdentifier());
      request.setRegistrationValuePart(((AdjustedFeeDto) entity).getAmountRegistrationValuePart());
      request.setOptional(((AdjustedFeeDto) entity).getAmountOptional());
      request.setPaymentOrderNumber(((AdjustedFeeDto) entity).getAmountPaymentOrderNumber());
      request.setRenewable(((AdjustedFeeDto) entity).getAmountRenewable());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AdjustedFeeDto.JSON_REGISTRATION_IDENTIFIER, AdjustedFeeDto.JSON_FEE_IDENTIFIER,
            AbstractAmountContainerDto.JSON_AMOUNT_VALUE,
            AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART,
            AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL,
            AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE,
            AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_IDENTIFIER,
            AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER));

    fees = new ActionExecutor<>(this, FeeService.GET_MANY_IDENTIFIER, () -> feeClient
        .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FeeDto.JSON_CATEGORY_AS_STRING), null, null, userIdentifier, null)
        .getDatas().stream()
        .map(dto -> new SelectItem(dto.getIdentifier(), dto.getCategoryAsString())).toList())
            .execute();

    if (Core.isStringBlank(filterController.getFilter().getRegistrationIdentifier())) {
      registrations =
          new ActionExecutor<>(this, RegistrationService.GET_MANY_IDENTIFIER,
              () -> registrationClient
                  .getMany(
                      new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                          AbstractIdentifiableCodableDto.JSON_CODE),
                      null, null, userIdentifier, null)
                  .getDatas().stream()
                  .map(dto -> new SelectItem(dto.getIdentifier(), dto.getCode())).toList())
                      .execute();
    }

    deadlines = new ActionExecutor<>(this, DeadlineService.GET_MANY_IDENTIFIER,
        () -> deadlineClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();

    amountValueTotalAsString = "---";
    amountRegistrationValuePartTotalAsString = "---";
    amountValuePaidTotalAsString = "---";
    amountValuePayableTotalAsString = "---";
  }
}
