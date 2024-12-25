package zip.agil.layar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieBannerResponse {

    private String id;

    private String url;

    private String name;

    private Long createdAt;

    private Long updatedAt;

    private Long deletedAt;
}
