package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;

/**
 * Cette classe représente un contrôleur de session.
 *
 * @author Christian
 *
 */
@Named
@SessionScoped
@Getter
public class SessionController extends AbstractController {

  String userIdentifierName;

  String schoolIdentifierName;

  String periodIdentifierName;

  boolean authentifiable;
  
  /**
   * Cette méthode permet de construire un objet.
   */
  public SessionController() {
    userIdentifierName = "user";
    schoolIdentifierName = "school";
    periodIdentifierName = "period";
    
    String value = System.getProperty("authentifiable");
    authentifiable = Core.isStringBlank(value) || Boolean.parseBoolean(value);
  }

  boolean loggedIn(HttpSession session) {
    return session != null && session.getAttribute(userIdentifierName) != null;
  }

  /**
   * Cette méthode permet de connecter un utilisateur.
   *
   * @param user utilisateur
   */
  public void login(UserDto user) {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    session.setAttribute(userIdentifierName, user);
  }

  /**
   * Cette méthode permet d'obtenir l'utilisateur.
   *
   * @return utilisateur
   */
  public UserDto getUser() {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    return (UserDto) session.getAttribute(userIdentifierName);
  }

  boolean configured(HttpSession session) {
    return session != null && session.getAttribute(schoolIdentifierName) != null
    /* && session.getAttribute(periodIdentifierName) != null */;
  }

  /**
   * Cette méthode permet de configurer la session.
   *
   * @param schoolIdentifier identifiant école
   * @param schoolName nom école
   * @param periodIdentifier identifiant période
   * @param periodName nom période
   */
  public void configure(String schoolIdentifier, String schoolName, String periodIdentifier,
      String periodName) {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    SchoolDto school = new SchoolDto();
    school.setIdentifier(schoolIdentifier);
    school.setName(schoolName);
    session.setAttribute(schoolIdentifierName, school);

    if (periodIdentifier != null) {
      PeriodDto period = new PeriodDto();
      period.setIdentifier(periodIdentifier);
      period.setName(periodName);
      session.setAttribute(periodIdentifierName, period);
    }
  }

  /**
   * Cette méthode permet d'obtenir l'identifiant de {@link SchoolDto}.
   *
   * @return identifiant de {@link SchoolDto}
   */
  public String getSchoolIdentifier() {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    SchoolDto school = (SchoolDto) session.getAttribute(schoolIdentifierName);
    if (school == null) {
      return null;
    }
    return school.getIdentifier();
  }

  /**
   * Cette méthode permet d'obtenir l'identifiant de {@link PeriodDto}.
   *
   * @return identifiant de {@link PeriodDto}
   */
  public String getPeriodIdentifier() {
    HttpSession session =
        (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    PeriodDto period = (PeriodDto) session.getAttribute(periodIdentifierName);
    if (period == null) {
      return null;
    }
    return period.getIdentifier();
  }

  /**
   * Cette méthode permet de savoir si la role est possédé.
   *
   * @param role role
   * @return oui ou non si la role est possédé
   */
  public boolean hasRole(String role) {
    if (!authentifiable) {
      return true;
    }
    UserDto user = getUser();
    if (user == null) {
      return false;
    }
    return user.hasOneOfRolesArray(role);
  }

  /**
   * Cette méthode permet de savoir si un des roles est possédé.
   *
   * @param roles roles
   * @return oui ou non si un des roles est possédé
   */
  public boolean hasOneOfRoles(ArrayList<String>  roles) {
    if (!authentifiable) {
      return true;
    }
    UserDto user = getUser();
    if (user == null) {
      return false;
    }
    return user.hasOneOfRolesArray(roles.toArray(new String[] {}));
  }
}
