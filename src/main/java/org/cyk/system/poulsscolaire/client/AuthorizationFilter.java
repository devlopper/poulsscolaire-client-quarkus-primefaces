package org.cyk.system.poulsscolaire.client;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
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
import org.cyk.system.poulsscolaire.server.api.configuration.UserDto;

/**
 * Cette classe représente le filtre de configuration session.
 *
 * @author Christian
 *
 */
@jakarta.servlet.annotation.WebFilter("/private/*")
@Priority(AuthorizationFilter.PRIORITY)
public class AuthorizationFilter implements Filter {

  @Inject
  SessionController sessionController;

  Map<String, Set<String>> map;

  public AuthorizationFilter() {
    map = new HashMap<>();
    map.put("/private/feecategory/administrator/list.jsf", Set.of("12"));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    UserDto user = (UserDto) httpRequest.getSession(true).getAttribute("user");
    if (map.containsKey(httpRequest.getRequestURI())
        && !map.get(httpRequest.getRequestURI()).containsAll(user.getRoles())) {
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/" + UnauthorizedPage.PATH);
      return;
    }
    chain.doFilter(request, response);
  }

  public static final int PRIORITY = SessionConfigurationFilter.PRIORITY + 1;
}
