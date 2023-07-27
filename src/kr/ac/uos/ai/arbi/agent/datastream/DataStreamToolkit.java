package kr.ac.uos.ai.arbi.agent.datastream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zeromq.ZMQ;

import kr.ac.uos.ai.arbi.framework.center.PredicateContainer;
import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.RuleFactory;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;

public class DataStreamToolkit {

	private HashMap<String, List<Rule>> streamRulesByPredicateName;
	private HashMap<String, Rule> streamRuleByID;
	private HashMap<String, ZMQ.Socket> socketByUrl;
	private HashMap<String, List<String>> streamIDByUrl;

	public DataStreamToolkit() {
		streamRulesByPredicateName = new HashMap<>();
		streamRuleByID = new HashMap<>();
		socketByUrl = new HashMap<>();
		streamIDByUrl = new HashMap<>();
	}

	public String connect(String ruleString) {
		synchronized (this) {
			String streamID = "Stream:" + System.nanoTime();
			GeneralizedList gl = null;
			String urlString = null;
			Rule rule = null;
			try {
				gl = GLFactory.newGLFromGLString(ruleString);
				GeneralizedList urlGL = gl.getExpression(0).asGeneralizedList();
				GeneralizedList ruleGL = gl.getExpression(1).asGeneralizedList();
				urlString = urlGL.getExpression(0).toString().replaceAll("\"", "");
				rule = RuleFactory.newRuleFromRuleString(urlString, ruleGL.toString().replaceAll("\"--&gt;\"", "-->"));
				addRule(rule);
				addSocket(urlString, streamID);
				streamRuleByID.put(streamID, rule);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("e = " + e.toString());
				e.printStackTrace();
			}
			return streamID;
		}
	}

	private void addRule(Rule r) {
		Condition[] condition = r.getConditions();
		for (int i = 0; i < condition.length; i++) {
			if (streamRulesByPredicateName.get(condition[i].getPredicateName()) == null) {
				streamRulesByPredicateName.put(condition[i].getPredicateName(), new ArrayList<Rule>());
			}
			streamRulesByPredicateName.get(condition[i].getPredicateName()).add(r);
		}
	}

	private void addSocket(String url, String streamID) {
		if (!socketByUrl.containsKey(url)) {
			ZMQ.Socket socket = ZMQ.context(1).socket(ZMQ.PUSH);
			socket.bind(url);
			socketByUrl.put(url, socket);
			streamIDByUrl.put(url, new ArrayList<>());
		}
		streamIDByUrl.get(url).add(streamID);
	}

	public void disconnect(String streamID) {
		synchronized (this) {
			Rule r = streamRuleByID.remove(streamID);
			removeRule(r);
			removeStreamID(streamID);
		}
	}

	private void removeRule(Rule r) {
		Condition[] conditions = r.getConditions();
		for (Condition condition : conditions) {
			streamRulesByPredicateName.get(condition.getPredicateName()).remove(r);
		}
	}

	private void removeStreamID(String streamID) {
		for (String url : streamIDByUrl.keySet()) {
			List<String> streamIDList = streamIDByUrl.get(url);
			for (int i = 0; i < streamIDList.size(); i++) {
				String id = streamIDList.get(i);
				if (id.equals(streamID)) {
					streamIDList.remove(i);
				}
			}
			if (streamIDList.size() == 0) {
				socketByUrl.get(url).close();
				socketByUrl.remove(url);
			}
		}
	}

	public void checkRules(String data) {

		try {
			Condition c = ConditionFactory.newConditionFromGLString(data);
			String predicateName = c.getPredicateName();
			List<Rule> rules = streamRulesByPredicateName.get(predicateName);
			if (rules == null || rules.isEmpty()) {
				return;
			}

			for (Rule rule : rules) {
				for (Condition con : rule.getConditions()) {
					GeneralizedList gl;
					String conString = ConditionFactory.toFactString(con);
					gl = GLFactory.newGLFromGLString(conString);
					Binding tempBind = gl.unify(GLFactory.newGLFromGLString(data), null);
					if (tempBind != null) {
						for (Action action : rule.getActions()) {
							action.bind(tempBind);
							send(action);
						}
					}

				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void send(Action action) {
		String url = action.getSubscriber();
		ZMQ.Socket socket = socketByUrl.get(url);
		socket.send(action.toActionContent());

	}
}
