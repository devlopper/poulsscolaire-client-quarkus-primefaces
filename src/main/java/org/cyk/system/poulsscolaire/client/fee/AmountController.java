package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputInteger;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneBooleanButton;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountClient;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDto;
import org.cyk.system.poulsscolaire.server.api.fee.AmountService;

/**
 * Cette classe représente le contrôleur de {@link AmountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AmountController extends AbstractController {

  @Inject
  AmountClient client;

  @Inject
  @Getter
  ListController listController;

  @Getter
  SelectOneBooleanButton optionalSelectOneBooleanButton;

  @Getter
  SelectOneBooleanButton renewableSelectOneBooleanButton;

  @Getter
  InputInteger valueInputInteger;

  @Getter
  InputInteger registrationPartInputInteger;

  @Getter
  InputInteger orderNumberInputInteger;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AmountDto.NAME;

    initialize();
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AmountDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AmountService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, AmountDto.JSON_VALUE_AS_STRING,
        AmountDto.JSON_REGISTRATION_VALUE_PART_AS_STRING, AmountDto.JSON_OPTIONAL_AS_STRING,
        AmountDto.JSON_RENEWABLE_AS_STRING, AmountDto.JSON_DEADLINE_AS_STRING,
        AmountDto.JSON_PAYMENT_ORDER_NUMBER_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(AmountReadPage.OUTCOME);
    
    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((AmountDto) entity).getValue(),
            ((AmountDto) entity).getRegistrationValuePart(),
            ((AmountDto) entity).getOptional(), ((AmountDto) entity).getPaymentOrderNumber(),
            ((AmountDto) entity).getRenewable(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((AmountDto) entity).getIdentifier(),
            ((AmountDto) entity).getValue(), ((AmountDto) entity).getRegistrationValuePart(),
            ((AmountDto) entity).getOptional(), ((AmountDto) entity).getPaymentOrderNumber(),
            ((AmountDto) entity).getRenewable(), userIdentifier, null));

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AmountDto.JSON_VALUE, AmountDto.JSON_REGISTRATION_VALUE_PART, AmountDto.JSON_OPTIONAL,
            AmountDto.JSON_RENEWABLE, AmountDto.JSON_PAYMENT_ORDER_NUMBER));

    optionalSelectOneBooleanButton = new SelectOneBooleanButton();
    optionalSelectOneBooleanButton.outputLabel().setValue("Facultatif");
    optionalSelectOneBooleanButton.addValueConsumer(
        value -> ((AmountDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setOptional(Optional.ofNullable(value).orElse(false)));

    renewableSelectOneBooleanButton = new SelectOneBooleanButton();
    renewableSelectOneBooleanButton.outputLabel().setValue("Reconductible");
    renewableSelectOneBooleanButton.addValueConsumer(
        value -> ((AmountDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setRenewable(Optional.ofNullable(value).orElse(false)));

    valueInputInteger = new InputInteger();
    valueInputInteger.outputLabel().setValue("Valeur");
    valueInputInteger.addValueConsumer(
        value -> ((AmountDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setValue(Optional.ofNullable(value).orElse(0)));

    registrationPartInputInteger = new InputInteger();
    registrationPartInputInteger.outputLabel().setValue("Inscription");
    registrationPartInputInteger.addValueConsumer(
        value -> ((AmountDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setRegistrationValuePart(Optional.ofNullable(value).orElse(0)));

    orderNumberInputInteger = new InputInteger();
    orderNumberInputInteger.outputLabel().setValue("Numéro d'ordre de paiement");
    orderNumberInputInteger.addValueConsumer(
        value -> ((AmountDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setPaymentOrderNumber(Optional.ofNullable(value).orElse(0)));
  }
}
