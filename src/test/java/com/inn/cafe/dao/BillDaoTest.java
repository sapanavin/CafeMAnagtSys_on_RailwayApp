package com.inn.cafe.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.pojo.Bill;
import com.inn.cafe.pojo.Category;

@DataJpaTest
//@Rollback(value=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BillDaoTest {
	
	
	@Autowired
	BillDao billDao;
	

	@Test
	void testgetAllBills() {
		
	
		List<Bill> list = billDao.getAllBills();
		assertThat(list).isNotEmpty();
		
		assertThat(billDao.count()).isEqualTo(list.size());
		
	}
	
	@Test
	void testgetBillByUserName() {
		
		
		String name = "sapanavmore@gmail.com";
	
		List<Bill> list = billDao.getBillByUserName(name);
		assertThat(list).isNotEmpty();
		
		assertThat(list.get(0).getCreatedBy()).isEqualTo(name);
		
		
		System.out.println(list);
		System.out.println(list.get(0).getEmail());
		System.out.println(list.get(0).getCreatedBy());
	
	}
	
	/*
	
	List<Bill> getAllBills();

	List<Bill> getBillByUserName(@Param("username") String currentUser);
	 
	 */

}
