package kr.ac.uos.ai.arbi.monitor.model;


import javax.xml.bind.annotation.adapters.XmlAdapter;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class GLAdapter extends XmlAdapter<String, GeneralizedList>{

	public String marshal(GeneralizedList gl) throws Exception {
		if (gl == null)
			return null;
		return gl.toString();
	}

	public GeneralizedList unmarshal(String str) throws Exception {
		return GLFactory.newGLFromGLString(str);
	}
	
}
