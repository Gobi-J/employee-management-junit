package com.i2i.ems.util;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.helper.UnAuthorizedException;

/**
 * <p>
 * Utility class to generate and validate JWT token.
 * </p>
 */
@Component
public class JwtTokenUtil {

  private static final SecretKey JWT_SECRET_KEY;
  private static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hour in milliseconds

  static {
    String key;
    KeyGenerator keyGenerator;
    try {
      keyGenerator = KeyGenerator.getInstance("HmacSHA256");
    } catch (NoSuchAlgorithmException e) {
      throw new EmployeeException("Issue with generating token", e);
    }
    SecretKey secretKey = keyGenerator.generateKey();
    key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    JWT_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
  }

  /**
   * <p>
   * Validates the token.
   * </p>
   *
   * @param token Token to be validated.
   * @return {@link Boolean} True if token is valid, false otherwise.
   * @throws UnAuthorizedException If token is invalid.
   * @throws UnAuthorizedException If token is expired.
   */
  public static boolean validate(String token) {
    try {
      Jwts.parser()
          .verifyWith(JWT_SECRET_KEY)
          .build()
          .parse(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new UnAuthorizedException("Token expired");
    } catch (UnAuthorizedException e) {
      throw new UnAuthorizedException(e.getMessage());
    } catch (Exception e) {
      throw new UnAuthorizedException("Invalid token");
    }
  }

  /**
   * <p>
   * Generates access token.
   * </p>
   *
   * @param username Username to generate token.
   * @return {@link String} Generated token.
   */
  public static String generateAccessToken(String username) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
        .signWith(JWT_SECRET_KEY)
        .compact();
  }

  /**
   * <p>
   * Extracts username from the token.
   * </p>
   *
   * @param token Token from which username is to be extracted.
   * @return {@link String} Username extracted from the token.
   */
  public static String getUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * <p>
   * Extracts claim from the token.
   * </p>
   *
   * @param token Token from which claim is to be extracted.
   * @param claimsResolver Function to extract claim.
   * @return {@link T} Claim extracted from the token.
   */
  private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * <p>
   * Extracts all claims from the token.
   * </p>
   *
   * @param token Token from which claims are to be extracted.
   * @return {@link Claims} All claims extracted from the token.
   */
  private static Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .verifyWith(JWT_SECRET_KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}