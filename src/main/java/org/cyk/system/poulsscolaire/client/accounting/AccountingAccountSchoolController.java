package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolService.AccountingAccountSchoolCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountSchoolService.AccountingAccountSchoolUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link AccountingAccountSchoolDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingAccountSchoolController extends AbstractController {

  @Inject
  AccountingAccountSchoolClient client;

  @Inject
  AccountingAccountSchoolRequestMapper requestMapper;

  @Inject
  @Getter
  AccountingPlanSelectOneController planSelectOneController;

  @Inject
  @Getter
  AccountingAccountSelectOneController accountSelectOneController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  AccountingAccountSchoolFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AccountingAccountSchoolDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AccountingAccountSchoolDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AccountingAccountSchoolService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();

    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AccountingAccountSchoolDto.JSON_PLAN_AS_STRING,
        AccountingAccountSchoolDto.JSON_ACCOUNT_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        AccountingAccountSchoolDto.JSON_SCHOOL_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    accountSelectOneController.setChoicable(false);
    schoolSelectOneController
        .setRenderable(filterController.getFilter().getSchoolIdentifier() == null);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((AccountingAccountSchoolDto) entity)
          .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      AccountingAccountSchoolCreateRequestDto request =
          requestMapper.mapCreate((AccountingAccountSchoolDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AccountingAccountSchoolDto.JSON_ACCOUNT_IDENTIFIER,
            AccountingAccountSchoolDto.JSON_SCHOOL_IDENTIFIER));

    listController.getUpdateController().addEntityConsumer(entity -> {
      accountSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingAccountSchoolDto) entity).getAccountIdentifier());

      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingAccountSchoolDto) entity).getSchoolIdentifier());
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingAccountSchoolUpdateRequestDto request =
          requestMapper.mapUpdate((AccountingAccountSchoolDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    planSelectOneController.setFilter(null);
    planSelectOneController.getSelectOneMenu().valueChangeAjax().configure(e -> {
      AccountingAccountFilter accountFilter = new AccountingAccountFilter();
      accountFilter.setPlanIdentifier(planSelectOneController.getSelectOneMenu().getValue());
      accountSelectOneController.setFilter(accountFilter.toDto());
      accountSelectOneController.setChoicable(true);
      accountSelectOneController.computeSelectOneMenuChoices();
    }, new CommandUpdatePropertyValueBuilder()
        .widgets(List.of(accountSelectOneController.getSelectOneMenu())));

    accountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingAccountSchoolDto.class)
            .setAccountIdentifier(identifier));

    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingAccountSchoolDto.class)
            .setSchoolIdentifier(identifier));
  }
}
