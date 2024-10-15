package com.i2i.ems.config;

import com.i2i.ems.helper.UnAuthorizedException;
import com.i2i.ems.service.UserService;
import com.i2i.ems.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * <p>
 * Filter to validate jwt token and set user identity on spring security context.
 * </p>
 */
public class JwtTokenFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver handlerExceptionResolver;

  @Autowired
  private UserService userService;

  public JwtTokenFilter(HandlerExceptionResolver handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  /**
   * <p>
   *   Checks if the request is an authentication request.
   *   If the request is an authentication request, it returns true.
   *   Otherwise, it returns false.
   * </p>
   *
   * @param request
   *    HttpServletRequest object to check if the request is an authentication request.
   * @return boolean
   *     returns true if the request is an authentication request.
   */
  private boolean isAuthRequest(HttpServletRequest request) {
    return request.getRequestURI().contains("/login") || request.getRequestURI().contains("/register");
  }

  /**
   * <p>
   *   Validates the Authorization header.
   * </p>
   *
   * @param request
   *        HttpServletRequest object to validate the Authorization header.
   * @return String
   *         Authorization header.
   * @throws UnAuthorizedException
   *         if the Authorization header is invalid(null or does not start with "Bearer ").
   */
  private String validateHeader(HttpServletRequest request) {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      throw new UnAuthorizedException("Invalid Authorization header");
    }
    return header;
  }

  /**
   * <p>
   *   Validates the jwt token.
   * </p>
   *
   * @param request
   *       HttpServletRequest object to validate the jwt token.
   * @return jwt token
   *         valid token from the Authorization header to be used for other operations.
   * @throws UnAuthorizedException
   *         if the jwt token is invalid.
   */
  private String validateToken(HttpServletRequest request) throws UnAuthorizedException {
    String header = validateHeader(request);
    String token = header.split(" ")[1].trim();
    if (!JwtTokenUtil.validate(token)) {
      throw new UnAuthorizedException("Invalid token");
    }
    return token;
  }

  /**
   * <p>
   * Filter to validate jwt token and set user identity on spring security context.
   * If the request is an authentication request, it passes the request and response to the next filter.
   * Otherwise it validates the jwt token and sets the user identity on spring security context.
   * If the jwt token is invalid or expired, it throws an UnAuthorizedException.
   * </p>
   *
   * @param request  HttpServletRequest object to get the request details.
   * @param response HttpServletResponse object to send the response.
   * @param chain    FilterChain object to pass the request and response to the next filter.
   * @throws ServletException Exception thrown by the servlet.
   * @throws IOException      Exception thrown by the input/output operation.
   */
  @Override
  public void doFilterInternal(HttpServletRequest request,
                               HttpServletResponse response,
                               FilterChain chain)
      throws ServletException, IOException {

    if (isAuthRequest(request)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      final String token = validateToken(request);

      // If the user is already authenticated, do not authenticate again
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
        chain.doFilter(request, response);
        return;
      }

      UserDetails userDetails = userService.loadUserByUsername(JwtTokenUtil.getUsername(token));

      if (userDetails == null) {
        chain.doFilter(request, response);
        return;
      }

      UsernamePasswordAuthenticationToken
          authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null,
          userDetails.getAuthorities()
      );

      authentication.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } catch (UnAuthorizedException e) {
      handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }
}