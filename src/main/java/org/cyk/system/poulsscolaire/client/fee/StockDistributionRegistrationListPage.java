package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationDto;

/**
 * Cette classe représente la page de liste de {@link StockDistributionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockDistributionRegistrationListPage extends AbstractPage {

  @Inject
  @Getter
  StockDistributionRegistrationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + StockDistributionRegistrationDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "stockDistributionRegistrationListPage";
}
