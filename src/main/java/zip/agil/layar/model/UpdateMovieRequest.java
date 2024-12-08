package zip.agil.layar.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMovieRequest {

    @Size(min = 3, max = 50)
    private String slug;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 3)
    private String description;
}
