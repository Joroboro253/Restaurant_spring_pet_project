package restaurant.petproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    private boolean active;

    // повторяющийся код, который будет необходимо удлаить в дальнейшем
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name="user_roles",
//            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName = "ID")},
//            inverseJoinColumns = {@JoinColumn(name="ROLE_ID", referencedColumnName = "ID")})
    private List<Role> roles = new ArrayList<>();

    //ManyToOne
    @ManyToMany(cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();


    public boolean isActive() {
        return active;
    }

    // Why null?
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User() {
    }

    public User(String name, String email, String password, boolean active, String activationCode, List<Role> roles, List<Dish> dishes) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.dishes = dishes;
    }



    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
