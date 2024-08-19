package org.cyk.system.poulsscolaire.client;

import jakarta.annotation.Priority;
import jakarta.inject.Named;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;

/**
 * Cette classe représente le filtre d'autorisation.
 *
 * @author Christian
 *
 */
@jakarta.servlet.annotation.WebFilter("/private/*")
@Priority(AuthorizationFilter.PRIORITY)
@Getter
@Named
public class AuthorizationFilter implements Filter {

  Map<String, Set<String>> map;
  String rootRoleCode;
  String founderRoleCode;
  String managerRoleCode;
  String accountantRoleCode;
  String studiesDirectorRoleCode;
  String educatorRoleCode;
  String cashierRoleCode;
  String debtCollectorRoleCode;

  /**
   * Cette méthode permet de construire un objet.
   */
  public AuthorizationFilter() {
    rootRoleCode = "0";
    founderRoleCode = "7";
    managerRoleCode = "12";
    accountantRoleCode = "13";
    studiesDirectorRoleCode = "11";
    educatorRoleCode = "9";
    cashierRoleCode = "14";
    debtCollectorRoleCode = "15";
    map = new HashMap<>();
    
    allowSession(founderRoleCode, managerRoleCode, accountantRoleCode);
    
    allowFeeCategory(founderRoleCode, managerRoleCode, accountantRoleCode);
    allowFee(founderRoleCode, managerRoleCode, accountantRoleCode);
    allowAmountDeadline(founderRoleCode, managerRoleCode, accountantRoleCode);
    allowSchooling(founderRoleCode, managerRoleCode, accountantRoleCode);
    
    allowStudent(founderRoleCode, managerRoleCode, accountantRoleCode);
    allowRegistration(founderRoleCode, managerRoleCode, accountantRoleCode);
    allowAdjustedFee(founderRoleCode, managerRoleCode, accountantRoleCode);
    
    allowPayment(founderRoleCode, managerRoleCode, accountantRoleCode);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    UserDto user = (UserDto) httpRequest.getSession(true).getAttribute("user");
    if (isNotAllowed(user, httpRequest.getRequestURI())) {
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/" + UnauthorizedPage.PATH);
      return;
    }
    chain.doFilter(request, response);
  }

  void allowSession(String... rolesCodes) {
    allow("/private/__session__/configure.xhtml", rolesCodes);
  }

  void allowFeeCategory(String... rolesCodes) {
    allow("/private/feecategory/administrator/list.xhtml", rolesCodes);
  }

  void allowFee(String... rolesCodes) {
    allow("/private/fee/administrator/list/index.xhtml", rolesCodes);
  }
  
  void allowAmountDeadline(String... rolesCodes) {
    allow("/private/amountdeadline/administrator/list/index.xhtml", rolesCodes);
  }
  
  void allowSchooling(String... rolesCodes) {
    allow("/private/schooling/accountant/list.xhtml", rolesCodes);
  }

  void allowStudent(String... rolesCodes) {
    allow("/private/student/administrator/list.xhtml", rolesCodes);
  }

  void allowRegistration(String... rolesCodes) {
    allow("/private/registration/accountant/list.xhtml", rolesCodes);
  }

  void allowAdjustedFee(String... rolesCodes) {
    allow("/private/adjustedfee/administrator/list/index.xhtml", rolesCodes);
  }

  void allowPayment(String... rolesCodes) {
    allow("/private/payment/administrator/list/index.xhtml", rolesCodes);
  }

  void allow(String uri, String... rolesCodes) {
    map.put(uri, Set.of(rolesCodes));
  }

  boolean isNotAllowed(UserDto user, String uri) {
    return !map.containsKey(uri) || !user.hasOneOfRoles(map.get(uri));
  }

  public static final int PRIORITY = SessionConfigurationFilter.PRIORITY + 1;
}
