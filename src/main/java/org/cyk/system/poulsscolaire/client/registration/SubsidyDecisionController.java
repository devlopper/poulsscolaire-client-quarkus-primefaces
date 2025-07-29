package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolingSelectOneController;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService.SubsidyDecisionCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService.SubsidyDecisionUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionController extends AbstractController {

  @Inject
  SubsidyDecisionClient client;

  @Inject
  SubsidyDecisionRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  InputTextController codeInputTextController;

  @Inject
  @Getter
  SchoolingSelectOneController schoolingSelectOneController;

  @Inject
  @Getter
  InputNumberController amountInputNumberController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = SubsidyDecisionDto.NAME;
  }

  /**
   * Initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SubsidyDecisionDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SubsidyDecisionService.PATH);

    ProjectionDto projection = new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, SubsidyDecisionDto.JSON_SCHOOLING_AS_STRING,
        SubsidyDecisionDto.JSON_REGISTRATION_COUNT_AS_STRING,
        SubsidyDecisionDto.JSON_AMOUNT_AS_STRING, SubsidyDecisionDto.JSON_PAYMENT_COUNT_AS_STRING,
        SubsidyDecisionDto.JSON_PAID_AMOUNT_AS_STRING,
        SubsidyDecisionDto.JSON_REMAINING_AMOUNT_TO_PAY_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(SubsidyDecisionReadPaymentsPage.OUTCOME);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      SubsidyDecisionCreateRequestDto request =
          requestMapper.mapCreate((SubsidyDecisionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AbstractIdentifiableCodableDto.JSON_CODE, SubsidyDecisionDto.JSON_SCHOOLING_IDENTIFIER,
            SubsidyDecisionDto.JSON_AMOUNT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      codeInputTextController.getInputText().writeValue(((SubsidyDecisionDto) entity).getCode());
      schoolingSelectOneController.getSelectOneMenu()
          .writeValue(((SubsidyDecisionDto) entity).getSchoolingIdentifier());
      amountInputNumberController.getInputInteger()
          .writeValue(((SubsidyDecisionDto) entity).getAmount());
    });

    listController.getUpdateController().setFunction(entity -> {
      SubsidyDecisionUpdateRequestDto request =
          requestMapper.mapUpdate((SubsidyDecisionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    codeInputTextController.setOutputLableValue("Numéro");
    codeInputTextController.getInputText().addValueConsumer(code -> listController
        .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionDto.class).setCode(code));

    schoolingSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionDto.class)
            .setSchoolingIdentifier(identifier));
    schoolingSelectOneController.getSelectOneMenu().getOutputLabel().setValue("Branche");

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputInteger().addValueConsumer(amount -> listController
        .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionDto.class).setAmount(amount));
  }
}
