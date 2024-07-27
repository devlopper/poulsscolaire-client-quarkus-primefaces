package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentDto;
import org.cyk.system.poulsscolaire.server.api.payment.PaymentFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link PaymentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class PaymentFilterController extends AbstractFilterController<PaymentFilter> {

  public PaymentFilterController() {
    super(PaymentFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter
        .setRegistrationIdentifier(getRequestParameter(PaymentFilter.JSON_REGISTRATION_IDENTIFIER));
    filter.setCanceled(getRequestParameterAsBoolean(PaymentFilter.JSON_CANCELED));
  }
}
