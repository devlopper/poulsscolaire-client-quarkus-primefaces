package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;

/**
 * Cette classe représente la page de liste de {@link BudgetDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class BudgetListPage extends AbstractPage {

  @Inject
  @Getter
  BudgetController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + BudgetDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "budgetListPage";
}
