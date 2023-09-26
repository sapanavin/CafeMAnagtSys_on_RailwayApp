package com.inn.cafe.dao;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.inn.cafe.pojo.User;
import com.inn.cafe.wrapper.UserWrapper;

@DataJpaTest
//@Rollback(value=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserDaoTest {
  
	@Autowired
	UserDao userDao;
	
	
	@Test
	 void testfindByemailId() {
		
		String email="sapanavmore@gmail.com";
		User user = userDao.findByemailId(email);
		
		assertThat(user).isNotNull();
		assertThat(user.getEmail()).isEqualTo(email);
		
		
	}
	
	@Test
	void testgetAllAdmin() {
		List<String> list =  userDao.getAllAdmin();
		assertThat(list).isNotEmpty();
	
		
	}
	@Test
	void testgetAllUser() {
		List<UserWrapper> list =  userDao.getAllUser();
		assertThat(list).isNotEmpty();
		
		assertThat(list.size()).isEqualTo((int) userDao.count());
		
	}
	
	@Test
	void testupdateStatus() {
		
		//updating for the user ID which is available in the User Table
		Integer id=2;
		String status = "true";
		Integer test_id = userDao.updateStatus(status, id);
		System.out.println(test_id);
		assertThat(test_id).isEqualTo(1);
		
		//updating for the user ID which is not available in the User Table
		int total_users = (int) userDao.count();
		System.out.println(total_users);
		Integer test_id2 = userDao.updateStatus(status, total_users+1);
		assertThat(test_id2).isEqualTo(0);
		
	}
}






/*
User findByemailId(@Param("email") String email);
	
		
	List<UserWrapper> getAllUser();

	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String string,@Param("id") Integer id);


	List<String> getAllAdmin();
	
	User findByemail(String email);


*/