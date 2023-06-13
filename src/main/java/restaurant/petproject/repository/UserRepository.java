package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
