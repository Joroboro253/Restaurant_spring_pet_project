package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
