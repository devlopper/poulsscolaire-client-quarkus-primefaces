package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;

/**
 * Cette classe représente la page de lecture de {@link StockDistributionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockDistributionReadPage extends AbstractPage {

  @Inject
  StockDistributionClient client;

  @Getter
  StockDistributionDto distribution;

  @Inject
  @Getter
  StockDistributionRegistrationController distributionRegistrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    distribution = client.getByIdentifier(identifier, new ProjectionDto()
        .addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, StockDto.JSON_QUANTITY_AS_STRING),
        userIdentifier, null);
    contentTitle = StockDistributionDto.NAME;

    // distributionRegistrationController.getFilterController()
    // .getFilter().setDistributionIdentifier(distribution.getIdentifier());

    // distributionRegistrationController.totalQuantityAsString = stock.getQuantityAsString();
    distributionRegistrationController.initialize();
  }

  public static final String OUTCOME = "stockDistributionReadPage";
}
