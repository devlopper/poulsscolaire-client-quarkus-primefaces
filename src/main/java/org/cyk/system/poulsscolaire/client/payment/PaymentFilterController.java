package org.cyk.system.poulsscolaire.client.payment;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.client.registration.RegistrationSelectOneController;
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

  @Inject
  SessionController sessionController;

  @Inject
  @Getter
  RegistrationSelectOneController registrationSelectOneController;
  
  public PaymentFilterController() {
    super(PaymentFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setSchoolIdentifier(
        Core.getOrDefaultIfNull(getRequestParameter(PaymentFilter.JSON_SCHOOL_IDENTIFIER),
            sessionController.getSchoolIdentifier()));
    filter
        .setRegistrationIdentifier(getRequestParameter(PaymentFilter.JSON_REGISTRATION_IDENTIFIER));
    filter.setCanceled(getRequestParameterAsBoolean(PaymentFilter.JSON_CANCELED));

    registrationSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setRegistrationIdentifier(identifier));
  }
}
