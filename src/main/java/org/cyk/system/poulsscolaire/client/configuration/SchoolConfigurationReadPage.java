package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationDto;

/**
 * Cette classe représente la page de lecture de {@link SchoolConfigurationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolConfigurationReadPage extends AbstractPage {

  @Inject
  SchoolConfigurationClient client;

  @Getter
  SchoolConfigurationDto schoolConfiguration;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    schoolConfiguration = client.getByIdentifier(identifier,
        new ProjectionDto().addNames(SchoolConfigurationDto.JSON_SCHOOL_AS_STRING,
            SchoolConfigurationDto.JSON_PAYMENT_ACCOUNTING_ACCOUNT_AS_STRING),
        userIdentifier, null);
    contentTitle = SchoolConfigurationDto.NAME + " - " + schoolConfiguration.getSchoolAsString();
  }

  public static final String OUTCOME = "schoolConfigurationReadPage";
}
