package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.component.OutputLabel;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneEnumerationController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;

/**
 * Cette classe représente le contrôleur de sélection de {@link AccountingAccountType}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingAccountTypeSelectOneController
    extends AbstractSelectOneEnumerationController<AccountingAccountType> {

  protected AccountingAccountTypeSelectOneController() {
    super(AccountingAccountType.values());
  }

  @Override
  protected void processOutputLabel(OutputLabel outputLabel) {
    outputLabel.setValue("Type de compte comptable");
  }
}
