package com.furniturehome.service;

import com.furniturehome.dto.ProductDTO;
import com.furniturehome.dto.ProductImageDTO;
import com.furniturehome.exception.BadRequestException;
import com.furniturehome.exception.ResourceNotFoundException;
import com.furniturehome.model.Category;
import com.furniturehome.model.Product;
import com.furniturehome.model.ProductImage;
import com.furniturehome.repository.CategoryRepository;
import com.furniturehome.repository.ProductImageRepository;
import com.furniturehome.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final Path uploadDir;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductImageRepository productImageRepository,
                          @Value("${app.upload.dir:uploads}") String uploadDir) throws IOException {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;

        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(this.uploadDir)) {
            Files.createDirectories(this.uploadDir);
        }
    }

    public ProductDTO createProduct(
            String name,
            String description,
            Double price,
            Double priceBeforeDiscount,
            Long categoryId,
            MultipartFile[] images
    ) throws IOException {

        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Product name must not be empty.");
        }
        if (price == null || price <= 0) {
            throw new BadRequestException("Price must be greater than 0.");
        }
        if (priceBeforeDiscount == null || priceBeforeDiscount <= 0) {
        	throw new BadRequestException("Price before discount must be greater than 0.");
        }
        if (priceBeforeDiscount != null && priceBeforeDiscount < price) {
            throw new BadRequestException("Price before discount must be greater than or equal to price.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // Create Product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setPriceBeforeDiscount(priceBeforeDiscount);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        // Save Images
        if (images != null) {
            for (MultipartFile image : images) {
                if (image.isEmpty()) continue;

                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path target = uploadDir.resolve(filename);
                Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                ProductImage img = new ProductImage();
                img.setImgUrl(filename);
                img.setProduct(saved);
                productImageRepository.save(img);
            }
        }

        return toDTO(saved);
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDTO(product);
    }

    public List<ProductDTO> listProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products available.");
        }

        List<ProductDTO> dtoList = new ArrayList<>();
        for (Product p : products) {
            dtoList.add(toDTO(p));
        }

        return dtoList;
    }
    
    public List<ProductDTO> listProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(this::toDTO)
                .toList();
    }


    public ProductDTO updateProduct(
            Long id,
            String name,
            String description,
            Double price,
            Double priceBeforeDiscount,
            Long categoryId,
            MultipartFile[] images
    ) throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }
        if (description != null) {
            product.setDescription(description);
        }
        if (price != null) {
            if (price <= 0) throw new BadRequestException("Price must be greater than 0.");
            product.setPrice(price);
        }
        if (priceBeforeDiscount != null) {
            if (priceBeforeDiscount < product.getPrice()) {
                throw new BadRequestException("Price before discount must be >= price.");
            }
            product.setPriceBeforeDiscount(priceBeforeDiscount);
        }
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            product.setCategory(category);
        }

        Product updated = productRepository.save(product);

        if (images != null) {
            for (MultipartFile image : images) {
                if (image.isEmpty()) continue;

                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path target = uploadDir.resolve(filename);
                Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                ProductImage img = new ProductImage();
                img.setImgUrl(filename);
                img.setProduct(updated);
                productImageRepository.save(img);
            }
        }

        return toDTO(updated);
    }

    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        List<ProductImage> imgs = productImageRepository.findByProductId(id);
        for (ProductImage img : imgs) {
            Path file = uploadDir.resolve(img.getImgUrl()).normalize();
            Files.deleteIfExists(file);
        }
        productRepository.delete(product);
    }

    public Resource loadImageAsResource(String filename) throws MalformedURLException {
        Path file = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists()) {
            throw new ResourceNotFoundException("Image not found: " + filename);
        }
        return resource;
    }

    private ProductDTO toDTO(Product p) {
        List<ProductImageDTO> imageDTOs = new ArrayList<>();
        List<ProductImage> images = Optional.ofNullable(p.getImages()).orElse(Collections.emptyList());
        for (ProductImage img : images) {
            imageDTOs.add(new ProductImageDTO(img.getId(), img.getImgUrl()));
        }

        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .priceBeforeDiscount(p.getPriceBeforeDiscount())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .images(imageDTOs)
                .build();
    }

}
