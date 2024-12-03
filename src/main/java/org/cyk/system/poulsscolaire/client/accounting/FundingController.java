package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.component.input.MonthSelectOneController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.DepartmentSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link FundingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingController extends AbstractController {

  @Inject
  FundingClient client;

  @Inject
  FundingRequestMapper requestMapper;

  @Inject
  @Getter
  BudgetSelectOneController budgetSelectOneController;

  @Inject
  @Getter
  AccountingAccountSelectOneController accountingAccountSelectOneController;

  @Inject
  @Getter
  DepartmentSelectOneController departmentSelectOneController;

  @Inject
  @Getter
  FundingSourceSelectOneController fundingSourceSelectOneController;

  @Inject
  @Getter
  MonthSelectOneController monthSelectOneController;

  @Inject
  @Getter
  InputNumberController amountInputNumberController;

  @Inject
  @Getter
  InputTextController justificationInputTextController;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  FundingFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FundingDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(FundingDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FundingService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        FundingDto.JSON_DEPARTMENT_AS_STRING, FundingDto.JSON_ACCOUNTING_ACCOUNT_AS_STRING,
        FundingDto.JSON_MONTH_AS_STRING, FundingDto.JSON_AMOUNT_AS_STRING,
        FundingDto.JSON_JUSTIFICATION);
    projection.addNamesIfStringBlank(filterController.getFilter().getBudgetIdentifier(),
        FundingDto.JSON_BUDGET_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getDepartmentIdentifier(),
        FundingDto.JSON_DEPARTMENT_AS_STRING);
    projection.addNamesIfNull(filterController.getFilter().getMonth(),
        FundingDto.JSON_MONTH_AS_STRING);

    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    budgetSelectOneController
        .setRenderable(filterController.getFilter().getBudgetIdentifier() == null);
    accountingAccountSelectOneController.setChoicable(!budgetSelectOneController.isRenderable());
    accountingAccountSelectOneController.getSelectOneMenu().setRequired(true);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((FundingDto) entity).setBudgetIdentifier(filterController.getFilter().getBudgetIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      FundingCreateRequestDto request = requestMapper.mapCreate((FundingDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FundingDto.JSON_BUDGET_IDENTIFIER, FundingDto.JSON_DEPARTMENT_IDENTIFIER,
            FundingDto.JSON_ACCOUNTING_ACCOUNT_IDENTIFIER, FundingDto.JSON_MONTH,
            FundingDto.JSON_AMOUNT, FundingDto.JSON_JUSTIFICATION));

    listController.getUpdateController().addEntityConsumer(entity -> {
      budgetSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getBudgetIdentifier());

      accountingAccountSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getAccountingAccountIdentifier());

      fundingSourceSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getSourceIdentifier());

      departmentSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getDepartmentIdentifier());

      monthSelectOneController.getSelectOneMenu().writeValue(
          Optional.ofNullable(((FundingDto) entity).getMonth()).map(Month::name).orElse(null));
    });

    listController.getUpdateController().setFunction(entity -> {
      FundingUpdateRequestDto request = requestMapper.mapUpdate((FundingDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    budgetSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getBudgetIdentifier()));
    budgetSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setBudgetIdentifier(identifier));
    budgetSelectOneController.getSelectOneMenu().valueChangeAjax().configure(e -> {
      AccountingAccountFilter accountingAccountFilter = new AccountingAccountFilter();
      accountingAccountFilter
          .setBudgetIdentifier(budgetSelectOneController.getSelectOneMenu().getValue());
      accountingAccountSelectOneController.setFilter(accountingAccountFilter.toDto());
      accountingAccountSelectOneController.setChoicable(true);
      accountingAccountSelectOneController.computeSelectOneMenuChoices();
    }, new CommandUpdatePropertyValueBuilder()
        .widgets(List.of(accountingAccountSelectOneController.getSelectOneMenu())));


    accountingAccountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setAccountingAccountIdentifier(identifier));

    fundingSourceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setSourceIdentifier(identifier));

    departmentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setDepartmentIdentifier(identifier));

    monthSelectOneController.getSelectOneMenu().addValueConsumer(
        month -> listController.getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setMonth(Core.isStringBlank(month) ? null : Month.valueOf(month)));

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputLong().addValueConsumer(amount -> listController
        .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class).setAmount(amount));

    justificationInputTextController.setOutputLableValue("Justification");
    justificationInputTextController.getInputTextarea()
        .addValueConsumer(justification -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setJustification(justification));
  }
}
