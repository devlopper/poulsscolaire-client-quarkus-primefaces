package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerFilter;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link AdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AdjustedFeeFilterController extends AbstractFilterController<AdjustedFeeFilter> {

  public AdjustedFeeFilterController() {
    super(AdjustedFeeFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setRegistrationSchoolingSchoolIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_SCHOOL_IDENTIFIER));
    filter.setRegistrationSchoolingBranchIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_BRANCH_IDENTIFIER));
    filter.setRegistrationSchoolingPeriodIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_PERIOD_IDENTIFIER));
    
    filter.setRegistrationIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_IDENTIFIER));
    filter.setAmountOptional(
        getRequestParameterAsBoolean(AbstractAmountContainerFilter.JSON_AMOUNT_OPTIONAL));
    filter.setLatePayment(
        getRequestParameterAsBoolean(AdjustedFeeFilter.JSON_LATE_PAYMENT));
    filter.setAmountValuePayableLessThanOrEqualsZero(getRequestParameterAsBoolean(
        AbstractAmountContainerFilter.JSON_AMOUNT_VALUE_PAYABLE_LESS_THAN_OR_EQUALS_ZERO));

    filterStringifier =
        filter -> String.format("Inscription %s", filter.getRegistrationIdentifier());
  }
}
