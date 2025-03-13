package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.IdentifiableProcessingController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.request.ByIdentifierRequestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.function.UnaryOperator;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
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

  ListController listController;

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    transmitProcessingController.setListController(listController);
    transmitProcessingController.prepareConfirmation(FundingDto.class,
        FundingStatus.TRANSMITTED.getAction(), "pi pi-send", transmitFunction());
    transmitProcessingController.initialize();

    acceptProcessingController.setListController(listController);
    acceptProcessingController.prepareConfirmation(FundingDto.class,
        FundingStatus.ACCEPTED.getAction(), "pi pi-check", acceptFunction());
    acceptProcessingController.initialize();

    approveProcessingController.setListController(listController);
    approveProcessingController.prepareConfirmation(FundingDto.class,
        FundingStatus.APPROVED.getAction(), "pi pi-thumbs-up", approveFunction());
    approveProcessingController.initialize();

    returnProcessingController.setListController(listController);
    returnProcessingController.prepareDialog(FundingDto.class, FundingStatus.RETURNED.getAction(),
        "pi pi-arrow-left", returnFunction(), "returnForm", client);
    returnProcessingController.initialize();
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
      request.setIdentifier(((BudgetDto) entity).getIdentifier());
      request.setReason(((BudgetDto) entity).getStatusReason());
      request.setAuditWho(userIdentifier);
      return client.returnBack(request);
    };
  }
}
