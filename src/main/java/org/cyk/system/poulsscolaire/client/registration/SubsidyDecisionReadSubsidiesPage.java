package org.cyk.system.poulsscolaire.client.registration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de lecture des subventions de
 * {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionReadSubsidiesPage
    extends AbstractSubsidyDecisionReadRegistrationsPage {

  @Override
  void initializeRegistrationController() {
    registrationController.getFilterController().getFilter()
        .setSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    super.initializeRegistrationController();
  }

  public static final String OUTCOME = "subsidyDecisionReadSubsidiesPage";
}
