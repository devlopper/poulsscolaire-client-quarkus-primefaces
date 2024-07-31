package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link PaymentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class PaymentListPage extends AbstractPage {

  @Inject
  @Getter
  PaymentController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des paiements";
    if (controller.getFilterController().getFilter().getCanceled() == null) {
      controller.getFilterController().getFilter().setCanceled(false);
    }
    controller.initialize();
  }

  public static final String OUTCOME = "paymentListPage";
}
