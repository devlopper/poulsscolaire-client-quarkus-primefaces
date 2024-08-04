package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.User;
import ci.gouv.dgbf.extension.primefaces.UserSession;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Cette classe représente une session utilisateur.
 *
 * @author Christian
 *
 */
@ApplicationScoped
public class UserSessionImpl implements UserSession {

  @Override
  public User getUser() {
    return null;
  }

  @Override
  public String getUserIdentifier() {
    return "testeur";
  }

  @Override
  public String logout() {
    return null;
  }

}
