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
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;

/**
 * Cette classe représente la page de lecture de {@link BudgetDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class BudgetReadPage extends AbstractPage {

  @Inject
  BudgetClient budgetClient;

  @Getter
  BudgetDto budget;

  @Inject
  @Getter
  FundingController fundingController;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        BudgetDto.JSON_AMOUNT_AS_STRING, BudgetDto.JSON_SCHOOL_AS_STRING,
        BudgetDto.JSON_ACCOUNTING_PLAN_AS_STRING);
    String identifier = getRequestParameterIdentifier();
    budget = budgetClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle = BudgetDto.NAME + " - " + budget.getCode() + " " + budget.getName();

    fundingController.getFilterController().getFilter().setBudgetIdentifier(identifier);
    fundingController.amountColumn().setFooterText(budget.getAmountAsString());
    fundingController.initialize();
  }

  public static final String OUTCOME = "budgetReadPage";
}
