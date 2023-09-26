package com.inn.cafe.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import com.inn.cafe.dao.UserDao;
import com.inn.cafe.service.UserService;


@Rollback(value=false)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserDao userDao;
	
	
	@Test
	void testsignUp() {
		Map<String, String> map = new HashMap<>();
		map.put("name","sapana");
		map.put("email","sapana111@gmail.com");
		map.put("password","123");
		map.put("contactNumber","123");
		
		
		System.out.println("userService := "+userService);
		System.out.println("userDao := "+userDao);
		System.out.println("MAP := "+map);
		
		ResponseEntity<String> responseEntity =userService.signUp(map);

		System.out.println("responseEntity := "+responseEntity);
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		//assertThat(responseEntity.getHeaders().containsKey(headerName)).isTrue();
		//assertThat(responseEntity.getHeaders().get(headerName)).containsExactly(headerValue1, headerValue2);
		//assertThat(responseEntity.getBody()).isEqualTo(entity);
		Map<String, String> map2 = new HashMap<>();
		map2.put("name","sapana");
		map2.put("email","sapana111@gmail.com");
		map2.put("password","123");
		//map.put("contactNumber","123");
		ResponseEntity<String> responseEntity2 =userService.signUp(map2);
		
		System.out.println("responseEntity2 := "+responseEntity2);
		assertThat(responseEntity2).isNotNull();
		assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		
		
	}
	
	@Test
	void testlogin() {
		
	}


}


/**
  public ResponseEntity<String> signUp(Map<String, String> requestMap)
 public ResponseEntity<String> login(Map<String, String> requestMap)
 
 */