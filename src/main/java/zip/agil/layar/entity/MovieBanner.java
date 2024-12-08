package zip.agil.layar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import zip.agil.layar.model.MovieBannerResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "movie_banners")
@SQLDelete(sql = "UPDATE movie_banners SET deleted_at = extract(epoch from now()) WHERE id = ?")
@SQLRestriction(value = "deleted_at is null")
public class MovieBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    private String url;

    private String name;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;

    public MovieBannerResponse toResponse() {
        return MovieBannerResponse.builder()
                .name(name)
                .url(url)
                .build();
    }
}
