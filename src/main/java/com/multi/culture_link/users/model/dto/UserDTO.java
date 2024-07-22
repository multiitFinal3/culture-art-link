package com.multi.culture_link.users.model.dto;

import lombok.Data;

@Data
public class UserDTO {
	
	
	private int userId;
	private String email;
	private String password;
	private String userName;
	private String tel;
	private String gender;
	private int regionId;
	private String roleId;
	private String roleContent;
}
