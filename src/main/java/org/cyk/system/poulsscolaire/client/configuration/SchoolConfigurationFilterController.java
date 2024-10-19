package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link SchoolConfigurationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolConfigurationFilterController
    extends AbstractFilterController<SchoolConfigurationFilter> {

  @Inject
  SessionController sessionController;

  public SchoolConfigurationFilterController() {
    super(SchoolConfigurationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(SchoolConfigurationFilter.JSON_SCHOOL_IDENTIFIER),
        sessionController.getSchoolIdentifier()));
  }
}
