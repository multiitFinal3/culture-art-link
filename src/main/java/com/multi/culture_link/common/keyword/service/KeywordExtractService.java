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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.DefaultTokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 키워드 추출 서비스 클래스
 *
 * @author 안지연
 * @since 2024-07-23
 */
@Service
public class KeywordExtractService {
	
	
	private ResourceLoader resourceLoader;
	
	@Value("${API-KEY.openAPIKey}")
	private String openAPIKey;
	
	/**
	 * 생성자 주입
	 *
	 * @param resourceLoader 해당 파일을 찾는 리소스 로더
	 */
	public KeywordExtractService(ResourceLoader resourceLoader) {
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
	public HashMap<String, Integer> getKeywordByKomoran(String allContent, String path) throws Exception {
		
		System.out.println("allContent : " + allContent);
		
		if (allContent==null || allContent.equals("")){
			
			return null;
			
		}

		allContent = allContent.trim().replaceAll("\\s+"," ");
		
		allContent = allContent.replaceAll("[^a-zA-Z0-9가-힣\\s]", " ").trim().replaceAll("\\s+"," ");
		
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		
		KomoranResult analyzeResultList = null;
		
		try {
			
			analyzeResultList = komoran.analyze(allContent);
			
		}catch (Exception e){
			System.out.println(e);
			return null;
			
		}
		
		
		System.out.println("analyzeResultList.getPlainText() : " + analyzeResultList.getPlainText());
		
		List<Token> tokenList = analyzeResultList.getTokenList();
		
		try {
			
			for (Token token : tokenList) {
				System.out.println("token");
				System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(), token.getEndIndex(), token.getMorph(), token.getPos());
			}
			
			
		}catch (Exception e){
			System.out.println("여기 : " + e);
			return null;
			
		}
		
		
		
		ArrayList<String> list = (ArrayList<String>) analyzeResultList.getNouns();
		
		ArrayList<String> stopWords = new ArrayList<String>();
		
		
		Resource resource = resourceLoader.getResource(path);
//		"classpath:static/txt/festival/stop.txt"
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())));
		
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null) {
			
			if (line.trim().length() > 0) {
				stopWords.add(line.trim());
			}
		}

//		System.out.println("stopWords : " + stopWords.toString());
		
		ArrayList<String> list2 = new ArrayList<>();
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		
		for (String token : list) {
			
			if ((!list2.contains(token)) && (!stopWords.contains(token))) {
				
				list2.add(token);
				
			}
			
		}
		
		for (String token : list2) {
			
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
		
		System.out.println("Komoran before : " + list2);
		System.out.println("Komoran after : " + map);
		
		return map;
		
		
	}
	
	
