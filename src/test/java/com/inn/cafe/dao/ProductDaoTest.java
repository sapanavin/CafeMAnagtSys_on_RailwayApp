package com.inn.cafe.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.pojo.Product;
import com.inn.cafe.pojo.User;
import com.inn.cafe.wrapper.ProductWrapper;

@DataJpaTest
//@Rollback(value=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class ProductDaoTest {
	
	@Autowired
	ProductDao productDao;
	
	@Test
	void testgetAllProduct() {
		List<ProductWrapper> list = productDao.getAllProduct();
		assertThat(list).isNotEmpty();
		int noOfProducts = (int) productDao.count();
		System.out.println(list.size());
	assertThat(list.size()).isEqualTo(noOfProducts);
	System.out.println(noOfProducts);
	}
	
	@Test
	void testupdateProductStatus() {
		
		//updating for the user ID which is available in the User Table
		Integer product_id=3;
		String status = "true";
		Integer test_id = productDao.updateProductStatus(status, product_id);
		System.out.println(test_id);
		assertThat(test_id).isEqualTo(1);
		
		//updating for the user ID which is not available in the User Table
		int max_Product_Id = (int) productDao.getMaxProductId();
		System.out.println("max_Product_Id  :"+max_Product_Id);
		Integer test_id2 = productDao.updateProductStatus(status, max_Product_Id+1);
		assertThat(test_id2).isEqualTo(0);
		
	}
	
	@Test
	void testgetProductByCategory() {
		List<ProductWrapper> list = productDao.getProductByCategory(1);
		assertThat(list).isNotEmpty();
		
	}
	@Test
	 void testgetProductById() {
		
		String email="sapanavmore@gmail.com";
		Optional<Product> product = productDao.findById(5);
		
		//assertThat(product.ifPresent((p)=>);
		assertThat(product.isPresent()).isEqualTo(true);
		
		
	}
}
/*
	List<ProductWrapper> getAllProduct();

	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status") String status, @Param("id")Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id")Integer id);
	
	ProductWrapper getProductById (@Param("id")Integer id);

*/