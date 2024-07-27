package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
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
import org.cyk.system.poulsscolaire.server.api.payment.PaymentService;

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
  RegistrationSelectOne registrationSelectOne;

  @Inject
  @Getter
  PaymentModeSelectOne modeSelectOne;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = PaymentDto.NAME;

    listController.setEntityClass(PaymentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(PaymentService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, PaymentDto.JSON_MODE_AS_STRING,
        PaymentDto.JSON_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((PaymentDto) entity).getRegistrationIdentifier(),
            ((PaymentDto) entity).getModeIdentifier(), ((PaymentDto) entity).getAmount(),
            userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((PaymentDto) entity).getIdentifier(),
            ((PaymentDto) entity).getModeIdentifier(), ((PaymentDto) entity).getAmount(),
            userIdentifier, null));

    registrationSelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> ((PaymentDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setRegistrationIdentifier(identifier));
    
    modeSelectOne.getSelectOneMenu().addValueConsumer(
        identifier -> ((PaymentDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setModeIdentifier(identifier));
  }
}
