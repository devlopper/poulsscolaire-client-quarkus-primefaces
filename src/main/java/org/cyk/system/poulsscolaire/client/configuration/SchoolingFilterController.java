package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolingFilterController extends AbstractFilterController<SchoolingFilter> {

  @Inject
  SessionController sessionController;

  public SchoolingFilterController() {
    super(SchoolingFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(SchoolingFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));

    filter.setPeriodIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(SchoolingFilter.JSON_PERIOD_IDENTIFIER),
            sessionController.getPeriodIdentifier()));
  }
}
