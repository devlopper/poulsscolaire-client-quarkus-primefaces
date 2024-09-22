package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanDto;

/**
 * Cette classe représente la page de liste de {@link AccountingPlanDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingPlanListPage extends AbstractPage {

  @Inject
  @Getter
  AccountingPlanController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AccountingPlanDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "accountingPlanListPage";
}
