package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ScriptManager;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.DataTable.RecordComponentController;
import ci.gouv.dgbf.extension.primefaces.crud.IdentifiableProcessingController;
import ci.gouv.dgbf.extension.server.service.api.request.ByIdentifierRequestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService.BudgetReturnRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService.BudgetStatusUpdateResponseDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetStatus;

/**
 * Cette classe représente un contrôleur des actions de {@link BudgetDto}.
 *
 * @author Christian
 */
@Dependent
public class BudgetActionsController extends AbstractController {

  @Inject
  BudgetClient client;

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
        "pi pi-arrow-left", returnFunction(), "returnForm", client);
    returnProcessingController.submitButton().setValue(BudgetStatus.RETURNED.getAction());
    returnProcessingController.submitButton()
        .setActionFunction(i -> returnProcessingController.getController().execute());
    returnProcessingController.submitButton()
        .addUpdate(CommandUpdatePropertyValueBuilder.formatObserver(statusChangedEvent));
    returnProcessingController.setShowDialogButtonController(new RecordComponentController());
    returnProcessingController.getShowDialogButtonController()
        .setRenderedFunction(entity -> ((BudgetDto) entity).getReturnable());
    addStatusUpdateResponseConsumer(returnProcessingController);
    returnProcessingController.getController()
        .addResponseConsumer(response -> scriptManager.reloadPage());
    returnProcessingController.showDialogButton().addStyleClass(buildButtonStyleClass("return"));
    returnProcessingController.initialize();
  }

  void initializeConfirmation(IdentifiableProcessingController identifiableProcessingController,
      BudgetStatus status, String icon, String styleClassSuffix,
      UnaryOperator<Object> processingFunction, Function<Object, Boolean> buttonRenderedFunction) {
    identifiableProcessingController.prepareConfirmation(BudgetDto.class, status.getAction(), icon,
        processingFunction);
    identifiableProcessingController.setSubmitButtonController(new RecordComponentController());
    identifiableProcessingController.getSubmitButtonController()
        .setRenderedFunction(buttonRenderedFunction);
    addStatusUpdateResponseConsumer(identifiableProcessingController);
    identifiableProcessingController.submitButton()
        .addStyleClass(buildButtonStyleClass(styleClassSuffix));
    identifiableProcessingController.submitButton()
        .addUpdates(CommandUpdatePropertyValueBuilder.formatObserver(statusChangedEvent));
    identifiableProcessingController.initialize();
  }

  String buildButtonStyleClass(String styleClassSuffix) {
    return "triennial-program-action-%s".formatted(styleClassSuffix);
  }

  UnaryOperator<Object> transmitFunction() {
    return identifier -> {
      ByIdentifierRequestDto request = new ByIdentifierRequestDto();
      request.setIdentifier((String) identifier);
      request.setAuditWho(userIdentifier);
      return client.transmit(request);
    };
  }

  UnaryOperator<Object> returnFunction() {
    return entity -> {
      BudgetReturnRequestDto request = new BudgetReturnRequestDto();
      request.setIdentifier(((BudgetDto) entity).getIdentifier());
      request.setReason(((BudgetDto) entity).getStatusReason());
      request.setAuditWho(userIdentifier);
      return client.returnBack(request);
    };
  }

  UnaryOperator<Object> acceptFunction() {
    return identifier -> {
      ByIdentifierRequestDto request = new ByIdentifierRequestDto();
      request.setIdentifier((String) identifier);
      request.setAuditWho(userIdentifier);
      return client.accept(request);
    };
  }

  UnaryOperator<Object> approveFunction() {
    return identifier -> {
      ByIdentifierRequestDto request = new ByIdentifierRequestDto();
      request.setIdentifier((String) identifier);
      request.setAuditWho(userIdentifier);
      return client.approve(request);
    };
  }

  void addStatusUpdateResponseConsumer(
      IdentifiableProcessingController identifiableProcessingController) {
    identifiableProcessingController.getController()
        .addResponseConsumer(statusUpdateResponseConsumer());
  }

  Consumer<Object> statusUpdateResponseConsumer() {
    return response -> budget.copy((BudgetStatusUpdateResponseDto) response);
  }
}
