package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StockDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockFilterController extends AbstractFilterController<StockFilter> {

  public StockFilterController() {
    super(StockFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setFeeCategoryIdentifier(getRequestParameter(StockFilter.JSON_FEE_CATEGORY_IDENTIFIER));
  }
}
