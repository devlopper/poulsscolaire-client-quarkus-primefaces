package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchClient;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchDto;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchService.BranchGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link BranchDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BranchSelectOneController extends
    AbstractSelectOneIdentifiableController<BranchDto, BranchGetManyResponseDto, BranchClient> {

  @Inject
  @Getter
  BranchClient client;

  @Inject
  SessionController sessionController;
  
  protected BranchSelectOneController() {
    super(BranchDto.class);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      BranchFilter branchFilter = new BranchFilter();
      branchFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = branchFilter.toDto();
    });
  }
}
