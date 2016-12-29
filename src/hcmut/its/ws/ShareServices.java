package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Path("/share/")
public class ShareServices {
	@POST
	@Path("SaveShareOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String SaveShareOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId, @FormParam("shareList") String shareList) {
		// shareList is JsonArray: {listShare:[{userId:},{userId:}]}
		
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		// get id user
		String userId = usr.GetUserId(tokenId);
		System.out.println("UserId is: " + userId);
		
		DBCollection shares = TrafficDB.getCollection("share");
		BasicDBObject queryShare = new BasicDBObject();
		queryShare.append("tripId", tripId);
		DBCursor cursorShare = shares.find(queryShare);
		
		if(cursorShare.size() == 0 || cursorShare == null){
			BasicDBObject newShare = new BasicDBObject();
			newShare.append("tripId", tripId).append("shareList", shareList);
			shares.insert(newShare);
			System.out.println("String share is : " + shareList);
			System.out.println("Add new share on tripId: " + tripId +" successfull!");
		}
		else{
			while (cursorShare.hasNext()) {
				BasicDBObject data = (BasicDBObject) cursorShare.next();
				
				//String str_jsonArray = (String) data.get("shareList");
				
				// Get Json string from data base
				//System.out.println("Get strJson:" + str_jsonArray);
				//JSONArray jsonArray = (JSONArray) JSONValue.parse(str_jsonArray); 
				
				// Input Json Str
				//System.out.println("Input strJson:" + shareList);
				//JSONArray inputJsonArray = (JSONArray) JSONValue.parse(shareList); 
				
				//System.out.println("size:" + inputJsonArray.size());
				
			   // for (int i = 0; i < inputJsonArray.size(); i++) {
			        //JSONObject explrObject = (JSONObject) inputJsonArray.get(i);
			        //if(!CheckContaintSharePerson(inputJsonArray, explrObject)){
			        //	inputJsonArray.add(explrObject);
			        //}
			    //}
				//shareList = inputJsonArray.toString();
				
				data.put("shareList", shareList);
				shares.save(data);
				System.out.println("String share is : " + shareList);
				System.out.println("Edit share on tripId: " + tripId +" successfull!");
				break;
			}
		}
		cursorShare.close();
		
		String code = "1";
		String description = "Share on trip successful!";
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
	
	boolean CheckContaintSharePerson(JSONArray jsonArray,JSONObject itemCheck) {
		String strCheckItem = itemCheck.toString();
		for(int i = 0; i < jsonArray.size(); i++){
        	String tempExplrObject = ((JSONObject)jsonArray.get(i)).toString();
        	if(tempExplrObject.equals(strCheckItem)){
        		return true;
        	}
        }
		return false;
	}
	
	@POST
	@Path("GetShareOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Produces(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String GetShareOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId){
		// shareList is JsonArray: {listShare:[{userId:},{userId:}]}

		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		// get id user
		String userId = usr.GetUserId(tokenId);
		System.out.println("UserId is: " + userId);
		DBCollection shares = TrafficDB.getCollection("share");
		BasicDBObject queryShare = new BasicDBObject();
		queryShare.append("tripId", tripId);
		System.out.println("TripId is : " + tripId);
		DBCursor cursorShare = shares.find(queryShare);
		String shareList = "{[{userId:NULL}]}";
		
		while (cursorShare.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorShare.next();
			shareList = data.getString("shareList");
			System.out.println("String share is : " + shareList);
			break;
		}
		
		//System.out.println("String share is : " + shareList);
		//JSONObject jsonResult = (JSONObject)JSONValue.parse(shareList);
		//JSONArray jArray = (JSONArray) JSONValue.parse(shareList);
		//JSONObject outputJsonObj = new JSONObject();
		//outputJsonObj.put("shareList", shareList);
		//return outputJsonObj.toJSONString();
		return shareList;
	}
}
