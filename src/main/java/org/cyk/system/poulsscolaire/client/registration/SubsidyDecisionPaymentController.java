package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.input.InputNumberController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentService;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentService.SubsidyDecisionPaymentCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionPaymentService.SubsidyDecisionPaymentUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SubsidyDecisionPaymentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionPaymentController extends AbstractController {

  @Inject
  SubsidyDecisionPaymentClient client;

  @Inject
  SubsidyDecisionPaymentRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  SubsidyDecisionPaymentFilterController filterController;

  @Inject
  @Getter
  SubsidyDecisionSelectOneController subsidyDecisionSelectOneController;

  @Inject
  @Getter
  InputNumberController amountInputNumberController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = SubsidyDecisionPaymentDto.NAME;
  }

  /**
   * Initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SubsidyDecisionPaymentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SubsidyDecisionPaymentService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        SubsidyDecisionPaymentDto.JSON_AMOUNT_AS_STRING);
    projection.addNamesIfStringBlank(filterController.getFilter().getSubsidyDecisionIdentifier(),
        SubsidyDecisionPaymentDto.JSON_SUBSIDY_DECISION_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> {
      ((SubsidyDecisionPaymentDto) entity).setSubsidyDecisionIdentifier(
          filterController.getFilter().getSubsidyDecisionIdentifier());
    });

    listController.getCreateController().setFunction(entity -> {
      SubsidyDecisionPaymentCreateRequestDto request =
          requestMapper.mapCreate((SubsidyDecisionPaymentDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            SubsidyDecisionPaymentDto.JSON_SUBSIDY_DECISION_IDENTIFIER,
            SubsidyDecisionPaymentDto.JSON_AMOUNT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      subsidyDecisionSelectOneController.getSelectOneMenu()
          .writeValue(((SubsidyDecisionPaymentDto) entity).getSubsidyDecisionIdentifier());
      amountInputNumberController.getInputInteger()
          .writeValue(((SubsidyDecisionPaymentDto) entity).getAmount());
    });

    listController.getUpdateController().setFunction(entity -> {
      SubsidyDecisionPaymentUpdateRequestDto request =
          requestMapper.mapUpdate((SubsidyDecisionPaymentDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    subsidyDecisionSelectOneController.setRenderable(
        Core.isStringBlank(filterController.getFilter().getSubsidyDecisionIdentifier()));
    subsidyDecisionSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionPaymentDto.class)
            .setSubsidyDecisionIdentifier(identifier));

    amountInputNumberController.setOutputLableValue("Montant");
    amountInputNumberController.getInputInteger()
        .addValueConsumer(amount -> listController
            .getCreateControllerOrUpdateControllerEntityAs(SubsidyDecisionPaymentDto.class)
            .setAmount(amount));
  }
}
