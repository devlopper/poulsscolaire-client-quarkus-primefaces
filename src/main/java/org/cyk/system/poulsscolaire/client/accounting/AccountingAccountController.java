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
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountService.AccountingAccountCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountService.AccountingAccountUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountType;

/**
 * Cette classe représente le contrôleur de {@link AccountingAccountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingAccountController extends AbstractController {

  @Inject
  AccountingAccountClient client;

  @Inject
  AccountingAccountRequestMapper requestMapper;

  @Inject
  @Getter
  AccountingPlanSelectOneController planSelectOneController;

  @Inject
  @Getter
  AccountingAccountTypeSelectOneController typeSelectOneController;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AccountingAccountDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AccountingAccountDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AccountingAccountService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        AccountingAccountDto.JSON_PLAN_AS_STRING, AccountingAccountDto.JSON_TYPE_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      AccountingAccountCreateRequestDto request =
          requestMapper.mapCreate((AccountingAccountDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AbstractIdentifiableCodableDto.JSON_CODE,
            AbstractIdentifiableCodableNamableDto.JSON_NAME,
            AccountingAccountDto.JSON_PLAN_IDENTIFIER, AccountingAccountDto.JSON_TYPE));

    listController.getUpdateController().addEntityConsumer(entity -> {
      planSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingAccountDto) entity).getPlanIdentifier());

      typeSelectOneController.getSelectOneRadio()
          .writeValue(((AccountingAccountDto) entity).getType().name());
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingAccountUpdateRequestDto request =
          requestMapper.mapUpdate((AccountingAccountDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    planSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingAccountDto.class)
            .setPlanIdentifier(identifier));

    typeSelectOneController.getSelectOneRadio()
        .addValueConsumer(name -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingAccountDto.class)
            .setType(Core.getEnumerationValueByName(AccountingAccountType.class, name)));
  }
}
