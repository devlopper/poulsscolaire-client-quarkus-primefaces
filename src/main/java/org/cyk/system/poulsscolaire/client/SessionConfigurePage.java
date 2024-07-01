package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneMenuString;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolService;

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
  SchoolClient schoolClient;

  @Getter
  SelectOneMenuString schoolSelectOneMenu;

  @Inject
  SessionController sessionController;

  public SessionConfigurePage() {
    contentTitle = "Configuation de la session";
    title = contentTitle;
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    schoolSelectOneMenu = new SelectOneMenuString();
    schoolSelectOneMenu.outputLabel().setValue("École");
    schoolSelectOneMenu
        .setChoices(
            new ActionExecutor<>(this, SchoolService.GET_MANY_IDENTIFIER,
                () -> schoolClient
                    .getMany(
                        new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                            AbstractIdentifiableNamableDto.JSON_NAME),
                        null, null, userIdentifier, null)
                    .getDatas().stream()
                    .map(dto -> new SelectItem(dto.getIdentifier(), dto.getName())).toList())
                        .execute());
  }

  /**
   * Cette méthode permet d'exécuter.
   */
  public void execute() {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    session.setAttribute(sessionController.schoolIdentifierName, schoolSelectOneMenu.getValue());
  }

  static final String OUTCOME = "sessionConfigurePage";
  static final String PATH = "private/__session__/configure.xhtml";
}
