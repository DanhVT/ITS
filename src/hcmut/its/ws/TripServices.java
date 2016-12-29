package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Path("/trip/")
public class TripServices {
	@POST
	@Path("CreateTrip")                             // DANH_FIX
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String CreateTrip(
			@FormParam("tokenId") String tokenId,
			@FormParam("tripName") String tripName,
			@FormParam("startTime") String startTime,
			@FormParam("endTime") String endTime,
			@FormParam("dateTime") String dateTime,
			@FormParam("fromX") String fromX,
			@FormParam("fromY") String fromY,
			@FormParam("fromZ") String fromZ,
			@FormParam("fromLocationName") String fromLocationName,
			@FormParam("fromDescription") String fromDescription,
			@FormParam("toX") String toX,
			@FormParam("toY") String toY,
			@FormParam("toZ") String toZ,
			@FormParam("toLocationName") String toLocationName,
			@FormParam("toDescription") String toDescription,
			@FormParam("privacy") String privacy,
			@FormParam("emotion") String emotion)

			throws Exception {
		System.out.println("tokenId: " + tokenId);
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		
		// get id user
		String userId = usr.GetUserId(tokenId);
		System.out.println("userId: "+ userId);
		
		DBCollection trip = TrafficDB.getCollection("trip");
		BasicDBObject newTrip = new BasicDBObject();
		
		
		String tripId = String.valueOf(UUID.randomUUID());
		newTrip.append("tripId", tripId)
		.append("userId", userId)
		.append("tripName",tripName)
		.append("startTime", startTime)
		.append("endTime", endTime)
		.append("fromX", fromX)
		.append("fromY", fromY)
		.append("fromZ", fromZ)
		.append("fromLocationName", fromLocationName)
		.append("fromDescription", fromDescription)
		.append("toX", toX)
		.append("toY", toY)
		.append("toZ", toZ)
		.append("toLocationName", toLocationName)
		.append("toDescription", toDescription)
		.append("dateTime", dateTime)
		.append("emotion", emotion)
		.append("dateTimeModify", dateTime)
		.append("privacy", privacy); // privacy: Private, Friend, Public
		trip.insert(newTrip);
		
		System.out.println("TripId: "+ tripId);
		System.out.println("tripName: "+ tripName);
		System.out.println("startTime: "+ startTime);
		System.out.println("endTime: "+ endTime);
		System.out.println("fromLocationName: "+ fromLocationName);
		System.out.println("toLocationName: "+ toLocationName);
		TripDetailServices initalTrip = new TripDetailServices();
		// Create Start point and End point
		initalTrip.CreatePointOnTrip(tokenId, tripId, fromX, fromY, fromZ, fromLocationName,fromDescription, "START");
		initalTrip.CreatePointOnTrip(tokenId, tripId, toX, toY, toZ, toLocationName,toDescription, "END");
		// Insert to Share
		String code = "1";
		String description = "Insert trip on trip successful!";
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("tripId", tripId);
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
	
	// Lay danh sach cac route public hoac dc share cua Friend
	@POST
	@Path("GetListTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListTrip(@FormParam("tokenId")String tokenId){ //DANH_FIX
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);	
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		System.out.println("UserId is: " + userId);
		
		DBCollection trip = TrafficDB.getCollection("trip");
		
		
		JSONArray ja = new JSONArray();
		
		FriendServices friendSer = new FriendServices();
		ArrayList<String> listFriend = friendSer.ListFriend(userId);
		
		BasicDBObject queryTrip = new BasicDBObject();
		
		BasicDBObject nePrivate = new BasicDBObject();
		nePrivate.put("privacy", new BasicDBObject("$ne", "Private")) ; 
		BasicDBObject inFriendList = new BasicDBObject();
		inFriendList.put("userId", new BasicDBObject("$in", listFriend));
		
		List<BasicDBObject> andObj = new ArrayList<BasicDBObject>();
		andObj.add(nePrivate); andObj.add(inFriendList) ;
		
		queryTrip.put("$and", andObj);
		DBCursor cursorTrip = trip.find(queryTrip);
		
		while (cursorTrip.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorTrip.next();
			
			String tripName = (String) data.get("tripName");
			String startTime = (String) data.get("startTime");
			String endTime = (String) data.get("endTime");
			String fromLocationName = (String) data.get("fromLocationName");
			String toLocationName = (String) data.get("toLocationName");
			String dateTime = (String) data.get("dateTime");
			String tripId = (String) data.get("tripId");
			String ownerId = (String) data.get("userId");
			String emotion = (String) data.get("emotion");
			String userNameAndLink = usr.GetUserNameAndLink(ownerId);
			LikeServices like = new LikeServices();
			ArrayList<String> listLiker = like.getListLiker(ownerId, tripId);

			CommentServices commentServices = new CommentServices(); 
			int numComment = commentServices.countCommentOnTrip(tripId);
			
			JSONObject jo = new JSONObject();
			jo.put("tripId",tripId);
			jo.put("userId",ownerId);
			jo.put("userName",userNameAndLink.split("::")[0]);
			jo.put("link", userNameAndLink.split("::")[1]);
			jo.put("tripName", tripName);
			jo.put("startTime", startTime);
			jo.put("endTime", endTime);
			jo.put("fromLocationName", fromLocationName);
			jo.put("toLocationName", toLocationName);
			jo.put("dateTime", dateTime);	
			jo.put("emotion", emotion);	
			jo.put("numComments", numComment);
			jo.put("listLiker", listLiker);
			// Put element into Json Array
			ja.add(jo);
		}
		cursorTrip.close();
		
//		for(int i = 0; i < listFriend.size(); i++){
//			String friendId = listFriend.get(i);
//			BasicDBObject queryTrip = new BasicDBObject();
//			queryTrip.append("userId", friendId);
//			DBCursor cursorTrip = trip.find(queryTrip);
//			while (cursorTrip.hasNext()) {
//				BasicDBObject data = (BasicDBObject) cursorTrip.next();
//				String tripName = (String) data.get("tripName");
//				String startTime = (String) data.get("startTime");
//				String endTime = (String) data.get("endTime");
//				String fromLocationName = (String) data.get("fromLocationName");
//				String toLocationName = (String) data.get("toLocationName");
//				String dateTime = (String) data.get("dateTime");
//				String tripId = (String) data.get("tripId");
//				String emotion = (String) data.get("emotion");
//				String userNameAndLink = usr.GetUserNameAndLink(friendId);
//				LikeServices like = new LikeServices();
//				ArrayList<String> listLiker = like.getListLiker(friendId, tripId);
//
//				CommentServices commentServices = new CommentServices(); 
//				int numComment = commentServices.countCommentOnTrip(tripId);
//
//				JSONObject jo = new JSONObject();
//				jo.put("tripId",tripId);
//				jo.put("userId",friendId);
//				jo.put("userName",userNameAndLink.split("::")[0]);
//				jo.put("link", userNameAndLink.split("::")[1]);
//				jo.put("tripName", tripName);
//				jo.put("startTime", startTime);
//				jo.put("endTime", endTime);
//				jo.put("fromLocationName", fromLocationName);
//				jo.put("toLocationName", toLocationName);
//				jo.put("dateTime", dateTime);	
//				jo.put("emotion", emotion);	
//				jo.put("numComments", numComment);
//				jo.put("listLiker", listLiker);
//				// Put element into Json Array
//				ja.add(jo);
//			}
//			cursorTrip.close();
//		}
		
		
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("listTrip", ja);
		return outputJsonObj.toJSONString();
	}
	
	
	@POST
	@Path("GetListPrivateTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListPrivateTrip(@FormParam("tokenId")String tokenId){   //DANH_FIX
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);	
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		System.out.println("UserId is: " + userId);
		
