package com.inn.cafe.dao;



import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.inn.cafe.pojo.User;
import com.inn.cafe.wrapper.UserWrapper;

@Repository
public interface UserDao extends JpaRepository<User,Integer>{

	
	
	User findByemailId(@Param("email") String email);
	
		
	List<UserWrapper> getAllUser();

	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String string,@Param("id") Integer id);


	List<String> getAllAdmin();
	
	User findByemail(String email);
	
}


