package com.multi.culture_link.users.model.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class VWUserRoleDTO extends User{
	
	
	private String email;
	private String password;
	private String roleName;

	
	public VWUserRoleDTO(String email, String password,  Collection<? extends GrantedAuthority> authorities) {
		super(email, password, authorities);
	}
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
