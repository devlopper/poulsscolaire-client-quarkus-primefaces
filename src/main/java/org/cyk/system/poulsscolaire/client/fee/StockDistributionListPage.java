package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionDto;

/**
 * Cette classe représente la page de liste de {@link StockDistributionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockDistributionListPage extends AbstractPage {

  @Inject
  @Getter
  StockDistributionController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + StockDistributionDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "stockDistributionListPage";
}
