package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.IdentifiableActionController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.AssignmentTypeSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SchoolingSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SenioritySelectOne;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingFilter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService.RegistrationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService.RegistrationUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentFilter;

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
  @Getter
  StudentSelectOneController studentSelectOne;

  @Inject
  @Getter
  SchoolingSelectOne schoolingSelectOne;

  @Inject
  @Getter
  SchoolingSelectOne schooling2SelectOne;

  @Inject
  @Getter
  SenioritySelectOne senioritySelectOne;

  @Inject
  @Getter
  AssignmentTypeSelectOne assignmentTypeSelectOne;

  @Inject
  RegistrationRequestMapper requestMapper;

  @Inject
  @Getter
  RegistrationFilterController filterController;

  @Inject
  @Getter
  IdentifiableActionController updateAmountsToZeroController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Inscription";

    listController.setEntityClass(RegistrationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(RegistrationService.PATH);
    listController.setFilterController(filterController);

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
    listController.getDataTable().getFilterButton().setRendered(true);
    
    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(RegistrationReadPage.OUTCOME);
    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(4);
    
    listController.getCreateController().addEntityConsumer(entity -> ((RegistrationDto) entity)
        .setStudentIdentifier(filterController.getFilter().getStudentIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      RegistrationCreateRequestDto request = requestMapper.mapCreate((RegistrationDto) entity);
      request.setSchooling2Identifier(((RegistrationDto) entity).getSchooling2Identifier());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            RegistrationDto.JSON_SCHOOLING_IDENTIFIER,
            RegistrationDto.JSON_ASSIGNMENT_TYPE_IDENTIFIER,
            RegistrationDto.JSON_SENIORITY_IDENTIFIER));

    listController.getUpdateController().setFunction(entity -> {
      RegistrationUpdateRequestDto request = requestMapper.mapUpdate((RegistrationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolingSelectOne.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchoolingIdentifier(identifier));
    schoolingSelectOne.getSelectOneMenu().outputLabel().setValue(BranchDto.NAME + " 1");

    schooling2SelectOne.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchooling2Identifier(identifier));
    schooling2SelectOne.getSelectOneMenu().outputLabel().setValue(BranchDto.NAME + " 2");

    senioritySelectOne.getSelectOneRadio()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSeniorityIdentifier(identifier));

    assignmentTypeSelectOne.getSelectOneRadio()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity())
                .setAssignmentTypeIdentifier(identifier));

    if (filterController.getFilter().getStudentIdentifier() == null) {
      studentSelectOne.getSelectOneMenu()
          .addValueConsumer(identifier -> ((RegistrationDto) listController
              .getCreateControllerOrUpdateControllerEntity()).setStudentIdentifier(identifier));
    } else {
      studentSelectOne.getSelectOneMenu()
          .writeValue(filterController.getFilter().getStudentIdentifier());
      listController.showCreateDialog();
    }

    if (!Core.isStringBlank(filterController.getFilter().getSchoolIdentifier())) {
      StudentFilter studentFilter = new StudentFilter();
      studentFilter.setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier());
      studentSelectOne.setFilter(studentFilter.toDto());
      studentSelectOne.computeSelectOneMenuChoices();

      SchoolingFilter schoolingFilter = new SchoolingFilter();
      schoolingFilter.setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier());
      schoolingSelectOne.setFilter(schoolingFilter.toDto());
      schoolingSelectOne.computeSelectOneMenuChoices();

      schooling2SelectOne.setFilter(schoolingFilter.toDto());
      schooling2SelectOne.computeSelectOneMenuChoices();
    }

    updateAmountsToZeroController
        .setFunction(identifier -> client.updateAmountsToZero(identifier, userIdentifier, null));
    listController.configureAction(updateAmountsToZeroController);
  }
}
