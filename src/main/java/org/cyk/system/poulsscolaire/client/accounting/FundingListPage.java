package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;

/**
 * Cette classe représente la page de liste de {@link FundingDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FundingListPage extends AbstractPage {

  @Inject
  @Getter
  FundingController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + FundingDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "fundingListPage";
}
