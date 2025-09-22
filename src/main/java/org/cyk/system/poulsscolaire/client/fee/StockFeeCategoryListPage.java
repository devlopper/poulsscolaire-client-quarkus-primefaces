package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryDto;

/**
 * Cette classe représente la page de liste de {@link StockFeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockFeeCategoryListPage extends AbstractPage {

  @Inject
  @Getter
  StockFeeCategoryController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + StockFeeCategoryDto.NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "stockFeeCategoryListPage";
}
