package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodClient;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodDto;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodService.PeriodGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link PeriodDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PeriodSelectOne extends
    AbstractSelectOneIdentifiableController<PeriodDto, PeriodGetManyResponseDto, PeriodClient> {

  @Inject
  @Getter
  PeriodClient client;

  protected PeriodSelectOne() {
    super(PeriodDto.class);
  }
}
