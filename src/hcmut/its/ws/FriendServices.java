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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Path("/friend/")
public class FriendServices {
	@POST
	@Path("SaveListFriend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String SaveListFriend(@FormParam("tokenId") String tokenId,
			@FormParam("listFriend") String listFriend,
			@FormParam("fromSocialNetwork") String fromSocialNetwork)
			throws JSONException {
		// userId, friend list = {listFriend:[{friendId:"friendId"}]}, status,
		// fromSocialNetwork, isFriendOnMember, dateTime, dateTimeModify
		UserServices usr = new UserServices();
		
		// kiem tra valid token
		
		usr.CheckValidToken(tokenId);
		// get id user
		String userId = usr.GetUserId(tokenId);
		System.out.println("UserId is: " + userId);

		DBCollection friend = TrafficDB.getCollection("friend");
		BasicDBObject newFriend = new BasicDBObject();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String dateTime = dateFormat.format(date);
		String dateTimeModify = dateFormat.format(date);
		String status = "FRIEND";

		JSONObject obj = new JSONObject(listFriend);
		JSONArray lstFriend = obj.getJSONArray("listFriend");
		int n = lstFriend.length();
		
		ArrayList <String> listAvailableFriend = ListFriend(userId);
		
		System.out.println("Number of friend must be inserted! " + n);
		for (int i = 0; i < n; ++i) {
			
			String isFriendOnMember = "TRUE";
			JSONObject friendJsonObj = lstFriend.getJSONObject(i);
			String friendId = (String) friendJsonObj.get("friendId");
			
			if(!listAvailableFriend.contains(friendId)){
				BasicDBObject newItem = new BasicDBObject();
				newItem.append("userId", userId).append("friendId", friendId)
				.append("status", "FRIEND").append("isFriendOnMember", isFriendOnMember)
				.append("dateTime", dateTime).append("dateTimeModify", dateTimeModify)
				.append("fromSocialNetwork", fromSocialNetwork);
				friend.insert(newItem);
				
				System.out.println("Insert user and friend ok!");
			}	
		}

		String code = "1";
		String description = "Insert friend successful!";
		org.json.simple.JSONObject outputJsonObj = new org.json.simple.JSONObject();
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
	
	public ArrayList<String> ListFriend(String userId){
		ArrayList<String> list_friend =new ArrayList<String>();
		
		DBCollection friends = TrafficDB.getCollection("friend");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);
		DBCursor cursor = friends.find(query);
		
		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			String friendId = (String) data.get("friendId");
			if(!list_friend.contains(friendId) && !userId.equals(friendId)){
				list_friend.add(friendId);
			}
		}
		System.out.println("Check userId: "+ cursor.size());
		
		BasicDBObject query1 = new BasicDBObject();
		query1.append("friendId", userId);
		DBCursor cursor1 = friends.find(query1);
		while (cursor1.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor1.next();
			String friendId = (String) data.get("userId");
			if(!list_friend.contains(friendId) && !userId.equals(friendId)){
				list_friend.add(friendId);
			}
		}
		System.out.println("Check friendId: "+ cursor1.size());
		cursor.close();
		cursor1.close();
		return list_friend;
	}
	
	@POST
	@Path("GetListFriend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListFriend(@FormParam("tokenId") String tokenId) throws JSONException{
		UserServices usrSv = new UserServices();
		// kiem tra valid token
		usrSv.CheckValidToken(tokenId);
		// get id user
		String userId = usrSv.GetUserId(tokenId);
		System.out.println("UserId is: " + userId);
		
		ArrayList<String> list_friend = ListFriend(userId);
		
		org.json.simple.JSONArray ja = new org.json.simple.JSONArray();
		for(int i = 0; i < list_friend.size(); i++){
			String id = (String) list_friend.get(i);
			System.out.println("friendId is: " + id);	
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userId", id);
			ja.add(jsonObj);	
		}
		
		org.json.simple.JSONObject outputJsonObj = new org.json.simple.JSONObject();
		outputJsonObj.put("listFriend", ja);
		return outputJsonObj.toJSONString();
	}
}
