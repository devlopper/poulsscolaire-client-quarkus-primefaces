package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityClient;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipType;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityService;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityService.IdentityCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityService.IdentityUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class IdentityController extends AbstractController {

  @Inject
  IdentityClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  IdentityFilterController filterController;

  @Inject
  @Getter
  IdentitySelectOneController relationshipChildSelectOneController;

  @Inject
  @Getter
  IdentityRelationshipTypeSelectOneController relationshipTypeSelectOneController;

  @Inject
  IdentityRequestMapper requestMapper;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = IdentityDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(IdentityDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(IdentityService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, IdentityDto.JSON_FIRST_NAME,
        IdentityDto.JSON_LAST_NAMES, IdentityDto.JSON_PHONE_NUMBER, IdentityDto.JSON_PROFESSION,
        IdentityDto.JSON_RESIDENCE, IdentityDto.JSON_EMAIL_ADDRESS,
        IdentityDto.JSON_RELATIONSHIP_TYPE_PARENT_AS_STRING);
    listController.getReadController().setProjection(projection);
    listController.getDataTable().getFilterButton().setRendered(true);
    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(IdentityReadPage.OUTCOME);
    listController.getDataTable().getActionColumn().computeWithForButtonsWithIconOnly(3);

    listController.getCreateController().addEntityConsumer(entity -> {
      ((IdentityDto) entity).setRelationshipParentIdentifier(
          filterController.getFilter().getRelationshipParentIdentifier());
      ((IdentityDto) entity).setRelationshipChildIdentifier(
          filterController.getFilter().getRelationshipChildIdentifier());
      ((IdentityDto) entity)
          .setRelationshipType(filterController.getFilter().getRelationshipType());
    });

    listController.getCreateController().setFunction(entity -> {
      IdentityCreateRequestDto request = requestMapper.mapCreate((IdentityDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            IdentityDto.JSON_FIRST_NAME, IdentityDto.JSON_LAST_NAMES, IdentityDto.JSON_PHONE_NUMBER,
            IdentityDto.JSON_PROFESSION, IdentityDto.JSON_RESIDENCE,
            IdentityDto.JSON_EMAIL_ADDRESS));

    listController.getUpdateController().setFunction(entity -> {
      IdentityUpdateRequestDto request = requestMapper.mapUpdate((IdentityDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    relationshipChildSelectOneController
        .setRenderable(filterController.getFilter().getRelationshipChildIdentifier() == null);
    Core.runIfNull(filterController.getFilter().getRelationshipChildIdentifier(), () -> {
      relationshipChildSelectOneController.getSelectOneMenu().getOutputLabel().setValue("Enfant");
      relationshipChildSelectOneController.getSelectOneMenu()
          .addValueConsumer(identifier -> listController
              .getCreateControllerOrUpdateControllerEntityAs(IdentityDto.class)
              .setRelationshipChildIdentifier(identifier));
    });

    relationshipTypeSelectOneController.getSelectOneRadio()
        .addValueConsumer(typeName -> listController
            .getCreateControllerOrUpdateControllerEntityAs(IdentityDto.class)
            .setRelationshipType(IdentityRelationshipType.valueOf(typeName)));
  }
}
