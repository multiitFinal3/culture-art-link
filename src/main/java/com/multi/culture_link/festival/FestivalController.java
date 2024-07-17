package com.multi.culture_link.festival;


import com.multi.culture_link.users.model.dto.VWUserRoleDTO;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

@Controller
@RequestMapping("/festival")
public class FestivalController {


	@GetMapping("/festival-list")
	public String festivalListPage(@AuthenticationPrincipal VWUserRoleDTO user, Model model){
		
		System.out.println("@AuthenticationPrincipal : " + user.toString());
		model.addAttribute("user", user.getUser());
		
		return "/festival/festivalList";

	}
	
	
	// XML >> string >> json 예시
	public static void main(String[] args) {
		
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
		
		
	}

}
