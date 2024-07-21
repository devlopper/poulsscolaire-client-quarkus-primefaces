package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDto;

/**
 * Cette classe représente la page de liste de {@link AmountDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class AmountListPage extends AbstractPage {

  @Inject
  @Getter
  AmountController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + AmountDto.NAME;
  }

  public static final String OUTCOME = "amountListPage";
}
