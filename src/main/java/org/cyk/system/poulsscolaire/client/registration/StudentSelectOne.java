package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
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
public class StudentSelectOne extends
    AbstractSelectOneIdentifiableController<StudentDto, StudentGetManyResponseDto, StudentClient> {

  @Inject
  @Getter
  StudentClient client;

  protected StudentSelectOne() {
    super(StudentDto.class);
    projection.addNames(AbstractIdentifiableDto.JSON_AS_STRING);
  }

  @Override
  protected String buildSelectItemLabel(StudentDto dto) {
    return dto.getAsString();
  }
}
