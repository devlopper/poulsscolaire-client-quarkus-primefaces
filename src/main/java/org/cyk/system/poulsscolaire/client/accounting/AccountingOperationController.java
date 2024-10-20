package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.CommandUpdatePropertyValueBuilder;
import ci.gouv.dgbf.extension.primefaces.component.input.InputText;
import ci.gouv.dgbf.extension.primefaces.crud.IdentifiableProcessingController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ByIdentifierRequestDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService.AccountingOperationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService.AccountingOperationUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanFilter;

/**
 * Cette classe représente le contrôleur de {@link AccountingOperationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingOperationController extends AbstractController {

  @Inject
  AccountingOperationClient client;

  @Inject
  AccountingOperationRequestMapper requestMapper;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  AccountingPlanSelectOneController planSelectOneController;

  @Inject
  @Getter
  AccountingAccountTypeSelectOneController accountTypeSelectOneController;

  @Getter
  InputText beneficiaryInputText;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  AccountingOperationFilterController filterController;

  @Inject
  @Getter
  IdentifiableProcessingController cancelController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AccountingOperationDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AccountingOperationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AccountingOperationService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        AccountingOperationDto.JSON_AMOUNT_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        AccountingOperationDto.JSON_SCHOOL_AS_STRING);
    if (filterController.getFilter().getAccountType() == null) {
      projection.addNames(AccountingOperationDto.JSON_ACCOUNT_TYPE_AS_STRING);
    } else {
      if (AccountingAccountType.EXPENDITURE.equals(filterController.getFilter().getAccountType())) {
        projection.addNames(AccountingOperationDto.JSON_BENEFICIARY);
        beneficiaryInputText = new InputText();
        beneficiaryInputText.outputLabel().setValue("Bénéficiaire");
        beneficiaryInputText.addValueConsumer(value -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
            .setBeneficiary(value));
        beneficiaryInputText.setRequired(true);
      }
    }
    
    if (Boolean.TRUE.equals(filterController.getFilter().getCanceled())) {
      listController.getShowCreateDialogButton().setRendered(false);
      listController.getShowUpdateDialogButton().setRendered(false);
    }

    listController.getReadController().setProjection(projection);
    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(AccountingOperationReadPage.OUTCOME);
    listController.getDeleteButton().setRendered(false);
    
    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    schoolSelectOneController
        .setRenderable(filterController.getFilter().getSchoolIdentifier() == null);
    planSelectOneController.setChoicable(!schoolSelectOneController.isRenderable());
    planSelectOneController.getSelectOneMenu().setRequired(true);
    
    listController.getCreateController().addEntityConsumer(entity -> {
      ((AccountingOperationDto) entity)
          .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier());
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingOperationDto) entity).getSchoolIdentifier());

      ((AccountingOperationDto) entity)
          .setAccountType(filterController.getFilter().getAccountType());
      accountTypeSelectOneController.getSelectOneRadio()
          .writeValue(Optional.ofNullable(((AccountingOperationDto) entity).getAccountType())
              .map(Enum::name).orElse(null));
    });

    listController.getCreateController().setFunction(entity -> {
      AccountingOperationCreateRequestDto request =
          requestMapper.mapCreate((AccountingOperationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setProjection(new ProjectionDto().addNames(
        AbstractIdentifiableDto.JSON_IDENTIFIER, AbstractIdentifiableCodableDto.JSON_CODE,
        AbstractIdentifiableCodableNamableDto.JSON_NAME, AccountingOperationDto.JSON_ACCOUNT_TYPE,
        AccountingOperationDto.JSON_BENEFICIARY, AccountingOperationDto.JSON_SCHOOL_IDENTIFIER));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingOperationDto) entity).getSchoolIdentifier());

      planSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingOperationDto) entity).getPlanIdentifier());

      accountTypeSelectOneController.getSelectOneRadio()
          .writeValue(((AccountingOperationDto) entity).getAccountType().name());

      if (beneficiaryInputText != null) {
        beneficiaryInputText.setValue(((AccountingOperationDto) entity).getBeneficiary());
      }
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingOperationUpdateRequestDto request =
          requestMapper.mapUpdate((AccountingOperationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getSchoolIdentifier()));
    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
            .setSchoolIdentifier(identifier));
    schoolSelectOneController.getSelectOneMenu().valueChangeAjax().configure(e -> {
      AccountingPlanFilter accountingPlanFilter = new AccountingPlanFilter();
      accountingPlanFilter
          .setSchoolIdentifier(schoolSelectOneController.getSelectOneMenu().getValue());
      planSelectOneController.setFilter(accountingPlanFilter.toDto());
      planSelectOneController.setChoicable(true);
      planSelectOneController.computeSelectOneMenuChoices();
    }, new CommandUpdatePropertyValueBuilder()
        .widgets(List.of(planSelectOneController.getSelectOneMenu())));


    planSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
            .setPlanIdentifier(identifier));

    if (Objects.isNull(filterController.getFilter().getAccountType())) {
      accountTypeSelectOneController.getSelectOneRadio()
          .addValueConsumer(name -> listController
              .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
              .setAccountType(Core.getEnumerationValueByName(AccountingAccountType.class, name)));
    } else {
      accountTypeSelectOneController.setRenderable(false);
    }

    cancelController.setName("Annulation opération");
    cancelController.prepareConfirmation("annuler", "pi pi-times");
    cancelController.setListController(listController);
    cancelController.initialize();
    cancelController.getController().setFunction(identifier -> {
      ByIdentifierRequestDto request = new ByIdentifierRequestDto();
      request.setIdentifier((String) identifier);
      request.setAuditWho(userIdentifier);
      return client.cancel(request);
    });

    cancelController.submitButton()
        .setRendered(!Boolean.TRUE.equals(filterController.getFilter().getCanceled()));
  }
}
