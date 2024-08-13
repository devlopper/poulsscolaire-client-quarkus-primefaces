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

/**
 * Cette classe représente le filtre de connexion d'utilisateur.
 *
 * @author Christian
 *
 */
@jakarta.servlet.annotation.WebFilter("/private/*")
@Priority(UserLoginFilter.PRIORITY)
public class UserLoginFilter implements Filter {

  @Inject
  SessionController sessionController;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    if (!sessionController.loggedIn(httpRequest.getSession())) {
      String uri = httpRequest.getRequestURI();
      if (!uri.contains(UserLoginPage.PATH)) {
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/" + UserLoginPage.PATH);
        return;
      }
    }
    chain.doFilter(request, response);
  }
  
  public static final int PRIORITY = 1;
}
