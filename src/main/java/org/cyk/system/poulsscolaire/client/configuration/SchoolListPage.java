package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;

/**
 * Cette classe représente la page de liste de {@link SchoolDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolListPage extends AbstractPage {

  @Inject
  @Getter
  SchoolController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des écoles";
    
  }
  
  public static final String OUTCOME = "schoolListPage";
}
