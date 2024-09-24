package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService.AccountingOperationGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link AccountingOperationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingOperationSelectOneController extends AbstractSelectOneIdentifiableController<
    AccountingOperationDto, AccountingOperationGetManyResponseDto, AccountingOperationClient> {

  @Inject
  @Getter
  AccountingOperationClient client;

  protected AccountingOperationSelectOneController() {
    super(AccountingOperationDto.class);
  }
}
