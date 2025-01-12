package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionService;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionService.FundingExecutionCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingExecutionService.FundingExecutionUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link FundingExecutionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingExecutionController extends AbstractController {

  @Inject
  FundingExecutionClient client;

  @Inject
  FundingExecutionRequestMapper requestMapper;

  @Inject
  @Getter
  FundingSelectOneController fundingSelectOneController;
  @Inject
  @Getter
  InputNumberController amountInputNumberController;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  FundingExecutionFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FundingExecutionDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(FundingExecutionDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FundingExecutionService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        FundingExecutionDto.JSON_AMOUNT_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getFundingIdentifier(),
        FundingExecutionDto.JSON_FUNDING_AS_STRING);

    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    fundingSelectOneController
        .setRenderable(filterController.getFilter().getFundingIdentifier() == null);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((FundingExecutionDto) entity)
          .setFundingIdentifier(filterController.getFilter().getFundingIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      FundingExecutionCreateRequestDto request =
          requestMapper.mapCreate((FundingExecutionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FundingExecutionDto.JSON_FUNDING_IDENTIFIER, FundingExecutionDto.JSON_AMOUNT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      fundingSelectOneController.getSelectOneMenu()
          .writeValue(((FundingExecutionDto) entity).getFundingIdentifier());

      amountInputNumberController.getInputInteger()
          .writeValue(((FundingExecutionDto) entity).getAmount());
    });

    listController.getUpdateController().setFunction(entity -> {
      FundingExecutionUpdateRequestDto request =
          requestMapper.mapUpdate((FundingExecutionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    fundingSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getFundingIdentifier()));
    fundingSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingExecutionDto.class)
            .setFundingIdentifier(identifier));
    fundingSelectOneController.getSelectOneMenu().setRequired(true);

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputInteger().setRequired(true);
    amountInputNumberController.getInputInteger()
        .addValueConsumer(amount -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingExecutionDto.class)
            .setAmount(amount));
  }

  /**
   * Cette méthode permet d'obtenir la concatenation des identifiants des messages.
   *
   * @return concatenation des identifiants des messages
   */
  public String getComaSeparatedMessagesIdentifiers() {
    return List
        .of(fundingSelectOneController.getSelectOneMenu(),
            amountInputNumberController.getInputInteger())
        .stream().map(input -> input.getMessage().getIdentifier()).collect(Collectors.joining(","));
  }
}
