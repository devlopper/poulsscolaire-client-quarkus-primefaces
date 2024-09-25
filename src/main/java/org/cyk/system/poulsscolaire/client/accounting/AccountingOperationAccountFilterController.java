package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link AccountingOperationAccountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingOperationAccountFilterController
    extends AbstractFilterController<AccountingOperationAccountFilter> {

  public AccountingOperationAccountFilterController() {
    super(AccountingOperationAccountFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setOperationIdentifier(
        getRequestParameter(AccountingOperationAccountFilter.JSON_OPERATION_IDENTIFIER));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

  }
}

