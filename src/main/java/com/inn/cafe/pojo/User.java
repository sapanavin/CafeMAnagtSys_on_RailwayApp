package com.inn.cafe.pojo;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;



@NamedQuery(name = "User.findByemailId", query= "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUser", query = "select new com.inn.cafe.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u")
@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='Admin'")


@Data
@javax.persistence.Entity
@DynamicUpdate
@DynamicInsert
@javax.persistence.Table(name = "user")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	
	@Column(name="name")
	private String name;
	
	
	@Column(name="contactNumber")
	private String contactNumber;
	
	@Column(name="email")
	private String email;
	
	
	@Column(name="password")
	private String password;
	
	
	@Column(name="status")
	private String status;
	
	
	@Column(name="role")
	private String role;

	
}
