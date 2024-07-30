package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodDto;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link PeriodDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PeriodFilterController extends AbstractFilterController<PeriodFilter> {

  @Inject
  SessionController sessionController;

  public PeriodFilterController() {
    super(PeriodFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setOpened(getRequestParameterAsBoolean(PeriodFilter.JSON_OPENED));
  }
}
