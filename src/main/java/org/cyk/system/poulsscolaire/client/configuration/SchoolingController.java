package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingRequestMapper;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService.SchoolingCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService.SchoolingUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolingController extends AbstractController {

  @Inject
  SchoolingClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  PeriodSelectOne periodSelectOne;

  @Inject
  @Getter
  BranchSelectOneController branchSelectOneController;

  @Inject
  @Getter
  InputNumberController preRegistrationAmountInputNumberController;

  @Inject
  @Getter
  InputNumberController subsidyAmountInputNumberController;

  @Inject
  SchoolingRequestMapper requestMapper;

  @Inject
  @Getter
  SchoolingFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Scolarités";
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SchoolingDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SchoolingService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        SchoolingDto.JSON_SCHOOL_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getPeriodIdentifier(),
        SchoolingDto.JSON_PERIOD_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, SchoolingDto.JSON_BRANCH_AS_STRING,
        SchoolingDto.JSON_PRE_REGISTRATION_AMOUNT_AS_STRING,
        SchoolingDto.JSON_SUBSIDY_AMOUNT_AS_STRING,
        SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_VALUE_AS_STRING,
        SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(SchoolingReadPage.OUTCOME);

    listController.getCreateController().addEntityConsumer(entity -> ((SchoolingDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      SchoolingCreateRequestDto request = requestMapper.mapCreate((SchoolingDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            SchoolingDto.JSON_SCHOOL_IDENTIFIER, SchoolingDto.JSON_PERIOD_IDENTIFIER,
            SchoolingDto.JSON_BRANCH_IDENTIFIER, SchoolingDto.JSON_PRE_REGISTRATION_AMOUNT,
            SchoolingDto.JSON_SUBSIDY_AMOUNT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((SchoolingDto) entity).getSchoolIdentifier());
      periodSelectOne.getSelectOneMenu().writeValue(((SchoolingDto) entity).getPeriodIdentifier());
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((SchoolingDto) entity).getBranchIdentifier());

      preRegistrationAmountInputNumberController.getInputInteger()
          .writeValue(((SchoolingDto) entity).getPreRegistrationAmount());
      subsidyAmountInputNumberController.getInputInteger()
          .writeValue(((SchoolingDto) entity).getSubsidyAmount());
    });

    listController.getUpdateController().setFunction(entity -> {
      SchoolingUpdateRequestDto request = requestMapper.mapUpdate((SchoolingDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolingDto.class)
            .setSchoolIdentifier(identifier));

    schoolSelectOneController.getSelectOneMenu().valueChangeAjax().setConsumer(e -> {
      PeriodFilter periodFilter = new PeriodFilter();
      periodFilter.setSchoolIdentifier(schoolSelectOneController.getSelectOneMenu().getValue());
      periodFilter.setOpened(true);
      periodSelectOne.setFilter(periodFilter.toDto());
      periodSelectOne.computeSelectOneMenuChoices();

      BranchFilter branchFilter = new BranchFilter();
      branchFilter.setSchoolIdentifier(schoolSelectOneController.getSelectOneMenu().getValue());
      branchSelectOneController.setFilter(branchFilter.toDto());
      branchSelectOneController.computeSelectOneMenuChoices();
    });
    schoolSelectOneController.getSelectOneMenu().valueChangeAjax().setDisabled(false);
    schoolSelectOneController.getSelectOneMenu().valueChangeAjax()
        .setUpdate(periodSelectOne.getSelectOneMenu().getIdentifier() + ","
            + schoolSelectOneController.getSelectOneMenu().getIdentifier());

    if (!Core.isStringBlank(filterController.getFilter().getSchoolIdentifier())) {
      schoolSelectOneController.getSelectOneMenu().setRendered(false);
      PeriodFilter periodFilter = new PeriodFilter();
      periodFilter.setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier());
      periodFilter.setOpened(true);
      periodSelectOne.setFilter(periodFilter.toDto());
      periodSelectOne.computeSelectOneMenuChoices();
    }

    periodSelectOne.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolingDto.class)
            .setPeriodIdentifier(identifier));

    branchSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolingDto.class)
            .setBranchIdentifier(identifier));

    preRegistrationAmountInputNumberController.setOutputLableValue("Montant pré-inscription");
    preRegistrationAmountInputNumberController.getInputInteger()
        .addValueConsumer(amount -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolingDto.class)
            .setPreRegistrationAmount(amount));
    
    subsidyAmountInputNumberController.setOutputLableValue("Montant subvention");
    subsidyAmountInputNumberController.getInputInteger()
        .addValueConsumer(amount -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolingDto.class)
            .setSubsidyAmount(amount));
  }
}
