package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationFilter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService.RegistrationGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link RegistrationDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class RegistrationSelectOneController extends AbstractSelectOneIdentifiableController<
    RegistrationDto, RegistrationGetManyResponseDto, RegistrationClient> {

  @Inject
  @Getter
  RegistrationClient client;

  @Inject
  SessionController sessionController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      RegistrationFilter registrationFilter = new RegistrationFilter();
      registrationFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = registrationFilter.toDto();
    });

  }

  protected RegistrationSelectOneController() {
    super(RegistrationDto.class);
    projection.addNames(AbstractIdentifiableCodableDto.JSON_CODE,
        RegistrationDto.JSON_STUDENT_AS_STRING, RegistrationDto.JSON_PAYABLE_AMOUNT_AS_STRING);
  }

  @Override
  protected String buildSelectItemLabel(RegistrationDto dto) {
    return dto.getCode() + " / " + dto.getStudentAsString()
        + (Core.isStringBlank(dto.getPayableAmountAsString()) ? ""
            : " - " + dto.getPayableAmountAsString());
  }
}
