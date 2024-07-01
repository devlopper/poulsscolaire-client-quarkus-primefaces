package org.cyk.system.poulsscolaire.client;

import jakarta.enterprise.context.SessionScoped;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

/**
 * Cette classe représente un contrôleur de session.
 *
 * @author Christian
 *
 */
@SessionScoped
@Getter
public class SessionController {

  String schoolIdentifier;
  String schoolIdentifierName;
  
  String periodIdentifier;
  String periodIdentifierName;

  public SessionController() {
    schoolIdentifierName = "idEcole";
    periodIdentifierName = "idPeriode";
  }

  boolean configured(HttpSession session) {
    return session != null && session.getAttribute(schoolIdentifierName) != null;
  }
}
