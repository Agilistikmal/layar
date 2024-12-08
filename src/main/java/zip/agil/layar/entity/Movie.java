package zip.agil.layar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import zip.agil.layar.model.MovieBannerResponse;
import zip.agil.layar.model.MovieResponse;
import zip.agil.layar.model.MovieVideoResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "movies")
@SQLDelete(sql = "UPDATE movies SET deleted_at = extract(epoch from now()) WHERE id = ?")
@SQLRestriction(value = "deleted_at is null")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String slug;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "id")
    private User uploader;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;

    public MovieResponse toResponse(List<MovieBanner> banners, List<MovieVideo> videos) {
        List<MovieBannerResponse> bannerResponses = new ArrayList<>();
        List<MovieVideoResponse> videoResponses = new ArrayList<>();

        for (MovieBanner banner : banners) {
            bannerResponses.add(banner.toResponse());
        }

        for (MovieVideo video : videos) {
            videoResponses.add(video.toResponse());
        }

        return MovieResponse.builder()
                .id(id)
                .slug(slug)
                .title(title)
                .description(description)
                .uploader(uploader.toResponse())
                .banners(bannerResponses)
                .videos(videoResponses)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
}
