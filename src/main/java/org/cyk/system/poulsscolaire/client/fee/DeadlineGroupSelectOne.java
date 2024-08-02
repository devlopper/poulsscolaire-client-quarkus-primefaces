package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupService.DeadlineGroupGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link DeadlineGroupDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DeadlineGroupSelectOne extends AbstractSelectOneIdentifiableController<
    DeadlineGroupDto, DeadlineGroupGetManyResponseDto, DeadlineGroupClient> {

  @Inject
  @Getter
  DeadlineGroupClient client;

  protected DeadlineGroupSelectOne() {
    super(DeadlineGroupDto.class);
  }
}
