package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de liste de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionListPage extends AbstractPage {

  @Inject
  @Getter
  SubsidyDecisionController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + SubsidyDecisionDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "subsidyDecisionListPage";
}
