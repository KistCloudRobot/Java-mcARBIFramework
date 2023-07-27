package kr.ac.uos.ai.arbi.monitor.util;

public class StringUtility {
	private StringUtility() {
		//
	}
	
	public static String escape(String content) {
		StringBuilder builder = new StringBuilder();
		for (int i=0, n=content.length(); i<n; i++) {
			char c = content.charAt(i);
			if(c == '<')
				builder.append("&lt;");
			else if(c == '>')
				builder.append("&gt;");
			else if(c == '&')
				builder.append("&amp;");
			else if(c == '"')
				builder.append("&quot;");
			else if(c == '\'')
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
