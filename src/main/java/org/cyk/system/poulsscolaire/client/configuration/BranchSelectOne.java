package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchClient;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchDto;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchService.BranchGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link BranchDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BranchSelectOne extends
    AbstractSelectOneIdentifiableController<BranchDto, BranchGetManyResponseDto, BranchClient> {

  @Inject
  @Getter
  BranchClient client;

  protected BranchSelectOne() {
    super(BranchDto.class);
  }
}
