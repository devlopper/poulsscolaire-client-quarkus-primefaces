package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;

/**
 * Cette classe représente la page de liste de {@link AdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AdjustedFeeListPage extends AbstractPage {

  @Inject
  @Getter
  AdjustedFeeCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des frais ajustés";
    crudController.initialize();
  }
  
  public static final String OUTCOME = "feeAdjustedListPage";
}
