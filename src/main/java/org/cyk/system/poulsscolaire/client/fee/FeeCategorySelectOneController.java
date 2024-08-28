package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryFilter;
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

  @Inject
  SessionController sessionController;
  
  protected FeeCategorySelectOneController() {
    super(FeeCategoryDto.class);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      FeeCategoryFilter feeCategoryFilter = new FeeCategoryFilter();
      feeCategoryFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = feeCategoryFilter.toDto();
    });

  }
}
