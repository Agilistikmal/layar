package zip.agil.layar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserRole {

    @Id
    String name;

    String description;
}
