package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionService.StockDistributionGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link StockDistributionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockDistributionSelectOneController extends AbstractSelectOneIdentifiableController<
    StockDistributionDto, StockDistributionGetManyResponseDto, StockDistributionClient> {

  @Inject
  @Getter
  StockDistributionClient client;

  protected StockDistributionSelectOneController() {
    super(StockDistributionDto.class, SelectItemLabelStrategy.AS_STRING);
  }
}
