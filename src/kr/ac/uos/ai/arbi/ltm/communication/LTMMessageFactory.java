package kr.ac.uos.ai.arbi.ltm.communication;

import java.util.UUID;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.message.*;

public class LTMMessageFactory {
	private static LTMMessageFactory				instance;
	
	private LTMMessageFactory() {}
	
	public  static LTMMessageFactory getInstance(){
		if(instance == null){ 
			instance = new LTMMessageFactory();
		}
		return instance;
	}
	
	public LTMMessage newMessage(String uri, LTMMessageAction action, String content){
		return newMessage(uri, action, content, UUID.randomUUID().toString());
	}
	
	public LTMMessage newAssertFactMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.AssertFact, content);
	}
	public LTMMessage newRetractFactMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.RetractFact, content);
	}
	public LTMMessage newRetrieveFactMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.RetrieveFact, content);
	}
	public LTMMessage newUpdateFactMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.UpdateFact, content);
	}
	public LTMMessage newMatchMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.Match, content);
	}
	public LTMMessage newRequestStreamMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.RequestStream, content);
	}
	public LTMMessage newReleaseStreamMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.ReleaseStream, content);
	}
	public LTMMessage newSubscribeMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.Subscribe, content);
	}
	public LTMMessage newUnsubscribeMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.Unsubscribe, content);
	}
	public LTMMessage newNotifyMessage(String uri, String content){
		return newMessage(uri, LTMMessageAction.Notify, content);
	}
	public LTMMessage newResultMessage(String uri, String content, String cID){
		return newMessage(uri, LTMMessageAction.Result, content, cID);
	}
	
	public LTMMessage newGetLastModifiedTimeMessage(String uri, String content) {
		return newMessage(uri, LTMMessageAction.GetLastModifiedTime, content);
	}
	
	
	public LTMMessage newMessage(String uri, LTMMessageAction action, String content, String cID){
		LTMMessage msg = null;
		switch(action){
		case AssertFact:
			msg = new AssertFactMessage(uri, content, cID);
			break;
		case RetractFact:
			msg = new RetractFactMessage(uri, content, cID);
			break;
		case RetrieveFact:
			msg = new RetrieveFactMessage(uri, content, cID);
			break;
		case UpdateFact:
			msg = new UpdateFactMessage(uri, content, cID);
			break;
		case Match:
			msg = new MatchMessage(uri, content, cID);
			break;
		case Result:
			msg = new ResultMessage(uri, content, cID);
			break;
		case ReleaseStream:
			msg = new ReleaseStreamMessage(uri, content, cID);
			break;
		case RequestStream:
			msg = new RequestStreamMessage(uri, content, cID);
			break;
		case Push:
			break;
		case Subscribe:
			msg = new SubscribeMessage(uri, content, cID);
			break;
		case Unsubscribe:
			msg = new UnsubscribeMessage(uri, content, cID);
			break;
		case Notify:
			msg = new NotifyMessage(uri, content, cID);
			break;
		case GetLastModifiedTime:
			msg = new GetLastModifiedTimeMessage(uri,content,cID);
			break;
		default:
			break;
		
		}
		
		return msg;
	}
	

}
