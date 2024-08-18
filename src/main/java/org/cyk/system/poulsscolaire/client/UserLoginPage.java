package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.MessageManager;
import ci.gouv.dgbf.extension.primefaces.NavigationManager;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.GetOneRequestDto;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.configuration.UserClient;
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;
import org.cyk.system.poulsscolaire.server.api.configuration.UserFilter;

/**
 * Cette classe représente la page de connexion.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class UserLoginPage extends AbstractPage {

  @Getter
  @Setter
  String username;

  @Getter
  @Setter
  String pass;

  @Inject
  UserClient client;

  @Inject
  SessionController sessionController;

  @Inject
  NavigationManager navigationManager;

  @Inject
  MessageManager messageManager;

  /**
   * Cette méthode permet de se connecter.
   */
  public void login() {
    GetOneRequestDto request = new GetOneRequestDto();
    UserFilter filter = new UserFilter();
    filter.setIdentifier(username);
    filter.setPass(pass);
    request.setFilter(filter.toDto());
    request.projection().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, UserDto.JSON_ROLES);
    request.setAuditWho(username);
    new ActionExecutor<>(this, "login", () -> {
      UserDto user = client.getOne(request);
      if (user == null) {
        messageManager.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Nom d'utilisateur ou mot de passe incorrect", ""));
      } else {
        sessionController.login(user);
        navigationManager.navigate(SessionConfigurePage.OUTCOME);
      }
      return user;
    }).execute();
  }

  static final String PATH = "public/login/index.xhtml";
}
