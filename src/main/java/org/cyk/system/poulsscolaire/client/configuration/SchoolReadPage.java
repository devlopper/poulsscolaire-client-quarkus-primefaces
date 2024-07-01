package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.fee.FeeCategoryController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;

/**
 * Cette classe représente la page de lecture de {@link SchoolDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolReadPage extends AbstractPage {

  @Inject
  SchoolClient schoolingClient;

  @Getter
  SchoolDto school;

  @Inject
  @Getter
  FeeCategoryController feeCategoryController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    school = schoolingClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(AbstractIdentifiableNamableDto.JSON_NAME), userIdentifier,
        null);
    contentTitle = SchoolDto.NAME + " - " + school.getName();

    feeCategoryController.getFilterController().getFilter()
        .setRegistrationSchoolingSchoolIdentifier(identifier);
    feeCategoryController.initialize();
  }

  public static final String OUTCOME = "schoolReadPage";
}
