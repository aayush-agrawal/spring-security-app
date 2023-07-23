package com.aayush.springsecurity.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.aayush.springsecurity.constant.SecurityConstant.JWT_HEADER;
import static com.aayush.springsecurity.constant.SecurityConstant.JWT_KEY;
import static java.nio.charset.StandardCharsets.UTF_8;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null) {
      SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(UTF_8));
      String jwt = Jwts.builder()
        .setIssuer("Eazy Bank")
        .setSubject("JWT Token")
        .claim("username", authentication.getName())
        .claim("authorities", populateAuthorities(authentication.getAuthorities()))
        .setIssuedAt(new java.util.Date())
        .setExpiration(new java.util.Date(System.currentTimeMillis() + 30000000))
        .signWith(key)
        .compact();
      response.setHeader(JWT_HEADER, jwt);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getServletPath().equals("/user");
  }

  private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return authorities
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));

  }
}
