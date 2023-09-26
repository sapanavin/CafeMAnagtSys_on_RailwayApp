package com.inn.cafe.restImpl;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class ForTesting {
	
	@GetMapping("/")
	public String testMe() {
		return "Ya I am Running";
	}
	
	@PostMapping("/testsingup")
	public ResponseEntity<String> signUpTest( @RequestBody(required=true) Map<String, String> requestMap) {
		try {
			log.info("Inside Test Rest Conroller SignUp " +requestMap.toString()+","+requestMap.get("name"));
			
			return CafeUtils.getResponseEntity("everething is good",HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
