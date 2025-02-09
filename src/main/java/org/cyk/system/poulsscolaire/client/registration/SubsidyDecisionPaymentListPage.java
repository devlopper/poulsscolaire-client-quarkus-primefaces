package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentDto;

/**
 * Cette classe représente la page de liste de {@link SubsidyDecisionPaymentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionPaymentListPage extends AbstractPage {

  @Inject
  @Getter
  SubsidyDecisionPaymentController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + SubsidyDecisionPaymentDto.NAME;
    controller.initialize();
  }

  public static final String OUTCOME = "subsidyDecisionPaymentListPage";
}
