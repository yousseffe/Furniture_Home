package com.furniturehome;

import com.furniturehome.model.Product;
import com.furniturehome.model.ProductImage;
import com.furniturehome.model.User;
import com.furniturehome.model.Category;
import com.furniturehome.repository.CategoryRepository;
import com.furniturehome.repository.ProductImageRepository;
import com.furniturehome.repository.ProductRepository;
import com.furniturehome.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FurnitureHomeApplication {

        public static void main(String[] args) {
                SpringApplication.run(FurnitureHomeApplication.class, args);
        }   

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
        if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .name("Ali Hassan")
                        .email("ali.hassan@example.com")
                        .phone("01012345678")
                        .password("hashed_password1")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Mona Youssef")
                        .email("mona.youssef@example.com")
                        .phone("01087654321")
                        .password("hashed_password2")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Omar Khaled")
                        .email("omar.khaled@example.com")
                        .phone("01123456789")
                        .password("hashed_password3")
                        .role("admin")
                        .build());

                userRepository.save(User.builder()
                        .name("Sara Adel")
                        .email("sara.adel@example.com")
                        .phone("01298765432")
                        .password("hashed_password4")
                        .role("customer")
                        .build());

                userRepository.save(User.builder()
                        .name("Ahmed Nabil")
                        .email("ahmed.nabil@example.com")
                        .phone("01555555555")
                        .password("hashed_password5")
                        .role("seller")
                        .build());
                }   
        };
}
        // @Bean
        // CommandLineRunner initProducts(CategoryRepository categoryRepo,
        //                                 ProductRepository productRepo,
        //                                 ProductImageRepository imageRepo) {
        // return args -> {
        //         if (categoryRepo.count() == 0 && productRepo.count() == 0) {
        //         Category chairs = categoryRepo.save(Category.builder()
        //                 .name("Chairs")
        //                 .description("Comfortable chairs for home and office")
        //                 .build());

        //         Category tables = categoryRepo.save(Category.builder()
        //                 .name("Tables")
        //                 .description("Dining tables, coffee tables, and office tables")
        //                 .build());

        //         Category beds = categoryRepo.save(Category.builder()
        //                 .name("Beds")
        //                 .description("Modern and classic beds for bedrooms")
        //                 .build());

        //         Product chair1 = productRepo.save(Product.builder()
        //                 .name("Ergonomic Office Chair")
        //                 .description("Adjustable ergonomic chair with lumbar support")
        //                 .price(2500.0)
        //                 .priceBeforeDiscount(3000.0)
        //                 .category(chairs)
        //                 .build());

        //         Product table1 = productRepo.save(Product.builder()
        //                 .name("Wooden Dining Table")
        //                 .description("Solid oak dining table, seats 6 people")
        //                 .price(5500.0)
        //                 .priceBeforeDiscount(6500.0)
        //                 .category(tables)
        //                 .build());

        //         Product bed1 = productRepo.save(Product.builder()
        //                 .name("Queen Size Bed")
        //                 .description("Comfortable queen size bed with storage")
        //                 .price(8000.0)
        //                 .priceBeforeDiscount(9500.0)
        //                 .category(beds)
        //                 .build());

        //         imageRepo.save(ProductImage.builder()
        //                 .imgUrl("https://example.com/images/office-chair.jpg")
        //                 .product(chair1)
        //                 .build());

        //         imageRepo.save(ProductImage.builder()
        //                 .imgUrl("https://example.com/images/dining-table.jpg")
        //                 .product(table1)
        //                 .build());

        //         imageRepo.save(ProductImage.builder()
        //                 .imgUrl("https://example.com/images/queen-bed.jpg")
        //                 .product(bed1)
        //                 .build());
        //         }
        // };
        // }
}
