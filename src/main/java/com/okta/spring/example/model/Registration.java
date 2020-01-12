package com.okta.spring.example.model;

import java.util.ArrayList;
import java.util.List;

public class Registration {

	private String firstName;
	private String lastName;
	private String login;
	private String email;
	private String password;
	private String groups;
	
	// Custom
	private String lastLogin;
	private int loginCount;
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	@Override
	public String toString() {
		return "Registration [firstName=" + firstName + ", lastName=" + lastName + ", login=" + login + ", email="
				+ email + groups + "]";
	}
	

}
