package com.multi.culture_link.users.service;

import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import com.multi.culture_link.users.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public AuthenticationServiceImpl(UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userMapper = userMapper;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		System.out.println("email : " + username);
		
		try {
			UserDTO userDTO = userMapper.findUserByEmail(username);
			
			if (userDTO==null){
				
				return null;
				
			}

			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			
			authorities.add(new SimpleGrantedAuthority(userDTO.getRoleId()));
		
			
			
			
			/*System.out.println("loadUserByUsername : " + authorities.toString());*/
			
			
			
			/*String encodedPassword = "{noop}" + userDTO.getPassword();*/
			
			/*System.out.println(userDTO.toString());
			
			
			System.out.println("encodedPassword : " + bCryptPasswordEncoder.encode(userDTO.getPassword()));*/
			
			VWUserRoleDTO result =  new VWUserRoleDTO(userDTO);
			
			/*System.out.println("VWUserRoleDTO : " + result.toString());*/
			
			return result;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
}
