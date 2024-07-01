package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
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

  public FeeCategoryFilterController() {
    super(FeeCategoryFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setRegistrationSchoolingSchoolIdentifier(
        getRequestParameter(FeeCategoryFilter.JSON_REGISTRATION_SCHOOLING_SCHOOL_IDENTIFIER));
  }
}
