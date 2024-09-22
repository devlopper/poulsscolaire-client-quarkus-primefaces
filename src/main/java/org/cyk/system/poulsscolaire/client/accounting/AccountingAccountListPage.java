package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountDto;

/**
 * Cette classe représente la page de liste de {@link AccountingAccountDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingAccountListPage extends AbstractPage {

  @Inject
  @Getter
  AccountingAccountController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AccountingAccountDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "accountingAccountListPage";
}
