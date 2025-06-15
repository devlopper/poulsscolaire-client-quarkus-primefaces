package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StockDistributionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockDistributionRegistrationFilterController
    extends AbstractFilterController<StockDistributionRegistrationFilter> {

  public StockDistributionRegistrationFilterController() {
    super(StockDistributionRegistrationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
  }
}
