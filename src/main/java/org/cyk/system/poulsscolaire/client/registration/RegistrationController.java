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
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeClient;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeService;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService;
import org.cyk.system.poulsscolaire.server.api.configuration.SeniorityClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SeniorityService;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService;

/**
 * Cette classe représente le contrôleur de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class RegistrationController extends AbstractController {

  @Inject
  RegistrationClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  StudentClient studentClient;

  @Getter
  @Setter
  List<SelectItem> students;

  @Inject
  SchoolingClient schoolingClient;

  @Getter
  @Setter
  List<SelectItem> schoolings;

  @Inject
  AssignmentTypeClient assignmentTypeClient;

  @Getter
  @Setter
  List<SelectItem> assignmentsType;

  @Inject
  SeniorityClient seniorityClient;

  @Getter
  @Setter
  List<SelectItem> seniorities;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Inscription";

    listController.setEntityClass(RegistrationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(RegistrationService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, RegistrationDto.JSON_STUDENT_AS_STRING,
        RegistrationDto.JSON_SCHOOLING_AS_STRING, RegistrationDto.JSON_ASSIGNMENT_TYPE_AS_STRING,
        RegistrationDto.JSON_SENIORITY_AS_STRING, RegistrationDto.JSON_TOTAL_AMOUNT_AS_STRING,
        RegistrationDto.JSON_PAID_AMOUNT_AS_STRING, RegistrationDto.JSON_PAYABLE_AMOUNT_AS_STRING,
        RegistrationDto.JSON_TOTAL_REGISTRATION_AMOUNT_AS_STRING,
        RegistrationDto.JSON_PAID_REGISTRATION_AMOUNT_AS_STRING,
        RegistrationDto.JSON_PAYABLE_REGISTRATION_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(RegistrationReadPage.OUTCOME);
    listController.getDataTable().getActionColumn().setWidth("150px");

    listController.getCreateController()
        .setFunction(entity -> client.create(((RegistrationDto) entity).getStudentIdentifier(),
            ((RegistrationDto) entity).getSchoolingIdentifier(),
            ((RegistrationDto) entity).getAssignmentTypeIdentifier(),
            ((RegistrationDto) entity).getSeniorityIdentifier(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((RegistrationDto) entity).getIdentifier(),
            ((RegistrationDto) entity).getStudentIdentifier(),
            ((RegistrationDto) entity).getSchoolingIdentifier(),
            ((RegistrationDto) entity).getAssignmentTypeIdentifier(),
            ((RegistrationDto) entity).getSeniorityIdentifier(), userIdentifier, null));

    students = new ActionExecutor<>(this, StudentService.GET_MANY_IDENTIFIER,
        () -> studentClient
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                StudentDto.JSON_FIRST_NAME), null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getFirstName()))
            .toList()).execute();

    schoolings = new ActionExecutor<>(this, SchoolingService.GET_MANY_IDENTIFIER,
        () -> schoolingClient
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                SchoolingDto.JSON_SCHOOL_AS_STRING, SchoolingDto.JSON_BRANCH_AS_STRING,
                SchoolingDto.JSON_PERIOD_AS_STRING), null, null, userIdentifier, null)
            .getDatas().stream()
            .map(dto -> new SelectItem(dto.getIdentifier(), String.format("%s %s %s",
                dto.getSchoolAsString(), dto.getBranchAsString(), dto.getPeriodAsString())))
            .toList()).execute();

    assignmentsType = new ActionExecutor<>(this, AssignmentTypeService.GET_MANY_IDENTIFIER,
        () -> assignmentTypeClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();

    seniorities = new ActionExecutor<>(this, SeniorityService.GET_MANY_IDENTIFIER,
        () -> seniorityClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();

  }
}
