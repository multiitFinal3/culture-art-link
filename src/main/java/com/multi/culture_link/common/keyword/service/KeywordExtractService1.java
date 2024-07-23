package com.multi.culture_link.common.keyword.service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 키워드 추출 서비스 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Service
public class KeywordExtractService1 {
	
	
	private ResourceLoader resourceLoader;
	
	/**
	 * 생성자 주입
	 * @param resourceLoader 해당 파일을 찾는 리소스 로더
	 */
	public KeywordExtractService1(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	/**
	 * 스트링에서 해당 키워드를 찾아 반환
	 * @param all 받는 스트링
	 * @return 키워드 리스트
	 * @throws Exception 예외를 앞단으로 던짐
	 */
	public ArrayList<String> getKeyword(String all) throws Exception{
		
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		
		KomoranResult analyzeResultList = komoran.analyze(all);
		
		System.out.println("analyzeResultList.getPlainText() : " + analyzeResultList.getPlainText());
		
		List<Token> tokenList = analyzeResultList.getTokenList();
		for (Token token : tokenList) {
			System.out.println("token");
			System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
		}
		
		ArrayList<String> list = (ArrayList<String>) analyzeResultList.getNouns();
		/*ArrayList<Token> list = (ArrayList<String>) analyzeResultList.getTokenList();*/
		
		ArrayList<String> stopWords = new ArrayList<String>();
		
		
		
		Resource resource = resourceLoader.getResource("classpath:static/txt/stop.txt");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())));
		
		String line = null;
		
		while ((line = bufferedReader.readLine())!=null){
			
			if (line.trim().length()>0){
				stopWords.add(line.trim());
			}
		}
		
		System.out.println("stopWords : " + stopWords.toString());
		
		ArrayList<String> list2 = new ArrayList<>();
		
		for (String token : list){
			
			if ((!list2.contains(token)) && (!stopWords.contains(token))){
				
				list2.add(token);
				
			}
			
		}
		
		System.out.println("analyzeResultList.getNouns() : " + list);
		System.out.println("final list : " + list2);
		
		
		return list2;
		
		
	}
	
	
}
