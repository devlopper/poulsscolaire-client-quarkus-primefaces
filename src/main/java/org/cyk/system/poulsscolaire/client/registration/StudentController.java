package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.BloodGroupSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.GenderSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.registration.BloodGroup;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService.StudentCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService.StudentUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StudentController extends AbstractController {

  @Inject
  StudentClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOne;

  @Inject
  @Getter
  GenderSelectOne genderSelectOne;

  @Inject
  @Getter
  BloodGroupSelectOne bloodGroupSelectOne;

  @Inject
  @Getter
  StudentFilterController filterController;

  @Inject
  StudentRequestMapper studentRequestMapper;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StudentDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(StudentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StudentService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    Core.runIfNull(filterController.getFilter().getSchoolIdentifier(), () -> {
      projection.addNames(StudentDto.JSON_SCHOOL_AS_STRING);
    });
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, StudentDto.JSON_REGISTRATION_NUMBER,
        StudentDto.JSON_FIRST_NAME, StudentDto.JSON_ARABIC_FIRST_NAME, StudentDto.JSON_LAST_NAMES,
        StudentDto.JSON_ARABIC_LAST_NAMES, StudentDto.JSON_GENDER_AS_STRING,
        StudentDto.JSON_BLOOD_GROUP_AS_STRING, StudentDto.JSON_BIRTH_DATE_AS_STRING,
        StudentDto.JSON_BIRTH_PLACE, StudentDto.JSON_BIRTH_CERTIFICATE_REFERENCE,
        StudentDto.JSON_NATIONALITY, StudentDto.JSON_ORIGIN_SCHOOL, StudentDto.JSON_RESIDENCE,
        StudentDto.JSON_EMAIL_ADDRESS, StudentDto.JSON_PHONE_NUMBER, StudentDto.JSON_HEALTH_STATUS);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);
    
    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(StudentReadPage.OUTCOME);
    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    listController.getCreateController().addEntityConsumer(entity -> ((StudentDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      StudentCreateRequestDto request = studentRequestMapper.mapCreate((StudentDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            StudentDto.JSON_SCHOOL_IDENTIFIER, StudentDto.JSON_REGISTRATION_NUMBER,
            StudentDto.JSON_FIRST_NAME, StudentDto.JSON_ARABIC_FIRST_NAME,
            StudentDto.JSON_LAST_NAMES, StudentDto.JSON_ARABIC_LAST_NAMES,
            StudentDto.JSON_GENDER_IDENTIFIER, StudentDto.JSON_BLOOD_GROUP,
            StudentDto.JSON_BIRTH_DATE, StudentDto.JSON_BIRTH_PLACE,
            StudentDto.JSON_BIRTH_CERTIFICATE_REFERENCE, StudentDto.JSON_NATIONALITY,
            StudentDto.JSON_ORIGIN_SCHOOL, StudentDto.JSON_RESIDENCE, StudentDto.JSON_EMAIL_ADDRESS,
            StudentDto.JSON_PHONE_NUMBER, StudentDto.JSON_HEALTH_STATUS));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOne.getSelectOneMenu().writeValue(((StudentDto) entity).getSchoolIdentifier());
      genderSelectOne.getSelectOneRadio().writeValue(((StudentDto) entity).getGenderIdentifier());
      Core.runIfNotNull(((StudentDto) entity).getBloodGroup(), () -> bloodGroupSelectOne
          .getSelectOneRadio().writeValue(((StudentDto) entity).getBloodGroup().name()));
    });

    listController.getUpdateController().setFunction(entity -> {
      StudentUpdateRequestDto request = studentRequestMapper.mapUpdate((StudentDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    if (Core.isStringBlank(filterController.getFilter().getSchoolIdentifier())) {
      schoolSelectOne.getSelectOneMenu().addValueConsumer(
          identifier -> ((StudentDto) listController.getCreateControllerOrUpdateControllerEntity())
              .setSchoolIdentifier(identifier));
    } else {
      schoolSelectOne.getSelectOneMenu().setRendered(false);
    }

    genderSelectOne.getSelectOneRadio().addValueConsumer(
        identifier -> ((StudentDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setGenderIdentifier(identifier));

    bloodGroupSelectOne.getSelectOneRadio().addValueConsumer(
        name -> ((StudentDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setBloodGroup(Core.getEnumerationValueByName(BloodGroup.class, name)));
  }
}
