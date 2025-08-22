package com.furniturehome.service;

import com.furniturehome.dto.ProductDTO;
import com.furniturehome.dto.ProductImageDTO;
import com.furniturehome.model.Category;
import com.furniturehome.model.Product;
import com.furniturehome.model.ProductImage;
import com.furniturehome.repository.CategoryRepository;
import com.furniturehome.repository.ProductImageRepository;
import com.furniturehome.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final Path uploadDir;

    public ProductService(ProductRepository productRepository,
                          ProductImageRepository productImageRepository,
                          CategoryRepository categoryRepository,
                          @Value("${app.upload.dir:uploads}") String uploadDir) throws IOException {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    @Transactional
    public ProductDTO createProduct(String name,
                                    String description,
                                    Double price,
                                    Double priceBeforeDiscount,
                                    Long categoryId,
                                    MultipartFile[] images) throws IOException {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));

    	
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .priceBeforeDiscount(priceBeforeDiscount)
                .category(category)
                .images(new ArrayList<>())
                .build();

        storeAndAttachImages(product, images);

        Product saved = productRepository.save(product);
        
        return toDTO(saved);
    }

    public ProductDTO getProduct(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        return toDTO(p);
    }

    public List<ProductDTO> listProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO updateProduct(Long id,
                                    String name,
                                    String description,
                                    Double price,
                                    Double priceBeforeDiscount,
                                    Long categoryId,
                                    MultipartFile[] newImages) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));

        if (name != null) product.setName(name);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(price);
        if (priceBeforeDiscount != null) product.setPriceBeforeDiscount(priceBeforeDiscount);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));
            product.setCategory(category);
        }

        // append new images if provided
        if (newImages != null && newImages.length > 0) {
            storeAndAttachImages(product, newImages);
        }

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));

        // delete all image files from disk
        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                deleteFileForImageUrl(img.getImgUrl());
                productImageRepository.delete(img); // also remove from DB
            }
        }

        productRepository.delete(product);
    }

    /**
     * Load a saved image as a Resource (for controller to serve).
     * filename must be the stored filename (UUID + extension).
     */
    public Resource loadImageAsResource(String filename) throws MalformedURLException {
        Path file = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) return resource;
        throw new MalformedURLException("File not found or not readable: " + filename);
    }

    /* ------------------ helpers ------------------ */

    private void storeAndAttachImages(Product product, MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) return;

        for (MultipartFile file : images) {
            if (file == null || file.isEmpty()) continue;

            String original = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            String filename = UUID.randomUUID().toString() + ext;
            Path target = uploadDir.resolve(filename);

            // copy file to disk (replace existing if unlikely collision)
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // build accessible URL
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/products/images/")
                    .path(filename)
                    .toUriString();

            ProductImage img = ProductImage.builder()
                    .imgUrl(url)
                    .product(product)
                    .build();

            product.getImages().add(img);
        }
    }

    private void deleteFileForImageUrl(String imgUrl) {
        if (imgUrl == null || imgUrl.isBlank()) return;
        try {
            String filename = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
            if (filename.isBlank()) return;
            Path filePath = uploadDir.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // don't fail product deletion if file removal fails; log it instead
            log.warn("Failed to delete image file for url={} : {}", imgUrl, e.getMessage());
        }
    }

    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .priceBeforeDiscount(p.getPriceBeforeDiscount())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .images(Optional.ofNullable(p.getImages()).orElse(Collections.emptyList())
                        .stream()
                        .map(img -> new ProductImageDTO(img.getId(), img.getImgUrl()))
                        .collect(Collectors.toList()))
                .build();
    }
}
