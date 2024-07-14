package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente la page de liste de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StudentListPage extends AbstractPage {

  @Inject
  @Getter
  StudentController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des élèves";
  }
  
  public static final String OUTCOME = "studentListPage";
}
