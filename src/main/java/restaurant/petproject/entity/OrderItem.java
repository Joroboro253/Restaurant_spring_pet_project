package restaurant.petproject.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    public OrderItem() {}

    public OrderItem(Order order, Dish dish, Integer quantity, Integer unitPrice) {
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}




