package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StockFeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockFeeCategoryFilterController
    extends AbstractFilterController<StockFeeCategoryFilter> {

  public StockFeeCategoryFilterController() {
    super(StockFeeCategoryFilter.class);
  }
}
