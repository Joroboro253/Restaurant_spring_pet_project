package restaurant.petproject.entity;

import jakarta.persistence.*;
import restaurant.petproject.entity.Dish;

import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "images_table")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String name;
//    private String originalFileName;
//    private Long size;
//    private String contentType;
    private boolean previewImage;
    @Lob
    private Blob image;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Dish dish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public boolean isPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(boolean previewImage) {
        this.previewImage = previewImage;
    }
}
