package com.inn.cafe.serviceImpl;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.jwt.CustomerUsersDetailsService;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.jwt.JwtUtil;
import com.inn.cafe.pojo.User;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.utils.EmailUtils;
import com.inn.cafe.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUsersDetailsService customerUsersDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	EmailUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		log.info("Inside SignUp " +requestMap.toString()+","+requestMap.get("name"));
		
		try {
				if(validateSignUpMap(requestMap)) {
			
				User user = userDao.findByemailId(requestMap.get("email"));
			 
					if(Objects.isNull(user)) {
					 User new_user = getUserFromMap(requestMap);//this method returns  a user object from requestParameters
					 userDao.save(new_user);
					 
					 return CafeUtils.getResponseEntity("Successfully Registerd New User ",HttpStatus.CREATED);
					}
					else {
					 return CafeUtils.getResponseEntity("Email Already Exist",HttpStatus.BAD_REQUEST);
					}
		}
			else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
			}
				
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		 
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside Login");
		log.info("email " ,requestMap.get("email"));
		System.out.println("Inside login in UserService Impl:2" +requestMap.get("email"));
		log.info("password " ,requestMap.get("password"));
		System.out.println("Inside login in UserService Impl:2" +requestMap.get("password"));
		System.out.println("Inside login in UserService Impl :1");
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
			if(auth.isAuthenticated()) {
				System.out.println("Inside login in UserService Impl:2");
				
				if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String> (jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
							customerUsersDetailsService.getUserDetail().getRole()),HttpStatus.OK);
				}
			
			else {
				return  new ResponseEntity<String> ("Waiting For Admin Approval", HttpStatus.BAD_REQUEST);
			}
			}
		}
		catch(Exception ex) {
			log.error("{}", ex);
		}
		return  new ResponseEntity<String> ("Bad Credentials", HttpStatus.BAD_REQUEST);
		
	}

	
	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if(jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);	
				}
		
			else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);	
			
			}
	}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				
				if(optional.isPresent()) {
				    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					 
				    sendMailToAdmin(requestMap.get("status"),optional.get().getEmail(), userDao.getAllAdmin());
					 
				    return CafeUtils.getResponseEntity("Usesr Status Updates Successfully", HttpStatus.OK);
					 
				
				}
				else {
					return CafeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
				}
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.OK);
			}
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	
		@Override
	public ResponseEntity<String> checkToken() {
		log.info("inside CheckToken");
		
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> changePassword(Map<String,String> requestMap) {
		try {
			 User userObj = userDao.findByemail(jwtFilter.getCurrentUser());
			 System.out.println("oldPassword : "+requestMap.get("oldPassword"));
			 System.out.println("newPassword : "+requestMap.get("newPassword"));
			 if(!userObj.equals(null)) {
				 if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					 
					userObj.setPassword(requestMap.get("newPassword")); 
					userDao.save(userObj);
					System.out.println("userObj : "+userObj.getPassword());
					
					return CafeUtils.getResponseEntity("Password updated Successfully", HttpStatus.OK);
				 }
				 return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
			 }
			 return CafeUtils.getResponseEntity("User is not Found", HttpStatus.BAD_REQUEST);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		System.out.println("Came to change forgot Pasword in UsewrserviceImpl : ");
		try {
			User user = userDao.findByemail(requestMap.get("email"));
			if(! Objects.isNull(user) &&  !Strings.isNullOrEmpty(user.getEmail())) {
				
				System.out.println("Email : "+user.getEmail());
				emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
			}
			return CafeUtils.getResponseEntity("Check your mail for Credentials", HttpStatus.OK);
	
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAdmin(String status, String user, List<String> allAdmin) {
		allAdmin.remove(jwtFilter.getCurrentUser());
		
		if(status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleEmail(jwtFilter.getCurrentUser(),
					                     "Account Approved" ,
					                     "USER:- "+ user +"\n  is approved by \n ADMIN:- "+jwtFilter.getCurrentUser(),
					                     allAdmin);
		}
		else {
			emailUtils.sendSimpleEmail(jwtFilter.getCurrentUser(),
                    "Account disapproved" ,
                    "USER:- "+ user +"\n  is disapproved by \n ADMIN:- "+jwtFilter.getCurrentUser(),
                    allAdmin);
		}
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if(requestMap.containsKey("name")&&requestMap.containsKey("contactNumber")&&
				requestMap.containsKey("email")&&
						requestMap.containsKey("password")) {
			return true;
			
		}
		else {
			return false;
		}
	}
	
	private User getUserFromMap(Map<String,String> requestMap) {
		User user=new User();
		user.setName(requestMap.get("name"));
		user.setEmail(requestMap.get("email"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
		}

	
	
}
