package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService.SchoolingGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolingSelectOne extends AbstractSelectOneIdentifiableController<SchoolingDto,
    SchoolingGetManyResponseDto, SchoolingClient> {

  @Inject
  @Getter
  SchoolingClient client;

  protected SchoolingSelectOne() {
    super(SchoolingDto.class);
    projection.addNames(AbstractIdentifiableCodableDto.JSON_CODE,
        SchoolingDto.JSON_BRANCH_AS_STRING);
  }

  @Override
  protected String buildSelectItemLabel(SchoolingDto dto) {
    return dto.getBranchAsString();
  }
}
