package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StudentFilterController extends AbstractFilterController<StudentFilter> {

  @Inject
  SessionController sessionController;

  public StudentFilterController() {
    super(StudentFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(StudentFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
  }
}
