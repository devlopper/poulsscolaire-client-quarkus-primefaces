package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentClient;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;

/**
 * Cette classe représente la base des pages de lecture de données de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
public abstract class AbstractDepartmentReadDatasPage extends AbstractPage {

  @Inject
  DepartmentClient client;

  @Getter
  DepartmentDto department;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    department = client.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableNamableDto.JSON_NAME), userIdentifier,
        null);
    department.setIdentifier(identifier);
    contentTitle = DepartmentDto.NAME + " - " + department.getName();
  }
}
