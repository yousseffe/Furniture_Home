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

    // Create product
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("price") Double price,
            @RequestParam("priceBeforeDiscount") Double priceBeforeDiscount,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {

        ProductDTO dto = productService.createProduct(name, description, price, priceBeforeDiscount, categoryId, images);
        return ResponseEntity
                .created(URI.create("/api/products/" + dto.getId()))
                .body(dto); // 🔹 201 Created
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    // List all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> listProducts(
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        if (categoryId != null) {
            // If categoryId provided return only products in that category
            return ResponseEntity.ok(productService.listProductsByCategory(categoryId));
        }
        // Otherwise return all products
        return ResponseEntity.ok(productService.listProducts());
    }

    // Update product
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "priceBeforeDiscount", required = false) Double priceBeforeDiscount,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        ProductDTO dto = productService.updateProduct(id, name, description, price, priceBeforeDiscount, categoryId, images);
        return ResponseEntity.ok(dto);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Serve product image
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable("filename") String filename) throws IOException {
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
