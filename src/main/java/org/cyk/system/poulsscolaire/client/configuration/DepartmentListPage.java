package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;

/**
 * Cette classe représente la page de liste de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DepartmentListPage extends AbstractPage {

  @Inject
  @Getter
  DepartmentController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des départements";
    
    controller.getListController().getShowCreateDialogButton().setRendered(false);
    controller.getListController().getShowUpdateDialogButton().setRendered(false);
    controller.getListController().getDeleteButton().setRendered(false);
  }
  
  public static final String OUTCOME = "departmentListPage";
}