		DBCollection trip = TrafficDB.getCollection("trip");
		BasicDBObject queryTrip = new BasicDBObject();
		queryTrip.append("userId", userId);
		DBCursor cursorTrip = trip.find(queryTrip);
		JSONArray ja = new JSONArray();
		while (cursorTrip.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorTrip.next();
			String tripName = (String) data.get("tripName");
			String startTime = (String) data.get("startTime");
			String endTime = (String) data.get("endTime");
			String fromLocationName = (String) data.get("fromLocationName");
			String toLocationName = (String) data.get("toLocationName");
			String dateTime = (String) data.get("dateTime");
			String tripId = (String) data.get("tripId");
			String ownerId = (String) data.get("userId");
			String emotion = (String) data.get("emotion");
			
			LikeServices like = new LikeServices();
			ArrayList<String>listLiker = like.getListLiker(ownerId, tripId);
			
			CommentServices commentServices = new CommentServices(); 
			int numComment = commentServices.countCommentOnTrip(tripId);

			JSONObject jo = new JSONObject();
			jo.put("tripId",tripId);
			jo.put("tripName", tripName);
			jo.put("startTime", startTime);
			jo.put("endTime", endTime);
			jo.put("fromLocationName", fromLocationName);
			jo.put("toLocationName", toLocationName);
			jo.put("dateTime", dateTime);	
			jo.put("emotion", emotion);
			jo.put("listLiker", listLiker);	
			jo.put("numComments", numComment);
			// Put element into Json Array
			ja.add(jo);
			
		}
		cursorTrip.close();
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("listTrip", ja);
		System.out.println(outputJsonObj);
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("GetListPublicTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListPublicTrip(@FormParam("tokenId")String tokenId){ // DANH_FIX
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);	
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		System.out.println("UserId is: " + userId);
		
