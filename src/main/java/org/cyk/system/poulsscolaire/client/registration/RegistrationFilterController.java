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
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

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

  @Inject
  @Getter
  SubsidyDecisionSelectOneController subsidyDecisionSelectOneController;

  @Inject
  @Getter
  SubsidyDecisionSelectOneController doesNotBelongsToSubsidyDecisionSelectOneController;

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
    filter.setSubsidyDecisionIdentifier(
        getRequestParameter(RegistrationFilter.JSON_SUBSIDY_DECISION_IDENTIFIER));
    filter.setDoesNotBelongsToSubsidyDecisionIdentifier(getRequestParameter(
        RegistrationFilter.JSON_DOES_NOT_BELONGS_TO_SUBSIDY_DECISION_IDENTIFIER));
    filter.setSubsidyRefused(getRequestParameterAsBoolean(RegistrationFilter.JSON_SUBSIDY_REFUSED));

    studentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setStudentIdentifier(identifier));

    branchInstanceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setBranchInstanceIdentifier(identifier));

    subsidyDecisionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setSubsidyDecisionIdentifier(identifier));

    doesNotBelongsToSubsidyDecisionSelectOneController.getSelectOneMenu().addValueConsumer(
        identifier -> filter.setDoesNotBelongsToSubsidyDecisionIdentifier(identifier));
    doesNotBelongsToSubsidyDecisionSelectOneController.getSelectOneMenu().outputLabel()
        .setValue("N'appartient pas à " + SubsidyDecisionDto.NAME);
  }
}
