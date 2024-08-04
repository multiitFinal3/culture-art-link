package com.multi.culture_link.users.service;

import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private final UserMapper userMapper;
	
	public UserService(UserMapper userMapper) throws Exception{
		this.userMapper = userMapper;
	}
	
	public void signUp(UserDTO userDTO) throws Exception{
		
		userMapper.insertUser(userDTO);
	
	}
	
	public UserDTO findUserByEmail(String email) throws Exception{
		
		UserDTO user = userMapper.findUserByEmail(email);
		
		return user;
		
	}
}
