package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link PaymentAdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class PaymentAdjustedFeeListPage extends AbstractPage {

  @Inject
  @Getter
  PaymentAdjustedFeeCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des paiements de frais ajustés";
  }
  
  public static final String OUTCOME = "paymentAdjustedFeeListPage";
}
