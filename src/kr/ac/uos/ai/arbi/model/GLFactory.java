package kr.ac.uos.ai.arbi.model;

import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.ac.uos.ai.arbi.model.functions.FunctionFactory;
import kr.ac.uos.ai.arbi.model.parser.GLParser;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class GLFactory {
	private static final Expression UNDEFINED_EXPRESSION = new UndefinedExpression();
	private static final Value UNDEFINED_VALUE = new UndefinedValue();

	private GLFactory() {
		//
	}

	public static Expression getUndefinedExpression() {
		return UNDEFINED_EXPRESSION;
	}

	public static Value getUndefinedValue() {
		return UNDEFINED_VALUE;
	}

	public static GeneralizedList newGL(String name, ExpressionList expressionList) {
		return newGL(name, expressionList.toArray());
	}

	public static GeneralizedList newGL(String name, Expression... expressions) {
		return new GeneralizedListImpl(name, expressions);
	}

	private static final Expression[] NULL_EXPRESSION = new Expression[0];

	public static GeneralizedList newGL(String name) {
		return new GeneralizedListImpl(name, NULL_EXPRESSION);
	}

	public static GeneralizedList newGLFromGLString(String glString) throws ParseException {
		GLParser parser = new GLParser();
		return parser.parseGL(glString);
	}

	public static GeneralizedList newGLFromGLString(InputSource source) throws ParseException, IOException {
		BufferedReader reader = new BufferedReader(source.getCharacterStream());
		StringBuilder glString = new StringBuilder();
		String s = null;
		while ((s = reader.readLine()) != null) {
			glString.append(s);
		}
		return newGLFromGLString(glString.toString());
	}

	public static JSONObject newJSONObjectFromGLString(String glString) {
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("GeneralizedList", newJSONObjectFromGL(GLFactory.newGLFromGLString(glString)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	public static JSONObject newJSONObjectFromGL(GeneralizedList gl) {

		JSONObject glObject = new JSONObject();

		String glName = gl.getName();
		int expressionSize = gl.getExpressionsSize();

		glObject.put("PredicateName", glName);
		glObject.put("ExPressionSize", String.valueOf(expressionSize));

		JSONArray expressionList = new JSONArray();
		for (int i = 0; i < expressionSize; i++) {
			JSONObject expression = newJSONFromExpression(gl.getExpression(i));
			expressionList.add(i, expression);
		}

		glObject.put("ExpressionList", expressionList);

		return glObject;
	}

	private static JSONObject newJSONObjectFromFunction(Function function) {

		JSONObject jsonObject = new JSONObject();
		JSONObject glObject = new JSONObject();

		String glName = function.getName();
		int expressionSize = function.getExpressionsSize();

		glObject.put("FunctionName", glName);
		glObject.put("ExPressionSize", String.valueOf(expressionSize));

		JSONArray expressionList = new JSONArray();
		for (int i = 0; i < expressionSize; i++) {
			JSONObject expression = newJSONFromExpression(function.getExpression(i));
			expressionList.add(i, expression);
		}

		glObject.put("ExpressionList", expressionList);
		jsonObject.put("Function", glObject);

		return jsonObject;
	}

	public static JSONObject newJSONFromExpression(Expression expression) {

		JSONObject expressionObject = new JSONObject();

		if (expression.isGeneralizedList()) {
			expressionObject.put("Type", "GeneralizedList");
			expressionObject.put("GeneralizedList", newJSONObjectFromGL(expression.asGeneralizedList()));
		} else if (expression.isValue()) {
			expressionObject.put("Type", "Value");
			expressionObject.put("ValueType", expression.asValue().getType().toString());
			expressionObject.put("Value", expression.toString());
		} else if (expression.isVariable()) {
			expressionObject.put("Type", "Variable");
			expressionObject.put("VariableName", expression.asVariable().getName());
		} else if (expression.isFunction()) {
			expressionObject.put("Type", "Function");
			expressionObject.put("GeneralizedList", newJSONObjectFromFunction(expression.asFunction()));
		}
		else
			return null;

		return expressionObject;
	}

	public static GeneralizedList newGLFromXML(InputSource source) {
		GeneralizedList generalizedList = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			Element glElement = (Element) document.getElementsByTagName("generalized-list").item(0);
			generalizedList = newGLFromXML(glElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generalizedList;
	}

	public static GeneralizedList newGLFromXML(Element glElement) {
		String encoding = glElement.getAttribute("encoding");
		if (encoding.equals("xml")) {

			Element nameElement = (Element) glElement.getElementsByTagName("name").item(0);
			String glName = nameElement.getTextContent();

			Element expressionListElement = (Element) glElement.getElementsByTagName("expression-list").item(0);
			int expressionSize = Integer.valueOf(expressionListElement.getAttribute("size"));
			Expression[] expressions = new Expression[expressionSize];

			NodeList expressionList = expressionListElement.getChildNodes();
			for (int i = 0, n = expressionList.getLength(); i < n; i++) {
				Element expressionElement = (Element) expressionList.item(i);
				expressions[i] = newExpressionFromXML(expressionElement);
			}

			return new GeneralizedListImpl(glName, expressions);

		} else if (encoding.equals("gl")) {

			GeneralizedList gl = null;
			try {
				gl = newGLFromGLString(glElement.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return gl;

		}
		return null;
	}

	public static Expression newExpressionFromXML(InputSource source) {
		Expression expression = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			Element expressionElement = (Element) document.getElementsByTagName("expression").item(0);
			expression = newExpressionFromXML(expressionElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expression;
	}

	public static Expression newExpressionFromXML(Element expressionElement) {
		Expression expression = Expression.UNDEFINED;
		String expressionType = expressionElement.getAttribute("type");
		if (expressionType.equals("generalized-list")) {
			Element glElement = (Element) expressionElement.getElementsByTagName("generalized-list").item(0);
			GeneralizedList gl = newGLFromXML(glElement);
			expression = newExpression(gl);
		} else if (expressionType.equals("value")) {
			Element valueElement = (Element) expressionElement.getElementsByTagName("value").item(0);
			String valueType = valueElement.getAttribute("type");
			if (valueType.equals("integer")) {
				expression = newValueExpression(Integer.valueOf(valueElement.getTextContent()));
			} else if (valueType.equals("float")) {
				expression = newValueExpression(Float.valueOf(valueElement.getTextContent()));
			} else if (valueType.equals("string")) {
				expression = newValueExpression(valueElement.getTextContent());
			}
		} else if (expressionType.equals("variable")) {
			Element variableElement = (Element) expressionElement.getElementsByTagName("variable").item(0);
			String variableName = variableElement.getTextContent();
			expression = newVariableExpression(variableName);
		} else if (expressionType.equals("function")) {
			Element functionElement = (Element) expressionElement.getElementsByTagName("function").item(0);
			Function function = newFunctionFromXML(functionElement);
			expression = newExpression(function);
		}
		return expression;
	}

	public static GeneralizedList newGLFromJSON(JSONObject glObject) {
		GeneralizedList generalizedList = null;

		if (!glObject.containsKey("PredicateName"))
			return null;
		if (!glObject.containsKey("ExpressionSize"))
			return null;
		if (!glObject.containsKey("ExpressionList"))
			return null;

		String glName = glObject.get("PredicateName").toString();
		int expressionSize = Integer.valueOf(glObject.get("ExpressionSize").toString());
		Expression[] expressions = new Expression[expressionSize];

		JSONArray expressionList = (JSONArray) glObject.get("ExpressionList");
		for (int i = 0, n = expressionList.size(); i < n; i++) {
			JSONObject expressionObject = (JSONObject) expressionList.get(i);
			expressions[i] = newExpressionFromJSON(expressionObject);
		}

		return new GeneralizedListImpl(glName, expressions);
	}

	private static Expression newExpressionFromJSON(JSONObject expressionObject) {
		// TODO Auto-generated method stub

		if (!expressionObject.containsKey("Type"))
			return null;

		Expression expression = Expression.UNDEFINED;
		String expressionType = expressionObject.get("Type").toString().toLowerCase();
		if (expressionType.equals("generalizedlist")) {
			JSONObject glObject = (JSONObject) expressionObject.get("GeneralizedList");
			GeneralizedList gl = newGLFromJSON(glObject);
			expression = newExpression(gl);
		} else if (expressionType.equals("value")) {
			String valueType = expressionObject.get("ValueType").toString().toLowerCase();
			String value = expressionObject.get("Value").toString();

			if (valueType.equals("int")) {
				expression = newValueExpression(Integer.valueOf(value));
			} else if (valueType.equals("float")) {
				expression = newValueExpression(Float.valueOf(value));
			} else if (valueType.equals("string")) {
				expression = newValueExpression(value);
			}
		} else if (expressionType.equals("Variable")) {
			String variableName = expressionObject.get("Variable").toString();
			expression = newVariableExpression(variableName);
		} else if (expressionType.equals("Function")) {
			JSONObject functionObject = (JSONObject) expressionObject.get("Function");
			Function function = newFunctionFromJSON(functionObject);
			expression = newExpression(function);
		}
		return expression;

	}

	public static Expression newExpression(GeneralizedList gl) {
		return new GLExpression(gl);
	}

	public static Expression newExpression(Function function) {
		return new FunctionExpression(function);
	}

	public static Expression newExpression(Value value) {
		return new ValueExpression(value);
	}

	public static Expression newExpression(Variable variable) {
		return new VariableExpression(variable);
	}

	public static Expression newGLExpression(String name, ExpressionList list) {
		return new GLExpression(newGL(name, list));
	}

	public static Expression newGLExpression(String name, Expression... expressions) {
		return new GLExpression(newGL(name, expressions));
	}

	public static Expression newFunctionExpression(String name, ExpressionList list) {
		return new FunctionExpression(newFunction(name, list.toArray()));
	}

	public static Expression newFunctionExpression(String name, Expression... expressions) {
		return new FunctionExpression(newFunction(name, expressions));
	}

	public static Expression newVariableExpression(String variableName) {
		return new VariableExpression(newVariable(variableName));
	}

	public static Expression newValueExpression(int value) {
		return new ValueExpression(newValue(value));
	}

	public static Expression newValueExpression(float value) {
		return new ValueExpression(newValue(value));
	}

	public static Expression newValueExpression(String value) {
		return new ValueExpression(newValue(value));
	}

	public static Variable newVariable(String variableName) {
		return new VariableImpl(variableName);
	}

	public static Value newValue(int value) {
		return new IntValue(value);
	}

	public static Value newValue(float value) {
		return new FloatValue(value);
	}

	public static Value newValue(String value) {
		return new StringValue(value);
	}

	public static Function newFunction(String name, ExpressionList expList) {
		return FunctionFactory.newFunction(name, expList.toArray());
	}

	public static Function newFunction(String name, Expression... expressions) {
		return FunctionFactory.newFunction(name, expressions);
	}

	public static Function newFunctionFromXML(InputSource source) {
		Function function = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			Element functionElement = (Element) document.getElementsByTagName("function").item(0);
			function = newFunctionFromXML(functionElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return function;
	}

	public static Function newFunctionFromXML(Element functionElement) {
		String name = functionElement.getElementsByTagName("name").item(0).getTextContent();
		Element expressionListElement = (Element) functionElement.getElementsByTagName("expression-list").item(0);
		int expressionSize = Integer.valueOf(expressionListElement.getAttribute("size"));
		Expression[] expressions = new Expression[expressionSize];
		NodeList expressionList = expressionListElement.getChildNodes();
		for (int i = 0, n = expressionList.getLength(); i < n; i++) {
			Element expressionElement = (Element) expressionList.item(i);
			expressions[i] = GLFactory.newExpressionFromXML(expressionElement);
		}
		return newFunction(name, expressions);
	}

	public static Function newFunctionFromJSON(JSONObject functionObject) {
		String name = functionObject.get("Name").toString();
		int expressionSize = Integer.valueOf(functionObject.get("ExpressionSize").toString());
		Expression[] expressions = new Expression[expressionSize];

		JSONArray expressionList = (JSONArray) functionObject.get("ExpressionList");
		for (int i = 0, n = expressionList.size(); i < n; i++) {
			JSONObject expressionObject = (JSONObject) expressionList.get(i);
			expressions[i] = GLFactory.newExpressionFromJSON(expressionObject);
		}
		return newFunction(name, expressions);
	}

	public static String escape(String content) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, n = content.length(); i < n; i++) {
			char c = content.charAt(i);
			if (c == '<')
				builder.append("&lt;");
			else if (c == '>')
				builder.append("&gt;");
			else if (c == '&')
				builder.append("&amp;");
			else if (c == '"')
				builder.append("&quot;");
			else if (c == '\'')
				builder.append("&apos;");
			else
				builder.append(c);
		}
		return builder.toString();
	}

	public static String unescape(String content) {
		String unescapedString = content;
		unescapedString = unescapedString.replaceAll("&lt;", "<");
		unescapedString = unescapedString.replaceAll("&gt;", ">");
		unescapedString = unescapedString.replaceAll("&amp;", "&");
		unescapedString = unescapedString.replaceAll("&quot;", "\"");
		unescapedString = unescapedString.replaceAll("&apos;", "\'");
		return unescapedString;
	}
}
