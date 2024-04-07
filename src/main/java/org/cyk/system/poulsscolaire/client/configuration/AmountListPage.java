package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.AmountDto;

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
  AmountCrudController crudController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des montants";
  }
  
  public static final String OUTCOME = "amountListPage";
}
