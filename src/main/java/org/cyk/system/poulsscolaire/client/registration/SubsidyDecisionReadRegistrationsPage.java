package org.cyk.system.poulsscolaire.client.registration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
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
@Named
@ViewScoped
public class SubsidyDecisionReadRegistrationsPage extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  RegistrationController registrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    registrationController.getFilterController().getFilter()
        .setSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    registrationController.initialize();
    registrationController.getListController().getCreateDialog()
        .setContentFilePath("/private/subsidydecision/component/addRegistrationsForm.xhtml");
  }
  
  /**
   * Cette méthode permet d'afficher les {@link RegistrationDto} sélectionnable.
   */
  public void showSelectableRegistrations() {
    
  }

  public static final String OUTCOME = "subsidyDecisionReadRegistrationsPage";
}
