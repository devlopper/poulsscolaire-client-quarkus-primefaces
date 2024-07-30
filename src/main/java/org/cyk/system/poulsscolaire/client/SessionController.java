package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.PeriodDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;

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

  String schoolIdentifierName;

  String periodIdentifierName;

  public SessionController() {
    schoolIdentifierName = "school";
    periodIdentifierName = "period";
  }

  boolean configured(HttpSession session) {
    return session != null && session.getAttribute(schoolIdentifierName) != null
        && session.getAttribute(periodIdentifierName) != null;
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
}
