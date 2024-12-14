package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StockMovementDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockMovementFilterController extends AbstractFilterController<StockMovementFilter> {

  public StockMovementFilterController() {
    super(StockMovementFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setStockIdentifier(getRequestParameter(StockMovementFilter.JSON_STOCK_IDENTIFIER));
  }
}
