package com.multi.culture_link.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
	
	// 암호화는 회원가입 기능 만든 후 사용
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	WebSecurityCustomizer configure() {
		
		return (web) -> web
				.ignoring()
				.requestMatchers("/css/**", "/js/**", "/img/**");
		
	}
	
	// 추후에 시간 남으면 permit list 테이블 만들 예정 및 에러 처리할 예정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		/*Map<String, List<String>> permitUrlListMap = authenticationService.getPermitUrlListMap();
		
		List<String> adminPermitUrlList = permitUrlListMap.get("adminPermitUrlList");
		List<String> usersPermitUrlList = permitUrlListMap.get("usersPermitUrlList");*/
		
		ArrayList<String> adminPermitUrlList = new ArrayList<>(List.of("/admin/**"));
		ArrayList<String> usersPermitUrlList = new ArrayList<>(List.of("/performance/**", "/exhibition/**","/museumEvent/**","/festival/**","/cultural-properties/**","/chat/**","/board/**","/myPage/**"));
		
		adminPermitUrlList.forEach(url -> System.out.println("admin permint list : " + url));
		usersPermitUrlList.forEach(url -> System.out.println("general users permint list : " + url));
		
		
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
						.requestMatchers("/","/home","/loginPost","/users/login","/users/login2").permitAll()
						.requestMatchers(usersPermitUrlList.toArray(new String[usersPermitUrlList.size()])).hasAnyRole("USERS", "ADMIN")
						.requestMatchers(adminPermitUrlList.toArray(new String[adminPermitUrlList.size()])).hasRole("ADMIN")
						.anyRequest().permitAll()
				
				)
				.formLogin(form -> form
								.loginPage("/users/login")
								.usernameParameter("email").passwordParameter("password")
						.loginProcessingUrl("/users/login2")
						.defaultSuccessUrl("/", true)
						/*.failureForwardUrl("/common/loginError")*/
						.failureForwardUrl("/")
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/userLogout"))
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.logoutSuccessUrl("/"))
				
				/*.exceptionHandling((exception) -> exception.accessDeniedPage("/common/accessDenied"))*/;
		
		
		return httpSecurity.build();
		
		
	}
	


}
