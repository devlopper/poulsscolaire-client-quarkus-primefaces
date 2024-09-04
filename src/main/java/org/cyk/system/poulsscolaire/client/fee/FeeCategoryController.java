package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneMenuString;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolService;
import org.cyk.system.poulsscolaire.server.api.fee.AmountStatisticsDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryFilter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService.FeeCategoryCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService.FeeCategoryUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeCategoryController extends AbstractController {

  @Inject
  FeeCategoryClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  FeeCategoryFilterController filterController;

  @Inject
  SchoolClient schoolClient;

  @Getter
  SelectOneMenuString schoolFilterSelectOneMenu;

  SchoolDto school;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOne;

  @Getter
  @Setter
  AmountStatisticsDto amountValueStatistics;

  @Getter
  @Setter
  AmountStatisticsDto amountRegistrationPartStatistics;

  @Inject
  FeeCategoryRequestMapper requestMapper;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FeeCategoryDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    schoolFilterSelectOneMenu = new SelectOneMenuString();
    schoolFilterSelectOneMenu.addValueConsumer(
        value -> filterController.getFilter().setRegistrationSchoolingSchoolIdentifier(value));

    listController.setEntityClass(FeeCategoryDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FeeCategoryService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        FeeCategoryDto.JSON_SCHOOL_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        FeeCategoryDto.JSON_TOTAL_AMOUNT_AS_STRING, FeeCategoryDto.JSON_PAID_AMOUNT_AS_STRING,
        FeeCategoryDto.JSON_PAYABLE_AMOUNT_AS_STRING,
        FeeCategoryDto.JSON_TOTAL_REGISTRATION_AMOUNT_AS_STRING,
        FeeCategoryDto.JSON_PAID_REGISTRATION_AMOUNT_AS_STRING,
        FeeCategoryDto.JSON_PAYABLE_REGISTRATION_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);
    //listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getDataTable().getOrderNumberColumn().setVisible(false);
    
    listController.getCreateController().addEntityConsumer(entity -> ((FeeCategoryDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));
    listController.getCreateController().setFunction(entity -> {
      FeeCategoryCreateRequestDto request = requestMapper.mapCreate((FeeCategoryDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FeeCategoryDto.JSON_SCHOOL_IDENTIFIER, AbstractIdentifiableCodableDto.JSON_CODE,
            AbstractIdentifiableCodableNamableDto.JSON_NAME));

    listController.getUpdateController().addEntityConsumer(entity -> schoolSelectOne
        .getSelectOneMenu().writeValue(((FeeCategoryDto) entity).getSchoolIdentifier()));

    listController.getUpdateController().setFunction(entity -> {
      FeeCategoryUpdateRequestDto request = requestMapper.mapUpdate((FeeCategoryDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolFilterSelectOneMenu
        .setChoices(
            new ActionExecutor<>(this, SchoolService.GET_MANY_IDENTIFIER,
                () -> schoolClient
                    .getMany(
                        new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                            AbstractIdentifiableNamableDto.JSON_NAME),
                        null, null, userIdentifier, null)
                    .getDatas().stream()
                    .map(dto -> new SelectItem(dto.getIdentifier(), dto.getName())).toList())
                        .execute());

    amountValueStatistics = new AmountStatisticsDto();
    amountRegistrationPartStatistics = new AmountStatisticsDto();

    listenFilter(filterController.getFilter());
    filterController.addFilterConsumer(this::listenFilter);

    schoolSelectOne.getSelectOneMenu()
        .addValueConsumer(identifier -> ((FeeCategoryDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setSchoolIdentifier(identifier));

    schoolSelectOne.getSelectOneMenu()
        .setRendered(Core.isStringBlank(filterController.getFilter().getSchoolIdentifier()));
  }

  void listenFilter(FeeCategoryFilter filter) {
    computeTotalAmounts(filter);
  }

  void computeTotalAmounts(FeeCategoryFilter filter) {
    if (!Core.isStringBlank(filter.getRegistrationSchoolingSchoolIdentifier())) {
      school = schoolClient.getByIdentifier(
          filterController.getFilter().getRegistrationSchoolingSchoolIdentifier(),
          new ProjectionDto().addNames(SchoolDto.JSON_TOTAL_AMOUNT_AS_STRING,
              SchoolDto.JSON_PAID_AMOUNT_AS_STRING, SchoolDto.JSON_PAYABLE_AMOUNT_AS_STRING,
              SchoolDto.JSON_TOTAL_REGISTRATION_AMOUNT_AS_STRING,
              SchoolDto.JSON_PAID_REGISTRATION_AMOUNT_AS_STRING,
              SchoolDto.JSON_PAYABLE_REGISTRATION_AMOUNT_AS_STRING),
          userIdentifier, null);
      amountValueStatistics.initializeWithValue(school);
      amountRegistrationPartStatistics.initializeWithRegistration(school);
    }
  }
}
