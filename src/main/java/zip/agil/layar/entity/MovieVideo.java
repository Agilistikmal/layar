package zip.agil.layar.entity;

import jakarta.persistence.*;
import lombok.*;
import zip.agil.layar.enumerate.VideoQuality;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "movie_videos")
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
}
