package org.cyk.system.poulsscolaire.client.registration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de lecture des inscriptions à subventionner de
 * {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionReadRegistrationsPageOld
    extends AbstractSubsidyDecisionReadRegistrationsPage {

  @Override
  void initializeRegistrationController() {
    registrationController.getFilterController().getFilter()
        .setDoesNotBelongsToSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    super.initializeRegistrationController();
  }

  public static final String OUTCOME = "subsidyDecisionReadRegistrationsPage";
}
