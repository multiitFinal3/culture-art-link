package com.multi.culture_link.users.model.mapper;


import com.multi.culture_link.users.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
	
	
	UserDTO findUserByEmail(String email) throws Exception;
}
