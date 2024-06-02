package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FeeCategoryListPage extends AbstractPage {

  @Inject
  @Getter
  FeeCategoryController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des rubriques";
  }
  
  public static final String OUTCOME = "feeCategoryListPage";
}
