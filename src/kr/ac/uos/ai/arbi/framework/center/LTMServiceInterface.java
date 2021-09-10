package kr.ac.uos.ai.arbi.framework.center;

public interface LTMServiceInterface {
	
	public void addLTMNotificationHandler(LTMNotificationHandler handler);
	public String match(String author, String fact);
	public String retrieveFact(String author, String fact);
	public String retractFact(String author, String fact) ;
	public String updateFact(String author, String fact) ;
	public String assertFact(String author, String string) ;
	public String subscribe(String author, String rule);
	public String unsubscribe(String author, String id) ;
	public String requestStream(String author, String string) ;
	public String releaseStream(String author, String string);
	public String getLastModifiedTime(String author, String fact);

}
