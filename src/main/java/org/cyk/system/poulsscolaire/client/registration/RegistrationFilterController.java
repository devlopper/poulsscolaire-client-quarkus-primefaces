package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class RegistrationFilterController extends AbstractFilterController<RegistrationFilter> {

  @Inject
  SessionController sessionController;

  public RegistrationFilterController() {
    super(RegistrationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(RegistrationFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
  }
}
