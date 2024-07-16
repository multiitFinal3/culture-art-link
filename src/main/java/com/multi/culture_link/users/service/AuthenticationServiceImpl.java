package com.multi.culture_link.users.service;

import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import com.multi.culture_link.users.model.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final UsersMapper usersMapper;
	
	@Autowired
	public AuthenticationServiceImpl(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("email : " + email);
		
		try {
			System.out.println(1);
			UserDTO userDTO = usersMapper.findUserByEmail(email);
			System.out.println(2);
			System.out.println("userDTO : " + userDTO);
			
			System.out.println(3);

			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			
			authorities.add(new SimpleGrantedAuthority(userDTO.getRoleName()));
		
			
			
			
			System.out.println("loadUserByUsername : " + authorities.toString());
			
			
			
			String encodedPassword = "{noop}" + userDTO.getPassword();
			
			VWUserRoleDTO result =  new VWUserRoleDTO(userDTO.getEmail(), encodedPassword, authorities);
			
			System.out.println("VWUserRoleDTO : " + result.toString());
			
			return result;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
}
