package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link IdentityRelationshipDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class IdentityRelationshipListPage extends AbstractPage {

  @Inject
  @Getter
  IdentityRelationshipController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des relations d'identités";
    controller.initialize();
  }
  
  public static final String OUTCOME = "identityRelationListPage";
}
