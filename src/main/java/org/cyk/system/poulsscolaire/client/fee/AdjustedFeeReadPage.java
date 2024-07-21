package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;

/**
 * Cette classe représente la page de lecture de {@link AdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AdjustedFeeReadPage extends AbstractPage {

  @Inject
  AdjustedFeeClient adjustedFeeClient;

  @Getter
  AdjustedFeeDto adjustedFeeDto;

  @Inject
  @Getter
  AmountDeadlineController amountDeadlineController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    adjustedFeeDto = adjustedFeeClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AbstractAmountContainerDto.JSON_AMOUNT_IDENTIFIER),
        userIdentifier, null);
    contentTitle = AdjustedFeeDto.NAME + " - " + adjustedFeeDto.getIdentifier();
    amountDeadlineController.getFilterController().getFilter()
        .setAmountIdentifier(adjustedFeeDto.getAmountIdentifier());

    amountDeadlineController.initialize();
  }

  public static final String OUTCOME = "adjustedFeeReadPage";
}
