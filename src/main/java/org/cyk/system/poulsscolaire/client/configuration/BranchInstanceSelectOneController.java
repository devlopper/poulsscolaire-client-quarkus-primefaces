package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceClient;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceDto;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceService.BranchInstanceGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link BranchInstanceDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BranchInstanceSelectOneController extends AbstractSelectOneIdentifiableController<
    BranchInstanceDto, BranchInstanceGetManyResponseDto, BranchInstanceClient> {

  @Inject
  @Getter
  BranchInstanceClient client;

  @Inject
  SessionController sessionController;

  protected BranchInstanceSelectOneController() {
    super(BranchInstanceDto.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      BranchInstanceFilter branchInstanceFilter = new BranchInstanceFilter();
      branchInstanceFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = branchInstanceFilter.toDto();
    });
  }
}
