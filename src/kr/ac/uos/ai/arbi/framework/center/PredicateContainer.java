package kr.ac.uos.ai.arbi.framework.center;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class PredicateContainer {
	private final String				author;
	private final long					createTime;
	private final GeneralizedList		predicate;
	private final String 				predicatePrefix;
	
	public PredicateContainer(String author, long createTime, GeneralizedList predicate) {
		this.author = author;
		this.createTime = createTime;
		this.predicate = predicate;
		this.predicatePrefix = RedisUtil.PredicatePrefix;
	}
	public PredicateContainer(String author, long createTime, String glString) {
		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.predicatePrefix = RedisUtil.PredicatePrefix;
		this.author = author;
		this.createTime = createTime;
		this.predicate = gl;
	}
	public String getAuthor() {
		return author;
	}
	public long getCreateTime() {
		return createTime;
	}
	public GeneralizedList getPredicate() {
		return predicate;
	}
	
	public String getPredicateKey() {
		return  predicatePrefix + predicate.getName() + ":" + createTime;
	}
}
