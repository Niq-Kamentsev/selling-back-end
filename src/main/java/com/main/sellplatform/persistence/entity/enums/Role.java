package com.main.sellplatform.persistence.entity.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_WRITE, Permission.USER_DELETE)),
    MODER(Set.of(
            Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_WRITE, Permission.USER_DELETE,
            Permission.MODER_READ, Permission.MODER_WRITE, Permission.MODER_DELETE
    )),
    ADMIN(Set.of(
            Permission.USER_READ, Permission.USER_UPDATE, Permission.USER_WRITE, Permission.USER_DELETE,
            Permission.MODER_READ, Permission.MODER_WRITE, Permission.MODER_DELETE,
            Permission.ADMIN_READ, Permission.ADMIN_WRITE, Permission.ADMIN_DELETE
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
