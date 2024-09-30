package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationFilter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link AccountingOperationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingOperationFilterController
    extends AbstractFilterController<AccountingOperationFilter> {

  @Inject
  SessionController sessionController;

  public AccountingOperationFilterController() {
    super(AccountingOperationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(DeadlineFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
    filter.setAccountType(Core.getEnumerationValueByName(AccountingAccountType.class,
        getRequestParameter(AccountingOperationFilter.JSON_ACCOUNT_TYPE)));
    filter.setCanceled(getRequestParameterAsBoolean(AccountingOperationFilter.JSON_CANCELED));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

  }
}

