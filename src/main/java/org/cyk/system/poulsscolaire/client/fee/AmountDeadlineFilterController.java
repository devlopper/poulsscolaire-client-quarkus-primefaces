package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link AmountDeadlineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AmountDeadlineFilterController
    extends AbstractFilterController<AmountDeadlineFilter> {

  public AmountDeadlineFilterController() {
    super(AmountDeadlineFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setAdjustedFeeIdentifier(
        getRequestParameter(AmountDeadlineFilter.JSON_ADJUSTED_FEE_IDENTIFIER));
    filter.setFeeIdentifier(
        getRequestParameter(AmountDeadlineFilter.JSON_FEE_IDENTIFIER));
    filter.setRegistrationIdentifier(
        getRequestParameter(AmountDeadlineFilter.JSON_REGISTRATION_IDENTIFIER));
    filter.setDeadlineIdentifier(
        getRequestParameter(AmountDeadlineFilter.JSON_DEADLINE_IDENTIFIER));
  }
}
