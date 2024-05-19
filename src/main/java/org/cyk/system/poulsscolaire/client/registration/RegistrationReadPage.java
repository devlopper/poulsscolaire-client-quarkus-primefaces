package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.fee.AdjustedFeeCrudController;
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
  RegistrationCrudController crudController;

  @Inject
  @Getter
  AdjustedFeeCrudController adjustedFeeController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    registration = registrationClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableCodableDto.JSON_CODE,
            RegistrationDto.JSON_STUDENT_AS_STRING, RegistrationDto.JSON_SCHOOLING_AS_STRING,
            RegistrationDto.JSON_ASSIGNMENT_TYPE_AS_STRING,
            RegistrationDto.JSON_SENIORITY_AS_STRING,
            RegistrationDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_VALUE_AS_STRING,
            RegistrationDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING),
        userIdentifier, null);
    contentTitle = RegistrationDto.NAME + " - " + registration.getCode();
    adjustedFeeController.setRegistrationIdentifier(identifier);
    adjustedFeeController.initialize();
    adjustedFeeController
        .setAmountValueTotalAsString(registration.getNotOptionalFeeAmountValueAsString());
    adjustedFeeController.setAmountRegistrationValuePartTotalAsString(
        registration.getNotOptionalFeeAmountRegistrationValuePartAsString());
  }

  public static final String OUTCOME = "registrationReadPage";
}
