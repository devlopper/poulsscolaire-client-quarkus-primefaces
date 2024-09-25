package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputInteger;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountService.AccountingOperationAccountCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationAccountService.AccountingOperationAccountUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link AccountingOperationAccountDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingOperationAccountController extends AbstractController {

  @Inject
  AccountingOperationAccountClient client;

  @Inject
  AccountingOperationAccountRequestMapper requestMapper;

  @Inject
  @Getter
  AccountingOperationSelectOneController operationSelectOneController;

  @Inject
  @Getter
  AccountingAccountSelectOneController accountSelectOneController;

  @Getter
  InputInteger amount;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  AccountingOperationAccountFilterController filterController;

  @Getter
  String amountAsString;
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AccountingOperationAccountDto.NAME;
    amountAsString = "---";
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AccountingOperationAccountDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AccountingOperationAccountService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        AccountingOperationAccountDto.JSON_ACCOUNT_AS_STRING,
        AccountingOperationAccountDto.JSON_AMOUNT_AS_STRING);
    if (Core.isStringBlank(filterController.getFilter().getOperationIdentifier())) {
      projection.addNames(AccountingOperationAccountDto.JSON_OPERATION_AS_STRING);
      operationSelectOneController.getSelectOneMenu()
          .addValueConsumer(identifier -> listController
              .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationAccountDto.class)
              .setOperationIdentifier(identifier));
    } else {
      operationSelectOneController.setRenderable(false);
    }
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .addEntityConsumer(entity -> ((AccountingOperationAccountDto) entity)
            .setOperationIdentifier(filterController.getFilter().getOperationIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      AccountingOperationAccountCreateRequestDto request =
          requestMapper.mapCreate((AccountingOperationAccountDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AbstractIdentifiableCodableDto.JSON_CODE,
            AbstractIdentifiableCodableNamableDto.JSON_NAME,
            AccountingOperationAccountDto.JSON_OPERATION_IDENTIFIER,
            AccountingOperationAccountDto.JSON_ACCOUNT_IDENTIFIER,
            AccountingOperationAccountDto.JSON_AMOUNT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      if (operationSelectOneController.isRenderable()) {
        operationSelectOneController.getSelectOneMenu()
            .writeValue(((AccountingOperationAccountDto) entity).getOperationIdentifier());
      }
      accountSelectOneController.getSelectOneMenu()
          .writeValue(((AccountingOperationAccountDto) entity).getAccountIdentifier());

      amount.writeValue(((AccountingOperationAccountDto) entity).getAmount());
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingOperationAccountUpdateRequestDto request =
          requestMapper.mapUpdate((AccountingOperationAccountDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    accountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationAccountDto.class)
            .setAccountIdentifier(identifier));
    
    amount = new InputInteger();
    amount.outputLabel().setValue("Montant");
    amount.addValueConsumer(value -> listController
        .getCreateControllerOrUpdateControllerEntityAs(AccountingOperationAccountDto.class)
        .setAmount(Optional.ofNullable(value).orElse(0)));
  }
}
