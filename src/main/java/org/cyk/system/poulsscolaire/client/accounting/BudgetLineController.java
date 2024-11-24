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
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineService;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineService.BudgetLineCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetLineService.BudgetLineUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link BudgetLineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BudgetLineController extends AbstractController {

  @Inject
  BudgetLineClient client;

  @Inject
  BudgetLineRequestMapper requestMapper;

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
  BudgetLineFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = BudgetLineDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(BudgetLineDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(BudgetLineService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        BudgetLineDto.JSON_DEPARTMENT_AS_STRING, BudgetLineDto.JSON_ACCOUNTING_ACCOUNT_AS_STRING,
        BudgetLineDto.JSON_MONTH_AS_STRING, BudgetLineDto.JSON_AMOUNT_AS_STRING,
        BudgetLineDto.JSON_JUSTIFICATION);
    projection.addNamesIfStringBlank(filterController.getFilter().getBudgetIdentifier(),
        BudgetLineDto.JSON_BUDGET_AS_STRING);

    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    budgetSelectOneController
        .setRenderable(filterController.getFilter().getBudgetIdentifier() == null);
    accountingAccountSelectOneController.setChoicable(!budgetSelectOneController.isRenderable());
    accountingAccountSelectOneController.getSelectOneMenu().setRequired(true);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((BudgetLineDto) entity)
          .setBudgetIdentifier(filterController.getFilter().getBudgetIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      BudgetLineCreateRequestDto request = requestMapper.mapCreate((BudgetLineDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            BudgetLineDto.JSON_BUDGET_IDENTIFIER, BudgetLineDto.JSON_DEPARTMENT_IDENTIFIER,
            BudgetLineDto.JSON_ACCOUNTING_ACCOUNT_IDENTIFIER, BudgetLineDto.JSON_MONTH,
            BudgetLineDto.JSON_AMOUNT, BudgetLineDto.JSON_JUSTIFICATION));

    listController.getUpdateController().addEntityConsumer(entity -> {
      budgetSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetLineDto) entity).getBudgetIdentifier());

      accountingAccountSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetLineDto) entity).getAccountingAccountIdentifier());

      fundingSourceSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetLineDto) entity).getFundingSourceIdentifier());

      departmentSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetLineDto) entity).getDepartmentIdentifier());

      monthSelectOneController.getSelectOneMenu().writeValue(
          Optional.ofNullable(((BudgetLineDto) entity).getMonth()).map(Month::name).orElse(null));
    });

    listController.getUpdateController().setFunction(entity -> {
      BudgetLineUpdateRequestDto request = requestMapper.mapUpdate((BudgetLineDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    budgetSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getBudgetIdentifier()));
    budgetSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
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
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
            .setAccountingAccountIdentifier(identifier));

    fundingSourceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
            .setFundingSourceIdentifier(identifier));

    departmentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
            .setDepartmentIdentifier(identifier));

    monthSelectOneController.getSelectOneMenu()
        .addValueConsumer(month -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
            .setMonth(Core.isStringBlank(month) ? null : Month.valueOf(month)));

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputLong().addValueConsumer(amount -> listController
        .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class).setAmount(amount));

    justificationInputTextController.setOutputLableValue("Justification");
    justificationInputTextController.getInputTextarea()
        .addValueConsumer(justification -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetLineDto.class)
            .setJustification(justification));
  }
}
