package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.cafe.pojo.Category;

public interface CategoryDao extends JpaRepository<Category,Integer> {

	
	
		List<Category> getAllCategory();
	
	
}


/*select category.id as id1_1_, category.name as name2_1_ from category 
 * where category.id in (select product1_.category_fk from product product1_ 
 * cross join category category2_ where product1_.category_fk=category2_.id and product1_.status='true')
*/
 