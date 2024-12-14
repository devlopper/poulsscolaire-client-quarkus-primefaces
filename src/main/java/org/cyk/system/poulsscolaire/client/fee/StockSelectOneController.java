package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockService.StockGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link StockDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockSelectOneController extends AbstractSelectOneIdentifiableController<
    StockDto, StockGetManyResponseDto, StockClient> {

  @Inject
  @Getter
  StockClient client;

  protected StockSelectOneController() {
    super(StockDto.class);
  }
}
