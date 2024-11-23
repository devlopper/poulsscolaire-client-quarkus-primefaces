package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceService.FundingSourceGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link FundingSourceDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingSourceSelectOneController extends AbstractSelectOneIdentifiableController<
    FundingSourceDto, FundingSourceGetManyResponseDto, FundingSourceClient> {

  @Inject
  @Getter
  FundingSourceClient client;

  @Inject
  SessionController sessionController;
  
  protected FundingSourceSelectOneController() {
    super(FundingSourceDto.class);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      FundingSourceFilter fundingSourceFilter = new FundingSourceFilter();
      fundingSourceFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = fundingSourceFilter.toDto();
    });
  }
}
