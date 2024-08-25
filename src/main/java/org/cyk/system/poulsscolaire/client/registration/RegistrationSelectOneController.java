package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
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

  protected RegistrationSelectOneController() {
    super(RegistrationDto.class);
    projection.addNames(RegistrationDto.JSON_STUDENT_AS_STRING,
        RegistrationDto.JSON_PAYABLE_AMOUNT_AS_STRING);
  }

  @Override
  protected String buildSelectItemLabel(RegistrationDto dto) {
    return dto.getStudentAsString() + " - " + dto.getPayableAmountAsString();
  }
}
