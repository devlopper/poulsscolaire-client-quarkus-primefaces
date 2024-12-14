package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService.StockMovementCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService.StockMovementCreateResponseDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService.StockMovementDeleteResponseDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService.StockMovementUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockMovementService.StockMovementUpdateResponseDto;

/**
 * Cette classe représente le contrôleur de {@link StockMovementDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockMovementController extends AbstractController {

  @Inject
  StockMovementClient client;

  @Inject
  StockMovementRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  StockMovementFilterController filterController;

  @Inject
  @Getter
  StockSelectOneController stockSelectOneController;

  @Inject
  @Getter
  InputNumberController quantityInputNumberController;

  @Inject
  @Getter
  InputTextController reasonInputTextController;

  @Getter
  String totalQuantityAsString;

  @Getter
  String totalQuantityChangedEvent;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StockMovementDto.NAME;
    totalQuantityChangedEvent = "totalQuantityChangedEvent";
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(StockMovementDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StockMovementService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getStockIdentifier(),
        StockMovementDto.JSON_STOCK_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        StockMovementDto.JSON_QUANTITY_AS_STRING, StockMovementDto.JSON_REASON);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> ((StockMovementDto) entity)
        .setStockIdentifier(filterController.getFilter().getStockIdentifier()));

    listController.getCreateController().setFunction(entity -> {
      StockMovementCreateRequestDto request = requestMapper.mapCreate((StockMovementDto) entity);
      request.setAuditWho(userIdentifier);
      StockMovementCreateResponseDto response = client.create(request);
      totalQuantityAsString = response.getStockQuantityAsString();
      return response;
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            StockMovementDto.JSON_STOCK_IDENTIFIER, StockMovementDto.JSON_QUANTITY,
            StockMovementDto.JSON_REASON));

    listController.getUpdateController().addEntityConsumer(entity -> {
      stockSelectOneController.getSelectOneMenu()
          .writeValue(((StockMovementDto) entity).getStockIdentifier());
      quantityInputNumberController.getInputInteger()
          .writeValue(((StockMovementDto) entity).getQuantity());
      reasonInputTextController.getInputText().writeValue(((StockMovementDto) entity).getReason());
    });

    listController.getUpdateController().setFunction(entity -> {
      StockMovementUpdateRequestDto request = requestMapper.mapUpdate((StockMovementDto) entity);
      request.setAuditWho(userIdentifier);
      StockMovementUpdateResponseDto response = client.update(request);
      totalQuantityAsString = response.getStockQuantityAsString();
      return response;
    });

    listController.getDeleteController().addResponseConsumer(response -> totalQuantityAsString =
        ((StockMovementDeleteResponseDto) response).getStockQuantityAsString());

    stockSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getStockIdentifier()));
    stockSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockMovementDto.class)
            .setStockIdentifier(identifier));

    quantityInputNumberController.setOutputLableValue("Quantité");
    quantityInputNumberController.getInputInteger()
        .addValueConsumer(quantity -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockMovementDto.class)
            .setQuantity(quantity));

    reasonInputTextController.setOutputLableValue("Motif");
    reasonInputTextController.getInputText().addValueConsumer(name -> listController
        .getCreateControllerOrUpdateControllerEntityAs(StockMovementDto.class).setReason(name));
  }
}
