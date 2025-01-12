package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionDto;

/**
 * Cette classe représente la page de liste de {@link FundingExecutionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FundingExecutionListPage extends AbstractPage {

  @Inject
  @Getter
  FundingExecutionController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + FundingExecutionDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "fundingExecutionListPage";
}
