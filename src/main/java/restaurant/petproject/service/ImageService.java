package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.Image;


import java.util.List;

@Service
public interface ImageService {
    Image create(Image image);
    List<Image> viewAll();
    Image viewById(long id);
}
