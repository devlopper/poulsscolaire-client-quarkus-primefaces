package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetFilter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link BudgetDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BudgetFilterController
    extends AbstractFilterController<BudgetFilter> {

  @Inject
  SessionController sessionController;

  public BudgetFilterController() {
    super(BudgetFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(DeadlineFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

  }
}

