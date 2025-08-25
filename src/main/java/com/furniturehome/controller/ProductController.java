package com.furniturehome.controller;

import com.furniturehome.dto.ProductDTO;
import com.furniturehome.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final Path uploadDir;

    public ProductController(ProductService productService,
                             @org.springframework.beans.factory.annotation.Value("${app.upload.dir:uploads}") String uploadDir) {
        this.productService = productService;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Double price,
            @RequestParam Double priceBeforeDiscount,
            @RequestParam Long categoryId,
            @RequestParam(name = "images", required = false) MultipartFile[] images
    ) throws IOException {

        ProductDTO dto = productService.createProduct(name, description, price, priceBeforeDiscount, categoryId, images);
        return ResponseEntity
                .created(URI.create("/api/products/" + dto.getId()))
                .body(dto); // 🔹 returns 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id)); // 🔹 200 OK
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> listProducts() {
        return ResponseEntity.ok(productService.listProducts()); // 🔹 200 OK
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double priceBeforeDiscount,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(name = "images", required = false) MultipartFile[] images
    ) throws IOException {
        ProductDTO dto = productService.updateProduct(id, name, description, price, priceBeforeDiscount, categoryId, images);
        return ResponseEntity.ok(dto); // 🔹 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 🔹 204 No Content
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Resource resource = productService.loadImageAsResource(filename);
        Path file = uploadDir.resolve(filename).normalize();
        String contentType = Files.probeContentType(file);
        MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
