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
				return userDTO.getRoleId();
			}
		});

//		System.out.println("collection : " + collection);
		return collection;
	}
	
	@Override
	public String getPassword() {
//		System.out.println("getPassword : "+userDTO.getPassword());
		return userDTO.getPassword();
	}
	
	@Override
	public String getUsername() {
		/*System.out.println("getUsername : "+ userDTO.getUserName());*/
		return userDTO.getUserName();
	}
	
	public int getUserId() {
		
		/*System.out.println("getUserId : "+ userDTO.getUserId());*/
		return userDTO.getUserId();
		
	}
	
	public String getTel() {
		
		/*System.out.println("getTel : "+ userDTO.getTel());*/
		return userDTO.getTel();
		
	}
	
	public String getGender() {
		
		/*System.out.println("getGender : "+ userDTO.getGender());*/
		return userDTO.getGender();
		
	}
	
	public int getRegionId() {
		
		/*System.out.println("getRegionId : "+ userDTO.getRegionId());*/
		return userDTO.getRegionId();
		
	}
	
	public UserDTO getUserDTO() {
		return userDTO;
	}
	
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	public String getRoleId() {
		
		/*System.out.println("getRoleId : "+ userDTO.getRoleId());*/
		return userDTO.getRoleId();
		
	}
	
	public String getRoleContent() {
		
		/*System.out.println("getRoleContent : "+ userDTO.getRoleContent());*/
		return userDTO.getRoleContent();
		
	}
	
	public int getUserAge() {
		
		return userDTO.getUserAge();
		
	}
	
	public void setUserAge(int age) {
		
		userDTO.setUserAge(age);
		
		return;
		
	}
	
	
	public String getEmail() {
		
		return userDTO.getEmail();
		
	}
	
	public void setEmail(String email) {
		
		userDTO.setEmail(email);
		
		return;
		
	}
	
	public String getUserProfilePic() {
		
		return userDTO.getUserProfilePic();
		
	}
	
	public void setUserProfilePic(String profilePic) {
		
		userDTO.setUserProfilePic(profilePic);
		
		return;
		
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
	
	public UserDTO getUser() {
		/*System.out.println("getUserDTO");*/
		return userDTO;
		
	}
	
	
	@Override
	public String toString() {
		return "VWUserRoleDTO{" +
				"userDTO=" + userDTO +
				", vwUserRoleDTO=" + vwUserRoleDTO +
				'}';
	}
}
