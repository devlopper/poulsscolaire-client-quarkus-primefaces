package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipClient;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipService;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipService.IdentityRelationshipCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipService.IdentityRelationshipUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipType;

/**
 * Cette classe représente le contrôleur de {@link IdentityRelationshipDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class IdentityRelationshipController extends AbstractController {

  @Inject
  IdentityRelationshipClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  IdentityRelationshipRequestMapper requestMapper;

  @Inject
  @Getter
  IdentitySelectOneController parentSelectOneController;

  @Inject
  @Getter
  IdentitySelectOneController childSelectOneController;

  @Inject
  @Getter
  IdentityRelationshipTypeSelectOneController typeSelectOneController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = IdentityRelationshipDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(IdentityRelationshipDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(IdentityRelationshipService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        IdentityRelationshipDto.JSON_PARENT_AS_STRING, IdentityRelationshipDto.JSON_TYPE_AS_STRING,
        IdentityRelationshipDto.JSON_CHILD_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      IdentityRelationshipCreateRequestDto request =
          requestMapper.mapCreate((IdentityRelationshipDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            IdentityRelationshipDto.JSON_PARENT_IDENTIFIER, IdentityRelationshipDto.JSON_TYPE,
            IdentityRelationshipDto.JSON_CHILD_IDENTIFIER));

    listController.getUpdateController().setFunction(entity -> {
      IdentityRelationshipUpdateRequestDto request =
          requestMapper.mapUpdate((IdentityRelationshipDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    parentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(IdentityRelationshipDto.class)
            .setParentIdentifier(identifier));

    childSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> listController
            .getCreateControllerOrUpdateControllerEntityAs(IdentityRelationshipDto.class)
            .setChildIdentifier(identifier));

    typeSelectOneController.getSelectOneRadio()
        .addValueConsumer(type -> listController
            .getCreateControllerOrUpdateControllerEntityAs(IdentityRelationshipDto.class)
            .setType(IdentityRelationshipType.valueOf(type)));
  }
}
