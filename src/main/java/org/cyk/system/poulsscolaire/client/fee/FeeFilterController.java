package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerFilter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link FeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeFilterController extends AbstractFilterController<FeeFilter> {

  public FeeFilterController() {
    super(FeeFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolingIdentifier(getRequestParameter(FeeFilter.JSON_SCHOOLING_IDENTIFIER));
    filter.setAmountOptional(
        getRequestParameterAsBoolean(AbstractAmountContainerFilter.JSON_AMOUNT_OPTIONAL));

    filterStringifier = filter -> String.format("Scolarité %s", filter.getSchoolingIdentifier());
  }
}
