package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.client.configuration.AssignmentTypeSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SchoolingSelectOne;
import org.cyk.system.poulsscolaire.client.configuration.SenioritySelectOne;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeFilter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService.FeeCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeService.FeeUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link FeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeController extends AbstractController {

  @Inject
  FeeClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SchoolingSelectOne schoolingSelectOne;

  @Inject
  @Getter
  AssignmentTypeSelectOne assignmentTypeSelectOne;

  @Inject
  @Getter
  SenioritySelectOne senioritySelectOne;

  @Inject
  @Getter
  FeeCategorySelectOne categorySelectOne;

  @Inject
  @Getter
  FeeFilterController filterController;

  @Inject
  @Getter
  SchoolingSelectOne schoolingFilterSelectOne;

  @Inject
  @Getter
  AssignmentTypeSelectOne assignmentTypeFilterSelectOne;

  @Inject
  @Getter
  SenioritySelectOne seniorityFilterSelectOne;

  @Getter
  @Setter
  FeeDto sum;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FeeDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(FeeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FeeService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    if (Core.isStringBlank(filterController.getFilter().getSchoolingIdentifier())) {
      projection.addNames(FeeDto.JSON_SCHOOLING_SCHOOL_AS_STRING,
          FeeDto.JSON_SCHOOLING_PERIOD_AS_STRING, FeeDto.JSON_SCHOOLING_BRANCH_AS_STRING);
    }
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        FeeDto.JSON_ASSIGNMENT_TYPE_AS_STRING, FeeDto.JSON_SENIORITY_AS_STRING,
        FeeDto.JSON_CATEGORY_AS_STRING, AbstractAmountContainerDto.JSON_AMOUNT_VALUE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_DEADLINE_AS_STRING,
        AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(FeeReadPage.OUTCOME);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> {
      ((FeeDto) entity)
          .setSchoolingIdentifier(filterController.getFilter().getSchoolingIdentifier());
      ((FeeDto) entity).setAmountOptional(filterController.getFilter().getAmountOptional());
    });

    listController.getUpdateController().addEntityConsumer(entity -> {
      categorySelectOne.getSelectOneMenu().useValue(((FeeDto) entity).getCategoryIdentifier());
      assignmentTypeSelectOne.getSelectOneRadio()
          .useValue(((FeeDto) entity).getAssignmentTypeIdentifier());
      senioritySelectOne.getSelectOneRadio().useValue(((FeeDto) entity).getSeniorityIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      FeeCreateRequestDto request = new FeeCreateRequestDto();
      request.setSchoolingIdentifier(((FeeDto) entity).getSchoolingIdentifier());
      request.setAssignmentTypeIdentifier(((FeeDto) entity).getAssignmentTypeIdentifier());
      request.setSeniorityIdentifier(((FeeDto) entity).getSeniorityIdentifier());
      request.setCategoryIdentifier(((FeeDto) entity).getCategoryIdentifier());
      request.setValue(((FeeDto) entity).getAmountValue());
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
      request.setSchoolingIdentifier(
          Optional.ofNullable(filterController.getFilter().getSchoolingIdentifier())
              .orElse(((FeeDto) entity).getSchoolingIdentifier()));
      request.setAssignmentTypeIdentifier(((FeeDto) entity).getAssignmentTypeIdentifier());
      request.setSeniorityIdentifier(((FeeDto) entity).getSeniorityIdentifier());
      request.setCategoryIdentifier(((FeeDto) entity).getCategoryIdentifier());
      request.setValue(((FeeDto) entity).getAmountValue());
      request.setRegistrationValuePart(((FeeDto) entity).getAmountRegistrationValuePart());
      request.setOptional(((FeeDto) entity).getAmountOptional());
      request.setPaymentOrderNumber(((FeeDto) entity).getAmountPaymentOrderNumber());
      request.setRenewable(((FeeDto) entity).getAmountRenewable());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FeeDto.JSON_SCHOOLING_IDENTIFIER, FeeDto.JSON_ASSIGNMENT_TYPE_IDENTIFIER,
            FeeDto.JSON_SENIORITY_IDENTIFIER, FeeDto.JSON_CATEGORY_IDENTIFIER,
            AbstractAmountContainerDto.JSON_AMOUNT_VALUE,
            AbstractAmountContainerDto.JSON_AMOUNT_REGISTRATION_VALUE_PART,
            AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL,
            AbstractAmountContainerDto.JSON_AMOUNT_RENEWABLE,
            AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER));

    listController.getFilterController().addFilterConsumer(f -> {
      FeeFilter filter = (FeeFilter) f;
      if (filter.getSchoolingIdentifier() != null && filter.getAssignmentTypeIdentifier() != null
          && filter.getSeniorityIdentifier() != null) {
        sum = client.getOne(
            new ProjectionDto().addNames(FeeDto.JSON_AMOUNT_VALUE_SUM_AS_STRING,
                FeeDto.JSON_AMOUNT_REGISTRATION_SUM_AS_STRING),
            filter.toDto(), userIdentifier, null);
      }
    });

    if (Core.isStringBlank(filterController.getFilter().getSchoolingIdentifier())) {
      schoolingSelectOne.getSelectOneMenu().addValueConsumer(
          identifier -> ((FeeDto) listController.getCreateControllerOrUpdateControllerEntity())
              .setSchoolingIdentifier(identifier));

      schoolingFilterSelectOne.getSelectOneMenu().addValueConsumer(
          identifier -> filterController.getFilter().setSchoolingIdentifier(identifier));
    } else {
      schoolingSelectOne = null;
      schoolingFilterSelectOne = null;
    }

    assignmentTypeSelectOne.getSelectOneRadio().addValueConsumer(
        identifier -> ((FeeDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setAssignmentTypeIdentifier(identifier));

    senioritySelectOne.getSelectOneRadio().addValueConsumer(
        identifier -> ((FeeDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setSeniorityIdentifier(identifier));

    categorySelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> ((FeeDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setCategoryIdentifier(identifier));

    /* Filter */

    assignmentTypeFilterSelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> filterController.getFilter().setAssignmentTypeIdentifier(identifier));

    seniorityFilterSelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> filterController.getFilter().setSeniorityIdentifier(identifier));
  }
}
