package com.inn.cafe.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inn.cafe.wrapper.UserWrapper;


@RequestMapping("/user")
public interface UserRest {
	
	@PostMapping("/signup")
	public ResponseEntity<String> signUp( @RequestBody(required=true) Map<String,String> requestMap);
	
	@PostMapping("/login")
	public ResponseEntity<String> login( @RequestBody(required=true) Map<String,String> requestMap);
	
	@GetMapping(path="/get")
	public ResponseEntity<List<UserWrapper>> getAllUser();
	
	@PostMapping("/update")
	public ResponseEntity<String> update( @RequestBody(required=true) Map<String,String> requestMap);
	
	@GetMapping(path="/checkToken")
	public ResponseEntity<String> checkToken();
	
	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword( @RequestBody(required=true) Map<String,String> requestMap);
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword( @RequestBody(required=true) Map<String,String> requestMap);
	

}
