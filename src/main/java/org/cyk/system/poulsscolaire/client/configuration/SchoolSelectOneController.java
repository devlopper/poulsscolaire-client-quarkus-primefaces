package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolService.SchoolGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link SchoolDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolSelectOneController extends
    AbstractSelectOneIdentifiableController<SchoolDto, SchoolGetManyResponseDto, SchoolClient> {

  @Inject
  @Getter
  SchoolClient client;

  protected SchoolSelectOneController() {
    super(SchoolDto.class);
  }
}
