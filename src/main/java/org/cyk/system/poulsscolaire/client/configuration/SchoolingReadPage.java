package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.fee.FeeCrudController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;

/**
 * Cette classe représente la page de lecture de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolingReadPage extends AbstractPage {

  @Inject
  SchoolingClient schoolingClient;

  @Getter
  SchoolingDto schooling;

  @Inject
  @Getter
  SchoolingCrudController crudController;

  @Inject
  @Getter
  FeeCrudController feeController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Lecture de scolarité";

    String identifier = getRequestParameterIdentifier();
    schooling =
        schoolingClient.getByIdentifier(identifier,
            new ProjectionDto().addNames(SchoolingDto.JSON_FEE_AMOUNT_VALUE_AS_STRING,
                SchoolingDto.JSON_FEE_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING),
            userIdentifier, null);
    feeController.setSchoolingIdentifier(identifier);
    feeController.initialize();
    feeController.setAmountValueTotalAsString(schooling.getFeeAmountValueAsString());
    feeController.setAmountRegistrationValuePartTotalAsString(
        schooling.getFeeAmountRegistrationValuePartAsString());
  }

  public static final String OUTCOME = "schoolingReadPage";
}
