package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingAccountFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingOperationDto;

/**
 * Cette classe représente la page de lecture de {@link AccountingOperationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AccountingOperationReadPage extends AbstractPage {

  @Inject
  AccountingOperationClient operationClient;

  @Getter
  AccountingOperationDto operation;

  @Inject
  @Getter
  AccountingOperationAccountController accountController;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        AccountingOperationDto.JSON_AMOUNT_AS_STRING, AccountingOperationDto.JSON_SCHOOL_AS_STRING,
        AccountingOperationDto.JSON_ACCOUNT_TYPE_AS_STRING,
        AccountingOperationDto.JSON_ACCOUNT_TYPE, AccountingOperationDto.JSON_BENEFICIARY,
        AccountingOperationDto.JSON_PLAN_IDENTIFIER, AccountingOperationDto.JSON_SCHOOL_IDENTIFIER);
    String identifier = getRequestParameterIdentifier();
    operation = operationClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle =
        AccountingOperationDto.NAME + " - " + operation.getCode() + " " + operation.getName();

    accountController.getFilterController().getFilter().setOperationIdentifier(identifier);

    AccountingAccountFilter accountFilter = new AccountingAccountFilter();
    accountFilter.setType(operation.getAccountType());
    accountFilter.setPlanIdentifier(operation.getPlanIdentifier());
    accountFilter.setSchoolIdentifier(operation.getSchoolIdentifier());
    accountController.accountSelectOneController.setFilter(accountFilter.toDto());

    accountController.initialize();
    accountController.amountAsString = operation.getAmountAsString();
  }

  public static final String OUTCOME = "accountingOperationReadPage";
}
