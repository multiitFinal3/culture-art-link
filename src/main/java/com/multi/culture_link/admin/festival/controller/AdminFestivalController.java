package com.multi.culture_link.admin.festival.controller;


import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;


@Controller
@RequestMapping("/admin/festival-regulate")
public class AdminFestivalController {
	
	
	@GetMapping
	public String festivalManage() {
		
		
		return "/admin/festival/festivalRegulate";
		
	}
	
	
	
	
	// xml >> json 바로 예시
	public static void main(String[] args) {
		
		
		UriComponents comp = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("apis.data.go.kr")
				.path("B551011/KorService1/areaCode1")
				.queryParam("MobileOS","WIN")
				.queryParam("MobileApp","service")
				.queryParam("serviceKey","chNg8jx96krRfOCTvGcO2PvBKnrCrH0Qm6%2FJmV1TOw%2FYu1T0x3jy0fHM8SOcZFnJIxdc7oqyM03PVmMA9UFOsA%3D%3D")
				.build(true);
		
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getMessageConverters().add(0,new StringHttpMessageConverter(StandardCharsets.UTF_8));
		
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(comp.toUri(), String.class);
		
		
		String xmlResponse = responseEntity.getBody();
		
		// XML을 JSON으로 변환
		JSONObject jsonObject = XML.toJSONObject(xmlResponse.toString());
		
		// JSON 객체 출력
		System.out.println(jsonObject.toString(4)); // 들여쓰기 포함 출력
	}
	
	
	
	// XML >> string >> json 예시
	/*public static void main(String[] args) {
		
		final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"+
				"<Emp id=\"1\"><name>Pankaj</name><age>25</age>\n"+
				"<role>Developer</role><gen>Male</gen></Emp>";
		
		System.out.println(xmlStr);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			System.out.println();
			System.out.println(output.getClass().getName());
			System.out.println("스트링 : " + output);
			
			JSONObject xmlJSONObj = XML.toJSONObject(output);
			String jsonPrettyPrintString = xmlJSONObj.toString(4);
			System.out.println("json : " + jsonPrettyPrintString);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}*/
}
