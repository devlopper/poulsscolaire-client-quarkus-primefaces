package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;

/**
 * Cette classe représente la page de liste de {@link StockDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockListPage extends AbstractPage {

  @Inject
  @Getter
  StockController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + StockDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "stockListPage";
}
