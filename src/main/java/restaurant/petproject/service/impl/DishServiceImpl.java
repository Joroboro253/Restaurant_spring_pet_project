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

    public void saveDish(Principal principal, Dish dish, MultipartFile file) throws IOException, SQLException {
        User user = getUserByPrincipal(principal);

        userRepository.save(user);

        dish.setUser(user);
//        byte[] bytes = file.getBytes();
//        Blob blob = new SerialBlob(bytes);
//        Image image = new Image();
//        image.setImage(blob);
//        Image image1;
//        Image image2;
//        if(file1.getSize() != 0){
//            image1 = toImageEntity(file1);
////            image1.setPreviewImage(true);
//            dish.addImageToProduct(image1);
//        }
//        if(file2.getSize() != 0) {
//            image2 = toImageEntity(file2);
//            dish.addImageToProduct(image2);
//        }
//        if(file3.getSize() != 0) {
//            image3 = toImageEntity(file3);
//            dish.addImageToProduct(image3);
//        }
        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        // Ругается на строку ниже object references an unsaved transient instance - save the transient instance before flushing : restaurant.petproject.entity.Dish.user -> restaurant.petproject.entity.User
        Dish dishFromDb = dishRepository.save(dish);
        // Строка вызывает ошибку: Index 0 out of bounds for length 0. Убрал так как, есть возможность добавления только одного фото
//        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());

    }

//    private Image toImageEntity(MultipartFile file) throws IOException {
//        Image image = new Image();
//        image.setName(file.getName());
//        image.setOriginalFileName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setSize(file.getSize());
//        image.setBytes(file.getBytes());
//        return image;
//    }

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
