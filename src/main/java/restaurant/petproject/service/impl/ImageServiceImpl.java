package restaurant.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restaurant.petproject.entity.Image;
import restaurant.petproject.repository.ImageRepository;
import restaurant.petproject.service.ImageService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image create(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public List<Image> viewAll() {
        return (List<Image>) imageRepository.findAll();
    }

    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }



//    public List<Image> fromFileToImage(MultipartFile[] files) throws IOException, SQLException {
//        List<Image> images = new ArrayList<>();
//        for (MultipartFile file : files) {
//            byte[] bytes = file.getBytes();
//            Blob blob = new SerialBlob(bytes);
//            Image image = new Image();
//            image.setImage(blob);
//            images.add(image);
//        }
//        return images;
//    }


    public void delete(Image image) {
        imageRepository.delete(image);
    }
}
