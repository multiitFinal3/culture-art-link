package com.multi.culture_link.common.keyword.service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
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
	 *
	 * @param resourceLoader 해당 파일을 찾는 리소스 로더
	 */
	public KeywordExtractService1(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	/**
	 * 코모란으로 스트링에서 해당 키워드를 찾아 반환하며 뽑힌 명사는 나쁘지 않지만 너무 많고 최빈값도 적절하지 않음
	 *
	 * @param all 받는 스트링
	 * @return 키워드 리스트
	 * @throws Exception 예외를 앞단으로 던짐
	 */
	public ArrayList<String> getKeywordByKomoran(String all) throws Exception {
		
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
		
		while ((line = bufferedReader.readLine()) != null) {
			
			if (line.trim().length() > 0) {
				stopWords.add(line.trim());
			}
		}
		
		System.out.println("stopWords : " + stopWords.toString());
		
		ArrayList<String> list2 = new ArrayList<>();
		
		for (String token : list) {
			
			if ((!list2.contains(token)) && (!stopWords.contains(token))) {
				
				list2.add(token);
				
			}
			
		}
		
		System.out.println("Komoran before : " + list);
		System.out.println("Komoran after : " + list2);
		
		
		return list2;
		
		
	}
	
	
	/**
	 * 아파치 루신을 이용한 키워드 분석으로 코모란보다 안좋음
	 *
	 * @param all 분석할 텍스트
	 * @return 키워드 스트링 반환
	 * @throws Exception 컨트롤러로 예외 던짐
	 */
	public ArrayList<String> getKeywordByApacheLucene(String all) throws Exception {
		
		
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		
		ArrayList<String> list = new ArrayList<String>();
		
		try (TokenStream stream = analyzer.tokenStream("s", new StringReader(all))) {
			stream.reset();
			while (stream.incrementToken()) {
				CharTermAttribute termAttr = stream.getAttribute(CharTermAttribute.class);
				OffsetAttribute offAttr = stream.getAttribute(OffsetAttribute.class);
				PositionIncrementAttribute posAttr = stream.getAttribute(PositionIncrementAttribute.class);
				TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);
//				System.out.println("코리안 분석기");
//				System.out.println(new String(termAttr.buffer(), 0, termAttr.length()));
				list.add(new String(termAttr.buffer(), 0, termAttr.length()));
				
				
			}
			stream.end();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("ApacheLucene : " + list);
		return list;
		
		//		Komoran after : [아림, 예술제, 군민, 체육대회, 날, 기념식, 시작, 평생, 학습, 축제, 농산물, 자전거, 향우, 다문화, 가족, 행사, 거리, 퍼레이드, 시골, 밥상, 플라이, 보드, 판타지, 라이트, 쇼, 키즈랜드, 페스티벌, 푸드, 트럭, 야시장, 파쿠, 르, 챔피언십, 눈, 입맛, 마당]
		
		//		ApacheLucene : [아리, 예술, 제, 군민, 체육, 대회, 등, 군민, 날, 기념, 식, 시작, 군민, 체육, 대회, 아리, 예술, 제, 평생, 학습, 축제, 농산, 물, 대, 축제, 자전, 거, tour, 향우, 체육, 대회, 문화, 가족, 축제, 등, 모두, 즐기, 수, 있, 행사, 하, 거리, 퍼레이드, 시골, 밥, 상, 플라이, 보드, 판타지, 라이트, 이, 키즈, 랜드, 페스티벌, 푸드, 트럭, 거창, 야, 시장, 파쿠, 르, 챔피언, 십, 등, 눈, 사로잡, 입, 맛, 돋구, 다양, 길, 거리, 하, 거창, 마당, 대, 축제]
		
		
	}
	
	
}
	

