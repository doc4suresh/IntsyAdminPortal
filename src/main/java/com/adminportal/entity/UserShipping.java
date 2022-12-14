package com.adminportal.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserShipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String userShippingName;
	private String userShippingStreet1;
	private String userShippingStreet2;
	private String userShippingCity;
	private String userShippingDistrict;
	private String userShippingPostalCode;
	private boolean userShippingDefault;
	
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users users;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserShippingName() {
		return userShippingName;
	}


	public void setUserShippingName(String userShippingName) {
		this.userShippingName = userShippingName;
	}


	public String getUserShippingStreet1() {
		return userShippingStreet1;
	}


	public void setUserShippingStreet1(String userShippingStreet1) {
		this.userShippingStreet1 = userShippingStreet1;
	}


	public String getUserShippingStreet2() {
		return userShippingStreet2;
	}


	public void setUserShippingStreet2(String userShippingStreet2) {
		this.userShippingStreet2 = userShippingStreet2;
	}


	public String getUserShippingCity() {
		return userShippingCity;
	}


	public void setUserShippingCity(String userShippingCity) {
		this.userShippingCity = userShippingCity;
	}


	public String getUserShippingDistrict() {
		return userShippingDistrict;
	}


	public void setUserShippingDistrict(String userShippingDistrict) {
		this.userShippingDistrict = userShippingDistrict;
	}


	public String getUserShippingPostalCode() {
		return userShippingPostalCode;
	}


	public void setUserShippingPostalCode(String userShippingPostalCode) {
		this.userShippingPostalCode = userShippingPostalCode;
	}


	public Users getUsers() {
		return users;
	}


	public void setUsers(Users user) {
		this.users = user;
	}


	public boolean isUserShippingDefault() {
		return userShippingDefault;
	}


	public void setUserShippingDefault(boolean userShippingDefault) {
		this.userShippingDefault = userShippingDefault;
	}
	
	
}
