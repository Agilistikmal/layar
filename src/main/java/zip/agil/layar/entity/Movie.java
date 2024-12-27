package zip.agil.layar.entity;

import jakarta.annotation.Nullable;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MovieBanner> banners;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MovieVideo> videos;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;

    public MovieResponse toResponse() {
        return MovieResponse.builder()
                .id(id)
                .slug(slug)
                .title(title)
                .description(description)
                .uploader(uploader.toResponse())
                .banners(banners.stream().map(MovieBanner::toResponse).toList())
                .videos(videos.stream().map(MovieVideo::toResponse).toList())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
}
