package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link AssignmentTypeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AssignmentTypeListPage extends AbstractPage {

  @Inject
  @Getter
  AssignmentTypeCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des types d'affectation";
  }
  
  public static final String OUTCOME = "assignmentTypeListPage";
}
