package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.client.configuration.BranchInstanceSelectOneController;
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

  @Inject
  @Getter
  StudentSelectOneController studentSelectOneController;

  @Inject
  @Getter
  BranchInstanceSelectOneController branchInstanceSelectOneController;

  public RegistrationFilterController() {
    super(RegistrationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(RegistrationFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
    filter.setStudentIdentifier(getRequestParameter(RegistrationFilter.JSON_STUDENT_IDENTIFIER));
    filter.setBranchInstanceIdentifier(
        getRequestParameter(RegistrationFilter.JSON_BRANCH_INSTANCE_IDENTIFIER));

    studentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setStudentIdentifier(identifier));
    
    branchInstanceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setBranchInstanceIdentifier(identifier));
  }
}
