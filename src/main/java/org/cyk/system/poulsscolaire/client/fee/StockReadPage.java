package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;

/**
 * Cette classe représente la page de lecture de {@link StockDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StockReadPage extends AbstractPage {

  @Inject
  StockClient client;

  @Getter
  StockDto stock;

  @Inject
  @Getter
  StockMovementController movementController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    stock = client.getByIdentifier(identifier, new ProjectionDto()
        .addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, StockDto.JSON_QUANTITY_AS_STRING),
        userIdentifier, null);
    contentTitle = StockDto.NAME;

    movementController.getFilterController().getFilter().setStockIdentifier(stock.getIdentifier());

    movementController.totalQuantityAsString = stock.getQuantityAsString();
    movementController.initialize();
  }

  public static final String OUTCOME = "stockReadPage";
}
