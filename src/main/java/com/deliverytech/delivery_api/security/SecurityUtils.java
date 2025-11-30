package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    // -------------------------------------------
    // RETORNAR USUÁRIO LOGADO
    // -------------------------------------------
    public Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        return (Usuario) auth.getPrincipal();
    }

    // -------------------------------------------
    // RETORNAR ID DO USUÁRIO LOGADO
    // -------------------------------------------
    public Long getCurrentUserId() {
        Usuario user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    // -------------------------------------------
    // VERIFICAR ROLE DO USUÁRIO
    // -------------------------------------------
    public boolean hasRole(Role role) {
        Usuario user = getCurrentUser();
        return user != null && user.getRole() == role;
    }
}
