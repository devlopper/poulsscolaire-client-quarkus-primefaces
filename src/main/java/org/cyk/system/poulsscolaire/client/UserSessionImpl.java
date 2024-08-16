package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.NavigationManager;
import ci.gouv.dgbf.extension.primefaces.User;
import ci.gouv.dgbf.extension.primefaces.UserSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import org.cyk.system.poulsscolaire.client.fee.FeeCategoryListPage;
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;

/**
 * Cette classe représente une session utilisateur.
 *
 * @author Christian
 *
 */
@SessionScoped
@Named("userSession")
public class UserSessionImpl implements UserSession {

  User user;

  @Inject
  NavigationManager navigationManager;
  
  @Override
  public User getUser() {
    if (FacesContext.getCurrentInstance() == null) {
      return null;
    }
    if (user != null) {
      return user;
    }
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    UserDto dto = (UserDto) session.getAttribute("user");
    if (dto == null) {
      return null;
    }
    user = new User();
    user.setIdentifier(dto.getIdentifier());
    user.setRoles(dto.getRoles());
    return user;
  }

  @Override
  public String getUserIdentifier() {
    User u = getUser();
    return u == null ? "__INCONNU__" : u.getIdentifier();
  }

  @Override
  public String logout() {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    session.setAttribute("user", null);
    session.invalidate();
    navigationManager.navigate(FeeCategoryListPage.OUTCOME);
    return null;
  }

}
