package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link SubsidyDecisionPaymentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionPaymentFilterController
    extends AbstractFilterController<SubsidyDecisionPaymentFilter> {

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  SubsidyDecisionSelectOneController subsidyDecisionSelectOneController;

  public SubsidyDecisionPaymentFilterController() {
    super(SubsidyDecisionPaymentFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(SubsidyDecisionPaymentFilter.JSON_SCHOOL_IDENTIFIER),
        sessionController.getSchoolIdentifier()));
    filter.setStudentIdentifier(
        getRequestParameter(SubsidyDecisionPaymentFilter.JSON_STUDENT_IDENTIFIER));
    filter.setSubsidyDecisionIdentifier(
        getRequestParameter(SubsidyDecisionPaymentFilter.JSON_SUBSIDY_DECISION_IDENTIFIER));

    subsidyDecisionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setSubsidyDecisionIdentifier(identifier));
  }
}
