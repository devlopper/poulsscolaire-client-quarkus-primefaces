package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DeadlineFilterController extends AbstractFilterController<DeadlineFilter> {

  @Inject
  SessionController sessionController;

  public DeadlineFilterController() {
    super(DeadlineFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(DeadlineFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
  }
}
