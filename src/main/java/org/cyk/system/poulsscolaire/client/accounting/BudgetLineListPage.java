package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineDto;

/**
 * Cette classe représente la page de liste de {@link BudgetLineDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class BudgetLineListPage extends AbstractPage {

  @Inject
  @Getter
  BudgetLineController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + BudgetLineDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "budgetLineListPage";
}
