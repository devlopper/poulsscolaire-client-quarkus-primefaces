package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService.FeeCategoryGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeCategorySelectOne extends AbstractSelectOneIdentifiableController<
    FeeCategoryDto, FeeCategoryGetManyResponseDto, FeeCategoryClient> {

  @Inject
  @Getter
  FeeCategoryClient client;

  protected FeeCategorySelectOne() {
    super(FeeCategoryDto.class);
    projection.addNames(AbstractIdentifiableCodableNamableDto.JSON_NAME);
  }

  @Override
  protected String buildSelectItemLabel(FeeCategoryDto dto) {
    return dto.getName();
  }
}
