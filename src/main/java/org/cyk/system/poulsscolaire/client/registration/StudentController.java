package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderClient;
import org.cyk.system.poulsscolaire.server.api.configuration.GenderService;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
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
  GenderClient genderClient;

  @Getter
  @Setter
  List<SelectItem> genders;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StudentDto.NAME;

    listController.setEntityClass(StudentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StudentService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, StudentDto.JSON_REGISTRATION_NUMBER,
        StudentDto.JSON_FIRST_NAME, StudentDto.JSON_LAST_NAMES, StudentDto.JSON_GENDER_AS_STRING,
        StudentDto.JSON_BIRTH_DATE_AS_STRING, StudentDto.JSON_BIRTH_PLACE,
        StudentDto.JSON_EMAIL_ADDRESS, StudentDto.JSON_PHONE_NUMBER);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      StudentCreateRequestDto request = new StudentCreateRequestDto();
      request.setRegistrationNumber(((StudentDto) entity).getRegistrationNumber());
      request.setFirstName(((StudentDto) entity).getFirstName());
      request.setLastNames(((StudentDto) entity).getLastNames());
      request.setGenderIdentifier(((StudentDto) entity).getGenderIdentifier());
      request.setBirthDate(((StudentDto) entity).getBirthDate());
      request.setBirthPlace(((StudentDto) entity).getBirthPlace());
      request.setSchoolIdentifier(((StudentDto) entity).getSchoolIdentifier());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            StudentDto.JSON_REGISTRATION_NUMBER, StudentDto.JSON_FIRST_NAME,
            StudentDto.JSON_LAST_NAMES, StudentDto.JSON_GENDER_IDENTIFIER));

    listController.getUpdateController().setFunction(entity -> {
      StudentUpdateRequestDto request = new StudentUpdateRequestDto();
      request.setRegistrationNumber(((StudentDto) entity).getRegistrationNumber());
      request.setFirstName(((StudentDto) entity).getFirstName());
      request.setLastNames(((StudentDto) entity).getLastNames());
      request.setGenderIdentifier(((StudentDto) entity).getGenderIdentifier());
      request.setBirthDate(((StudentDto) entity).getBirthDate());
      request.setBirthPlace(((StudentDto) entity).getBirthPlace());
      request.setSchoolIdentifier(((StudentDto) entity).getSchoolIdentifier());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    genders = new ActionExecutor<>(this, GenderService.GET_MANY_IDENTIFIER,
        () -> genderClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}
