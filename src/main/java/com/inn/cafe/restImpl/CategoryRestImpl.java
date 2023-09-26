package com.inn.cafe.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.rest.CategoryRest;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.CafeUtils;

import com.inn.cafe.pojo.Category;

@RestController
public class CategoryRestImpl<Category> implements CategoryRest{

		@Autowired
		CategoryService categoryService;
	
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			return categoryService.addNewCategory(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}


	@Override
	public ResponseEntity<List<com.inn.cafe.pojo.Category>> getAllCategory(String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity(new ArrayList<Category>(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	@Override
	public ResponseEntity<List<com.inn.cafe.pojo.Category>> getAllCategory() {
		try {
			return categoryService.getAllCategory();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity(new ArrayList<Category>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			return categoryService.updataCategory(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	

}
