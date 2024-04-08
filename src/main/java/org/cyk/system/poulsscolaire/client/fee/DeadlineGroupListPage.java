package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link DeadlineGroupDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DeadlineGroupListPage extends AbstractPage {

  @Inject
  @Getter
  DeadlineGroupCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des groupes d'échéances";
  }
  
  public static final String OUTCOME = "dueGroupListPage";
}
