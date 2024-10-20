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
  AdjustedFeeController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des frais ajustés";
    
    if (controller.getFilterController().getFilter().getAmountOptional() == null) {
      controller.getFilterController().getFilter().setAmountOptional(false);
    }
    
    controller.initialize();
  }
  
  public static final String OUTCOME = "adjustedFeeListPage";
}
