package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class IdentityListPage extends AbstractPage {

  @Inject
  @Getter
  IdentityController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des identités";
    controller.initialize();
  }
  
  public static final String OUTCOME = "identityListPage";
}
