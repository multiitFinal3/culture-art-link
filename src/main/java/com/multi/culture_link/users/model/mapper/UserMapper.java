package com.multi.culture_link.users.model.mapper;


import com.multi.culture_link.users.model.dto.UserDTO;
import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	
	
	UserDTO findUserByEmail(String email) throws Exception;
	
	int insertUser(UserDTO userDTO) throws Exception;
	
	void deleteUserAccount(VWUserRoleDTO user) throws Exception;
	
	void updateUserAccount(UserDTO userDTO) throws Exception;
	
	void insertRoleId(UserDTO userDTO) throws Exception;
}
