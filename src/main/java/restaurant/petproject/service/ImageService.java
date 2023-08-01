package restaurant.petproject.service;

import org.springframework.stereotype.Service;
import restaurant.petproject.entity.Image;


import java.util.List;

@Service
public interface ImageService {
    public Image create(Image image);
    public List<Image> viewAll();
    public Image viewById(long id);
}
