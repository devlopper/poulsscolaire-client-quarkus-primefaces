package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;

/**
 * Cette classe représente la page de lecture de {@link FeeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FeeReadPage extends AbstractPage {

  @Inject
  FeeClient feeClient;

  @Getter
  FeeDto fee;

  @Inject
  @Getter
  AmountDeadlineController amountDeadlineController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    fee = feeClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(FeeDto.JSON_SCHOOLING_BRANCH_AS_STRING,
            FeeDto.JSON_CATEGORY_AS_STRING, FeeDto.JSON_ASSIGNMENT_TYPE_AS_STRING,
            FeeDto.JSON_SENIORITY_AS_STRING, AbstractAmountContainerDto.JSON_AMOUNT_IDENTIFIER,
            AbstractAmountContainerDto.JSON_AMOUNT_VALUE_AS_STRING,
            AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING,
            AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER_AS_STRING,
            AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL,
            AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL_AS_STRING),
        userIdentifier, null);
    contentTitle = FeeDto.NAME + " - " + fee.getCategoryAsString();
    amountDeadlineController.getFilterController().getFilter()
        .setAmountIdentifier(fee.getAmountIdentifier());

    amountDeadlineController.initialize();
  }

  public static final String OUTCOME = "feeReadPage";
}
