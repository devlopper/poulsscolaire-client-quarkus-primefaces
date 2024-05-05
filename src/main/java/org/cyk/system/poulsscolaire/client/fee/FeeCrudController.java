package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
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
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService.FeeCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService.FeeUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de CRUD de {@link FeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeCrudController extends AbstractController {

  @Inject
  FeeClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  SchoolingClient schoolingClient;

  @Getter
  @Setter
  List<SelectItem> schoolings;

  @Inject
  AssignmentTypeClient assignmentTypeClient;

  @Getter
  @Setter
  List<SelectItem> assignmentTypes;

  @Inject
  SeniorityClient seniorityClient;

  @Getter
  @Setter
  List<SelectItem> seniorities;

  @Inject
  FeeCategoryClient categoryClient;

  @Getter
  @Setter
  List<SelectItem> categories;

  @Inject
  DeadlineClient deadlineClient;

  @Getter
  @Setter
  List<SelectItem> deadlines;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Frais";

    listController.setEntityClass(FeeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FeeService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, FeeDto.JSON_SCHOOLING_AS_STRING,
        FeeDto.JSON_ASSIGNMENT_TYPE_AS_STRING, FeeDto.JSON_SENIORITY_AS_STRING,
        FeeDto.JSON_CATEGORY_AS_STRING, AbstractAmountContainerDto.JSON_AMOUNT_VALUE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      FeeCreateRequestDto request = new FeeCreateRequestDto();
      request.setSchoolingIdentifier(((FeeDto) entity).getSchoolingIdentifier());
      request.setAssignmentTypeIdentifier(((FeeDto) entity).getAssignmentTypeIdentifier());
      request.setSeniorityIdentifier(((FeeDto) entity).getSeniorityIdentifier());
      request.setAssignmentTypeIdentifier(((FeeDto) entity).getAssignmentTypeIdentifier());
      request.setCategoryIdentifier(((FeeDto) entity).getCategoryIdentifier());
      request.setValue(((FeeDto) entity).getAmountValue());
      request.setDeadlineIdentifier(((FeeDto) entity).getAmountDeadlineIdentifier());
      request.setRegistrationValuePart(((FeeDto) entity).getAmountRegistrationValuePart());
      request.setOptional(((FeeDto) entity).getAmountOptional());
      request.setPaymentOrderNumber(((FeeDto) entity).getAmountPaymentOrderNumber());
      request.setRenewable(((FeeDto) entity).getAmountRenewable());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      FeeUpdateRequestDto request = new FeeUpdateRequestDto();
      request.setIdentifier(((FeeDto) entity).getIdentifier());
      request.setAssignmentTypeIdentifier(((FeeDto) entity).getAssignmentTypeIdentifier());
      request.setValue(((FeeDto) entity).getAmountValue());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    listController.getUpdateController().setProjection(new ProjectionDto().addNames(
        AbstractIdentifiableDto.JSON_IDENTIFIER, AbstractAmountContainerDto.JSON_AMOUNT_VALUE,
        AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART,
        AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL,
        AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE,
        AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_IDENTIFIER,
        AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER));

    schoolings = new ActionExecutor<>(this, SchoolingService.GET_MANY_IDENTIFIER,
        () -> schoolingClient
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                SchoolingDto.JSON_SCHOOL_AS_STRING, SchoolingDto.JSON_BRANCH_AS_STRING,
                SchoolingDto.JSON_PERIOD_AS_STRING), null, null, userIdentifier, null)
            .getDatas().stream()
            .map(dto -> new SelectItem(dto.getIdentifier(), dto.getSchoolAsString() + " "
                + dto.getBranchAsString() + " " + dto.getPeriodAsString()))
            .toList()).execute();

    assignmentTypes = new ActionExecutor<>(this, AssignmentTypeService.GET_MANY_IDENTIFIER,
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

    categories = new ActionExecutor<>(this, FeeCategoryService.GET_MANY_IDENTIFIER,
        () -> categoryClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();

    deadlines = new ActionExecutor<>(this, DeadlineService.GET_MANY_IDENTIFIER,
        () -> deadlineClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}
