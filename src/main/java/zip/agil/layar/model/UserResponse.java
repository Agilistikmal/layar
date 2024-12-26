package zip.agil.layar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zip.agil.layar.enumerate.UserRole;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String id;

    private String username;

    private String fullName;

    private UserRole roles;

    private Long createdAt;

    private Long updatedAt;
}
