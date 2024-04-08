package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderDto;

/**
 * Cette classe représente la page de liste de {@link GenderDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class GenderListPage extends AbstractPage {

  @Inject
  @Getter
  GenderCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des genres";
  }
  
  public static final String OUTCOME = "genderListPage";
}
