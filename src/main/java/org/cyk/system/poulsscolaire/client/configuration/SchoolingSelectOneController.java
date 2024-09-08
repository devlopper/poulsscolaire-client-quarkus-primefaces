package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService.SchoolingGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolingSelectOneController extends AbstractSelectOneIdentifiableController<
    SchoolingDto, SchoolingGetManyResponseDto, SchoolingClient> {

  @Inject
  @Getter
  SchoolingClient client;

  @Inject
  SessionController sessionController;
  
  protected SchoolingSelectOneController() {
    super(SchoolingDto.class);
    projection.addNames(SchoolingDto.JSON_BRANCH_AS_STRING);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      SchoolingFilter schoolingFilter = new SchoolingFilter();
      schoolingFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = schoolingFilter.toDto();
    });
  }

  @Override
  protected String buildSelectItemLabel(SchoolingDto dto) {
    return dto.getBranchAsString();
  }
}
