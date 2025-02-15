package org.cyk.system.poulsscolaire.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;

/**
 * Cette classe représente le gestionnaire d'icône.
 *
 * @author Christian
 *
 */
@Named
@ApplicationScoped
@Getter
public class IconManager {

  String dashboard;
  
  /*
   * Subsidies
   */
  String acceptedSubsidy;
  String refusedSubsidy;
  String notSubsidy;
  
  String scriptParameter;
  String scriptRelation;
  String scriptEvaluation;
  String scriptEvaluator;
  
  String all;
  
  /**
   * Cette méthode permet d'instancier.
   */
  public IconManager() {
    dashboard = "pi pi-chart-bar";
    acceptedSubsidy = "pi pi-list";
    refusedSubsidy = "pi pi-link";
    notSubsidy = "pi pi-calculator";
  }
}
