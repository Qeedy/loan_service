package com.projectlos.loan_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan/debug")
@Slf4j
public class DebugController {

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> debugAuth(Authentication authentication) {
        Map<String, Object> debugInfo = new HashMap<>();
        
        if (authentication == null) {
            debugInfo.put("status", "NOT_AUTHENTICATED");
            return ResponseEntity.ok(debugInfo);
        }
        
        debugInfo.put("status", "AUTHENTICATED");
        debugInfo.put("name", authentication.getName());
        debugInfo.put("principal", authentication.getPrincipal().getClass().getSimpleName());
        
        // Extract authorities/roles
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        debugInfo.put("authorities", authorities);
        
        // Check specific roles
        boolean hasChecker = authorities.contains("ROLE_CHECKER");
        boolean hasAdmin = authorities.contains("ROLE_ADMIN");
        boolean hasMaker = authorities.contains("ROLE_MAKER");
        
        debugInfo.put("has_role_checker", hasChecker);
        debugInfo.put("has_role_admin", hasAdmin);
        debugInfo.put("has_role_maker", hasMaker);
        
        // If JWT, extract claims
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            debugInfo.put("jwt_subject", jwt.getSubject());
            debugInfo.put("jwt_issuer", jwt.getIssuer());
            
            // Extract realm_access
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            debugInfo.put("realm_access", realmAccess);
            
            // Extract all claims
            debugInfo.put("all_claims", jwt.getClaims());
        }
        
        log.info("Debug auth info: {}", debugInfo);
        return ResponseEntity.ok(debugInfo);
    }
    
    @GetMapping("/test-loan-access")
    public ResponseEntity<String> testLoanAccess() {
        return ResponseEntity.ok("CHECKER can access this endpoint that mimics loan-applications GET!");
    }
}
