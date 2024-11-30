package zip.agil.layar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_banners")
public class MovieBanner {

    @Id
    private String id;

    @Column(name = "movie_id")
    private String movieId;

    private String url;

    private String name;
}