//	/**
//	 * 지피티 키워드 5개 뽑기 기능
//	 *
//	 * @param allContent
//	 * @return
//	 * @throws Exception
//	 */
//	public ArrayList<String> getKeywordByGPT(String allContent) throws Exception {
//
//		OpenAiChatModel model = OpenAiChatModel.builder()
//				.apiKey(openAPIKey)
//				.modelName("gpt-3.5-turbo")
//				.temperature(0.1)
//				.build();
//
//		GPTAssistance assistance = AiServices.builder(GPTAssistance.class)
//				.chatLanguageModel(model)
//				.build();
//
//
//		String answer = assistance.chat(allContent);
//		System.out.println("GPT answer : " + answer);
//
//		String[] list = answer.trim().split(",");
//
//		ArrayList<String> list2 = new ArrayList<>();
//
//		for (String s : list) {
//
//			s = s.trim();
//			list2.add(s);
//
//
//		}
//
//
//		return list2;
//	}



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


		String answer = assistance.chat2(allContent);
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
	 *
	 * @param args
	 */
	public static void main(String[] args) {


		OpenAiChatModel model = OpenAiChatModel.builder()
				.apiKey("키")
				.modelName("gpt-3.5-turbo")
				.temperature(0.1)
				.build();

		GPTAssistance assistance = AiServices.builder(GPTAssistance.class)
				.chatLanguageModel(model)
				.build();


		String allContent = "조선시대 한양도성의 정문으로 남쪽에 있다고 해서 남대문이라고도 불렀다. 현재 서울에 남아 있는 목조 건물 중 가장 오래된 것으로 태조 5년(1396)에 짓기 시작하여 태조 7년(1398)에 완성하였다. 이 건물은 세종 30년(1448)에 고쳐 지은 것인데 1961∼1963년 해체·수리 때 성종 10년(1479)에도 큰 공사가 있었다는 사실이 밝혀졌다. 이후, 2008년 2월 10일 숭례문 방화 화재로 누각 2층 지붕이 붕괴되고 1층 지붕도 일부 소실되는 등 큰 피해를 입었으며, 5년 2개월에 걸친 복원공사 끝에 2013년 5월 4일 준공되어 일반에 공개되고 있다. 이 문은 돌을 높이 쌓아 만든 석축 가운데에 무지개 모양의 홍예문을 두고, 그 위에 앞면 5칸·옆면 2칸 크기로 지은 누각형 2층 건물이다. 지붕은 앞면에서 볼 때 사다리꼴 형태를 하고 있는데, 이러한 지붕을 우진각지붕이라 한다. 지붕 처마를 받치기 위해 기둥 위부분에 장식하여 짠 구조가 기둥 위뿐만 아니라 기둥 사이에도 있는 다포 양식으로, 그 형태가 곡이 심하지 않고 짜임도 건실해 조선 전기의 특징을 잘 보여주고 있다. 『지봉유설』의 기록에는 ‘숭례문’이라고 쓴 현판을 양녕대군이 썼다고 한다. 지어진 연대를 정확히 알 수 있는 서울 성곽 중에서 제일 오래된 목조 건축물이다. Ο 숭례문 방화 화재(2008.2.10) \u00AD\u00AD\u00AD2008년 숭례문 방화 사건(崇禮門放火事件)은 2008년 2월 10일 ~ 2월 11일 숭례문 건물이 방화로 타 무너진 사건이다. 화재는 2008년 2월 10일 오후 8시 40분 전후에 발생하여 다음날인 2008년 2월 11일 오전 0시 40분경 숭례문의 누각 2층 지붕이 붕괴하였고 이어 1층에도 불이 붙어 화재 5시간 만인 오전 1시 55분쯤 석축을 제외한 건물이 훼손되었다.\n.";


		String answer = assistance.chat2(allContent);
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



//	/**
//	 * 지피티 예시
//	 *
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//
//		OpenAiChatModel model = OpenAiChatModel.builder()
//				.apiKey("직접 넣기")
//				.modelName("gpt-3.5-turbo")
//				.temperature(0.1)
//				.build();
//
//		GPTAssistance assistance = AiServices.builder(GPTAssistance.class)
//				.chatLanguageModel(model)
//				.build();
//
//
//		String allContent = "(서울=연합뉴스) 곽민서 기자 = 윤석열 대통령은 27일 2024 파리 올림픽에서 활약하는 국가대표 선수들에게 축하를 보냈다.\n" +
//				"\n" +
//				"윤 대통령은 이날 페이스북 축전을 통해 \"한계를 뛰어넘는 국가대표 여러분의 도전은 계속될 것\"이라며 \"끝까지 국민과 함께 한마음으로 응원하겠다\"고 말했다.\n" +
//				"\n" +
//				"윤 대통령은 강호 독일을 상대로 승리한 여자 핸드볼 국가대표에 \"4골 차를 뒤집고 종료 22초 전 역전에 성공한 투지는 2004년 '우생순(우리 생애 최고의 순간)' 그 자체였다\"며 \"승리 후 모든 선수가 모여 보여준 강강술래 세리머니는 저와 대한민국 국민 모두에게 큰 감동을 줬다\"고 말했다.";
//
//
//		String answer = assistance.chat(allContent);
//		System.out.println("GPT answer : " + answer);
//
//		String[] list = answer.trim().split(",");
//
//		ArrayList<String> list2 = new ArrayList<>();
//
//		for (String s : list) {
//
//			s = s.trim();
//			list2.add(s);
//
//		}
//
//		System.out.println(list2);
//
//		return;
//
//	}


	
	/**
	 * 키워드를 뽑으려하는 전체 작품들의 스트링들을 각각 리스트에 담아 넣으면
	 * 각각의 키워드가 word2vec(유사도, 문맥 추출)과 tf-idf(통계적 추출)를 모두 고려해 곱한 결합 벡터로 추출함
	 *
	 * @param allContentList
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, ArrayList<String>> getKeywordByCombineVector(ArrayList<String> allContentList) throws Exception {
		
		long beforeTime = System.currentTimeMillis();
		
		// 해당 카테고리(예시 : 축제)의 분류(예시 : 리뷰)에서 키워드를 뽑을 스트링을 모든 작품에 대해 순서대로 리스트에 담아 파라미터로 전달
		
		// 해당 작품의 인덱스, 키워드 리스트를 맵에 담아 반환할 예정
		Map<Integer, ArrayList<String>> returnMap = new HashMap<Integer, ArrayList<String>>();
		
		// 각각 개별의 컨텐트 별로 이터레이터로 돌릴 예정
		CollectionSentenceIterator collectionSentenceIterator = new CollectionSentenceIterator(allContentList);
		
		// 코모란으로 명사만 추출해 키워드 중요도를 계산할 예정
		TokenizerFactory komoranFactory = new TokenizerFactory() {
			@Override
			public Tokenizer create(String toTokenize) {
				
				Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
				
				// 명사만 추출
				ArrayList<String> list = (ArrayList<String>) komoran.analyze(toTokenize).getNouns();
				
				DefaultTokenizer defaultTokenizer = new DefaultTokenizer(String.join(" ", list));
				
				return defaultTokenizer;
				
			}
			
			@Override
			public Tokenizer create(InputStream toTokenize) {
				return null;
			}
			
			@Override
			public void setTokenPreProcessor(TokenPreProcess preProcessor) {
			
			}
			
			@Override
			public TokenPreProcess getTokenPreProcessor() {
				return null;
			}
		};
		
		
		// 특정 단어의 유사도 벡터를 만드는 Word2Vec 설정.
		Word2Vec word2Vec = new Word2Vec.Builder()
				.minWordFrequency(1) // 최소 한번이라도 등장한 단어는 모두 학습
				.layerSize(5) // 각 단어는 100차원의 벡터로 표현 [0,12, 0.1.....]
				.seed(42) // 랜덤 시드 결정으로 랜덤하지 않고 결과가 재현됨을 의미
				.windowSize(5) // 컨텍스트 윈도우 크기로 좌우 5개의 단어를 고려해 관계를 학습함
				.iterate(collectionSentenceIterator) // 하나의 작품당 이터레이트하며 학습
				.tokenizerFactory(komoranFactory) // 코모란으로 명사만 분석
				.build(); // 빌드
		
		word2Vec.fit();// 학습
		
		
		// TfidfVectorizer
		
		// 아파치루신의 그냥 분석기는 한글을 인식 못해 아파치 루신의 한국어 분석기 이용
		KoreanAnalyzer koreanAnalyzer = new KoreanAnalyzer();
		
		// 각각 스트링들을 램(휘발성 저장공간)에 저장해 빠르게 인덱스로 찾을 수 있게 함
		Directory index = new ByteBuffersDirectory();
		
		IndexWriterConfig config = new IndexWriterConfig(koreanAnalyzer);
		IndexWriter writer = new IndexWriter(index, config);
		
		for (String content : allContentList) {
			
			Document document = new Document();
			// 토큰화된 내용 뿐만 아니라 원본 내용도 저장됨
			document.add(new TextField("content", content, Field.Store.YES));
			writer.addDocument(document);
			
		}
		
		writer.close();
		
		// 주석처리해도 똑같은 결과가 잘 나오는데 주석처리 하면 조금 더 느린 것 같긴 하다
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
		// 각각 작품의 순서대로 tf-id, word2vec, combined vec 각각 추출
		for (int i = 0; i < allContentList.size(); i++) {
			
			System.out.println(i + "번째 컨텐트");
			// 해당 스트링을 추출
			String content = allContentList.get(i);
			
			// 특수문자 제거 : ^ 반대의 경우, \\s : 공백 스페이스 탭 줄바꿈 등
			content = content.replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
			
			// 두 벡터를 합한 결과인 결합 벡터 맵을 선언
			Map<String, INDArray> combinedVectors = new HashMap<>();
			
			
			TokenizerFactory tf = komoranFactory;
			ArrayList<String> list = (ArrayList<String>) tf.create(allContentList.get(i)).getTokens();
			System.out.println(i + "번째 토큰 리스트 : " + list);
			
			
			// tf-id 구하기
			
			int allWordsCount = list.size();
			int allDocuCount = allContentList.size();
			
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			// 각각 단어에 대한 값을 구함
			for (String term : list) {
				
				map.put(term, map.getOrDefault(term, 0) + 1);
				
			}
			
			
			// 각각 단어에 대한 값을 구함
			for (String term : map.keySet()) {
				
				System.out.println("term : " + term);
				
				// word2vector 구하기
				INDArray wordVector = word2Vec.getWordVectorMatrix(term);
				System.out.println("wordVector : " + wordVector);
				
				// word2vector의 차원에 맞게 tfidf의 벡터의 차원을 맞춤(tf-idf는 각 단어당 하나의 숫자값임). ex) [0.12, 0.12...]
				int freq = 0;
				freq = map.get(term);
				
				int includeWordDocuCount = 0;
				
				for (String content2 : allContentList) {
					
					if (content2.contains(term)) {
						includeWordDocuCount++;
					}
				}
				
				System.out.println("term : " + term + " freq : " + freq + " allWordsCount : " + allWordsCount + " allDocuCount : " + allDocuCount + " includeWordDocuCount : " + includeWordDocuCount);
				
				double tfIdf = ((double) freq / allWordsCount) * Math.log10((double) (allDocuCount / (includeWordDocuCount)));
				System.out.println("tfIdf는 : " + tfIdf);
				
				INDArray tfidVector = Nd4j.valueArrayOf(wordVector.shape(), tfIdf);
				System.out.println("tfidVector : " + tfidVector);
				
				// 두 벡터를 합함(TF-IDF 기반 키워드 추출에서의 의미적 요소 반영을 위한 결합벡터 제안 논문 참고함)
				
				INDArray combinedVector = null;
				
				if (tfIdf < 0) {
					
					combinedVector = wordVector;
					
				} else {
					
					combinedVector = wordVector.mul(tfidVector);
				}
				
				
				// 맵에 넣음
				combinedVectors.put((String) term, combinedVector);
				
				System.out.println();
				
			}
			
			
			// 키워드와 점수 맵을 반환 : 각 벡터의 원소를 제곱해서 더하고 루트를 한 값을 반환
			Map<String, Double> normScore = new HashMap<>();
			
			for (Map.Entry<String, INDArray> entry : combinedVectors.entrySet()) {
				
				double norm = entry.getValue().norm2Number().doubleValue();
				
				normScore.put(entry.getKey(), norm);
				
				
			}
			
			// 값 기준으로 탑 10만 반환
			List<Map.Entry<String, Double>> sortedList = normScore.entrySet().stream()
					.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
					.limit(10)
					.collect(Collectors.toList());
			
			ArrayList<String> list2 = new ArrayList<>();
			
			// 각각 순서별 키워드의 값과 점수를 콘솔에 표시
			System.out.println("[content index " + i + "th keyword : combined value]");
			for (Map.Entry<String, Double> entry : sortedList) {
				
				System.out.println(entry.getKey() + " : " + entry.getValue());
				list2.add(entry.getKey().replace(".", "").trim());
				
				
			}
			
			System.out.println();
			
			returnMap.put(i, list2);
			
		}
		
		// 리턴맵(<인덱스, 키워드 리스트>)을 반환함
		System.out.println("returnMap : " + returnMap);
		
		// 인덱스 리더 닫기
		reader.close();
		
		long afterTime = System.currentTimeMillis();
		
		long diffTime = (afterTime - beforeTime) / 1000;
		System.out.println("인덱스 사용 시 시간 차이 : " + diffTime + "s");

		
		return returnMap;
		
	}
	
	
	/**
	 * word2vec > tf-idf 결합 벡터 예시
	 *
	 * @param args
	 */
//	public static void main(String[] args) throws Exception {
//
//		long beforeTime = System.currentTimeMillis();
//
//		ArrayList<String> allContentList = new ArrayList<>();
//		allContentList.add("박주언 문화복지위원장 “문화예술이 경쟁력”\n" +
//				"제12대 후반기 원구성 이후 첫 현지의정활동에 나선 경남도의회 문화복지위원회가 비회기 중에도 활발한 의정활동을 하고 있다.\n" +
//				"지난 26일 산청 성심원과 서울우유 거창공장을 방문한데 이어 제34회 거창국제연극제 개막식에 참석해 개막작 ‘우먼후드:메디아에 대한 오해’를 관람하고 지역자원을 연계한 공연예술제의 발전 방향을 모색했다고 29일 밝혔다.\n" +
//				"개막식에 참석한 박주언 위원장은 “아름다운 자연 속에서 열리는 거창국제연극제는 자연과 문화의 조화로운 만남을 선사하는 독특한 매력을 지닌 축제” 라면서 “문화예술이 곧 지역의 경쟁력이 된 현 시점에서 지역의 특색을 반영한 문화·예술공연을 발전시켜 지역의 고유한 브랜드 창출은 물론 지역이미지 향상과 경제 활성화에 이바지해야한다”고 강조했다.\n" +
//				"\n");
//		allContentList.add("거창=뉴시스] 서희원 기자 = 경남 거창군은 결혼·출산·육아에 대한 관심을 유도하고 가족 친화적인 사회 분위기를 조성하는 등 저출산 극복에 대한 사회적 공감대를 형성하기 위해 ‘2024년 제4회 거창군 가족사진 공모전’을 개최한다고 23일 밝혔다.\n" +
//				"\n" +
//				"이번 공모전의 주제는 ‘가족과 함께하는 모든 날, 모든 순간을 담아요’로, 공모대상은 ▲사랑스러운 아이의 출생, 성장 모습을 담은 사진 ▲다양한 형태의 행복한 가족의 사진 ▲다자녀, 다세대가 함께하는 행복한 가족사진 ▲거창의 명승지를 배경으로 찍은 가족사진 등이다.\n");
//
//		allContentList.add("<앵커>\n" +
//				"한여름, 시원한 물놀이와 연극을 동시에 즐길 수 있었던 거창국제연극제가 내일(9) 막을 내립니다.\n" +
//				"\n" +
//				"무더위를 날리고 국내외 다양한 연극을 즐길 수 있는 올해 연극제에는 2만 여명이 다녀갈 정도로 성황을 이뤘습니다.\n" +
//				"\n" +
//				"이태훈 기자가 다녀왔습니다.\n" +
//				"\n" +
//				"<기자>\n" +
//				"서원과 배롱나무를 배경으로, 묘기에 가까운 줄타기 공연이 이어집니다.\n" +
//				"\n" +
//				"줄을 타는 사람은 영화 왕의 남자에서 광대 대역으로 출연했습니다.\n" +
//				"\n" +
//				"남사당놀이 줄타기는 거창국제연극제의 공식 참가 공연으로, 유네스코 인류무형문화유산이기도 합니다.\n" +
//				"\n" +
//				"재미는 물론이고, 관객과 대화를 주고 받는 소통 예술 공연입니다.\n" +
//				"\n" +
//				"{이라온 정용상/거창읍/\"처음보는 거라서 아저씨가 좀 넘어질까봐 걱정되기도 했지만 재밌었어요.\"}\n" +
//				"\n" +
//				"밤이 되자, 인극 무대에서는 연극 공연이 이어집니다.\n" +
//				"\n" +
//				"1925년, 폴란드 초등학교를 배경으로 아이들이 전쟁과 이념의 갈등을 겪게되는 이야기입니다.\n" +
//				"\n" +
//				"{김업순 황성문 박미진/거창읍/\"연기하시는 분의 생동감 넘치는 연기를 직접 볼 수 있어서 너무 좋았습니다.\"}\n" +
//				"\n" +
//				"올해로 34회째를 맞은 거창국제연극제에서는 7개 나라, 51개 작품이 공연됐습니다.\n" +
//				"\n" +
//				"거창국제연극제는 낮에는 수승대 계곡에서 물놀이를 즐기고 밤에는 야외에서 연극을 즐길 수 있는게 특징입니다.\n" +
//				"\n" +
//				"올해 연극제 기간에는 무려 2만 여명이 다녀갔습니다.\n" +
//				"\n" +
//				"거창국제연극제는 프랑스 아비뇽페스티벌과 영국 에딘버러페스티벌과 함께 세계 3대 야외공연축제로 거듭나는게 목표입니다.\n" +
//				"\n" +
//				"{황국재/거창국제연극제 예술감독/\"야외공연 연극제라고 보시면 되겠습니다. 지금 인터뷰하는 공간도 저희들이 무대 세트를 따로 만든게 아니라 자연스럽게 고가를 배경으로 하는 이런 공연들을 하고 있습니다.\"}\n" +
//				"\n" +
//				"2주간의 일정을 뒤로 한 거창국제연극제는 내년 새로운 작품들과 함께 다시 찾아올 예정입니다.\n" +
//				"\n" +
//				"KNN 이태훈입니다.");
//
//		// 해당 카테고리(예시 : 축제)의 분류(예시 : 리뷰)에서 키워드를 뽑을 스트링을 모든 작품에 대해 순서대로 리스트에 담아 파라미터로 전달
//
//		// 해당 작품의 인덱스, 키워드 리스트를 맵에 담아 반환할 예정
//		Map<Integer, ArrayList<String>> returnMap = new HashMap<Integer, ArrayList<String>>();
//
//		// 각각 개별의 컨텐트 별로 이터레이터로 돌릴 예정
//		CollectionSentenceIterator collectionSentenceIterator = new CollectionSentenceIterator(allContentList);
//
//		// 코모란으로 명사만 추출해 키워드 중요도를 계산할 예정
//		TokenizerFactory komoranFactory = new TokenizerFactory() {
//			@Override
//			public Tokenizer create(String toTokenize) {
//
//				Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
//
//				// 명사만 추출
//				ArrayList<String> list = (ArrayList<String>) komoran.analyze(toTokenize).getNouns();
//
//				DefaultTokenizer defaultTokenizer = new DefaultTokenizer(String.join(" ", list));
//
//				return defaultTokenizer;
//
//			}
//
//			@Override
//			public Tokenizer create(InputStream toTokenize) {
//				return null;
//			}
//
//			@Override
//			public void setTokenPreProcessor(TokenPreProcess preProcessor) {
//
//			}
//
//			@Override
//			public TokenPreProcess getTokenPreProcessor() {
//				return null;
//			}
//		};
//
//
//		// 특정 단어의 유사도 벡터를 만드는 Word2Vec 설정.
//		Word2Vec word2Vec = new Word2Vec.Builder()
//				.minWordFrequency(1) // 최소 한번이라도 등장한 단어는 모두 학습
//				.layerSize(5) // 각 단어는 100차원의 벡터로 표현 [0,12, 0.1.....]
//				.seed(42) // 랜덤 시드 결정으로 랜덤하지 않고 결과가 재현됨을 의미
//				.windowSize(5) // 컨텍스트 윈도우 크기로 좌우 5개의 단어를 고려해 관계를 학습함
//				.iterate(collectionSentenceIterator) // 하나의 작품당 이터레이트하며 학습
//				.tokenizerFactory(komoranFactory) // 코모란으로 명사만 분석
//				.build(); // 빌드
//
//		word2Vec.fit();// 학습
//
//
//		// TfidfVectorizer
//
//		// 아파치루신의 그냥 분석기는 한글을 인식 못해 아파치 루신의 한국어 분석기 이용
//		KoreanAnalyzer koreanAnalyzer = new KoreanAnalyzer();
//
//		// 각각 스트링들을 램(휘발성 저장공간)에 저장해 빠르게 인덱스로 찾을 수 있게 함
//		Directory index = new ByteBuffersDirectory();
//
//		IndexWriterConfig config = new IndexWriterConfig(koreanAnalyzer);
//		IndexWriter writer = new IndexWriter(index, config);
//
//		for (String content : allContentList) {
//
//			Document document = new Document();
//			// 토큰화된 내용 뿐만 아니라 원본 내용도 저장됨
//			document.add(new TextField("content", content, Field.Store.YES));
//			writer.addDocument(document);
//
//		}
//
//		writer.close();
//
//		// 주석처리해도 똑같은 결과가 잘 나오는데 주석처리 하면 조금 더 느린 것 같긴 하다
//		IndexReader reader = DirectoryReader.open(index);
//		IndexSearcher searcher = new IndexSearcher(reader);
//
//
//		// 각각 작품의 순서대로 tf-id, word2vec, combined vec 각각 추출
//		for (int i = 0; i < allContentList.size(); i++) {
//
//			System.out.println(i + "번째 컨텐트");
//			// 해당 스트링을 추출
//			String content = allContentList.get(i);
//
//			// 특수문자 제거 : ^ 반대의 경우, \\s : 공백 스페이스 탭 줄바꿈 등
//			content = content.replaceAll("[^a-zA-Z0-9가-힣\\s]", " ");
//
//			// 두 벡터를 합한 결과인 결합 벡터 맵을 선언
//			Map<String, INDArray> combinedVectors = new HashMap<>();
//
//
//			TokenizerFactory tf = komoranFactory;
//			ArrayList<String> list = (ArrayList<String>) tf.create(allContentList.get(i)).getTokens();
//			System.out.println(i + "번째 토큰 리스트 : " + list);
//
//
//			// tf-id 구하기
//
//			int allWordsCount = list.size();
//			int allDocuCount = allContentList.size();
//
//
//			Map<String, Integer> map = new HashMap<String, Integer>();
//
//			// 각각 단어에 대한 값을 구함
//			for (String term : list) {
//
//				map.put(term, map.getOrDefault(term, 0) + 1);
//
//			}
//
//
//			// 각각 단어에 대한 값을 구함
//			for (String term : map.keySet()) {
//
//				System.out.println("term : " + term);
//
//				// word2vector 구하기
//				INDArray wordVector = word2Vec.getWordVectorMatrix(term);
//				System.out.println("wordVector : " + wordVector);
//
//				// word2vector의 차원에 맞게 tfidf의 벡터의 차원을 맞춤(tf-idf는 각 단어당 하나의 숫자값임). ex) [0.12, 0.12...]
//				int freq = 0;
//				freq = map.get(term);
//
//				int includeWordDocuCount = 0;
//
//				for (String content2 : allContentList) {
//
//					if (content2.contains(term)) {
//						includeWordDocuCount++;
//					}
//				}
//
//				System.out.println("term : " + term + " freq : " + freq + " allWordsCount : " + allWordsCount + " allDocuCount : " + allDocuCount + " includeWordDocuCount : " + includeWordDocuCount);
//
//				double tfIdf = ((double) freq / allWordsCount) * Math.log10((double) (allDocuCount / (includeWordDocuCount)));
//				System.out.println("tfIdf는 : " + tfIdf);
//
//				INDArray tfidVector = Nd4j.valueArrayOf(wordVector.shape(), tfIdf);
//				System.out.println("tfidVector : " + tfidVector);
//
//				// 두 벡터를 합함(TF-IDF 기반 키워드 추출에서의 의미적 요소 반영을 위한 결합벡터 제안 논문 참고함)
//
//				INDArray combinedVector = null;
//
//				if (tfIdf < 0) {
//
//					combinedVector = wordVector;
//
//				} else {
//
//					combinedVector = wordVector.mul(tfidVector);
//				}
//
//
//				// 맵에 넣음
//				combinedVectors.put((String) term, combinedVector);
//
//				System.out.println();
//
//			}
//
//
//			// 키워드와 점수 맵을 반환 : 각 벡터의 원소를 제곱해서 더하고 루트를 한 값을 반환
//			Map<String, Double> normScore = new HashMap<>();
//
//			for (Map.Entry<String, INDArray> entry : combinedVectors.entrySet()) {
//
//				double norm = entry.getValue().norm2Number().doubleValue();
//
//				normScore.put(entry.getKey(), norm);
//
//
//			}
//
//			// 값 기준으로 탑 10만 반환
//			List<Map.Entry<String, Double>> sortedList = normScore.entrySet().stream()
//					.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//					.limit(10)
//					.collect(Collectors.toList());
//
//			ArrayList<String> list2 = new ArrayList<>();
//
//			// 각각 순서별 키워드의 값과 점수를 콘솔에 표시
//			System.out.println("[content index " + i + "th keyword : combined value]");
//			for (Map.Entry<String, Double> entry : sortedList) {
//
//				System.out.println(entry.getKey() + " : " + entry.getValue());
//				list2.add(entry.getKey().replace(".", "").trim());
//
//
//			}
//
//			System.out.println();
//
//			returnMap.put(i, list2);
//
//		}
//
//		// 리턴맵(<인덱스, 키워드 리스트>)을 반환함
//		System.out.println("returnMap : " + returnMap);
//
//		// 인덱스 리더 닫기
//		reader.close();
//
//		long afterTime = System.currentTimeMillis();
//
//		long diffTime = (afterTime - beforeTime) / 1000;
//		System.out.println("인덱스 사용 시 시간 차이 : " + diffTime + "s");
//
//		return;
//	}

	
}
	

