package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderClient;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderDto;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderService.GenderGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link GenderDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class GenderSelectOne extends AbstractSelectOneIdentifiableController<
    GenderDto, GenderGetManyResponseDto, GenderClient> {

  @Inject
  @Getter
  GenderClient client;

  protected GenderSelectOne() {
    super(GenderDto.class);
  }
}
