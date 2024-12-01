package zip.agil.layar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserResponse {

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpiredAt;

    private Long refreshTokenExpiredAt;
}
