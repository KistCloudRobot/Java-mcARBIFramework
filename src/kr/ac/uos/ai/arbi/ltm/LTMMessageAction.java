package kr.ac.uos.ai.arbi.ltm;

public enum LTMMessageAction {
	RequestStream, Push, ReleaseStream,
	
	AssertFact, RetrieveFact, UpdateFact, RetractFact, Match, Result,
	
	Subscribe, Unsubscribe, Notify, GetLastModifiedTime
	
	
	
}
