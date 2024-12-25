package zip.agil.layar.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyTokenRequest {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}