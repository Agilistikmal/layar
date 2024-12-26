package zip.agil.layar.enumerate;

import lombok.*;

@RequiredArgsConstructor
@Getter
public enum UserPermission {

    ADMIN_READ("admin.read"),
    ADMIN_WRITE("admin.write"),
    ADMIN_DELETE("admin.delete"),
    ADMIN_EDIT("admin.edit"),

    USER_READ("user.read"),
    USER_WRITE("user.write"),
    USER_DELETE("user.delete"),
    USER_EDIT("user.edit");

    private final String permission;
}
