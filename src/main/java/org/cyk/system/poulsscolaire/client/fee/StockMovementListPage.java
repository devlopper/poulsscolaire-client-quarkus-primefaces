package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementDto;

/**
 * Cette classe représente la page de liste de {@link StockMovementDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockMovementListPage extends AbstractPage {

  @Inject
  @Getter
  StockMovementController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + StockMovementDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "stockMovementListPage";
}
