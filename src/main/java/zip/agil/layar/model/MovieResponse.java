package zip.agil.layar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {

    private String id;

    private String slug;

    private String title;

    private String description;

    private UserResponse uploader;

    private List<MovieBannerResponse> banners;

    private List<MovieVideoResponse> videos;

    private Long createdAt;

    private Long updatedAt;

    private Long deletedAt;
}
