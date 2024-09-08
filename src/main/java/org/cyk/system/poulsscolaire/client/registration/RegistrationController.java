package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.IdentifiableActionController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.AssignmentTypeSelectOneController;
import org.cyk.system.poulsscolaire.client.configuration.BranchInstanceSelectOneController;
import org.cyk.system.poulsscolaire.client.configuration.SchoolingSelectOneController;
import org.cyk.system.poulsscolaire.client.configuration.SenioritySelectOneController;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchDto;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceDto;
import org.cyk.system.poulsscolaire.server.api.configuration.BranchInstanceFilter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService.RegistrationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService.RegistrationUpdateRequestDto;

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
  StudentSelectOneController studentSelectOneController;

  @Inject
  @Getter
  SchoolingSelectOneController schoolingSelectOneController;

  @Inject
  @Getter
  BranchInstanceSelectOneController branchInstanceSelectOneController;

  @Inject
  @Getter
  SchoolingSelectOneController schooling2SelectOneController;

  @Inject
  @Getter
  BranchInstanceSelectOneController branchInstance2SelectOneController;

  @Inject
  @Getter
  SenioritySelectOneController senioritySelectOneController;

  @Inject
  @Getter
  AssignmentTypeSelectOneController assignmentTypeSelectOneController;

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
        RegistrationDto.JSON_PAYABLE_REGISTRATION_AMOUNT_AS_STRING,
        RegistrationDto.JSON_BRANCH_INSTANCE_AS_STRING);
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

    schoolingSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchoolingIdentifier(identifier));
    schoolingSelectOneController.getSelectOneMenu().outputLabel().setValue(BranchDto.NAME + " 1");
    schoolingSelectOneController.getSelectOneMenu().valueChangeAjax().setRunnable(() -> {
      BranchInstanceFilter branchInstanceFilter = new BranchInstanceFilter();
      branchInstanceFilter
          .setSchoolingIdentifier(schoolingSelectOneController.getSelectOneMenu().getValue());
      branchInstanceSelectOneController.setFilter(branchInstanceFilter.toDto());
      branchInstanceSelectOneController.computeSelectOneMenuChoices();
    });
    schoolingSelectOneController.getSelectOneMenu().valueChangeAjax().setDisabled(false);
    schoolingSelectOneController.getSelectOneMenu().valueChangeAjax()
        .setUpdate(branchInstanceSelectOneController.getSelectOneMenu().getIdentifier());

    branchInstanceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity())
                .setBranchInstanceIdentifier(identifier));
    branchInstanceSelectOneController.getSelectOneMenu().outputLabel()
        .setValue(BranchInstanceDto.NAME + " 1");

    schooling2SelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchooling2Identifier(identifier));
    schooling2SelectOneController.getSelectOneMenu().outputLabel().setValue(BranchDto.NAME + " 2");
    schooling2SelectOneController.getSelectOneMenu().valueChangeAjax().setRunnable(() -> {
      BranchInstanceFilter branchInstanceFilter = new BranchInstanceFilter();
      branchInstanceFilter
          .setSchoolingIdentifier(schooling2SelectOneController.getSelectOneMenu().getValue());
      branchInstance2SelectOneController.setFilter(branchInstanceFilter.toDto());
      branchInstance2SelectOneController.computeSelectOneMenuChoices();
    });
    schooling2SelectOneController.getSelectOneMenu().valueChangeAjax().setDisabled(false);
    schooling2SelectOneController.getSelectOneMenu().valueChangeAjax()
        .setUpdate(branchInstance2SelectOneController.getSelectOneMenu().getIdentifier());

    branchInstance2SelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity())
                .setBranchInstance2Identifier(identifier));
    branchInstance2SelectOneController.getSelectOneMenu().outputLabel()
        .setValue(BranchInstanceDto.NAME + " 2");

    senioritySelectOneController.getSelectOneRadio()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSeniorityIdentifier(identifier));

    assignmentTypeSelectOneController.getSelectOneRadio()
        .addValueConsumer(identifier -> ((RegistrationDto) listController
            .getCreateControllerOrUpdateControllerEntity())
                .setAssignmentTypeIdentifier(identifier));

    if (filterController.getFilter().getStudentIdentifier() == null) {
      studentSelectOneController.getSelectOneMenu()
          .addValueConsumer(identifier -> ((RegistrationDto) listController
              .getCreateControllerOrUpdateControllerEntity()).setStudentIdentifier(identifier));
    } else {
      studentSelectOneController.getSelectOneMenu()
          .writeValue(filterController.getFilter().getStudentIdentifier());
      listController.showCreateDialog();
    }

    updateAmountsToZeroController
        .setFunction(identifier -> client.updateAmountsToZero(identifier, userIdentifier, null));
    listController.configureAction(updateAmountsToZeroController);
  }
}
