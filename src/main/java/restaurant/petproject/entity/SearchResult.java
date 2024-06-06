package restaurant.petproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SearchResult {
    @Id
    private Long id;
    private String title;
    private String description;
}
