package com.multi.culture_link.users.model.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class VWUserRoleDTO implements UserDetails {
	
	
	private UserDTO userDTO;
	
	public VWUserRoleDTO vwUserRoleDTO;
	
	public VWUserRoleDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userDTO.getRoleName();
			}
		});
		
		
		return collection;
	}
	
	@Override
	public String getPassword() {
		return userDTO.getPassword();
	}
	
	@Override
	public String getUsername() {
		return userDTO.getUserName();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
