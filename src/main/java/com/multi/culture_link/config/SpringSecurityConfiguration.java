package com.multi.culture_link.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// 추후에 시간 남으면 permit list 테이블 만들 예정 및 에러 처리할 예정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeHttpRequests((auth) -> auth
				
				.requestMatchers("/","/home","/user/login/**","/user/logout/**","/user/signUp/**","/user/login2/**","/festival/findPopularFestivalKeyword").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/user/myPage/**", "/festival/**","/museumEvent/**","/calendar/**","/mapDetail/**","/performance/**","/cultural-properties/**","/chat/**","/exhibition/**","/board/**").hasAnyRole("ADMIN", "USERS")
				
				.anyRequest().permitAll()
		
		);
		
		
		/*httpSecurity.formLogin((auth) -> auth.loginPage("/user/login").loginProcessingUrl("/user/login2").permitAll());*/
		httpSecurity.formLogin((auth) -> auth.loginPage("/user/login").loginProcessingUrl("/user/login2").defaultSuccessUrl("/", true));
		
		
		httpSecurity.logout((auth) -> auth.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/")
		
		);
		
		httpSecurity.csrf((auth) -> auth.disable());
		
		
		return httpSecurity.build();
		
		
	}
	
	
}
