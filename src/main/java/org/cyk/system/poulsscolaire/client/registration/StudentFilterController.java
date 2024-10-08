package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputText;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StudentFilterController extends AbstractFilterController<StudentFilter> {

  @Inject
  SessionController sessionController;

  @Getter
  InputText registrationNumberInputText;

  @Getter
  InputText firstNameInputText;

  @Getter
  InputText lastNamesInputText;

  @Getter
  InputText arabicFirstNameInputText;

  @Getter
  InputText arabicLastNamesInputText;

  public StudentFilterController() {
    super(StudentFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(StudentFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));

    registrationNumberInputText = new InputText();
    registrationNumberInputText.outputLabel().setValue("Matricule national");
    registrationNumberInputText.addValueConsumer(value -> filter.setRegistrationNumber(value));

    firstNameInputText = new InputText();
    firstNameInputText.outputLabel().setValue("Nom");
    firstNameInputText.addValueConsumer(value -> filter.setFirstName(value));

    lastNamesInputText = new InputText();
    lastNamesInputText.outputLabel().setValue("Prénom(s)");
    lastNamesInputText.addValueConsumer(value -> filter.setLastNames(value));

    arabicFirstNameInputText = new InputText();
    arabicFirstNameInputText.outputLabel().setValue("اسم");
    arabicFirstNameInputText.addValueConsumer(value -> filter.setArabicFirstName(value));

    arabicLastNamesInputText = new InputText();
    arabicLastNamesInputText.outputLabel().setValue("   الأسماء الأولى");
    arabicLastNamesInputText.addValueConsumer(value -> filter.setArabicLastNames(value));
  }
}
