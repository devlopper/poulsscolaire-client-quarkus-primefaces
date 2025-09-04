package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.component.TabMenu;
import ci.gouv.dgbf.extension.server.service.api.filter.IsRejectedFilter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.IconManager;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente le contrôleur de menu en onglet de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 */
@Dependent
public class SubsidyDecisionTabMenuController extends AbstractController {

  @Getter
  TabMenu menu;

  @Inject
  IconManager iconManager;

  SubsidyDecisionDto subsidyDecision;

  /**
   * Cette méthode permet de construire.
   */
  protected SubsidyDecisionTabMenuController() {
    menu = new TabMenu();
    menu.setMenuItemStyleClassPrefix("subsidydecision");
    menu.getOutcomesMap().put(SubsidyDecisionReadSubsidiesPage.class,
        SubsidyDecisionReadSubsidiesPage.OUTCOME);
    menu.getOutcomesMap().put(SubsidyDecisionReadRegistrationsPage.class,
        SubsidyDecisionReadRegistrationsPage.OUTCOME);
    menu.getOutcomesMap().put(SubsidyDecisionReadPaymentsPage.class,
        SubsidyDecisionReadPaymentsPage.OUTCOME);
    menu.getOutcomesMap().put(SubsidyDecisionReadRegistrationsForCreationPage.class,
        SubsidyDecisionReadRegistrationsForCreationPage.OUTCOME);
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    menu.tabBuilder().menuItemValue("Paiement").menuItemIcon(iconManager.getScriptParameter())
        .type(SubsidyDecisionReadPaymentsPage.class)
        .addMenuItemParameter(requestParameterIdentifierName, subsidyDecision.getIdentifier())
        .build();

    menu.tabBuilder().menuItemValue("Acceptation").menuItemIcon(iconManager.getScriptParameter())
        .type(SubsidyDecisionReadRegistrationsPage.class)
        .addMenuItemParameter(requestParameterIdentifierName, subsidyDecision.getIdentifier())
        .addMenuItemParameter(IsRejectedFilter.JSON_KEY, false).build();

    menu.tabBuilder().menuItemValue("Refus").menuItemIcon(iconManager.getScriptParameter())
        .type(SubsidyDecisionReadRegistrationsPage.class)
        .addMenuItemParameter(requestParameterIdentifierName, subsidyDecision.getIdentifier())
        .addMenuItemParameter(IsRejectedFilter.JSON_KEY, true).build();

    menu.tabBuilder().menuItemValue("Inscription").menuItemIcon(iconManager.getScriptParameter())
        .type(SubsidyDecisionReadRegistrationsPage.class)
        .addMenuItemParameter(requestParameterIdentifierName, subsidyDecision.getIdentifier())
        .build();

    menu.tabBuilder().menuItemValue("Inscription à mettre sur une décision")
        .menuItemIcon(iconManager.getScriptParameter())
        .type(SubsidyDecisionReadRegistrationsForCreationPage.class)
        .addMenuItemParameter(requestParameterIdentifierName, subsidyDecision.getIdentifier())
        .build();

    menu.initialize();
  }
}
