package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;

/**
 * Cette classe représente la page de liste de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class RegistrationListPage extends AbstractPage {

  @Inject
  @Getter
  RegistrationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + RegistrationDto.PLURAL_NAME;
    controller.initialize();
  }
  
  public static final String OUTCOME = "registrationListPage";
}
