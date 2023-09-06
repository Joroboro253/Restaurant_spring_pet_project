package restaurant.petproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;

@Entity
public class Category {
    @Getter
    @Id
    @GeneratedValue
    private Long id;
    @Getter
    @NotNull
    private String name;
    @Transient
    private int dishNumber;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories")
    private Set<Dish> dishes;
    public Set<Dish> getDishes() {
        return dishes;
    }
    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }
    public int getDishNumber() {
        return this.dishes.size();
    }
    public Category() {

    }
    public Category(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Category other = (Category) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
