package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService.DeadlineGetManyResponseDto;

/**
 * Cette classe représente un sélecteur d'un élément de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Getter
@Dependent
public class DeadlineSelectOneController extends AbstractSelectOneIdentifiableController<
    DeadlineDto, DeadlineGetManyResponseDto, DeadlineClient> {

  @Inject
  @Getter
  DeadlineClient client;

  protected DeadlineSelectOneController() {
    super(DeadlineDto.class);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableNamableDto.JSON_NAME, DeadlineDto.JSON_DATE_AS_STRING);
  }

  @Override
  protected String buildSelectItemLabel(DeadlineDto dto) {
    return String.format("%s(%s)", dto.getName(), dto.getDateAsString());
  }
}
