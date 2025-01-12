package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link FundingExecutionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingExecutionFilterController
    extends AbstractFilterController<FundingExecutionFilter> {

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  FundingSelectOneController fundingSelectOneController;

  public FundingExecutionFilterController() {
    super(FundingExecutionFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter
        .setFundingIdentifier(getRequestParameter(FundingExecutionFilter.JSON_FUNDING_IDENTIFIER));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

    fundingSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setFundingIdentifier(identifier));
  }
}

