package com.multi.culture_link.common.keyword.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GPTAssistance {
	
	
	@SystemMessage("핵심 키워드 5개를 뽑아 ,로 이어서 반환")
	String chat(@UserMessage String userMessage);

//	@SystemMessage("문화재 이름이랑 지역 제외한 핵심 키워드 5개를 뽑아 ,로 이어서 반환")
//	String chat2(@UserMessage String userMessage);

	@SystemMessage("핵심 키워드 5개를 문화재 이름이랑 지역 빼고 중복없이 뽑아 ,로 이어서 반환")
	String chat2(@UserMessage String userMessage);



}
