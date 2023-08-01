package restaurant.petproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import restaurant.petproject.entity.Image;
import restaurant.petproject.repository.ImageRepository;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;

//    @GetMapping("/images/{id}")
//    private ResponseEntity<?> getImageById(@PathVariable Long id) {
////        Image image = imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + id));
//        // Добавления обработчика ошибок
////        Image image = imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + id));
////        return ResponseEntity.ok()
////                .header("fileName", image.getOriginalFileName())
////                .contentType(MediaType.valueOf(image.getContentType()))
////                .contentLength(image.getSize())
////                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
////    }
//        ResponseEntity.ok();
//    }
}
