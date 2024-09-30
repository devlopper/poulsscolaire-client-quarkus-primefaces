package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;

/**
 * Cette classe représente la page de liste de {@link AccountingOperationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingOperationListPage extends AbstractPage {

  @Inject
  @Getter
  AccountingOperationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AccountingOperationDto.NAME;
    if (controller.getFilterController().getFilter().getAccountType() == null) {
      controller.getFilterController().getFilter()
          .setAccountType(AccountingAccountType.EXPENDITURE);
    }
    if (controller.getFilterController().getFilter().getCanceled() == null) {
      controller.getFilterController().getFilter().setCanceled(false);
    }
    controller.initialize();
  }

  public static final String OUTCOME = "accountingOperationListPage";
}
