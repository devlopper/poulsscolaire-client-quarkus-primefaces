package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountDto;

/**
 * Cette classe représente la page de liste de {@link AccountingOperationAccountDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingOperationAccountListPage extends AbstractPage {

  @Inject
  @Getter
  AccountingOperationAccountController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AccountingOperationAccountDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "accountingOperationAccountListPage";
}
