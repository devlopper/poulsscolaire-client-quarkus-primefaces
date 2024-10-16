package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;

/**
 * Cette classe représente la page de liste de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DeadlineListPage extends AbstractPage {

  @Inject
  @Getter
  DeadlineController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des échéances";
  }
  
  public static final String OUTCOME = "deadlineListPage";
}
