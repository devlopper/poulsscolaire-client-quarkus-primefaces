package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.registration.StockDistributionDto;
import org.cyk.system.poulsscolaire.server.api.registration.StockDistributionFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StockDistributionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockDistributionFilterController
    extends AbstractFilterController<StockDistributionFilter> {

  public StockDistributionFilterController() {
    super(StockDistributionFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
  }
}
