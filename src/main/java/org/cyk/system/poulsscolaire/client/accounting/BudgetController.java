package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.core.segregation.HasDeadlineAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasDeadlineDto;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.input.InputDateController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService.BudgetCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService.BudgetUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link BudgetDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BudgetController extends AbstractController {

  @Inject
  BudgetClient client;

  @Inject
  BudgetRequestMapper requestMapper;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  AccountingPlanSelectOneController accountingPlanSelectOneController;

  @Inject
  @Getter
  InputNumberController yearInputNumberController;

  @Inject
  @Getter
  InputDateController deadlineInputDateController;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  BudgetFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = BudgetDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(BudgetDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(BudgetService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        BudgetDto.JSON_ACCOUNTING_PLAN_AS_STRING, BudgetDto.JSON_YEAR,
        BudgetDto.JSON_AMOUNT_AS_STRING, BudgetDto.JSON_STATUS_AS_STRING,
        BudgetDto.JSON_COMMITMENT_AMOUNT_AS_STRING, HasDeadlineAsStringDto.JSON_DEADLINE_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        BudgetDto.JSON_SCHOOL_AS_STRING);

    listController.getReadController().setProjection(projection);
    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(BudgetReadPage.OUTCOME);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    schoolSelectOneController
        .setRenderable(filterController.getFilter().getSchoolIdentifier() == null);
    accountingPlanSelectOneController.setChoicable(!schoolSelectOneController.isRenderable());
    accountingPlanSelectOneController.getSelectOneMenu().setRequired(true);

    listController.getCreateController().addEntityConsumer(entity -> ((BudgetDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      BudgetCreateRequestDto request = requestMapper.mapCreate((BudgetDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            BudgetDto.JSON_ACCOUNTING_PLAN_IDENTIFIER, BudgetDto.JSON_SCHOOL_IDENTIFIER,
            BudgetDto.JSON_YEAR, HasDeadlineDto.JSON_DEADLINE));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetDto) entity).getSchoolIdentifier());

      accountingPlanSelectOneController.getSelectOneMenu()
          .writeValue(((BudgetDto) entity).getAccountingPlanIdentifier());

      yearInputNumberController.getInputInteger().writeValue(((BudgetDto) entity).getYear());
    });

    listController.getUpdateController().setFunction(entity -> {
      BudgetUpdateRequestDto request = requestMapper.mapUpdate((BudgetDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getSchoolIdentifier()));
    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetDto.class)
            .setSchoolIdentifier(identifier));
    schoolSelectOneController.getSelectOneMenu().valueChangeAjax().configure(e -> {
      AccountingPlanFilter accountingPlanFilter = new AccountingPlanFilter();
      accountingPlanFilter
          .setSchoolIdentifier(schoolSelectOneController.getSelectOneMenu().getValue());
      accountingPlanSelectOneController.setFilter(accountingPlanFilter.toDto());
      accountingPlanSelectOneController.setChoicable(true);
      accountingPlanSelectOneController.computeSelectOneMenuChoices();
    }, new CommandUpdatePropertyValueBuilder()
        .widgets(List.of(accountingPlanSelectOneController.getSelectOneMenu())));


    accountingPlanSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(BudgetDto.class)
            .setAccountingPlanIdentifier(identifier));

    yearInputNumberController.setOutputLableValue("Année");
    yearInputNumberController.getInputInteger().addValueConsumer(year -> listController
        .getCreateControllerOrUpdateControllerEntityAs(BudgetDto.class).setYear(year));

    deadlineInputDateController.setOutputLableValue("Date butoir");
    deadlineInputDateController.getInputLocalDateTime().addValueConsumer(date -> listController
        .getCreateControllerOrUpdateControllerEntityAs(BudgetDto.class).setDeadline(date));
  }
}
