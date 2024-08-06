package com.multi.culture_link.users.service;

import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import com.multi.culture_link.users.model.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private final UserMapper userMapper;
	
	public UserService(UserMapper userMapper) throws Exception{
		this.userMapper = userMapper;
	}
	
	/**
	 * 회원의 정보를 삽입
	 * @param userDTO
	 * @throws Exception
	 */
	public void signUp(UserDTO userDTO) throws Exception{
		
		userMapper.insertUser(userDTO);
	
	}
	
	
	/**
	 * 이메일로 회원을 식별
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public UserDTO findUserByEmail(String email) throws Exception{
		
		UserDTO user = userMapper.findUserByEmail(email);
		
		return user;
		
	}
	
	public void deleteUserAccount(VWUserRoleDTO user) throws Exception{
		
		userMapper.deleteUserAccount(user);
		
	}
}
