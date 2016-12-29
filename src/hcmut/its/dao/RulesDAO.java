package hcmut.its.dao;

import hcmut.its.db.RuleDB;
import hcmut.its.rule.Rule;
import hcmut.its.rule.RuleImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class RulesDAO {

	private DBObject ruleBson = null;
	private static final String RULE_COLLECTION_NAME = "limitedlist";

	public RulesDAO(int id) {
		DBCollection ruleCollection = RuleDB
				.getCollection(RULE_COLLECTION_NAME);
		ruleBson = ruleCollection.findOne(new BasicDBObject("segment_id", id));
	}

	public List<Rule> getRules() {
		List<Rule> rules = new ArrayList<Rule>();
		if (ruleBson != null) {
			ListIterator<Object> list = ((BasicDBList) ruleBson.get("rule"))
					.listIterator();
			while (list.hasNext()) {
				BasicDBObject item = (BasicDBObject) list.next();
				double maxWeight = item.getDouble("maxWeight");
				double minWeight = item.getDouble("minWeight");
				double maxLoad = item.getDouble("maxLoad");
				double minLoad = item.getDouble("minLoad");
				double maxTotalLoad = item.getDouble("maxTotalLoad");
				double minTotalLoad = item.getDouble("minTotalLoad");
				int startTime = item.getInt("startTime");
				int endTime = item.getInt("endTime");
				Rule r = new RuleImpl(maxWeight, minWeight, maxLoad, minLoad,
						maxTotalLoad, minTotalLoad, startTime, endTime);
				rules.add(r);
			}
		}
		return rules;
	}
	
	public static void main (String[] args) {
		Map<String, String> l = new HashMap<>();
		l.put("e", "e");
		
		String a  = l.get("b");
		
		System.out.print(a == null);
		
		/*
		Truck v = new TruckImpl("", "", 50000000, 500000, 600000000);
		RulesDAO r = new RulesDAO(new Integer(11558));
		List<Rule> rules = r.getRules();
		for (Rule ru : rules) {
			System.out.println(ru.allows(v, 480));
		}
		*/
	}

}
