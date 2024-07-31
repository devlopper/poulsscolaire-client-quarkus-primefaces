package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
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
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;

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
  StudentSelectOne studentSelectOne;

  @Inject
  @Getter
  SchoolingSelectOne schoolingSelectOne;

  @Inject
  @Getter
  SenioritySelectOne senioritySelectOne;

  @Inject
  @Getter
  AssignmentTypeSelectOne assignmentTypeSelectOne;

  @Inject
  @Getter
  RegistrationFilterController filterController;

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

    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(RegistrationReadPage.OUTCOME);
    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(4);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((RegistrationDto) entity)
          .setStudentIdentifier(filterController.getFilter().getStudentIdentifier());
    });

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

    schoolingSelectOne.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchoolingIdentifier(identifier));

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
          .useValue(filterController.getFilter().getStudentIdentifier());
      listController.showCreateDialog();
    }

  }
}
