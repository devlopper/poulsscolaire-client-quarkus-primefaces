package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link FundingSourceDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingSourceFilterController extends AbstractFilterController<FundingSourceFilter> {

  @Inject
  SessionController sessionController;

  public FundingSourceFilterController() {
    super(FundingSourceFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(FundingSourceFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
  }
}
