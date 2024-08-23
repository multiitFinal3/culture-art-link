package com.multi.culture_link.users.controller;


import com.multi.culture_link.redis.service.RedisService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;

@RestController
@RequestMapping("/sms")
public class CoolSMSController {
	
	@Value("${API-KEY.coolSMS.api-key}")
	private String apiKey;
	
	@Value("${API-KEY.coolSMS.api-secret}")
	private String apiSecret;
	
	@Value("${API-KEY.coolSMS.from-number}")
	private String fromNumber;
	
	private RedisService redisService;
	
	public CoolSMSController(RedisService redisService) {
		// 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
		this.redisService = redisService;
	}
	
	/**
	 * 단일 메시지 발송 예제
	 */
	@PostMapping("/send-one")
	public String sendOne(@RequestParam(name = "toNumber", required = true) String toNumber) {
		
		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
		
		toNumber = toNumber.replace("-", "").trim();
		System.out.println("from : " + fromNumber + ", to : " + toNumber);
		Message message = new Message();
		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		message.setFrom(fromNumber);
		message.setTo(toNumber);
		
		Random random = new Random();
		String authNum = "";
		
		for (int i = 1; i <= 4; i++) {
			
			int num = random.nextInt(10);
			authNum += num;
			
		}
		
		System.out.println("인증 문자 : " + authNum);
		
		message.setText("다음 네자리 숫자를 입력해주세요. [" + authNum + "].");
		
		redisService.setValues(toNumber, authNum, Duration.ofMillis(1000*60*5));
		
		SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
		
		System.out.println("response : " + response);

		return "문자 전송 완료";
	}
	
	
	@PostMapping("/validateNum")
	public boolean validateSMS(@RequestParam(name = "tel", required = true) String tel, @RequestParam(name = "authNum", required = true) String authNum) {
		
		tel = tel.replace("-","").trim();
		
		String redisNum = redisService.getValues(tel);
		
		System.out.println("tel : " + tel + ", authnum : " + authNum + ", redisNum : " + redisNum);
		
		if (redisNum.equals(authNum)) {
			
			return true;
			
		} else {
			
			return false;
			
		}
		
	}
}
