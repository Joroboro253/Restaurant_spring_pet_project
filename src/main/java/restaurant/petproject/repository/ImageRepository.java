package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.petproject.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
