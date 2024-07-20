package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineDto;

/**
 * Cette classe représente la page de liste de {@link AmountDeadlineDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AmountDeadlineListPage extends AbstractPage {

  @Inject
  @Getter
  AmountDeadlineController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AmountDeadlineDto.NAME;



    controller.initialize();
  }

  public static final String OUTCOME = "adjustedFeeListPage";
}
