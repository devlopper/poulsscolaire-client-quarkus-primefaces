package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
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
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceClient;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceService;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceService.FundingSourceCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.FundingSourceService.FundingSourceUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link FundingSourceDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FundingSourceController extends AbstractController {

  @Inject
  FundingSourceClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  FundingSourceFilterController filterController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Inject
  @Getter
  InputTextController codeInputTextController;

  @Inject
  @Getter
  InputTextController nameInputTextController;

  @Inject
  FundingSourceRequestMapper requestMapper;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FundingSourceDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(FundingSourceDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FundingSourceService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        FundingSourceDto.JSON_SCHOOL_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        FundingSourceDto.JSON_SCHOOL_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> ((FundingSourceDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));
    listController.getCreateController().setFunction(entity -> {
      FundingSourceCreateRequestDto request = requestMapper.mapCreate((FundingSourceDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            FundingSourceDto.JSON_SCHOOL_IDENTIFIER, AbstractIdentifiableCodableDto.JSON_CODE,
            AbstractIdentifiableCodableNamableDto.JSON_NAME));

    listController.getUpdateController().addEntityConsumer(entity -> {
      schoolSelectOneController.getSelectOneMenu()
          .writeValue(((FundingSourceDto) entity).getSchoolIdentifier());
      codeInputTextController.getInputText().writeValue(((FundingSourceDto) entity).getCode());
      nameInputTextController.getInputText().writeValue(((FundingSourceDto) entity).getName());
    });

    listController.getUpdateController().setFunction(entity -> {
      FundingSourceUpdateRequestDto request = requestMapper.mapUpdate((FundingSourceDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController
        .setRenderable(Core.isStringBlank(filterController.getFilter().getSchoolIdentifier()));
    schoolSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(FundingSourceDto.class)
            .setSchoolIdentifier(identifier));

    codeInputTextController.setOutputLableValue("Code");
    codeInputTextController.getInputText().addValueConsumer(code -> listController
        .getCreateControllerOrUpdateControllerEntityAs(FundingSourceDto.class).setCode(code));

    nameInputTextController.setOutputLableValue("Libellé");
    nameInputTextController.getInputText().addValueConsumer(name -> listController
        .getCreateControllerOrUpdateControllerEntityAs(FundingSourceDto.class).setName(name));
  }
}
