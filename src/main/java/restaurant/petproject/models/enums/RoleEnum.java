package restaurant.petproject.models.enums;

import org.springframework.security.core.GrantedAuthority;
import restaurant.petproject.models.Role;


public enum RoleEnum implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
