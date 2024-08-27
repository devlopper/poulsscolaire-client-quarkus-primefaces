package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService.StudentGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class StudentSelectOneController extends
    AbstractSelectOneIdentifiableController<StudentDto, StudentGetManyResponseDto, StudentClient> {

  @Inject
  @Getter
  StudentClient client;

  protected StudentSelectOneController() {
    super(StudentDto.class, SelectItemLabelStrategy.AS_STRING);
  }
}
