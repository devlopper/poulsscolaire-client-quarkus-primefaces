package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputTextController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.StockClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.StockService;
import org.cyk.system.poulsscolaire.server.api.fee.StockService.StockCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockService.StockUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link StockDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockController extends AbstractController {

  @Inject
  StockClient client;

  @Inject
  StockRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  StockFilterController filterController;

  @Inject
  @Getter
  FeeCategorySelectOneController feeCategorySelectOneController;

  @Inject
  @Getter
  InputTextController nameInputTextController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StockDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(StockDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StockService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getFeeCategoryIdentifier(),
        StockDto.JSON_FEE_CATEGORY_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        StockDto.JSON_QUANTITY_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(StockReadPage.OUTCOME);
    
    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> {
      ((StockDto) entity)
          .setFeeCategoryIdentifier(filterController.getFilter().getFeeCategoryIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      StockCreateRequestDto request = requestMapper.mapCreate((StockDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            StockDto.JSON_FEE_CATEGORY_IDENTIFIER,
            AbstractIdentifiableCodableNamableDto.JSON_NAME));

    listController.getUpdateController().addEntityConsumer(entity -> {
      feeCategorySelectOneController.getSelectOneMenu()
          .writeValue(((StockDto) entity).getFeeCategoryIdentifier());
      nameInputTextController.getInputText().writeValue(((StockDto) entity).getName());
    });

    listController.getUpdateController().setFunction(entity -> {
      StockUpdateRequestDto request = requestMapper.mapUpdate((StockDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    feeCategorySelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDto.class)
            .setFeeCategoryIdentifier(identifier));

    nameInputTextController.setOutputLableValue("Libellé");
    nameInputTextController.getInputText().addValueConsumer(name -> listController
        .getCreateControllerOrUpdateControllerEntityAs(StockDto.class).setName(name));
  }
}
