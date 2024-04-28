package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeService;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentAdjustedFeeClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentAdjustedFeeDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentAdjustedFeeService;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link PaymentAdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentAdjustedFeeCrudController extends AbstractController {

  @Inject
  PaymentAdjustedFeeClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  PaymentClient paymentClient;

  @Inject
  AdjustedFeeClient adjustedFeeClient;

  @Getter
  @Setter
  List<SelectItem> payments;

  @Getter
  @Setter
  List<SelectItem> adjustedFees;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Paiements";

    listController.setEntityClass(PaymentAdjustedFeeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(PaymentAdjustedFeeService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        PaymentAdjustedFeeDto.JSON_PAYMENT_AS_STRING,
        PaymentAdjustedFeeDto.JSON_ADJUSTED_FEE_AS_STRING,
        PaymentAdjustedFeeDto.JSON_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(
        entity -> client.create(((PaymentAdjustedFeeDto) entity).getPaymentIdentifier(),
            ((PaymentAdjustedFeeDto) entity).getAdjustedFeeIdentifier(),
            ((PaymentAdjustedFeeDto) entity).getAmount(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((PaymentAdjustedFeeDto) entity).getIdentifier(),
            ((PaymentAdjustedFeeDto) entity).getPaymentIdentifier(),
            ((PaymentAdjustedFeeDto) entity).getAdjustedFeeIdentifier(),
            ((PaymentAdjustedFeeDto) entity).getAmount(), userIdentifier, null));

    payments = new ActionExecutor<>(this, PaymentService.GET_MANY_IDENTIFIER, () -> paymentClient
        .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AbstractIdentifiableCodableDto.JSON_CODE), null, null, userIdentifier, null)
        .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getCode()))
        .toList()).execute();

    adjustedFees = new ActionExecutor<>(this, AdjustedFeeService.GET_MANY_IDENTIFIER,
        () -> adjustedFeeClient
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                AdjustedFeeDto.JSON_FEE_AS_STRING), null, null, userIdentifier, null)
            .getDatas().stream()
            .map(dto -> new SelectItem(dto.getIdentifier(), dto.getFeeAsString())).toList())
                .execute();
  }
}
