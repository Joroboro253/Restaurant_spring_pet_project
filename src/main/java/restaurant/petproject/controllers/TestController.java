//package restaurant.petproject.controllers;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//import restaurant.petproject.entity.Image;
//import restaurant.petproject.entity.Image2;
//import restaurant.petproject.service.ImageService;
//
//import javax.sql.rowset.serial.SerialBlob;
//import javax.sql.rowset.serial.SerialException;
//import java.io.IOException;
//import java.sql.Blob;
//import java.sql.SQLException;
//import java.util.List;
//
//@Controller
//public class TestController {
//    @Autowired
//    private ImageService imageService;
//
//    @GetMapping("/display")
//    public ResponseEntity<byte[]> displayName(@RequestParam("id") long id) throws IOException, SQLException {
//        Image2 image = imageService.viewById(id);
//        byte [] imageBytes = null;
//        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
//    }
//
//    @GetMapping("/")
//    public ModelAndView home() {
//        ModelAndView mv = new ModelAndView("insex");
//        List<Image2> image2List = imageService.viewAll();
//        mv.addObject("imageList", image2List);
//        return mv;
//    }
//
//    @GetMapping("/add")
//    public ModelAndView addImage() {
//        return new ModelAndView("addImage");
//    }
//
//    @PostMapping("/add")
//    public String addImagePost(HttpServletRequest request, @RequestParam("image")MultipartFile file) throws IOException, SerialException, SQLException {
//        byte[] bytes = file.getBytes();
//        Blob blob = new SerialBlob(bytes);
//        Image2 image = new Image2();
//        image.setImage(blob);
//        imageService.create(image);
//        return "redirect:/";
//    }
//}
