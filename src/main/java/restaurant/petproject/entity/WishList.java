package restaurant.petproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "whishlist")
public class WishList {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String sessionToken;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<WishListItem> items = new HashSet<WishListItem>();

    public WishList() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Set<WishListItem> getItems() {
        return items;
    }

    public void setItems(Set<WishListItem> items) {
        this.items = items;
    }
}
