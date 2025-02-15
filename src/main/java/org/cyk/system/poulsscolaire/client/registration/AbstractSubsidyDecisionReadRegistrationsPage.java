package org.cyk.system.poulsscolaire.client.registration;

import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de lecture de {@link RegistrationDto} de
 * {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
public abstract class AbstractSubsidyDecisionReadRegistrationsPage
    extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  RegistrationController registrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    initializeRegistrationController();
  }

  void initializeRegistrationController() {
    registrationController.initialize();
    registrationController.getListController().getShowCreateDialogButton().setRendered(false);
    configureDataTable(registrationController.getListController().getDataTable());
  }
}
