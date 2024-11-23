package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentClient;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentFilter;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentService.DepartmentGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DepartmentSelectOneController extends AbstractSelectOneIdentifiableController<
    DepartmentDto, DepartmentGetManyResponseDto, DepartmentClient> {

  @Inject
  @Getter
  DepartmentClient client;

  @Inject
  SessionController sessionController;

  protected DepartmentSelectOneController() {
    super(DepartmentDto.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      DepartmentFilter departmentFilter = new DepartmentFilter();
      departmentFilter.setIdentifier(sessionController.getSchoolIdentifier());
      filter = departmentFilter.toDto();
    });
  }
}
