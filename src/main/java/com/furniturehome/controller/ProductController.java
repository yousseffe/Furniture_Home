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
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // keep a local reference to upload dir only for content-type probing; matches service property
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
            @RequestParam(required = true) Double price,
            @RequestParam(required = false) Double priceBeforeDiscount,
            @RequestParam(required = true) Long categoryId,
            @RequestParam(name = "images", required = false) MultipartFile[] images
    ) throws IOException {

        ProductDTO dto = productService.createProduct(name, description, price, priceBeforeDiscount, categoryId, images);
        // return 201 Created with Location header pointing to GET endpoint
        return ResponseEntity.created(
                java.net.URI.create("/api/products/" + dto.getId())
        ).body(dto);
    	
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> listProducts() {
        return ResponseEntity.ok(productService.listProducts());
    }

    /**
     * Update product partially and optionally append new images.
     */
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
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Serve images saved on disk:
     * GET /api/products/images/{filename}
     */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Resource resource = productService.loadImageAsResource(filename);
            Path file = uploadDir.resolve(filename).normalize();
            String contentType = Files.probeContentType(file);
            MediaType mediaType;
            try {
                mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
