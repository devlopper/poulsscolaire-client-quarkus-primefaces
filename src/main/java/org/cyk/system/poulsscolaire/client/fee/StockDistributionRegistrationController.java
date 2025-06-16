package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.enumeration.BusinessValueType;
import ci.gouv.dgbf.extension.core.segregation.HasDateDto;
import ci.gouv.dgbf.extension.core.segregation.HasDistributionAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasQuantityAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasQuantityDto;
import ci.gouv.dgbf.extension.core.segregation.HasRegistrationAsStringDto;
import ci.gouv.dgbf.extension.core.segregation.HasStockIdentifierDto;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.Column;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import ci.gouv.dgbf.extension.server.service.api.response.IdentifiableResponseDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.function.Function;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.registration.RegistrationSelectOneController;
import org.cyk.system.poulsscolaire.server.api.configuration.HasBranchInstanceIdentifierDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationClient;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationService;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationService.StockDistributionRegistrationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationService.StockDistributionRegistrationUpdateQuantityRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.StockDistributionRegistrationService.StockDistributionRegistrationUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link StockDistributionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StockDistributionRegistrationController extends AbstractController {

  @Inject
  StockDistributionRegistrationClient client;

  @Inject
  StockDistributionRegistrationRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  StockDistributionRegistrationFilterController filterController;

  @Inject
  @Getter
  StockDistributionSelectOneController distributionSelectOneController;

  @Inject
  @Getter
  RegistrationSelectOneController registrationSelectOneController;

  @Inject
  @Getter
  InputNumberController quantityInputNumberController;

  @Getter
  Column quantityColumn;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = StockDistributionRegistrationDto.NAME;
    quantityColumn = Column.newBuilder().businessValueType(BusinessValueType.QUANTITY).build();
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(StockDistributionRegistrationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(StockDistributionRegistrationService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        HasDistributionAsStringDto.JSON_DISTRIBUTION_AS_STRING,
        HasRegistrationAsStringDto.JSON_REGISTRATION_AS_STRING, HasQuantityDto.JSON_QUANTITY,
        HasQuantityAsStringDto.JSON_QUANTITY_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      StockDistributionRegistrationCreateRequestDto request =
          requestMapper.mapCreate((StockDistributionRegistrationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            HasStockIdentifierDto.JSON_STOCK_IDENTIFIER,
            HasBranchInstanceIdentifierDto.JSON_BRANCH_INSTANCE_IDENTIFIER, HasDateDto.JSON_DATE));

    listController.getUpdateController().setFunction(entity -> {
      StockDistributionRegistrationUpdateRequestDto request =
          requestMapper.mapUpdate((StockDistributionRegistrationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    distributionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDistributionRegistrationDto.class)
            .setDistributionIdentifier(identifier));

    registrationSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDistributionRegistrationDto.class)
            .setRegistrationIdentifier(identifier));

    quantityInputNumberController.setOutputLableValue("Quantité");
    quantityInputNumberController.getInputInteger()
        .addValueConsumer(quantity -> listController
            .getCreateControllerOrUpdateControllerEntityAs(StockDistributionRegistrationDto.class)
            .setQuantity(quantity));

    quantityColumn.initialize();
  }

  /**
   * Cette méthode permet de préparer l'édition de la colonne de quantité.
   */
  public void prepareQuantityColumnAsEditable() {
    quantityColumn.prepareEdit();
    quantityColumn.setUpdateRequestBuilderFunction(quantityColumnUpdateRequestBuilderFunction());
    quantityColumn.setUpdateRequestConsumerFunction(quantityColumnUpdateRequestConsumerFunction());
    quantityColumn.setDataTable(listController.getDataTable());
  }

  Function<Object, Object> quantityColumnUpdateRequestBuilderFunction() {
    return dto -> {
      StockDistributionRegistrationDto stockDistributionRegistration =
          (StockDistributionRegistrationDto) dto;
      StockDistributionRegistrationUpdateQuantityRequestDto request =
          new StockDistributionRegistrationUpdateQuantityRequestDto();
      request.setIdentifier(stockDistributionRegistration.getIdentifier());
      request.setQuantity(stockDistributionRegistration.getQuantity());
      request.setAuditWho(userIdentifier);
      return request;
    };
  }

  Function<Object, IdentifiableResponseDto> quantityColumnUpdateRequestConsumerFunction() {
    return request -> client
        .updateQuantity((StockDistributionRegistrationUpdateQuantityRequestDto) request);
  }
}
