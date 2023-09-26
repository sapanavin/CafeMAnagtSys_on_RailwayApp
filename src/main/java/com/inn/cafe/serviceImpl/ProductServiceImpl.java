package com.inn.cafe.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.ProductDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.pojo.Category;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.wrapper.ProductWrapper;

import lombok.extern.slf4j.Slf4j;

import com.inn.cafe.pojo.Product;

@Slf4j
@Service
public class ProductServiceImpl  implements ProductService{

	@Autowired
	ProductDao productDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
		   if(jwtFilter.isAdmin()) {
			   if(validateProductMap(requestMap, false)) {
				   
				   productDao.save(getProductFromMap(requestMap, false));
				   return CafeUtils.getResponseEntity("Product Added Successfully ",HttpStatus.OK);
					
			   }
			   return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.UNAUTHORIZED);
				 
		   }
		   else {
			   return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
				
		}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}

	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
		
		Category category = new Category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		
		Product product= new Product();
		product.setCategory(category);
		if(isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		}
		else {
			product.setStatus("true");
		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
		
	}

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if(requestMap.containsKey("name")) {
			log.info("inside first if" +requestMap);
			if(requestMap.containsKey("id") && validateId){
			return true;
			}
			else if(!validateId){
				log.info("inside else if" +requestMap);
			 return true;
		}
				
	}
		return false;
	}

	
	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		log.info("Inside getAllProduct");
		
     try {
			return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	
		
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				if(validateProductMap(requestMap,true)){
					Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					
					if(optional.isPresent()) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return CafeUtils.getResponseEntity("Product updated Successfully",HttpStatus.OK);
						
					}
					else {
						return CafeUtils.getResponseEntity("Product Id does not exist",HttpStatus.BAD_REQUEST);
						
					}
				
				}
				else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
					
				}
				
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
				
			}
				//return( productDao.updateProduct(),HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if(optional.isPresent()) {
					productDao.deleteById(id);
					return  CafeUtils.getResponseEntity("Product deleted Successfully", HttpStatus.OK);
					
				}
				else {
					return CafeUtils.getResponseEntity("Product does not exists", HttpStatus.BAD_REQUEST);
					
				}
				
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
				
			}
			
		}
		catch(Exception ex) { 
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	
	}

	@Override
	public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				if(optional.isPresent()) {
					productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					return  CafeUtils.getResponseEntity("Product updated Successfully", HttpStatus.OK);
					
				}
				else {
					return CafeUtils.getResponseEntity("Product does not exists", HttpStatus.BAD_REQUEST);
					
				}
				
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
				return  new ResponseEntity<List<ProductWrapper>>(productDao.getProductByCategory(id), HttpStatus.OK);
								
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<ProductWrapper>(), HttpStatus.INTERNAL_SERVER_ERROR);
	
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			Optional<ProductWrapper> optional=Optional.ofNullable(productDao.getProductById(id));
			if(optional.isPresent())
			return new ResponseEntity<ProductWrapper>(productDao.getProductById(id),HttpStatus.OK);
			else
				return new ResponseEntity<>(new ProductWrapper(), HttpStatus.NO_CONTENT);			
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}
	return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
