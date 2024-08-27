package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
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
public class FeeCategorySelectOneController extends AbstractSelectOneIdentifiableController<
    FeeCategoryDto, FeeCategoryGetManyResponseDto, FeeCategoryClient> {

  @Inject
  @Getter
  FeeCategoryClient client;

  protected FeeCategorySelectOneController() {
    super(FeeCategoryDto.class);
  }
}
