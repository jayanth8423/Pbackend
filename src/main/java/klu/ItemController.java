package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "*") // ✅ Allow any frontend (React config handles URLs)
@RequestMapping("/backend")
public class ItemController {

    @Autowired
    private ItemRepo itemRepository;

    private static final String IMAGE_DIR = "images/";

    // ✅ Add Item
    @PostMapping("/items")
    public ResponseEntity<Items> addItem(
            @RequestParam int pid,
            @RequestParam String pname,
            @RequestParam float pprs,
            @RequestParam String pcategory,
            @RequestParam int quantity,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            // Ensure images directory exists
            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) dir.mkdirs();

            // Generate unique filename to prevent overwrite
            String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(IMAGE_DIR + imageName);
            Files.write(imagePath, imageFile.getBytes());

            // Save item to DB
            Items item = new Items();
            item.setPid(pid);
            item.setPname(pname);
            item.setPprs(pprs);
            item.setPcategory(pcategory);
            item.setQuantity(quantity);
            item.setPimg(imageName); // ✅ Only filename stored

            Items savedItem = itemRepository.save(item);
            return ResponseEntity.ok(savedItem);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Get All Items
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    // ✅ Delete Item
    @DeleteMapping("/items/{pid}")
    public ResponseEntity<Void> deleteItem(@PathVariable int pid) {
        if (itemRepository.existsById(pid)) {
            itemRepository.deleteById(pid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Serve Images
    @GetMapping(value = "/images/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(IMAGE_DIR, filename);
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
