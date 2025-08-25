package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.segregation.HasBranchInstanceAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasBranchInstanceIdentifierDto;
import ci.gouv.dgbf.extension.core.segregation.HasDateAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasDateDto;
import ci.gouv.dgbf.extension.core.segregation.HasQuantityAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasStockAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasStockIdentifierDto;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputDateController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.BranchInstanceSelectOneController;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionService;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionService.StockDistributionCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionService.StockDistributionUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link StockDistributionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockDistributionController extends AbstractController {

  @Inject
  StockDistributionClient client;

  @Inject
  StockDistributionRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  StockDistributionFilterController filterController;

  @Inject
  @Getter
  StockSelectOneController stockSelectOneController;

  @Inject
  @Getter
  BranchInstanceSelectOneController branchInstanceSelectOneController;

  @Inject
  @Getter
  InputDateController dateInputDateController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StockDistributionDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(StockDistributionDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StockDistributionService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, HasStockAsStringDto.JSON_STOCK_AS_STRING,
        HasBranchInstanceAsStringDto.JSON_BRANCH_INSTANCE_AS_STRING,
        HasDateAsStringDto.JSON_DATE_AS_STRING, HasQuantityAsStringDto.JSON_QUANTITY_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(StockDistributionReadPage.OUTCOME);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      StockDistributionCreateRequestDto request =
          requestMapper.mapCreate((StockDistributionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            HasStockIdentifierDto.JSON_STOCK_IDENTIFIER,
            HasBranchInstanceIdentifierDto.JSON_BRANCH_INSTANCE_IDENTIFIER, HasDateDto.JSON_DATE));

    listController.getUpdateController().setFunction(entity -> {
      StockDistributionUpdateRequestDto request =
          requestMapper.mapUpdate((StockDistributionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    stockSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDistributionDto.class)
            .setStockIdentifier(identifier));

    branchInstanceSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDistributionDto.class)
            .setBranchInstanceIdentifier(identifier));

    dateInputDateController.setOutputLableValue("Date");
    dateInputDateController.getInputLocalDateTime().addValueConsumer(date -> listController
        .getCreateControllerOrUpdateControllerEntityAs(StockDistributionDto.class).setDate(date));
  }
}
