package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.Column;
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
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.client.configuration.DepartmentSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingUpdateAmountRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingUpdateAmountResponseDto;
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
  FundingSourceSelectOneController sourceSelectOneController;

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

  @Getter
  @Setter
  Column amountColumn;

  /*  */

  @Inject
  @Getter
  FundingActionsController actionsController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FundingDto.NAME;
    amountColumn = new Column().initializeAsAmount();
    amountColumn.setHeaderText("Montant");
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
        FundingDto.JSON_ACCOUNTING_ACCOUNT_AS_STRING, FundingDto.JSON_SOURCE_AS_STRING,
        FundingDto.JSON_MONTH_AS_STRING, FundingDto.JSON_AMOUNT, FundingDto.JSON_AMOUNT_AS_STRING,
        FundingDto.JSON_AMOUNT_INPUTABLE, FundingDto.JSON_JUSTIFICATION,
        FundingDto.JSON_STATUS_AS_STRING, FundingDto.JSON_STATUS_REASON,
        FundingDto.JSON_COMMITMENT_AMOUNT_AS_STRING, FundingDto.JSON_TRANSMITABLE,
        FundingDto.JSON_ACCEPTABLE, FundingDto.JSON_APPROVABLE, FundingDto.JSON_RETURNABLE);
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

    actionsController.listController = listController;
    actionsController.initialize();

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
            FundingDto.JSON_BUDGET_IDENTIFIER, FundingDto.JSON_MONTH,
            FundingDto.JSON_DEPARTMENT_IDENTIFIER, FundingDto.JSON_ACCOUNTING_ACCOUNT_IDENTIFIER,
            FundingDto.JSON_SOURCE_IDENTIFIER, FundingDto.JSON_AMOUNT,
            FundingDto.JSON_JUSTIFICATION));

    listController.getUpdateController().addEntityConsumer(entity -> {
      budgetSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getBudgetIdentifier());

      monthSelectOneController.getSelectOneMenu().writeValue(
          Optional.ofNullable(((FundingDto) entity).getMonth()).map(Month::name).orElse(null));

      departmentSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getDepartmentIdentifier());

      accountingAccountSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getAccountingAccountIdentifier());

      sourceSelectOneController.getSelectOneMenu()
          .writeValue(((FundingDto) entity).getSourceIdentifier());

      amountInputNumberController.getInputLong().writeValue(((FundingDto) entity).getAmount());

      justificationInputTextController.getInputTextarea()
          .writeValue(((FundingDto) entity).getJustification());
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
    budgetSelectOneController.getSelectOneMenu().setRequired(true);
    budgetSelectOneController.getSelectOneMenu().valueChangeAjax().configure(e -> {
      AccountingAccountFilter accountingAccountFilter = new AccountingAccountFilter();
      accountingAccountFilter
          .setBudgetIdentifier(budgetSelectOneController.getSelectOneMenu().getValue());
      accountingAccountSelectOneController.setFilter(accountingAccountFilter.toDto());
      accountingAccountSelectOneController.setChoicable(true);
      accountingAccountSelectOneController.computeSelectOneMenuChoices();
    }, new CommandUpdatePropertyValueBuilder()
        .widgets(List.of(accountingAccountSelectOneController.getSelectOneMenu())));

    monthSelectOneController.getSelectOneMenu().setRequired(true);
    monthSelectOneController.getSelectOneMenu().addValueConsumer(
        month -> listController.getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setMonth(Core.isStringBlank(month) ? null : Month.valueOf(month)));

    departmentSelectOneController.getSelectOneMenu().setRequired(true);
    departmentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setDepartmentIdentifier(identifier));

    accountingAccountSelectOneController.getSelectOneMenu().setRequired(true);
    accountingAccountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setAccountingAccountIdentifier(identifier));

    sourceSelectOneController.getSelectOneMenu().setRequired(true);
    sourceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setSourceIdentifier(identifier));

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputLong().setRequired(true);
    amountInputNumberController.getInputLong().addValueConsumer(amount -> listController
        .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class).setAmount(amount));

    justificationInputTextController.setOutputLableValue("Justification");
    justificationInputTextController.getInputTextarea()
        .addValueConsumer(justification -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingDto.class)
            .setJustification(justification));
  }

  /**
   * Cette méthode permet d'obtenir la concatenation des identifiants des messages.
   *
   * @return concatenation des identifiants des messages
   */
  public String getComaSeparatedMessagesIdentifiers() {
    return List.of(budgetSelectOneController.getSelectOneMenu(),
        monthSelectOneController.getSelectOneMenu(),
        departmentSelectOneController.getSelectOneMenu(),
        accountingAccountSelectOneController.getSelectOneMenu(),
        sourceSelectOneController.getSelectOneMenu(), amountInputNumberController.getInputLong())
        .stream().map(input -> input.getMessage().getIdentifier()).collect(Collectors.joining(","));
  }

  /**
   * Cette méthode permet d'ecouter un changement de montant.
   *
   * @param funding {@link FundingDto}
   */
  public void listenAmountChange(FundingDto funding) {
    FundingUpdateAmountRequestDto request = new FundingUpdateAmountRequestDto();
    request.setIdentifier(funding.getIdentifier());
    request.setAmount(funding.getAmount());
    request.setFilter(filterController.getFilter().toDto());
    request.setAuditWho(userIdentifier);
    new ActionExecutor<>(this, "Mise à jour de montant", () -> {
      FundingUpdateAmountResponseDto response = client.updateAmount(request);
      amountColumn.setFooterText(response.getTotalAmountAsString());
      return response;
    }).execute();
  }
}
