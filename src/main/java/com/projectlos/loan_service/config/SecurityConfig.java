package com.projectlos.loan_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE = "ROLE_";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info(">>> Loading custom SecurityConfig");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/debug/auth").permitAll()

                        // Loan debug endpoint
                        .requestMatchers(HttpMethod.GET, "/api/loan/debug/test-loan-access").hasAnyRole("ADMIN", "MAKER")

                        // Applicant endpoints
                        .requestMatchers(HttpMethod.GET, "/api/loan/applicant/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.POST, "/api/loan/applicant/**").hasAnyRole("ADMIN", "MAKER")
                        .requestMatchers(HttpMethod.PUT, "/api/loan/applicant/**").hasAnyRole("ADMIN", "MAKER", "CHECKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/loan/applicant/**").hasRole("ADMIN")

                        // Task management endpoints
                        .requestMatchers(HttpMethod.GET, "/api/loan/tasks/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.POST, "/api/loan/tasks/*/approve").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.POST, "/api/loan/tasks/*/reject").hasAnyRole("ADMIN", "CHECKER", "APPROVER")

                        // Identity documents endpoints
                        .requestMatchers(HttpMethod.POST, "/api/loan/identity-documents/*/verify").hasAnyRole("ADMIN", "CHECKER")
                        .requestMatchers(HttpMethod.POST, "/api/loan/identity-documents").hasAnyRole("ADMIN", "MAKER")
                        .requestMatchers(HttpMethod.GET, "/api/loan/identity-documents/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.PUT, "/api/loan/identity-documents/**").hasAnyRole("ADMIN", "MAKER", "CHECKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/loan/identity-documents/**").hasRole("ADMIN")

                        // Collateral endpoints
                        .requestMatchers(HttpMethod.POST, "/api/loan/collaterals/*/verify").hasAnyRole("ADMIN", "CHECKER")
                        .requestMatchers(HttpMethod.POST, "/api/loan/collaterals").hasAnyRole("ADMIN", "MAKER")
                        .requestMatchers(HttpMethod.GET, "/api/loan/collaterals/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.PUT, "/api/loan/collaterals/**").hasAnyRole("ADMIN", "MAKER", "CHECKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/loan/collaterals/**").hasRole("ADMIN")

                        // Process management endpoints
                        .requestMatchers(HttpMethod.POST, "/api/loan/processes/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.GET, "/api/loan/processes/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.PUT, "/api/loan/processes/**").hasAnyRole("ADMIN", "MAKER", "CHECKER", "APPROVER")
                        .requestMatchers(HttpMethod.DELETE, "/api/loan/processes/**").hasAnyRole("ADMIN", "CHECKER", "APPROVER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS);
            if (realmAccess == null || realmAccess.isEmpty()) return List.of();

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get(ROLES);
            if (roles == null || roles.isEmpty()) return List.of();

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE + role))
                    .collect(Collectors.toList());
        });
        return converter;
    }

}