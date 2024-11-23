package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link BudgetLineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BudgetLineFilterController
    extends AbstractFilterController<BudgetLineFilter> {

  @Inject
  SessionController sessionController;

  public BudgetLineFilterController() {
    super(BudgetLineFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setBudgetIdentifier(getRequestParameter(BudgetLineFilter.JSON_BUDGET_IDENTIFIER));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

  }
}

