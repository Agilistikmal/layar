package zip.agil.layar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;
import zip.agil.layar.enumerate.VideoQuality;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMovieVideoRequest {

    @NotBlank
    @URL
    private String url;

    @NotBlank
    @UUID
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    private VideoQuality quality;
}
