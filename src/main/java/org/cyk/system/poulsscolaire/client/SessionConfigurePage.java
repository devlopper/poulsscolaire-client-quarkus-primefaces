package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.NavigationManager;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.PeriodSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.client.fee.FeeCategoryListPage;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;

/**
 * Cette classe représente la page de configuration de session.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SessionConfigurePage extends AbstractPage {

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  PeriodSelectOne periodSelectOne;

  @Inject
  SessionController sessionController;

  @Inject
  NavigationManager navigationManager;

  public SessionConfigurePage() {
    contentTitle = "Configuation de la session";
    title = contentTitle;
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    UserDto user = sessionController.getUser();
    if (user != null) {
      SchoolFilter schoolFilter = new SchoolFilter();
      schoolFilter.setUserIdentifier(user.getIdentifier());
      schoolSelectOneController.setFilter(schoolFilter.toDto());
    }
  }

  /**
   * Cette méthode permet d'exécuter.
   */
  public void execute() {
    sessionController.configure(schoolSelectOneController.getSelectOneMenu().getValue(),
        schoolSelectOneController.getSelectOneMenu()
            .getChoiceByValue(schoolSelectOneController.getSelectOneMenu().getValue()).getLabel(),
        periodSelectOne.getSelectOneMenu().getValue(), periodSelectOne.getSelectOneMenu()
            .getChoiceByValue(periodSelectOne.getSelectOneMenu().getValue()).getLabel());

    navigationManager.navigate(FeeCategoryListPage.OUTCOME);
  }

  static final String OUTCOME = "sessionConfigurePage";
  static final String PATH = "private/__session__/configure.xhtml";
}
