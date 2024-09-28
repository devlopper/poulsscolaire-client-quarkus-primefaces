package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanService.AccountingPlanGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link AccountingPlanDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingPlanSelectOneController extends AbstractSelectOneIdentifiableController<
    AccountingPlanDto, AccountingPlanGetManyResponseDto, AccountingPlanClient> {

  @Inject
  @Getter
  AccountingPlanClient client;

  @Inject
  SessionController sessionController;
  
  protected AccountingPlanSelectOneController() {
    super(AccountingPlanDto.class);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      AccountingPlanFilter accountingPlanFilter = new AccountingPlanFilter();
      accountingPlanFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = accountingPlanFilter.toDto();
    });
  }
}
