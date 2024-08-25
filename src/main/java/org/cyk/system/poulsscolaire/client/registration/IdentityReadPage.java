package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityClient;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;

/**
 * Cette classe représente la page de lecture de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class IdentityReadPage extends AbstractPage {

  @Inject
  IdentityClient identityClient;

  @Getter
  IdentityDto identity;

  @Inject
  @Getter
  IdentityController parentController;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableDto.JSON_AS_STRING);

    String identifier = getRequestParameterIdentifier();
    identity = identityClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle = IdentityDto.NAME + " - " + identity.getAsString();

    parentController.getListController().getDataTable().setTitle("Tableau de responsables");
    
    parentController.filterController.getFilter()
        .setRelationshipChildIdentifier(identity.getIdentifier());
    parentController.initialize();
    
    parentController.getListController().getGotoReadPageButton().setRendered(false);
    parentController.getListController().getDataTable().getFilterButton().setRendered(false);
  }

  public static final String OUTCOME = "identityReadPage";
}
