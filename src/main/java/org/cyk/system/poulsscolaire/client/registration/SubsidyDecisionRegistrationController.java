package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectBooleanController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationService;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationService.SubsidyDecisionRegistrationCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRegistrationService.SubsidyDecisionRegistrationUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SubsidyDecisionRegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionRegistrationController extends AbstractController {

  @Inject
  SubsidyDecisionRegistrationClient client;

  @Inject
  SubsidyDecisionRegistrationRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SubsidyDecisionRegistrationFilterController filterController;

  @Inject
  @Getter
  SubsidyDecisionSelectOneController subsidyDecisionSelectOneController;

  @Inject
  @Getter
  RegistrationSelectOneController registrationSelectOneController;

  @Inject
  @Getter
  SelectBooleanController rejectedSelectBooleanController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = SubsidyDecisionRegistrationDto.NAME;
  }

  /**
   * Initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SubsidyDecisionRegistrationDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SubsidyDecisionRegistrationService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        SubsidyDecisionRegistrationDto.JSON_REGISTRATION_AS_STRING,
        SubsidyDecisionRegistrationDto.JSON_REJECTED_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getSubsidyDecisionIdentifier(),
        SubsidyDecisionRegistrationDto.JSON_SUBSIDY_DECISION_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> {
      ((SubsidyDecisionRegistrationDto) entity).setSubsidyDecisionIdentifier(
          filterController.getFilter().getSubsidyDecisionIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      SubsidyDecisionRegistrationCreateRequestDto request =
          requestMapper.mapCreate((SubsidyDecisionRegistrationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            SubsidyDecisionRegistrationDto.JSON_SUBSIDY_DECISION_IDENTIFIER,
            SubsidyDecisionRegistrationDto.JSON_REGISTRATION_IDENTIFIER,
            SubsidyDecisionRegistrationDto.JSON_REJECTED));

    listController.getUpdateController().addEntityConsumer(entity -> {
      subsidyDecisionSelectOneController.getSelectOneMenu()
          .writeValue(((SubsidyDecisionRegistrationDto) entity).getSubsidyDecisionIdentifier());
      registrationSelectOneController.getSelectOneMenu()
          .writeValue(((SubsidyDecisionRegistrationDto) entity).getRegistrationIdentifier());
      rejectedSelectBooleanController.getSelectOneRadioBoolean()
          .writeValue(((SubsidyDecisionRegistrationDto) entity).getRejected());
    });

    listController.getUpdateController().setFunction(entity -> {
      SubsidyDecisionRegistrationUpdateRequestDto request =
          requestMapper.mapUpdate((SubsidyDecisionRegistrationDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    subsidyDecisionSelectOneController.setRenderable(
        Core.isStringBlank(filterController.getFilter().getSubsidyDecisionIdentifier()));
    subsidyDecisionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionRegistrationDto.class)
            .setSubsidyDecisionIdentifier(identifier));

    registrationSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionRegistrationDto.class)
            .setRegistrationIdentifier(identifier));
    
    rejectedSelectBooleanController.setOutputLableValue("Rejetée ?");
    rejectedSelectBooleanController.getSelectOneRadioBoolean().addTrueOrFalseChoices();
    rejectedSelectBooleanController.getSelectOneRadioBoolean()
        .addValueConsumer(value -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionRegistrationDto.class)
            .setRejected(value));
  }
}
