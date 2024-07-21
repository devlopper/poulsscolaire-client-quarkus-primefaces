package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountClient;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDto;

/**
 * Cette classe représente la page de lecture de {@link AmountDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AmountReadPage extends AbstractPage {

  @Inject
  AmountClient amountClient;

  @Getter
  AmountDto amount;

  @Inject
  @Getter
  AmountDeadlineController amountDeadlineController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    amount = amountClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(AmountDto.JSON_IDENTIFIER),
        userIdentifier, null);
    contentTitle = AmountDto.NAME + " - " + amount.getIdentifier();
    amountDeadlineController.getFilterController().getFilter()
        .setAmountIdentifier(amount.getIdentifier());

    amountDeadlineController.initialize();
  }

  public static final String OUTCOME = "amountReadPage";
}
