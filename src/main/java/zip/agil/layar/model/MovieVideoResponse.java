package zip.agil.layar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zip.agil.layar.enumerate.VideoQuality;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieVideoResponse {

    private String url;

    private String name;

    private VideoQuality quality;
}
