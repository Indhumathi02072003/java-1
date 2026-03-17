package com.ic.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Header-based pre-authentication filter.
 *
 * <p>Reads user identity and roles from incoming request headers and seeds the
 * Spring Security context with a pre-authenticated token when all required
 * headers are present.</p>
 *
 * <p>Expected headers:</p>
 * <ul>
 *   <li><code>X-User-Id</code> &mdash; unique user identifier</li>
 *   <li><code>X-User-Username</code> &mdash; username</li>
 *   <li><code>X-User-Roles</code> &mdash; comma-separated roles</li>
 * </ul>
 */
@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Username");
        String rolesHeader = request.getHeader("X-User-Roles");

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            // An authentication is already present, do not override it.
            filterChain.doFilter(request, response);
            return;
        }

        if (userId != null && username != null && rolesHeader != null) {

            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            PreAuthenticatedAuthenticationToken preAuth = new PreAuthenticatedAuthenticationToken(
                    username,
                    userId,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(preAuth);

        }

        filterChain.doFilter(request, response);
    }
}
