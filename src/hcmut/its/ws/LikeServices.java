package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Path("/like/")
public class LikeServices {
	@POST
	@Path("LikeTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String LikeTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId){
	
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		
		// get id user
		String userId = usr.GetUserId(tokenId);
		System.out.println("Trip IDs: "+ tripId);
		DBCollection likes = TrafficDB.getCollection("likeTrip");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId).append("tripId", tripId);
		
		DBCursor cursor = likes.find(query);	
		
		if (cursor.size() == 0 | cursor == null) {
			BasicDBObject newLike = new BasicDBObject();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String date_time = dateFormat.format(date);
			String date_time_modify = dateFormat.format(date);
			
			newLike.append("userId", userId).append("tripId", tripId).append("dateTime", date_time).append("dateTimeModify", date_time_modify);// LIKE or DISLIKE 
			
			likes.insert(newLike);
		}
		else{
			likes.remove(query);
		}
		
		cursor.close();
		
		String code = "1";
		String description = "Like on trip successful!";
		JSONObject outputJsonObj = new JSONObject();
		
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("CountLikeOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String CountLikeOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId){
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		DBCollection likes = TrafficDB.getCollection("likeTrip");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId).append("tripId", tripId);
		DBCursor cursor = likes.find(query);
		
		
		int numLike = cursor.count();
		cursor.close();
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("numLike", numLike);
		
		return outputJsonObj.toJSONString();
	}
	
	public ArrayList<String> getListLiker(String userId,String tripId){     //DANH_FIX
		DBCollection likes = TrafficDB.getCollection("likeTrip");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId).append("tripId", tripId);
		DBCursor cursor = likes.find(query);
		ArrayList<String> list_liker =new ArrayList<String>();

		String userID = "";

		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			userID = (String) data.get("userId");
			list_liker.add(userID);
		}		
		cursor.close();
		return list_liker;
	}
}
