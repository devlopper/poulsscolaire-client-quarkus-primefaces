package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService.AccountingOperationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationService.AccountingOperationUpdateRequestDto;

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
  AccountingAccountTypeSelectOneController accountTypeSelectOneController;

  @Inject
  @Getter
  ListController listController;

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

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        AccountingOperationDto.JSON_ACCOUNT_TYPE_AS_STRING, AccountingOperationDto.JSON_BENEFICIARY,
        AccountingOperationDto.JSON_SCHOOL_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

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

      accountTypeSelectOneController.getSelectOneRadio()
          .writeValue(((AccountingOperationDto) entity).getAccountType().name());
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingOperationUpdateRequestDto request =
          requestMapper.mapUpdate((AccountingOperationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
            .setSchoolIdentifier(identifier));

    accountTypeSelectOneController.getSelectOneRadio()
        .addValueConsumer(name -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationDto.class)
            .setAccountType(Core.getEnumerationValueByName(AccountingAccountType.class, name)));
  }
}
