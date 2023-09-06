package restaurant.petproject.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Lombok создаст автоматически геттеры и сеттеры
@Data
@Table(name = "dishes")
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    private String description;
    private int price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dish")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    // cascade = CascadeType.REFRESH // was cascade = CascadeType.ALL
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    private LocalDateTime dateOfCreated;
    // Новая система с реализацией скидок
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Coupon discount;
    // Добавление категорий в код
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dish_category", joinColumns = {
            @JoinColumn(name = "dish_id", referencedColumnName = "id")})
    private Set<Category> categories = new HashSet<>();
    @PrePersist
    private void onCreate() {
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageToProduct(Image image) {
        image.setDish(this);
        images.add(image);
    }
    public Dish(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }
    public Dish() {
    }
}
