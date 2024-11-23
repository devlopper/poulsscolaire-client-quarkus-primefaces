package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceDto;

/**
 * Cette classe représente la page de liste de {@link FundingSourceDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FundingSourceListPage extends AbstractPage {

  @Inject
  @Getter
  FundingSourceController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + FundingSourceDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "fundingSourceListPage";
}
