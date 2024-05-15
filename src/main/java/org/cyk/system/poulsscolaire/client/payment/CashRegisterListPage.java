package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link CashRegisterDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class CashRegisterListPage extends AbstractPage {

  @Inject
  @Getter
  CashRegisterCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des caisses";
  }
  
  public static final String OUTCOME = "cashRegisterListPage";
}
