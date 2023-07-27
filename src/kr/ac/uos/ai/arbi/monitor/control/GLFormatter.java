package kr.ac.uos.ai.arbi.monitor.control;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class GLFormatter {
	public static String format(String str) {
		if (str.length() <= 40)
			return str;

		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(str);
		} catch (ParseException e) {
			return str;
		} catch (Throwable t) {
			return str;
		}

		return format(gl, "");
	}

	private static String format(GeneralizedList gl, String prefix) {
		StringBuilder builder = new StringBuilder();

		String str = gl.toString();
		if (str.length() <= 20) {
			builder.append(prefix).append(str);
			return builder.toString();
		}

		builder.append(prefix).append("(").append(gl.getName());

		if (!(isSimpleLengthGL(gl))) {
			builder.append("\n");
		}

		for (int i = 0; i < gl.getExpressionsSize(); ++i) {
			Expression exp = gl.getExpression(i);
			if (exp.isGeneralizedList()) {
				builder.append(format(exp.asGeneralizedList(), prefix + "        "));
				builder.append("\n");
			} else {
				if (gl.getExpressionsSize() > 1)
					builder.append(prefix).append("        ");
				else {
					builder.append(" ");
				}

				builder.append(exp.toString());

				if (gl.getExpressionsSize() > 1) {
					builder.append("\n");
				}
			}
		}

		if (!(isSimpleLengthGL(gl))) {
			builder.append(prefix);
		}

		builder.append(")");

		return builder.toString();
	}

	private static boolean isSimpleLengthGL(GeneralizedList gl) {
		if (gl.getExpressionsSize() == 0)
			return true;
		return ((gl.getExpressionsSize() == 1) && (!(gl.getExpression(0).isGeneralizedList())));
	}
}