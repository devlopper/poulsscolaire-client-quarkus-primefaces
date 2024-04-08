package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link PaymentModeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentModeCrudController extends AbstractController {

  @Inject
  PaymentModeClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Modes de paiement";

    listController.setEntityClass(PaymentModeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(PaymentModeService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((PaymentModeDto) entity).getCode(),
            ((PaymentModeDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((PaymentModeDto) entity).getIdentifier(),
            ((PaymentModeDto) entity).getCode(), ((PaymentModeDto) entity).getName(),
            userIdentifier, null));
  }
}
