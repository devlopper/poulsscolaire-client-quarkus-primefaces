package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentClient;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;

/**
 * Cette classe représente la page de saisie des financements de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DepartmentInputFundingsPage extends AbstractPage {

  @Inject
  DepartmentClient departmentClient;

  @Getter
  DepartmentDto department;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    department = departmentClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableNamableDto.JSON_NAME), userIdentifier,
        null);
    contentTitle = "Saisie des finnancements";
  }

  public static final String OUTCOME = "departmentInputFundingsPage";
}
