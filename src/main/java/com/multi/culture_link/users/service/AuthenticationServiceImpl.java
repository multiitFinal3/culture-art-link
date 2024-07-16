package com.multi.culture_link.users.service;

import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import com.multi.culture_link.users.model.mapper.UsersMapper;
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
	
	private final UsersMapper usersMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public AuthenticationServiceImpl(UsersMapper usersMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersMapper = usersMapper;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("email : " + username);
		
		try {
			System.out.println(1);
			UserDTO userDTO = usersMapper.findUserByEmail(username);
			System.out.println(2);
			System.out.println("userDTO : " + userDTO);
			
			System.out.println(3);
			
			if (userDTO==null){
				
				return null;
				
			}

			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			
			authorities.add(new SimpleGrantedAuthority(userDTO.getRoleName()));
		
			
			
			
			System.out.println("loadUserByUsername : " + authorities.toString());
			
			
			
			/*String encodedPassword = "{noop}" + userDTO.getPassword();*/
			
			System.out.println(userDTO.toString());
			
			
			/*System.out.println("encodedPassword : " + encodedPassword);*/
			
			VWUserRoleDTO result =  new VWUserRoleDTO(userDTO);
			
			System.out.println("VWUserRoleDTO : " + result.toString());
			
			return result;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}
}
