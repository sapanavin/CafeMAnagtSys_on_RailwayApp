package com.inn.cafe.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.inn.cafe.wrapper.ProductWrapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.inn.cafe.pojo.Product;

public interface ProductDao extends JpaRepository<Product, Integer>{

	List<ProductWrapper> getAllProduct();

	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status") String status, @Param("id")Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id")Integer id);
	
	ProductWrapper getProductById (@Param("id")Integer id);
	
       int getMaxProductId();

	

}
