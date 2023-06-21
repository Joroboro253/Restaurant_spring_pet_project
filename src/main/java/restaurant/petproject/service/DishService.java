package restaurant.petproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.models.Dish;
import restaurant.petproject.models.Image;
import restaurant.petproject.models.User;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public List<Dish> listDishes(String title) {
        if(title != null) return dishRepository.findByTitle(title);
        return dishRepository.findAll();
    }

    public void saveDish(Principal principal, Dish dish, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws Exception {
        dish.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            dish.addImageToDish(image1);
        }
        if(file2.getSize() != 0) {
            image2 = toImageEntity(file1);
            image2.setPreviewImage(true);
            dish.addImageToDish(image2);
        }
        if(file1.getSize() != 0) {
            image2 = toImageEntity(file1);
            image2.setPreviewImage(true);
            dish.addImageToDish(image2);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        Dish dishFromDb = dishRepository.save(dish);
        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws Exception {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(User user, Long id){
        Dish dish = dishRepository.findById(id)
                .orElse(null);
        if(dish != null) {
            if(dish.getUser().getId().equals(user.getId())) {
                dishRepository.delete(dish);
                log.info("Dish with id = {} was deleted", id);
            } else {
                log.error("User: {} haven`t this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }
}
