package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

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
  
  public void login() {
    
  }
  
  static final String PATH = "public/login/index.xhtml";
}
