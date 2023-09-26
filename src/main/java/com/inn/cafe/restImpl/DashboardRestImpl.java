package com.inn.cafe.restImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.rest.DashboardRest;
import com.inn.cafe.service.DashBoardService;


@RestController
public class DashboardRestImpl implements DashboardRest{

	
	@Autowired
	DashBoardService dashBoardService;
	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		try {
			return dashBoardService.getCount();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
	}

}
