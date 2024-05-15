package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link PaymentModeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class PaymentModeListPage extends AbstractPage {

  @Inject
  @Getter
  PaymentModeCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des modes de paiement";
  }
  
  public static final String OUTCOME = "paymentModeListPage";
}
