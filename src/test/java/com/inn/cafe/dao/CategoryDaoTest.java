package com.inn.cafe.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.inn.cafe.pojo.Category;



@DataJpaTest
//@Rollback(value=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryDaoTest {

	@Autowired
	CategoryDao categoryDao;
	

	@Test
	void testgetAllCategory() {
		
		System.out.println("categoryDao  :"+categoryDao);
		List<Category> list = categoryDao.getAllCategory();
		assertThat(list).isNotEmpty();
		
	}
}
