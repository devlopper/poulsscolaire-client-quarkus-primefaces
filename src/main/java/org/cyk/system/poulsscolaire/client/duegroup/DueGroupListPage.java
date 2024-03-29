package org.cyk.system.poulsscolaire.client.duegroup;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link DueGroupDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DueGroupListPage extends AbstractPage {

  @Inject
  @Getter
  DueGroupCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des groupes d'échéances";
  }
  
  public static final String OUTCOME = "dueGroupListPage";
}
