package com.librart.managament.utility;

import com.librart.managament.config.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public  String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated!");
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String userId = principal.getId();
        return userId;

    }

    public  boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isLibrarian(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_LIBRARIAN"));
    }

    public  boolean isSelf(String userId){
        return  getCurrentUserId().equals(userId);
    }

}
