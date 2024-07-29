package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.NavigationManager;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.PeriodSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOne;

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
  SchoolSelectOne schoolSelectOne;

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

  /**
   * Cette méthode permet d'exécuter.
   */
  public void execute() {
    sessionController.configure(schoolSelectOne.getSelectOneMenu().getValue(),
        schoolSelectOne.getSelectOneMenu()
            .getChoiceByValue(schoolSelectOne.getSelectOneMenu().getValue()).getLabel(),
        periodSelectOne.getSelectOneMenu().getValue(), periodSelectOne.getSelectOneMenu()
            .getChoiceByValue(periodSelectOne.getSelectOneMenu().getValue()).getLabel());
    
    navigationManager.navigate("indexPage");
  }

  static final String OUTCOME = "sessionConfigurePage";
  static final String PATH = "private/__session__/configure.xhtml";
}
