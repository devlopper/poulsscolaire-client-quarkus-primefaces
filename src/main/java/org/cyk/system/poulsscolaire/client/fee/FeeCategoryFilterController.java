package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeCategoryFilterController extends AbstractFilterController<FeeCategoryFilter> {

  @Inject
  SessionController sessionController;

  public FeeCategoryFilterController() {
    super(FeeCategoryFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(FeeCategoryFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));

    filter.setRegistrationSchoolingSchoolIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(FeeCategoryFilter.JSON_REGISTRATION_SCHOOLING_SCHOOL_IDENTIFIER),
        sessionController.getSchoolIdentifier()));

    filter.setRegistrationSchoolingPeriodIdentifier(Core.getOrDefaultIfNull(
        getRequestParameter(FeeCategoryFilter.JSON_REGISTRATION_SCHOOLING_PERIOD_IDENTIFIER),
        sessionController.getPeriodIdentifier()));
  }
}
