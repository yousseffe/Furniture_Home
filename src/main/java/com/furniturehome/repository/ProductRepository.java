package com.furniturehome.repository;

import com.furniturehome.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryId(Long categoryId);
}
