package com.auth.security;

import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepsoitory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final UserRepsoitory userRepsoitory;
    private  final  IJwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {

            // ✅ Extract token from cookie
            String jwtToken = extractTokenFromCookies(request);

            // ✅ No token
            if (jwtToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ Extract email
            String email =
                    jwtService.extractEmail(jwtToken);

            // ✅ Check existing authentication
            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // ✅ Find user
                User user = userRepsoitory
                        .findByEmail(email)
                        .orElse(null);

                // ✅ Validate token
                if (user != null
                        && user.isActive()
                        && user.isEmailVerified()
                        && jwtService.isTokenValid(jwtToken, user)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_USER"
                                            )
                                    )
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // ✅ Set authentication
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }

        } catch (Exception ex) {

            // ✅ Clear invalid auth
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT from cookies
     */
    private String extractTokenFromCookies(
            HttpServletRequest request
    ) {

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            // ✅ Access token cookie name
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
    }

