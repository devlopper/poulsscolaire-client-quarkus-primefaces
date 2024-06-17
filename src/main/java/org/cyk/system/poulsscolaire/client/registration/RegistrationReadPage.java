package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.fee.AdjustedFeeController;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;

/**
 * Cette classe représente la page de lecture de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class RegistrationReadPage extends AbstractPage {

  @Inject
  RegistrationClient registrationClient;

  @Getter
  RegistrationDto registration;

  @Inject
  @Getter
  RegistrationController crudController;

  @Inject
  @Getter
  AdjustedFeeController adjustedFeeController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    ProjectionDto projection = new ProjectionDto();
    if (Boolean.FALSE
        .equals(adjustedFeeController.getFilterController().getFilter().getAmountOptional())) {
      projection.addNames(RegistrationDto.JSON_TOTAL_AMOUNT_AS_STRING,
          RegistrationDto.JSON_PAID_AMOUNT_AS_STRING, RegistrationDto.JSON_PAYABLE_AMOUNT_AS_STRING,
          RegistrationDto.JSON_TOTAL_REGISTRATION_AMOUNT_AS_STRING);
    }
    projection.addNames(AbstractIdentifiableCodableDto.JSON_CODE,
        RegistrationDto.JSON_STUDENT_AS_STRING, RegistrationDto.JSON_SCHOOLING_AS_STRING,
        RegistrationDto.JSON_ASSIGNMENT_TYPE_AS_STRING, RegistrationDto.JSON_SENIORITY_AS_STRING);
    registration = registrationClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle = RegistrationDto.NAME + " - " + registration.getCode();
    adjustedFeeController.setRegistrationIdentifier(identifier);

    adjustedFeeController.getFilterController().getFilter().setRegistrationIdentifier(identifier);
    if (adjustedFeeController.getFilterController().getFilter().getAmountOptional() == null) {
      adjustedFeeController.getFilterController().getFilter().setAmountOptional(false);
    }

    adjustedFeeController.initialize();

    adjustedFeeController.setAmountValueTotalAsString(registration.getTotalAmountAsString());
    adjustedFeeController.setAmountValuePaidTotalAsString(registration.getPaidAmountAsString());
    adjustedFeeController
        .setAmountValueLeftToPayTotalAsString(registration.getPayableAmountAsString());
    adjustedFeeController.setAmountRegistrationValuePartTotalAsString(
        registration.getTotalRegistrationAmountAsString());

  }

  public static final String OUTCOME = "registrationReadPage";
}
