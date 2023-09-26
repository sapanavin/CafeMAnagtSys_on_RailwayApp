package com.inn.cafe.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Data
@javax.persistence.Entity
@DynamicUpdate
@DynamicInsert
@javax.persistence.Table(name = "category")

@NamedQuery(name="Category.getAllCategory",query = "select c from Category c where c.id IN(select p.category from Product p where p.status='true')")

public class Category  implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	
	@Column(name="name")
	private String name;
	
	
	
	
	


}
