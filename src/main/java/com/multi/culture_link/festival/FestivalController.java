package com.multi.culture_link.festival;


import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/festival")
public class FestivalController {


	@GetMapping("/festival-list")
	public String festivalListPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model){
		
		System.out.println("@AuthenticationPrincipal : " + user.toString());
		model.addAttribute("user", user.getUser());
		
		return "/festival/festivalList";

	}




}
