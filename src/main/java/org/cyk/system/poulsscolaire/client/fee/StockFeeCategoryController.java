package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.core.segregation.HasFeeCategoryAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasFeeCategoryIdentifierDto;
import ci.gouv.dgbf.extension.core.segregation.HasStockAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasStockIdentifierDto;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryService;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryService.StockFeeCategoryCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockFeeCategoryService.StockFeeCategoryUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link StockFeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockFeeCategoryController extends AbstractController {

  @Inject
  StockFeeCategoryClient client;

  @Inject
  StockFeeCategoryRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  StockFeeCategoryFilterController filterController;

  @Inject
  @Getter
  StockSelectOneController stockSelectOneController;

  @Inject
  @Getter
  FeeCategorySelectOneController feeCategorySelectOneController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StockFeeCategoryDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(StockFeeCategoryDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StockFeeCategoryService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getStockIdentifier(),
        HasStockAsStringDto.JSON_STOCK_AS_STRING,
        HasFeeCategoryAsStringDto.JSON_FEE_CATEGORY_AS_STRING);

    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> {
      ((StockFeeCategoryDto) entity)
          .setStockIdentifier(filterController.getFilter().getStockIdentifier());
      ((StockFeeCategoryDto) entity)
          .setFeeCategoryIdentifier(filterController.getFilter().getFeeCategoryIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      StockFeeCategoryCreateRequestDto request =
          requestMapper.mapCreate((StockFeeCategoryDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            HasStockIdentifierDto.JSON_STOCK_IDENTIFIER,
            HasFeeCategoryIdentifierDto.JSON_FEE_CATEGORY_IDENTIFIER));

    listController.getUpdateController().addEntityConsumer(entity -> {
      stockSelectOneController.getSelectOneMenu()
          .writeValue(((StockFeeCategoryDto) entity).getStockIdentifier());
      feeCategorySelectOneController.getSelectOneMenu()
          .writeValue(((StockFeeCategoryDto) entity).getFeeCategoryIdentifier());
    });

    listController.getUpdateController().setFunction(entity -> {
      StockFeeCategoryUpdateRequestDto request =
          requestMapper.mapUpdate((StockFeeCategoryDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    stockSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getStockIdentifier()));
    stockSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockFeeCategoryDto.class)
            .setStockIdentifier(identifier));

    feeCategorySelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getFeeCategoryIdentifier()));
    feeCategorySelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockFeeCategoryDto.class)
            .setFeeCategoryIdentifier(identifier));
  }
}
