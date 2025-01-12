package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingService.FundingGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link FundingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingSelectOneController extends AbstractSelectOneIdentifiableController<
    FundingDto, FundingGetManyResponseDto, FundingClient> {

  @Inject
  @Getter
  FundingClient client;

  @Inject
  SessionController sessionController;
  
  protected FundingSelectOneController() {
    super(FundingDto.class, SelectItemLabelStrategy.AS_STRING);
  }
}
