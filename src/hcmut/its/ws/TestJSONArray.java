package hcmut.its.ws;

import hcmut.its.multipoints.Query;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URLDecoder;
import java.net.URLEncoder;



public class TestJSONArray {

	/*
	 * multiple points example parameters: [ segment:
	 * {lat=10.796379859206985,lng=106.6732120513916,id=89355} segment:
	 * {lat=10.784154456566771,lng=106.66226863861084,id=1935} segment:
	 * {lat=10.753293645253464,lng=106.65407180786133,id=86877}
	 * 
	 * ]
	 */

	public static void main(String[] args) {
		List<Query> queries = new ArrayList<Query>();
		//String parameters = "[{\"lat\":10.796379859206985,\"lng\":106.6732120513916,\"id\":\"89355\"},{\"lat\":10.784154456566771,\"lng\":106.66226863861084,\"id\":\"1935\"},{\"lat\":10.753293645253464,\"lng\":106.65407180786133,\"id\":\"86877\"} ]";

		String parameters = "[{\"lat\":10.796379859206985,\"lng\":106.6732120513916,\"id\":\"89355\"},{\"lat\":10.753293645253464,\"lng\":106.65407180786133,\"id\":\"86877\"}]";
		
		String s1 = URLEncoder.encode(parameters);
		String s2 = URLDecoder.decode(s1);
		System.out.println(s1);
		System.out.println(s2);

		/*
		 * %5B%7B%22lat%22%3A10.796379859206985%2C%22lng%22%3A106.6732120513916%2C%22id%22%3A%2289355%22%7D%2C%
7B%22lat%22%3A10.784154456566771%2C%22lng%22%3A106.66226863861084%2C%22id%22%3A%221935%22%7D%2C%7B%2
2lat%22%3A10.753293645253464%2C%22lng%22%3A106.65407180786133%2C%22id%22%3A%2286877%22%7D+%5D
		 */
		
		/*
		 * %5B%7B%22lat%22%3A10.796379859206985%2C%22lng%22%3A106.6732120513916%2C%22id%22%3A%2289355%22%7D%2C%
7B%22lat%22%3A10.753293645253464%2C%22lng%22%3A106.65407180786133%2C%22id%22%3A%2286877%22%7D%5D
		 */
		
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
		
		

		System.out.println(jArray.toString());
		System.out.println(parameters);


	}
}
