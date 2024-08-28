package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneRadioBoolean;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.client.registration.RegistrationSelectOneController;
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

  @Inject
  @Getter
  SessionController sessionController;

  @Inject
  @Getter
  RegistrationSelectOneController registrationSelectOneController;

  @Inject
  @Getter
  FeeCategorySelectOneController feeCategoryController;

  @Getter
  SelectOneRadioBoolean amountValuePayableEqualsZeroSelectOneRadio;

  @Getter
  SelectOneRadioBoolean amountDeadlineDateOverSelectOneRadio;

  public AdjustedFeeFilterController() {
    super(AdjustedFeeFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setRegistrationSchoolingSchoolIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_SCHOOL_IDENTIFIER),
        sessionController.getSchoolIdentifier()));
    filter.setRegistrationSchoolingBranchIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_BRANCH_IDENTIFIER));
    filter.setRegistrationSchoolingPeriodIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_SCHOOLING_PERIOD_IDENTIFIER));

    filter.setRegistrationIdentifier(
        getRequestParameter(AdjustedFeeFilter.JSON_REGISTRATION_IDENTIFIER));
    filter.setAmountOptional(
        getRequestParameterAsBoolean(AbstractAmountContainerFilter.JSON_AMOUNT_OPTIONAL));
    filter.setLatePayment(getRequestParameterAsBoolean(AdjustedFeeFilter.JSON_LATE_PAYMENT));
    filter.setAmountValuePayableLessThanOrEqualsZero(getRequestParameterAsBoolean(
        AbstractAmountContainerFilter.JSON_AMOUNT_VALUE_PAYABLE_LESS_THAN_OR_EQUALS_ZERO));

    registrationSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setRegistrationIdentifier(identifier));

    feeCategoryController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setFeeCategoryIdentifier(identifier));

    filterStringifier =
        filter -> String.format("Inscription %s", filter.getRegistrationIdentifier());

    amountValuePayableEqualsZeroSelectOneRadio = new SelectOneRadioBoolean();
    amountValuePayableEqualsZeroSelectOneRadio.outputLabel().setValue("Soldé");
    amountValuePayableEqualsZeroSelectOneRadio.addTrueOrFalseChoices(true)
        .addValueConsumer(value -> filter.setAmountValuePayableLessThanOrEqualsZero(value));

    amountDeadlineDateOverSelectOneRadio = new SelectOneRadioBoolean();
    amountDeadlineDateOverSelectOneRadio.addTrueOrFalseChoices(true);
    amountDeadlineDateOverSelectOneRadio.outputLabel().setValue("En retard");
    amountDeadlineDateOverSelectOneRadio
        .addValueConsumer(value -> filter.setAmountDeadlineDateOver(value));
  }
}
