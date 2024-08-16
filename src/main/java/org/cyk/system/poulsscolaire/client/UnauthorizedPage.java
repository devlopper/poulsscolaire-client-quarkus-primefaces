package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 * Cette classe représente la page de non authorisation.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class UnauthorizedPage extends AbstractPage {


  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Non autorisé";
    title = contentTitle;
  }

  static final String PATH = "public/error/unauthorized.xhtml";
}
