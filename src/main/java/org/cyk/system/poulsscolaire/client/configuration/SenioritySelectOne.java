package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SeniorityClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SeniorityDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SeniorityService.SeniorityGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link SeniorityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SenioritySelectOne extends AbstractSelectOneIdentifiableController<
    SeniorityDto, SeniorityGetManyResponseDto, SeniorityClient> {

  @Inject
  @Getter
  SeniorityClient client;

  protected SenioritySelectOne() {
    super(SeniorityDto.class);
  }
}
