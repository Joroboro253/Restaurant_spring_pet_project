package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.*;
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

    public final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageService;
//    public CategoryRepository categoryRepository;
//    private CouponRepository couponRepository;

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

    // Principal не доходит до этого метода
    @Override
    public Optional<User> getUserByPrincipal(Principal principal) {
//        if(principal == null) return new User();
//        return userRepository.findByEmail(principal.getName());
        if(principal == null) return Optional.empty();
//        log.info("Returning userRepository.findByEmail(principal.getName())", userRepository.findByEmail(principal.getName()));
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


    public void deleteDish(User user, Long id){
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

    public void changeDishName(Long id, String name) {
        Dish d = new Dish();
        d = dishRepository.findById(id).get();
        d.setTitle(name);
        dishRepository.save(d);
    }

    public void changeDishDescription(Long id, String description){
        Dish d = new Dish();
        d = dishRepository.findById(id).get();
        d.setDescription(description);
        dishRepository.save(d);
    }

    public void changeDishPrice(Long id, int price) {
        Dish d = new Dish();
        d = dishRepository.findById(id).get();
        d.setPrice(price);
    }
    public void changeDishDiscount(Long id, int discount) {
        Dish d = new Dish();
        d= dishRepository.findById(id).get();
//        d.getDiscount().setDiscount(discount);
        dishRepository.save(d);
    }

    private String bufferedImageTobase64(BufferedImage image ) throws UnsupportedEncodingException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", Base64.getEncoder().wrap(out));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return out.toString(StandardCharsets.ISO_8859_1.name());
    }

    private BufferedImage base64ToBufferedImage(String base64Img) {
        BufferedImage image = null;
        byte[] decodedBytes = Base64.getDecoder().decode(base64Img);

        try {
            image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return image;
    }


}
