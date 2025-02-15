package org.cyk.system.poulsscolaire.client.registration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de lecture de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionReadPaymentsPage extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  SubsidyDecisionPaymentController paymentController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    paymentController.getFilterController().getFilter()
        .setSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    paymentController.initialize();
    configureDataTable(paymentController.getListController().getDataTable());
  }

  public static final String OUTCOME = "subsidyDecisionReadPaymentsPage";
}
