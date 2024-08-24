package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractFilterController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityFilter;

/**
 * Cette classe représente le contrôleur de filtre de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class IdentityFilterController extends AbstractFilterController<IdentityFilter> {

  @Inject
  @Getter
  IdentitySelectOneController relationshipParentSelectOneController;

  @Inject
  @Getter
  IdentitySelectOneController relationshipChildSelectOneController;

  @Inject
  @Getter
  IdentityRelationshipTypeSelectOneController relationshipTypeSelectOneController;

  public IdentityFilterController() {
    super(IdentityFilter.class);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    filter.setRelationshipParentIdentifier(
        getRequestParameter(IdentityFilter.JSON_RELATIONSHIP_PARENT_IDENTIFIER));
    filter.setRelationshipChildIdentifier(
        getRequestParameter(IdentityFilter.JSON_RELATIONSHIP_CHILD_IDENTIFIER));

    relationshipParentSelectOneController.getSelectOneMenu().getOutputLabel().setValue("Parent");
    relationshipParentSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setRelationshipParentIdentifier(identifier));


    relationshipChildSelectOneController.getSelectOneMenu().getOutputLabel().setValue("Enfant");
    relationshipChildSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> filter.setRelationshipChildIdentifier(identifier));
  }
}
