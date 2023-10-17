package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.*;
import restaurant.petproject.repository.CategoryRepository;
import restaurant.petproject.repository.CouponRepository;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.DishService;
import restaurant.petproject.service.ImageService;

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
import static org.hibernate.criterion.Projections.id;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {

    public final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ImageServiceImpl imageService;
    // this repo need Autowired or no ?
    // change it all to private later ?
    public CategoryRepository categoryRepository;
    private CouponRepository couponRepository;

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


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
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

    public void changeDishPrice(Long id, Double price) {
        Dish d = new Dish();
        d = dishRepository.findById(id).get();
        d.setPrice(price);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void saveDishDiscount(Long id, int discount) {
        Dish d = new Dish();
        d = dishRepository.findById(id).get();
        if(d.getDiscount() == null) {
            Coupon c = new Coupon();
            c.setDiscount(discount);
            d.setDiscount(c);
        }

        else {
            d.getDiscount().setDiscount(discount);
        }
        dishRepository.save(d);
    }

    public void changeDishDiscount(Long id, int discount) {
        Dish d = new Dish();
        d= dishRepository.findById(id).get();
        d.getDiscount().setDiscount(discount);
        dishRepository.save(d);
    }

    // No API ??
    public String resizeImageForUser(String src, int width, int height) {
        BufferedImage image = base64ToBufferedImage(src);
//        try {
////            image = resizeImage(image, width, height);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            return bufferedImageTobase64(image);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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

//    Thumbnails ???
//    private  BufferedImage resizeImage(BufferedImage image , int width , int height) throws IOException {
//        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
//        try {
////            Thumbnails.of(image).size(width, height).outputFormat("JPEG").outputQuality(1).toOutputStream(outputstream);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        byte[] data = outputstream.toByteArray();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
//        return ImageIO.read(inputStream);
//    }

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

    public Dish getDishWithBiggestDiscount() {
        Coupon discount = couponRepository.findMax();
        List<Dish> dishes = dishRepository.findAll();
        Dish featuredDish = null;
        for(Dish d : dishes) {
            if(d.getDiscount().equals(discount)){
                featuredDish = d;
                break;
            }
        }
        return featuredDish;
    }
}
