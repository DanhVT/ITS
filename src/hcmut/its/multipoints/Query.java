package hcmut.its.multipoints;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Query {
	public double lat1;
	public double lng1;
	public String id1;
	public double lat2;
	public double lng2;
	public String id2;

	public Query(double lat1, double lng1, String id1, double lat2,
			double lng2, String id2) {
		super();
		this.lat1 = lat1;
		this.lng1 = lng1;
		this.id1 = id1;
		this.lat2 = lat2;
		this.lng2 = lng2;
		this.id2 = id2;
	}

	public static List<Query> queries(String parameters) {
		List<Query> queries = new ArrayList<Query>();
		JSONArray jArray = (JSONArray) JSONValue.parse(parameters);
		for (int i = 0; i < jArray.size() - 1; i++) {
			JSONObject jObj1 = (JSONObject) jArray.get(i);
			JSONObject jObj2 = (JSONObject) jArray.get(i + 1);
			Query q = new Query((Double) jObj1.get("lat"),
					(Double) jObj1.get("lng"), (String) jObj1.get("id"),
					(Double) jObj2.get("lat"), (Double) jObj2.get("lng"),
					(String) jObj2.get("id"));
			queries.add(q);
		}

		return queries;
	}

}
