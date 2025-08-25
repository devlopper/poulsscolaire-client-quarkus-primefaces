package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationDto;

/**
 * Cette classe représente la page de liste de {@link SubsidyDecisionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionRegistrationListPage extends AbstractPage {

  @Inject
  @Getter
  SubsidyDecisionRegistrationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + SubsidyDecisionRegistrationDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "subsidyDecisionRegistrationListPage";
}
