package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountService.AccountingAccountGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link AccountingAccountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingAccountSelectOneController extends AbstractSelectOneIdentifiableController<
    AccountingAccountDto, AccountingAccountGetManyResponseDto, AccountingAccountClient> {

  @Inject
  @Getter
  AccountingAccountClient client;

  protected AccountingAccountSelectOneController() {
    super(AccountingAccountDto.class);
  }
}
