package restaurant.petproject.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;
import restaurant.petproject.entity.UserEntity;

import java.time.LocalDateTime;

@Data
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    private String description;
    private int price;
    private LocalDateTime dateOfCreated;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @PrePersist
    private void onCreate() {
        dateOfCreated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Dish(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Dish() {
    }
}
