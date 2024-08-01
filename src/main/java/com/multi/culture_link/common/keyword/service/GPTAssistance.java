package com.multi.culture_link.common.keyword.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GPTAssistance {
	
	
	@SystemMessage("핵심 키워드 5개를 뽑아 ,로 이어서 반환")
	String chat(@UserMessage String userMessage);
	
	
}
