package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;

/**
 * Cette classe représente la page de lecture de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StudentReadPage extends AbstractPage {

  @Inject
  StudentClient studentClient;

  @Getter
  StudentDto student;

  @Inject
  @Getter
  IdentityController parentController;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        StudentDto.JSON_IDENTITY_IDENTIFIER, StudentDto.JSON_SCHOOL_AS_STRING,
        AbstractIdentifiableCodableDto.JSON_CODE, StudentDto.JSON_REGISTRATION_NUMBER,
        StudentDto.JSON_FIRST_NAME, StudentDto.JSON_ARABIC_FIRST_NAME, StudentDto.JSON_LAST_NAMES,
        StudentDto.JSON_ARABIC_LAST_NAMES, StudentDto.JSON_GENDER_AS_STRING,
        StudentDto.JSON_BLOOD_GROUP_AS_STRING, StudentDto.JSON_BIRTH_DATE_AS_STRING,
        StudentDto.JSON_BIRTH_PLACE, StudentDto.JSON_BIRTH_CERTIFICATE_REFERENCE,
        StudentDto.JSON_NATIONALITY, StudentDto.JSON_ORIGIN_SCHOOL, StudentDto.JSON_RESIDENCE,
        StudentDto.JSON_EMAIL_ADDRESS, StudentDto.JSON_PHONE_NUMBER);

    projection.addNames(StudentDto.JSON_FATHER_IDENTIFIER, StudentDto.JSON_FATHER_FIRST_NAME,
        StudentDto.JSON_FATHER_LAST_NAMES, StudentDto.JSON_FATHER_PHONE_NUMBER,
        StudentDto.JSON_MOTHER_IDENTIFIER, StudentDto.JSON_MOTHER_FIRST_NAME,
        StudentDto.JSON_MOTHER_LAST_NAMES, StudentDto.JSON_MOTHER_PHONE_NUMBER,
        StudentDto.JSON_TUTOR_IDENTIFIER, StudentDto.JSON_TUTOR_FIRST_NAME,
        StudentDto.JSON_TUTOR_LAST_NAMES, StudentDto.JSON_TUTOR_PHONE_NUMBER);

    String identifier = getRequestParameterIdentifier();
    student = studentClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle = StudentDto.NAME + " - " + student.getCode();

    parentController.getListController().getDataTable().setTitle("Tableau de responsables");

    parentController.filterController.getFilter()
        .setRelationshipChildIdentifier(student.getIdentityIdentifier());
    parentController.initialize();

    parentController.getListController().getGotoReadPageButton().setRendered(false);
    parentController.getListController().getDataTable().getFilterButton().setRendered(false);
  }

  public static final String OUTCOME = "studentReadPage";
}
