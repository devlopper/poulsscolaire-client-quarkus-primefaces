package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.OutputLabel;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneEnumerationController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityRelationshipType;

/**
 * Cette classe représente le contrôleur de sélection de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class IdentityRelationshipTypeSelectOneController
    extends AbstractSelectOneEnumerationController<IdentityRelationshipType> {

  protected IdentityRelationshipTypeSelectOneController() {
    super(IdentityRelationshipType.values());
  }

  @Override
  protected void processOutputLabel(OutputLabel outputLabel) {
    outputLabel.setValue("Relation");
  }
}
