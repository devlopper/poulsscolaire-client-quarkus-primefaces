package org.cyk.system.poulsscolaire.client.configuration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.accounting.BudgetController;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;

/**
 * Cette classe représente la page de lecture des budgets de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DepartmentReadBudgetsPage extends AbstractDepartmentReadDatasPage {

  @Inject
  @Getter
  BudgetController budgetController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    budgetController.getFilterController().getFilter()
        .setDepartmentIdentifier(department.getIdentifier());
    budgetController.initialize();
    budgetController.getListController().getGotoReadPageButton()
        .setOutcome(DepartmentReadFundingsPage.OUTCOME);
  }

  public static final String OUTCOME = "departmentReadBudgetsPage";
}
