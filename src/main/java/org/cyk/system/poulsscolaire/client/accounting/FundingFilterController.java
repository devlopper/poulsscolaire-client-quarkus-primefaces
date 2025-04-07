package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.component.input.MonthSelectOneController;
import ci.gouv.dgbf.extension.server.service.api.AbstractIdentifiableFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.time.Month;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.client.configuration.DepartmentSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link FundingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingFilterController extends AbstractFilterController<FundingFilter> {

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  BudgetSelectOneController budgetSelectOneController;

  @Inject
  @Getter
  InputNumberController budgetYearInputNumberController;

  @Inject
  @Getter
  MonthSelectOneController monthSelectOneController;

  @Inject
  @Getter
  InputNumberController monthIndexInputNumberController;

  @Inject
  @Getter
  DepartmentSelectOneController departmentSelectOneController;

  @Inject
  @Getter
  AccountingAccountSelectOneController accountingAccountSelectOneController;

  @Inject
  @Getter
  FundingSourceSelectOneController sourceSelectOneController;

  public FundingFilterController() {
    super(FundingFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setBudgetIdentifier(getRequestParameter(FundingFilter.JSON_BUDGET_IDENTIFIER));
    filter.setSearchText(getRequestParameter(AbstractIdentifiableFilter.JSON_SEARCH_TEXT));

    budgetSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setBudgetIdentifier(identifier));

    budgetYearInputNumberController.setOutputLableValue("Année");
    budgetYearInputNumberController.getInputInteger()
        .addValueConsumer(year -> filter.setBudgetYear(year));

    monthSelectOneController.getSelectOneMenu().addValueConsumer(
        name -> filter.setMonth(Optional.ofNullable(name).map(Month::valueOf).orElse(null)));

    monthIndexInputNumberController.getInputInteger()
        .addValueConsumer(index -> filter.setMonthIndex(index));

    departmentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setDepartmentIdentifier(identifier));

    accountingAccountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setAccountingAccountIdentifier(identifier));

    sourceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setSourceIdentifier(identifier));
  }
}
