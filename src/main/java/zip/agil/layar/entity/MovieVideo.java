package zip.agil.layar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import zip.agil.layar.enumerate.VideoQuality;
import zip.agil.layar.model.MovieVideoResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "movie_videos")
@SQLDelete(sql = "UPDATE movie_videos SET deleted_at = extract(epoch from now()) WHERE id = ?")
@SQLRestriction(value = "deleted_at is null")
public class MovieVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    private String url;

    private String name;

    @Enumerated(EnumType.STRING)
    private VideoQuality quality;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "deleted_at")
    private Long deletedAt;

    public MovieVideoResponse toResponse() {
        return MovieVideoResponse.builder()
                .name(name)
                .url(url)
                .quality(quality)
                .build();
    }
}
