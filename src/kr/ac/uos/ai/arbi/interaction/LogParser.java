package kr.ac.uos.ai.arbi.interaction;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class LogParser {

	public static JSONObject LogParseFromGL(String content) {
		
		JSONObject jsonObject = new JSONObject();
		try {
			System.out.println("content to parse : " + content);
			GeneralizedList gl = GLFactory.newGLFromGLString(content);
			for(int i=0; i<gl.getExpressionsSize(); i++) {
				GeneralizedList glChild = gl.getExpression(i).asGeneralizedList();
				String childName = toProperCase(glChild.getName());
				jsonObject.put(childName, glChild.getExpression(0));
			}
			jsonObject.put("LogType", gl.getName());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
