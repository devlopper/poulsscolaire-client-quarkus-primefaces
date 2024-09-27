package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolDto;

/**
 * Cette classe représente la page de liste de {@link AccountingAccountSchoolDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingAccountSchoolListPage extends AbstractPage {

  @Inject
  @Getter
  AccountingAccountSchoolController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AccountingAccountSchoolDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "accountingAccountSchoolListPage";
}
