package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.segregation.HasQuantityAsStringDto;
import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionDto;

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
    distribution = client.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            HasQuantityAsStringDto.JSON_QUANTITY_AS_STRING),
        userIdentifier, null);
    contentTitle = StockDistributionDto.NAME;

    // distributionRegistrationController.getFilterController()
    // .getFilter().setDistributionIdentifier(distribution.getIdentifier());

    // distributionRegistrationController.totalQuantityAsString = stock.getQuantityAsString();

    distributionRegistrationController.quantityColumn.setIsCellEditableFunction(o -> true);
    distributionRegistrationController.prepareQuantityColumnAsEditable();

    distributionRegistrationController.initialize();

    computeQuantityColumnFooterText();

    distributionRegistrationController.getQuantityColumn()
        .addUpdateResponseConsumer(response -> onStockDistributionRegistrationQuantityUpdate());
  }

  void onStockDistributionRegistrationQuantityUpdate() {
    StockDistributionDto dto = client.getByIdentifier(distribution.getIdentifier(),
        new ProjectionDto().addNames(HasQuantityAsStringDto.JSON_QUANTITY_AS_STRING),
        userIdentifier, null);

    distribution.setQuantityAsString(dto.getQuantityAsString());

    computeQuantityColumnFooterText();
  }

  void computeQuantityColumnFooterText() {
    distributionRegistrationController.getQuantityColumn()
        .setFooterText(distribution.getQuantityAsString());
  }

  public static final String OUTCOME = "stockDistributionReadPage";
}
