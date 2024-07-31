package com.multi.culture_link.common.keyword.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.ko.POS;
import org.apache.lucene.analysis.ko.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	@Value("${API-KEY.openAPIKey}")
	private String openAPIKey;
	
	/**
	 * 생성자 주입
	 *
	 * @param resourceLoader 해당 파일을 찾는 리소스 로더
	 */
	public KeywordExtractService1(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	
	/**
	 * 아파치 루신을 이용한 키워드 분석으로 코모란보다 안좋음
	 *
	 * @param all 분석할 텍스트
	 * @return 키워드 스트링 반환
	 * @throws Exception 컨트롤러로 예외 던짐
	 */
	public ArrayList<String> getKeywordByApacheLucene(String all) throws Exception {
		
		ArrayList<String> stopWords = new ArrayList<String>();
		
		Resource resource = resourceLoader.getResource("classpath:static/txt/festival/stop.txt");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())));
		
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null) {
			
			if (line.trim().length() > 0) {
				stopWords.add(line.trim());
			}
		}
		
		KoreanAnalyzer analyzer = new KoreanAnalyzer();
		
		ArrayList<String> list = new ArrayList<String>();
		
		try (TokenStream stream = analyzer.tokenStream("s", new StringReader(all))) {
			stream.reset();
			while (stream.incrementToken()) {
				CharTermAttribute termAttr = stream.getAttribute(CharTermAttribute.class);
				OffsetAttribute offAttr = stream.getAttribute(OffsetAttribute.class);
				PositionIncrementAttribute posAttr = stream.getAttribute(PositionIncrementAttribute.class);
				TypeAttribute typeAttr = stream.getAttribute(TypeAttribute.class);
				PartOfSpeechAttribute partOfSpeechAttribute = stream.getAttribute(PartOfSpeechAttribute.class);


//				System.out.println("코리안 분석기");
//				System.out.println(new String(termAttr.buffer(), 0, termAttr.length()));
//
//				System.out.println("type: " + typeAttr.type());
//				type: word라서 소용 없음
//				if (typeAttr.type().equals("NNP") || typeAttr.type().equals("NNG")){
//
//					list.add(new String(termAttr.buffer(), 0, termAttr.length()));
//				}

//				System.out.println(partOfSpeechAttribute.getPOSType());
//				System.out.println(partOfSpeechAttribute.getLeftPOS());
//				System.out.println(partOfSpeechAttribute.getRightPOS());
				
				if ((partOfSpeechAttribute.getPOSType() == POS.Type.MORPHEME) && ((partOfSpeechAttribute.getLeftPOS() == POS.Tag.NNG) || (partOfSpeechAttribute.getLeftPOS() == POS.Tag.NNP)) && (!stopWords.contains(termAttr.toString()))) {
					
					list.add(termAttr.toString());
					
				}
				
				
			}
			stream.end();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		ArrayList<String> list2 = new ArrayList<>();
		
		for (String s : list) {
			
			if (!list2.contains(s)) {
				list2.add(s);
			}
			
		}
		
		System.out.println("ApacheLucene : " + list2);
		
		return list2;

//		Komoran after : [춘천, 예술, 문화, 축제, 한여름, 밤, 여름밤, 아리아]
//		ApacheLucene : [춘천, 예술, 문화, 축제, 여름, 밤, 아리아]
//
//		Komoran after : [폐막식, 단편, 영화, 시상, 상영, 관객, 대화, 부대, 행사, 춘천, 영화제]
//		ApacheLucene : [개, 폐막, 식, 단편, 영화, 시상, 상영, 관객, 대화, 부대, 행사, 춘천, 제]
//
//		Komoran after : [국내외, 연극, 공연, 거창국제연극제]
//		ApacheLucene : [국내, 외, 연극, 공연, 거창, 국제, 연극제]
//
//		Komoran after : [아림, 예술제, 군민, 체육대회, 날, 기념식, 시작, 평생, 학습, 축제, 농산물, 자전거, 향우, 다문화, 가족, 행사, 거리, 퍼레이드, 시골, 밥상, 플라이, 보드, 판타지, 라이트, 쇼, 키즈랜드, 페스티벌, 푸드, 트럭, 야시장, 파쿠, 르, 챔피언십, 눈, 입맛, 마당]
//		ApacheLucene : [예술, 제, 군민, 체육, 대회, 날, 기념, 식, 시작, 평생, 학습, 축제, 농산, 물, 대, 자전, 거, 향우, 문화, 가족, 모두, 행사, 거리, 퍼레이드, 시골, 밥, 상, 플라이, 보드, 판타지, 라이트, 키즈, 랜드, 페스티벌, 푸드, 트럭, 야, 시장, 파쿠, 르, 챔피언, 눈, 입, 맛, 길, 마당]
//
//		Komoran after : [산수유, 꽃, 프로그램, 새봄, 정취, 대한민국, 대표, 봄꽃, 축제, 개화, 봄, 소식, 꽃말, 영원불변, 사랑, 주제, 체험, 음악회, 개최, 구례, 산수유꽃축제]
//		ApacheLucene : [산, 수유, 꽃, 프로그램, 봄, 정취, 대한, 민국, 대표, 축제, 개화, 소식, 말, 영원, 불변, 사랑, 주제, 체험, 음악, 회, 개최, 구례]
	
	}
	
	
	/**
	 * 코모란으로 전체 명사 뽑기
	 *
	 * @param allContent
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Integer> getKeywordByKomoran(String allContent) throws Exception {
		
		
		System.out.println("allContent : " + allContent);
		
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		
		KomoranResult analyzeResultList = komoran.analyze(allContent);
		
		System.out.println("analyzeResultList.getPlainText() : " + analyzeResultList.getPlainText());
		
		List<Token> tokenList = analyzeResultList.getTokenList();
		for (Token token : tokenList) {
			System.out.println("token");
			System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
		}
		
		ArrayList<String> list = (ArrayList<String>) analyzeResultList.getNouns();
		
		ArrayList<String> stopWords = new ArrayList<String>();
		
		
		Resource resource = resourceLoader.getResource("classpath:static/txt/festival/stop.txt");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())));
		
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null) {
			
			if (line.trim().length() > 0) {
				stopWords.add(line.trim());
			}
		}
		
		System.out.println("stopWords : " + stopWords.toString());
		
		ArrayList<String> list2 = new ArrayList<>();
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		
		for (String token : list) {
			
			if ((!list2.contains(token)) && (!stopWords.contains(token))) {
				
				list2.add(token);
				
			}
			
		}
		
		for (String token : list) {
			
			boolean isStop = false;
			
			for (String stop : stopWords) {
				
				
				if ((stopWords.contains(token)) || (stop.contains(token))) {
					System.out.println(token + " : " + stop);
					isStop = true;
					break;
					
				}
				
			}
			
			if (!isStop) {
				
				if (map.keySet().contains(token)) {
					
					int val = map.get(token);
					map.put(token, val + 1);
					
				} else {
					
					map.put(token, 1);
					
				}
				
			}
			
		}
		
		System.out.println("Komoran before : " + list);
		System.out.println("Komoran after : " + map);
		
		return map;
		
		
	}
	
	
	/**
	 * 지피티 키워드 5개 뽑기 기능
	 *
	 * @param allContent
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getKeywordByGPT(String allContent) throws Exception {
		
		OpenAiChatModel model = OpenAiChatModel.builder()
				.apiKey(openAPIKey)
				.modelName("gpt-3.5-turbo")
				.temperature(0.1)
				.build();
		
		GPTAssistance assistance = AiServices.builder(GPTAssistance.class)
				.chatLanguageModel(model)
				.build();
		
		
		String answer = assistance.chat(allContent);
		System.out.println("GPT answer : " + answer);
		
		String[] list = answer.trim().split(",");
		
		ArrayList<String> list2 = new ArrayList<>();
		
		for (String s : list) {
			
			s = s.trim();
			list2.add(s);
			
			
		}
		
		
		return list2;
	}
	
	
	/**
	 * 지피티 예시
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		OpenAiChatModel model = OpenAiChatModel.builder()
				.apiKey("직접 넣을 것")
				.modelName("gpt-3.5-turbo")
				.temperature(0.1)
				.build();
		
		GPTAssistance assistance = AiServices.builder(GPTAssistance.class)
				.chatLanguageModel(model)
				.build();
		
		
		String allContent = "(서울=연합뉴스) 곽민서 기자 = 윤석열 대통령은 27일 2024 파리 올림픽에서 활약하는 국가대표 선수들에게 축하를 보냈다.\n" +
				"\n" +
				"윤 대통령은 이날 페이스북 축전을 통해 \"한계를 뛰어넘는 국가대표 여러분의 도전은 계속될 것\"이라며 \"끝까지 국민과 함께 한마음으로 응원하겠다\"고 말했다.\n" +
				"\n" +
				"윤 대통령은 강호 독일을 상대로 승리한 여자 핸드볼 국가대표에 \"4골 차를 뒤집고 종료 22초 전 역전에 성공한 투지는 2004년 '우생순(우리 생애 최고의 순간)' 그 자체였다\"며 \"승리 후 모든 선수가 모여 보여준 강강술래 세리머니는 저와 대한민국 국민 모두에게 큰 감동을 줬다\"고 말했다.";
		
		
		String answer = assistance.chat(allContent);
		System.out.println("GPT answer : " + answer);
		
		String[] list = answer.trim().split(",");
		
		ArrayList<String> list2 = new ArrayList<>();
		
		for (String s : list) {
			
			s = s.trim();
			list2.add(s);
			
			
		}
		
		System.out.println(list2);
		
		return;
		
	}
	
}
	

