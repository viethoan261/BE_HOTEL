package com.example.hotel.common.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;


public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return Optional.ofNullable("");
        }

        if (auth.getPrincipal() instanceof String) {
            return Optional.ofNullable((String) auth.getPrincipal());
        }

        UserDetails currentAuditor = (UserDetails) auth.getPrincipal();
        return Optional.ofNullable(currentAuditor.getUsername());
    }

}

