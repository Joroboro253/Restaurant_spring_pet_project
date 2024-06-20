package restaurant.petproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.*;
import restaurant.petproject.repository.CartItemRepository;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.DishService;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

//import net.coobird.thumbnailator.Thumbnails;


@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    public final DishRepository dishRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageService;

    public List<Dish> listDishes(String title) {
        if(title != null) return dishRepository.findByTitle(title);
        return dishRepository.findAll();
    }

    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }


    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public void saveDish(Principal principal, Dish dish) throws IOException, SQLException {
        Optional<User> optionalUser = getUserByPrincipal(principal);
        System.out.println(optionalUser);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = optionalUser.get();
        userRepository.save(user);
        dish.setUser(user);

        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        Dish dishFromDb = dishRepository.save(dish);
        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    @Override
    public Optional<User> getUserByPrincipal(Principal principal) {
        if(principal == null) return Optional.empty();
        return Optional.ofNullable(userRepository.findByEmail(principal.getName()));

    }

    private List<Image> toImageEntity(MultipartFile[] files) throws IOException, SQLException {
        Image image = null;
        List<Image> images = new ArrayList<>();
        for(MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            Blob blob = new SerialBlob(bytes);
            image = new Image();
            image.setImage(blob);
            images.add(image);
        }
        imageService.create(image);
        return images;
    }

    @Transactional
    public void deleteDish(Long dishId) {
        cartItemRepository.deleteByDishId(dishId);
        dishRepository.deleteById(dishId);
    }

    public Dish updateDishImages(Dish dish, MultipartFile[] files) throws IOException, SQLException {
        // Deleting old images
        for (Image oldImage : dish.getImages()) {
            imageService.delete(oldImage);
        }
        //Adding new images
        List<Image> newImages = toImageEntity(files);
        for (Image newImage : newImages) {
            imageService.create(newImage);
            dish.addImageToProduct(newImage);
        }
        dishRepository.save(dish);

        return dish;
    }
}
