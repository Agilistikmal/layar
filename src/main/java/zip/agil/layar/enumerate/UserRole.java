package zip.agil.layar.enumerate;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    USER(
            Set.of(
                    UserPermission.USER_READ,
                    UserPermission.USER_WRITE,
                    UserPermission.USER_EDIT,
                    UserPermission.USER_DELETE
            )
    ),

    ADMIN(
            Set.of(
                    UserPermission.USER_READ,
                    UserPermission.USER_WRITE,
                    UserPermission.USER_EDIT,
                    UserPermission.USER_DELETE,

                    UserPermission.ADMIN_READ,
                    UserPermission.ADMIN_WRITE,
                    UserPermission.ADMIN_EDIT,
                    UserPermission.ADMIN_DELETE
            )
    );

    private final Set<UserPermission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
