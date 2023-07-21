package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.UserEntity;
import restaurant.petproject.models.Dish;
import restaurant.petproject.models.Image;
import restaurant.petproject.repository.DishRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.DishService;

import java.io.IOException;
import java.security.Principal;
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

    public void saveDish(Principal principal, Dish dish, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        dish.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize() != 0){
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            dish.addImageToProduct(image1);
        }
        if(file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            dish.addImageToProduct(image2);
        }
        if(file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            dish.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", dish.getTitle(), dish.getUser().getEmail());
        Dish dishFromDb = dishRepository.save(dish);
        dishFromDb.setPreviewImageId(dishFromDb.getImages().get(0).getId());
        dishRepository.save(dish);
    }

    @Override
    public UserEntity getUserByPrincipal(Principal principal) {
        if(principal == null) return new UserEntity();
        return userRepository.findByEmail(principal.getName());

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


}
