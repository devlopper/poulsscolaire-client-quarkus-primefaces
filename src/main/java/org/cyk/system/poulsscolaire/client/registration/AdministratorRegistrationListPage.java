package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AdministratorRegistrationListPage extends AbstractPage {

  @Inject
  @Getter
  RegistrationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des inscriptions";
  }
  
  public static final String OUTCOME = "administratorRegistrationListPage";
}
