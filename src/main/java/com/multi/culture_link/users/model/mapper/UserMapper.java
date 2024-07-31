package com.multi.culture_link.users.model.mapper;


import com.multi.culture_link.users.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	
	
	UserDTO findUserByEmail(String email) throws Exception;
	
	int insertUser(UserDTO userDTO) throws Exception;
}