		DBCollection trip = TrafficDB.getCollection("trip");
		
		BasicDBObject publicQuery = new BasicDBObject();
		publicQuery.append("privacy", "Public");
		
		BasicDBObject userIdNotNullQuery = new BasicDBObject();
		userIdNotNullQuery.put("userId", new BasicDBObject("$ne", "null"));
		
		List<BasicDBObject> andQuery = new ArrayList<BasicDBObject>();
		andQuery.add(publicQuery); andQuery.add(userIdNotNullQuery);
		
		BasicDBObject queryTrip = new BasicDBObject();
		queryTrip.put("$and", andQuery);
		
		DBCursor cursorTrip = trip.find(queryTrip);
		JSONArray ja = new JSONArray();
		while (cursorTrip.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorTrip.next();
			String ownID = (String) data.get("userId");
			String privacy = (String) data.get("privacy");
			String tripName = (String) data.get("tripName");
			String startTime = (String) data.get("startTime");
			String endTime = (String) data.get("endTime");
			String fromLocationName = (String) data.get("fromLocationName");
			String toLocationName = (String) data.get("toLocationName");
			String dateTime = (String) data.get("dateTime");
			String tripId = (String) data.get("tripId");
			String emotion = (String) data.get("emotion");
			String userNameAndLink = usr.GetUserNameAndLink(ownID);
			
			CommentServices commentServices = new CommentServices(); 
			int numComment = commentServices.countCommentOnTrip(tripId);

			LikeServices like = new LikeServices();
			ArrayList<String>listLiker = like.getListLiker(ownID, tripId);
			
			JSONObject jo = new JSONObject();
			jo.put("tripId",tripId);
			jo.put("userId",ownID);
			jo.put("tripName",tripName);
			jo.put("userName",userNameAndLink.split("::")[0]);
			jo.put("link", userNameAndLink.split("::")[1]);
			jo.put("startTime", startTime);
			jo.put("endTime", endTime);
			jo.put("fromLocationName", fromLocationName);
			jo.put("toLocationName", toLocationName);
			jo.put("dateTime", dateTime);		
			jo.put("emotion", emotion);	
			jo.put("listLiker", listLiker);	
			jo.put("numComments", numComment);
			// Put element into Json Array
			ja.add(jo);
			
		}
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("listTrip", ja);
		return outputJsonObj.toJSONString();
	}	
	
	@POST
	@Path("GetTripInfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetTripInfo(@FormParam("tokenId") String tokenId, @FormParam("tripId") String tripId){
		// Kiem tra xem tokenID nay co quyen truy cap trip hay ko
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		// get id user
		String userId = usr.GetUserId(tokenId);
		String startTime = "";
		String endTime = "";
		String fromDescription = "";
		String toDescription = "";
		DBCollection trips = TrafficDB.getCollection("trip");
		BasicDBObject query = new BasicDBObject();
		query.append("tripId", tripId);
		DBCursor cursor = trips.find(query);
		System.out.println("tokenId: " + tokenId);
		System.out.println("tripId: " + tripId);
		System.out.println("Number row trip: " + cursor.size());
		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			startTime = (String) data.get("startTime");
			System.out.println("startTime: " + startTime);
			endTime = (String) data.get("endTime");
			System.out.println("endTime: " + endTime);
			fromDescription = (String) data.get("fromDescription");
			System.out.println("fromDescription: " + fromDescription);
			toDescription = (String) data.get("toDescription");
			System.out.println("toDescription: " + toDescription);
		}
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("startTime", startTime);
		outputJsonObj.put("endTime", endTime);
		outputJsonObj.put("fromDescription", fromDescription);
		outputJsonObj.put("toDescription", toDescription);
		return outputJsonObj.toJSONString();
	}
	
	ArrayList<String> GetShareTripIdForUserId(String userId){
		ArrayList<String> result = new ArrayList<String>();
		DBCollection shares = TrafficDB.getCollection("share");
		BasicDBObject queryShare = new BasicDBObject();
		queryShare.put("shareList", userId );
		//System.out.println("TripId is : " + tripId);
		DBCursor cursorShare = shares.find(queryShare);
		//String shareList = "{[{userId:NULL}]}";
//		System.out.println("shareList is: "+ cursorShare.size());
		while (cursorShare.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorShare.next();
			String shareList = data.getString("shareList");			
			String tripId = data.getString("tripId");
			if(shareList.contains(userId)){
				result.add(tripId);
			}
		}
		return result;
	}
	
	
	@POST
	@Path("GetListShareTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListShareTrip(@FormParam("tokenId")String tokenId){
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);	
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		System.out.println("UserId is: " + userId);
		
		ArrayList<String> listTripId = GetShareTripIdForUserId(userId);
		
		System.out.println("There is: " + listTripId.size() + " shared trips");
		
		JSONArray ja = new JSONArray();
		for(int i = 0; i < listTripId.size();i++){
			String tripId = listTripId.get(i);
			System.out.println("tripId: " + tripId);
			DBCollection trip = TrafficDB.getCollection("trip");
			BasicDBObject queryTrip = new BasicDBObject();
			queryTrip.append("tripId", tripId);
			DBCursor cursorTrip = trip.find(queryTrip);
			while (cursorTrip.hasNext()) {
				BasicDBObject data = (BasicDBObject) cursorTrip.next();
				String owner = (String) data.get("userId");
				String tripName = (String) data.get("tripName");				
				String startTime = (String) data.get("startTime");
				String endTime = (String) data.get("endTime");
				String fromLocationName = (String) data.get("fromLocationName");
				String toLocationName = (String) data.get("toLocationName");
				String dateTime = (String) data.get("dateTime");
				String emotion = (String) data.get("emotion");
				CommentServices commentServices = new CommentServices(); 
				int numComment = commentServices.countCommentOnTrip(tripId);

				LikeServices like = new LikeServices();
				ArrayList<String>listLiker = like.getListLiker(owner, tripId);

				JSONObject jo = new JSONObject();
				jo.put("userId", owner); // Trip owner
				jo.put("tripId",tripId);
				jo.put("tripName", tripName);
				jo.put("startTime", startTime);
				jo.put("endTime", endTime);
				jo.put("fromLocationName", fromLocationName);
				jo.put("toLocationName", toLocationName);
				jo.put("dateTime", dateTime);	
				jo.put("emotion", emotion);		
				jo.put("listLiker", listLiker);	
				jo.put("numComments", numComment);	
				// Put element into Json Array
				ja.add(jo);
			}
			cursorTrip.close();
		}
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("listTrip", ja);
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("RemoveTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String RemoveTrip(@FormParam("tokenId")String tokenId, @FormParam("tripId")String tripId){
		UserServices usr = new UserServices();
		usr.CheckValidToken(tokenId);	
		// get id user
		String userId = usr.GetUserId(tokenId);
		DBCollection trip = TrafficDB.getCollection("trip");
		BasicDBObject queryTrip = new BasicDBObject();
		queryTrip.append("tripId", tripId);
		trip.remove(queryTrip);
		
		String code = "1";
		String description = "Delete trip on trip collection successful!";
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("tripId", tripId);
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
}
