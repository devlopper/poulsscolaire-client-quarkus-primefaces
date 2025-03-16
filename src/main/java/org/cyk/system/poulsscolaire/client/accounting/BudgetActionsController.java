package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ScriptManager;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.DataTable.RecordComponentController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.crud.IdentifiableProcessingController;
import ci.gouv.dgbf.extension.server.service.api.request.ByFilterRequestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetStatus;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.ByFilterWithReasonRequestDto;

/**
 * Cette classe représente un contrôleur des actions de {@link BudgetDto}.
 *
 * @author Christian
 */
@Dependent
public class BudgetActionsController extends AbstractController {

  @Inject
  FundingClient client;

  @Inject
  BudgetClient budgetClient;
  
  @Inject
  @Getter
  IdentifiableProcessingController transmitProcessingController;

  @Inject
  @Getter
  IdentifiableProcessingController returnProcessingController;

  @Inject
  @Getter
  IdentifiableProcessingController acceptProcessingController;

  @Inject
  @Getter
  IdentifiableProcessingController approveProcessingController;

  FundingFilterController fundingFilterController;
  
  @Inject
  @Getter
  InputTextController statusReasonInputTextController;
  
  @Getter
  BudgetDto budget;

  @Getter
  @Setter
  String statusChangedEvent;

  @Inject
  ScriptManager scriptManager;

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    initializeConfirmation(transmitProcessingController, BudgetStatus.TRANSMITTED, "pi pi-send",
        "transmit", transmitFunction(), entity -> ((BudgetDto) entity).getTransmitable());
    transmitProcessingController.getController()
        .addResponseConsumer(response -> scriptManager.reloadPage());

    initializeConfirmation(acceptProcessingController, BudgetStatus.ACCEPTED, "pi pi-check",
        "accept", acceptFunction(), entity -> ((BudgetDto) entity).getAcceptable());

    initializeConfirmation(approveProcessingController, BudgetStatus.APPROVED, "pi pi-thumbs-up",
        "approve", approveFunction(), entity -> ((BudgetDto) entity).getApprovable());
    approveProcessingController.getController()
        .addResponseConsumer(response -> scriptManager.reloadPage());

    returnProcessingController.prepareDialog(BudgetDto.class, BudgetStatus.RETURNED.getAction(),
        "pi pi-arrow-left", returnFunction(), "returnByFilterForm", budgetClient);
    returnProcessingController.submitButton().setValue(BudgetStatus.RETURNED.getAction());
    returnProcessingController.submitButton()
        .setActionFunction(i -> returnProcessingController.getController().execute());
    returnProcessingController.submitButton()
        .addUpdate(CommandUpdatePropertyValueBuilder.formatObserver(statusChangedEvent));
    returnProcessingController.setShowDialogButtonController(new RecordComponentController());
    //returnProcessingController.getShowDialogButtonController()
    //    .setRenderedFunction(entity -> ((BudgetDto) entity).getReturnable());
    //addStatusUpdateResponseConsumer(returnProcessingController);
    returnProcessingController.getController()
        .addResponseConsumer(response -> scriptManager.reloadPage());
    returnProcessingController.showDialogButton().addStyleClass(buildButtonStyleClass("return"));
    returnProcessingController.initialize();
    
    statusReasonInputTextController.setOutputLableValue("Motif");
    statusReasonInputTextController.getInputTextarea().setRequired(true);
    statusReasonInputTextController.getInputTextarea().addValueConsumer(
        reason -> ((BudgetDto) returnProcessingController.getController().getEntity())
            .setStatusReason(reason));

    returnProcessingController.submitButton()
        .addUpdate(statusReasonInputTextController.getInputTextarea().getMessage().getIdentifier());
  }

  void initializeConfirmation(IdentifiableProcessingController identifiableProcessingController,
      BudgetStatus status, String icon, String styleClassSuffix,
      UnaryOperator<Object> processingFunction, Function<Object, Boolean> buttonRenderedFunction) {
    identifiableProcessingController.prepareConfirmation(BudgetDto.class, status.getAction(), icon,
        processingFunction);
    identifiableProcessingController.setSubmitButtonController(new RecordComponentController());
    //identifiableProcessingController.getSubmitButtonController()
    //    .setRenderedFunction(buttonRenderedFunction);
    //addStatusUpdateResponseConsumer(identifiableProcessingController);
    identifiableProcessingController.submitButton()
        .addStyleClass(buildButtonStyleClass(styleClassSuffix));
    identifiableProcessingController.submitButton()
        .addUpdates(CommandUpdatePropertyValueBuilder.formatObserver(statusChangedEvent));
    identifiableProcessingController.initialize();
  }

  String buildButtonStyleClass(String styleClassSuffix) {
    return "budget-action-%s".formatted(styleClassSuffix);
  }

  UnaryOperator<Object> transmitFunction() {
    return identifier -> {
      ByFilterRequestDto request = new ByFilterRequestDto();
      request.setFilter(fundingFilterController.getFilter().toDto());
      request.setAuditWho(userIdentifier);
      return client.transmitByFilter(request);
    };
  }

  UnaryOperator<Object> returnFunction() {
    return entity -> {
      ByFilterWithReasonRequestDto request = new ByFilterWithReasonRequestDto();
      request.setFilter(fundingFilterController.getFilter().toDto());
      request.setReason(((BudgetDto) entity).getStatusReason());
      request.setAuditWho(userIdentifier);
      return client.returnBackByFilter(request);
    };
  }

  UnaryOperator<Object> acceptFunction() {
    return identifier -> {
      ByFilterRequestDto request = new ByFilterRequestDto();
      request.setFilter(fundingFilterController.getFilter().toDto());
      request.setAuditWho(userIdentifier);
      return client.acceptByFilter(request);
    };
  }

  UnaryOperator<Object> approveFunction() {
    return identifier -> {
      ByFilterRequestDto request = new ByFilterRequestDto();
      request.setFilter(fundingFilterController.getFilter().toDto());
      request.setAuditWho(userIdentifier);
      return client.approveByFilter(request);
    };
  }
}
