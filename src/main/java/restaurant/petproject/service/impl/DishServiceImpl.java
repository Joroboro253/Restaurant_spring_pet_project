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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    public final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageService;

    public List<Dish> listDishes(String title) {
        if(title != null) return dishRepository.findByTitle(title);
        return dishRepository.findAll();
    }

//    public void saveDish(Principal principal, Dish dish) throws Exception {
//        dish.setUser(getUserByPrincipal(principal));
//    }

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

//    , List<Image> images
    public void saveDish(Principal principal, Dish dish) throws IOException, SQLException {
        Optional<User> optionalUser = getUserByPrincipal(principal);
        System.out.println(optionalUser);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = optionalUser.get();
        userRepository.save(user);
        dish.setUser(user);
//        for (Image image : images) {
//            dish.addImageToProduct(image);
//        }
        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        Dish dishFromDb = dishRepository.save(dish);
        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    // Principal не доходит до этого метода
    @Override
    public Optional<User> getUserByPrincipal(Principal principal) {
//        if(principal == null) return new User();
//        return userRepository.findByEmail(principal.getName());
        if(principal == null) return Optional.empty();
//        log.info("Returning userRepository.findByEmail(principal.getName())", userRepository.findByEmail(principal.getName()));
        return Optional.ofNullable(userRepository.findByEmail(principal.getName()));

    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
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

    public Dish updateDishImages(Dish dish, MultipartFile files) throws IOException, SQLException {
        // Deleting old images
        for (Image oldImage : dish.getImages()) {
            imageService.delete(oldImage);
        }
        //Adding new images
        List<Image> newImages = (List<Image>) toImageEntity(files);
        for (Image newImage : newImages) {
            imageService.create(newImage);
            dish.addImageToProduct(newImage);
        }
        dishRepository.save(dish);

        return dish;
    }


}
