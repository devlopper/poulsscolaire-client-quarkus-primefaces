package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.component.OutputLabel;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneEnumerationController;
import jakarta.enterprise.context.Dependent;
import org.cyk.system.poulsscolaire.server.api.registration.BloodGroup;

/**
 * Cette classe représente le contrôleur de sélection de {@link BloodGroup}.
 *
 * @author Christian
 *
 */
@Dependent
public class BloodGroupSelectOne extends AbstractSelectOneEnumerationController<BloodGroup> {

  protected BloodGroupSelectOne() {
    super(BloodGroup.values());
  }

  @Override
  protected void processOutputLabel(OutputLabel outputLabel) {
    outputLabel.setValue("Groupe sanguin");
  }
}
