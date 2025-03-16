package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.DataTable.RecordComponentController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.crud.IdentifiableProcessingController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.request.ByIdentifierRequestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingReturnRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingStatus;

/**
 * Cette classe représente un contrôleur des actions de {@link FundingDto}.
 *
 * @author Christian
 */
@Dependent
public class FundingActionsController extends AbstractController {

  @Inject
  FundingClient client;

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
  @Setter
  String statusChangedEvent;
  
  @Inject
  @Getter
  InputTextController statusReasonInputTextController;

  ListController listController;

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    initializeConfirmation(transmitProcessingController, FundingStatus.TRANSMITTED, "pi pi-send",
        "transmit", transmitFunction(), entity -> ((FundingDto) entity).getTransmitable());    

    initializeConfirmation(acceptProcessingController, FundingStatus.ACCEPTED, "pi pi-check",
        "accept", acceptFunction(), entity -> ((FundingDto) entity).getAcceptable());
    
    initializeConfirmation(approveProcessingController, FundingStatus.APPROVED, "pi pi-thumbs-up",
        "approve", approveFunction(), entity -> ((FundingDto) entity).getApprovable());
    
    returnProcessingController.setListController(listController);
    returnProcessingController.prepareDialog(FundingDto.class, FundingStatus.RETURNED.getAction(),
        "pi pi-arrow-left", returnFunction(), "returnForm", client);
    returnProcessingController.submitButton().setValue(FundingStatus.RETURNED.getAction());
    returnProcessingController.submitButton()
        .setActionFunction(i -> returnProcessingController.getController().execute());
    returnProcessingController.setShowDialogButtonController(new RecordComponentController());
    returnProcessingController.getShowDialogButtonController()
        .setRenderedFunction(entity -> ((FundingDto) entity).getReturnable());
    returnProcessingController.initialize();
    
    statusReasonInputTextController.setOutputLableValue("Motif");
    statusReasonInputTextController.getInputTextarea().setRequired(true);
    statusReasonInputTextController.getInputTextarea().addValueConsumer(
        reason -> ((FundingDto) returnProcessingController.getController().getEntity())
            .setStatusReason(reason));

    returnProcessingController.submitButton()
        .addUpdate(statusReasonInputTextController.getInputTextarea().getMessage().getIdentifier());
  }
  
  void initializeConfirmation(IdentifiableProcessingController identifiableProcessingController,
      FundingStatus status, String icon, String styleClassSuffix,
      UnaryOperator<Object> processingFunction, Function<Object, Boolean> buttonRenderedFunction) {
    identifiableProcessingController.setListController(listController);
    identifiableProcessingController.prepareConfirmation(FundingDto.class, status.getAction(), icon,
        processingFunction);
    identifiableProcessingController.setSubmitButtonController(new RecordComponentController());
    identifiableProcessingController.getSubmitButtonController()
        .setRenderedFunction(buttonRenderedFunction);
    //addStatusUpdateResponseConsumer(identifiableProcessingController);
    identifiableProcessingController.submitButton()
        .addStyleClass(buildButtonStyleClass(styleClassSuffix));
    identifiableProcessingController.submitButton()
        .addUpdates(CommandUpdatePropertyValueBuilder.formatObserver(statusChangedEvent));
    identifiableProcessingController.initialize();
  }
  
  String buildButtonStyleClass(String styleClassSuffix) {
    return "funding-action-%s".formatted(styleClassSuffix);
  }

  UnaryOperator<Object> transmitFunction() {
    return identifier -> {
      ByIdentifierRequestDto request = new ByIdentifierRequestDto();
      request.setIdentifier((String) identifier);
      request.setAuditWho(userIdentifier);
      return client.transmit(request);
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

  UnaryOperator<Object> returnFunction() {
    return entity -> {
      FundingReturnRequestDto request = new FundingReturnRequestDto();
      request.setIdentifier(((FundingDto) entity).getIdentifier());
      request.setReason(((FundingDto) entity).getStatusReason());
      request.setAuditWho(userIdentifier);
      return client.returnBack(request);
    };
  }
}
