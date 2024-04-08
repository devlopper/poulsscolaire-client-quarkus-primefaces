package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolingListPage extends AbstractPage {

  @Inject
  @Getter
  SchoolingCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des scolarités";
  }
  
  public static final String OUTCOME = "schoolingListPage";
}
