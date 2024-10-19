package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.accounting.AccountingAccountSelectOneController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationService;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationService.SchoolConfigurationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationService.SchoolConfigurationUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SchoolConfigurationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolConfigurationController extends AbstractController {

  @Inject
  SchoolConfigurationClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  AccountingAccountSelectOneController paymentAccountingAccountSelectOneController;

  @Inject
  SchoolConfigurationRequestMapper requestMapper;

  @Inject
  @Getter
  SchoolConfigurationFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Scolarités";
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SchoolConfigurationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SchoolConfigurationService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        SchoolConfigurationDto.JSON_SCHOOL_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        SchoolConfigurationDto.JSON_PAYMENT_ACCOUNTING_ACCOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(SchoolConfigurationReadPage.OUTCOME);

    listController.getCreateController()
        .addEntityConsumer(entity -> ((SchoolConfigurationDto) entity)
            .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      SchoolConfigurationCreateRequestDto request =
          requestMapper.mapCreate((SchoolConfigurationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            SchoolConfigurationDto.JSON_SCHOOL_IDENTIFIER,
            SchoolConfigurationDto.JSON_PAYMENT_ACCOUNTING_ACCOUNT_IDENTIFIER));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((SchoolConfigurationDto) entity).getSchoolIdentifier());
      paymentAccountingAccountSelectOneController.getSelectOneMenu()
          .writeValue(((SchoolConfigurationDto) entity).getPaymentAccountingAccountIdentifier());
    });

    listController.getUpdateController().setFunction(entity -> {
      SchoolConfigurationUpdateRequestDto request =
          requestMapper.mapUpdate((SchoolConfigurationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolConfigurationDto.class)
            .setSchoolIdentifier(identifier));

    paymentAccountingAccountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SchoolConfigurationDto.class)
            .setPaymentAccountingAccountIdentifier(identifier));
  }
}
