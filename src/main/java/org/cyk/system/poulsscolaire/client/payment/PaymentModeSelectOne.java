package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeClient;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentModeService.PaymentModeGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link PaymentModeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentModeSelectOne extends AbstractSelectOneIdentifiableController<
    PaymentModeDto, PaymentModeGetManyResponseDto, PaymentModeClient> {

  @Inject
  @Getter
  PaymentModeClient client;

  protected PaymentModeSelectOne() {
    super(PaymentModeDto.class);
  }
}
