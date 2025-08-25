package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link SubsidyDecisionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionRegistrationFilterController
    extends AbstractFilterController<SubsidyDecisionRegistrationFilter> {

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  SubsidyDecisionSelectOneController subsidyDecisionSelectOneController;

  public SubsidyDecisionRegistrationFilterController() {
    super(SubsidyDecisionRegistrationFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(SubsidyDecisionRegistrationFilter.JSON_SCHOOL_IDENTIFIER),
        sessionController.getSchoolIdentifier()));
    filter.setStudentIdentifier(
        getRequestParameter(SubsidyDecisionRegistrationFilter.JSON_STUDENT_IDENTIFIER));
    filter.setSubsidyDecisionIdentifier(
        getRequestParameter(SubsidyDecisionRegistrationFilter.JSON_SUBSIDY_DECISION_IDENTIFIER));

    subsidyDecisionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setSubsidyDecisionIdentifier(identifier));
  }
}
