package kr.ac.uos.ai.arbi.model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BindingFactory {
	private BindingFactory() {
		//
	}
	
	public static Binding newBinding() {
		return new BindingImpl();
	}
	
	public static Binding newBinding(Binding binding) {
		return new BindingImpl().copy(binding);
	}
	
	public static Binding newBindingFromXML(InputSource source) {
		Binding binding = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			Element bindingElement = (Element)document.getElementsByTagName("binding").item(0);
			return newBindingFromXML(bindingElement);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return binding;
	}
	
	public static Binding newBindingFromXML(Element bindingElement) {
		Binding binding = newBinding();
		NodeList bindingExpressionList = bindingElement.getElementsByTagName("binding-expression");
		for (int i=0, n=bindingExpressionList.getLength(); i<n; i++) {
			Element bindingExpressionElement = (Element)bindingExpressionList.item(i);
			Element expressionElement = (Element)bindingExpressionElement.getElementsByTagName("expression").item(0);
			String variableName = bindingExpressionElement.getAttribute("variable");
			Expression expression = GLFactory.newExpressionFromXML(expressionElement);
			binding.bind(variableName, expression);
		}
		return binding;
	}
	
	public static Binding[] newBindingListFromXML(InputSource source) {
		Binding[] bindings = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			NodeList bindingNodeList = document.getElementsByTagName("binding");
			bindings = new Binding[bindingNodeList.getLength()];
			for (int i=0, n=bindingNodeList.getLength(); i<n; i++) {
				Element bindingElement = (Element)bindingNodeList.item(i);
				bindings[i] = newBindingFromXML(bindingElement);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bindings;
	}
	
//	public static Binding[] newBindingListFromMapMessage(MapMessage message) {
//		Binding[] bindings = null;
//		try {
//			int bindingSize = message.getInt(MessageSpec.binding_size);
//			bindings = new Binding[bindingSize];
//			for(int i=0, n=bindingSize; i<n; i++) {
//				String bindingString = message.getString(MessageSpec.binding+i);
//				bindings[i] = newBindingFromBindingString(bindingString);
//			}
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		return bindings;
//	}
//	
//	public static Binding newBindingFromBindingString(String bindingString) {
//		Binding binding = null;
//		return binding;
//	}
}
