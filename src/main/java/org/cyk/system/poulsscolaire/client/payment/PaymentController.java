package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.IdentifiableActionController;
import ci.gouv.dgbf.extension.primefaces.component.Button;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.registration.RegistrationSelectOne;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentRequestMapper;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentService;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentService.PaymentCreateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link PaymentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentController extends AbstractController {

  @Inject
  PaymentClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  PaymentFilterController filterController;

  @Inject
  @Getter
  RegistrationSelectOne registrationSelectOne;

  @Inject
  @Getter
  PaymentModeSelectOne modeSelectOne;

  @Inject
  PaymentRequestMapper requestMapper;

  /* Cancel */

  @Inject
  @Getter
  IdentifiableActionController cancelController;

  @Getter
  Button cancelButton;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = PaymentDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {

    listController.setEntityClass(PaymentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(PaymentService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, PaymentDto.JSON_MODE_AS_STRING,
        PaymentDto.JSON_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    if (Boolean.TRUE.equals(filterController.getFilter().getCanceled())) {
      listController.getDataTable().getActionColumn().setRendered(false);
    } else {
      listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(1);
    }

    listController.getCreateController().addEntityConsumer(entity -> ((PaymentDto) entity)
        .setRegistrationIdentifier(filterController.getFilter().getRegistrationIdentifier()));
    
    listController.getCreateController().setFunction(entity -> {
      PaymentCreateRequestDto request = requestMapper.mapCreate((PaymentDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getShowUpdateDialogButton().setRendered(false);
    listController.getDeleteButton().setRendered(false);

    cancelController.setFunction(identifier -> client.cancel(identifier, userIdentifier, null));

    modeSelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> ((PaymentDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setModeIdentifier(identifier));

    cancelButton = new Button();
    cancelButton.initializeConfirm("annuler");
    cancelButton.setIcon("pi pi-times");
    cancelButton.setValue(null);
    cancelController.setName("Annulation paiement");

    if (filterController.getFilter().getRegistrationIdentifier() == null) {
      registrationSelectOne.getSelectOneMenu().addValueConsumer(
          identifier -> ((PaymentDto) listController.getCreateControllerOrUpdateControllerEntity())
              .setRegistrationIdentifier(identifier));
    } else {
      registrationSelectOne.getSelectOneMenu()
          .writeValue(filterController.getFilter().getRegistrationIdentifier());
      listController.showCreateDialog();
    }
  }
}
