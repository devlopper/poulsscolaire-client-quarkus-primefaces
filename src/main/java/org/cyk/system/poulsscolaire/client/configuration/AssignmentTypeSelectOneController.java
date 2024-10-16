package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeClient;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeDto;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeService.AssignmentTypeGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link AssignmentTypeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AssignmentTypeSelectOneController extends AbstractSelectOneIdentifiableController<
    AssignmentTypeDto, AssignmentTypeGetManyResponseDto, AssignmentTypeClient> {

  @Inject
  @Getter
  AssignmentTypeClient client;

  protected AssignmentTypeSelectOneController() {
    super(AssignmentTypeDto.class);
  }
}
