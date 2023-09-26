package com.inn.cafe.restImpl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.rest.UserRest;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;




@RestController
@Slf4j
@CrossOrigin
public class UserRestImpl implements UserRest{

	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
		
	
	@Override
	public ResponseEntity<String> signUp( Map<String, String> requestMap) {
		try {
			log.info("Inside UserRestImpl SignUp " +requestMap.toString()+","+requestMap.get("name"));
			
			return userService.signUp(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			
			
			return userService.login(requestMap);
		}
		catch(Exception ex) {
			
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

	}


	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			return userService.getAllUser();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}


	
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			return userService.update(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	
	public ResponseEntity<String> checkToken() {
		try {
			
			log.info("inside CheckToken");
			return userService.checkToken();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

	}


	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			return userService.changePassword(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			return userService.forgotPassword(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

	}


	
	
		
}