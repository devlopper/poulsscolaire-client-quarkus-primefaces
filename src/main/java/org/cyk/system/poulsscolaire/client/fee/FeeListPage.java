package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;

/**
 * Cette classe représente la page de liste de {@link FeeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FeeListPage extends AbstractPage {

  @Inject
  @Getter
  FeeController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des frais";
    crudController.initialize();
  }
  
  public static final String OUTCOME = "feeListPage";
}
