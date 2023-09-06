package restaurant.petproject.entity;

//Maybe useless class

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    @NotNull
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;
    private Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }
    @Override
    public String toString() {
        return "Carousel [id=" + id + ", image=" + image + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Carousel other = (Carousel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
