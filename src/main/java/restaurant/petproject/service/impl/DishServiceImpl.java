package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.User;
import restaurant.petproject.entity.Dish;
import restaurant.petproject.entity.Image;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.DishService;
import restaurant.petproject.service.ImageService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    public final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public List<Dish> listDishes(String title) {
        if(title != null) return dishRepository.findByTitle(title);
        return dishRepository.findAll();
    }

    public void saveDish(Principal principal, Dish dish) throws Exception {
        dish.setUser(getUserByPrincipal(principal));
    }

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public void saveDish(Principal principal, Dish dish, List<Image> images) throws IOException, SQLException {
        User user = getUserByPrincipal(principal);
        userRepository.save(user);
        dish.setUser(user);
        for (Image image : images) {
            dish.addImageToProduct(image);
        }

//        dish.setPreviewImageId(0L);


        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        Dish dishFromDb = dishRepository.save(dish);
        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());

    }

    private Image toImageEntity(MultipartFile file) throws IOException, SQLException {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        Image image = new Image();
        image.setImage(blob);

        imageService.create(image);
        return image;
    }

    public void deleteProduct(User user, Long id){
        Dish dish = dishRepository.findById(id).orElse(null);
        if(dish != null) {
            if(dish.getUser().getId().equals(user.getId())) {
                dishRepository.delete(dish);
                log.info("Dish with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this dish with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Dish with id = {} is not found", id);
        }
    }
}
