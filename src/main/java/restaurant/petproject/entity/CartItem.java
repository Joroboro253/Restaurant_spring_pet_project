package restaurant.petproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
//@Table(name = "cart_item")
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    private Dish dish;
    private Integer quantity;
    @Transient
     private double subtotal;

    public CartItem(Dish dish, Integer quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public CartItem() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Double.compare(cartItem.getSubtotal(), getSubtotal()) == 0 && Objects.equals(getId(), cartItem.getId()) && Objects.equals(getDish(), cartItem.getDish());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDish(), getSubtotal());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", dish=" + dish +
                ", subtotal=" + subtotal +
                '}';
    }


}
