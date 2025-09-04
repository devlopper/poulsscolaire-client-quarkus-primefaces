package org.cyk.system.poulsscolaire.client.registration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
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
public class SubsidyDecisionReadRegistrationsPage extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  SubsidyDecisionRegistrationController registrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    registrationController.getFilterController().getFilter()
        .setSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    registrationController.initialize();
    configureDataTable(registrationController.getListController().getDataTable());
  }

  public static final String OUTCOME = "subsidyDecisionReadRegistrationsPage";
}
