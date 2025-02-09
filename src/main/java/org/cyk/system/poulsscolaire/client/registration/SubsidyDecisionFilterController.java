package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.client.configuration.SchoolingSelectOneController;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionFilterController
    extends AbstractFilterController<SubsidyDecisionFilter> {

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  StudentSelectOneController studentSelectOneController;

  @Inject
  @Getter
  SchoolingSelectOneController schoolingSelectOneController;

  public SubsidyDecisionFilterController() {
    super(SubsidyDecisionFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(SubsidyDecisionFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
    filter.setStudentIdentifier(getRequestParameter(SubsidyDecisionFilter.JSON_STUDENT_IDENTIFIER));
    filter.setSchoolingIdentifier(
        getRequestParameter(SubsidyDecisionFilter.JSON_SCHOOLING_IDENTIFIER));

    studentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setStudentIdentifier(identifier));

    schoolingSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setSchoolingIdentifier(identifier));
  }
}
