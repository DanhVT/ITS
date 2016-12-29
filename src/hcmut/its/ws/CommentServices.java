package hcmut.its.ws;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import hcmut.its.db.TrafficDB;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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

@Path("/comment/")
public class CommentServices {
	@POST
	@Path("SaveCommentOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String SaveCommentOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId,
			@FormParam("content") String content){
		
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		
		// get id user
		String userId = usr.GetUserId(tokenId);
		
		DBCollection comments = TrafficDB.getCollection("tripComment");
		BasicDBObject newComment = new BasicDBObject();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String dateTime = dateFormat.format(date);
		String commentId = String.valueOf(UUID.randomUUID());
		String dateTimeModify = dateFormat.format(date);
		
		newComment.append("commentId", commentId).append("userId", userId).append("tripId", tripId)
		.append("content", content).append("dateTime", dateTime).append("dateTimeModify", dateTimeModify);
		
		comments.insert(newComment);
		System.out.println("CommentId:" + commentId);
		
		String code = "1";
		String description = "Insert comment on trip successful!";
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("GetListCommentOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListCommentOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId){
		
		UserServices usrSv = new UserServices();
		// kiem tra valid token
		usrSv.CheckValidToken(tokenId);
		
		// get id user
		String userId = usrSv.GetUserId(tokenId);
		
		DBCollection comments = TrafficDB.getCollection("tripComment");
		BasicDBObject queryComment = new BasicDBObject();
		queryComment.append("tripId", tripId);
		DBCursor cursorComment = comments.find(queryComment);
		JSONArray ja = new JSONArray();
		while (cursorComment.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorComment.next();
			
			String commentId = (String) data.get("commentId");
			String comUserId = (String) data.get("userId");
			String content = (String) data.get("content");
			String dateTime = (String) data.get("dateTime");
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("commentId", commentId);
			jsonObj.put("userId", comUserId);
			jsonObj.put("content", content);
			jsonObj.put("dateTime", dateTime);
			ja.add(jsonObj);
			System.out.println("Get comment with id: " + commentId);
		}
		org.json.simple.JSONObject outputJsonObj = new org.json.simple.JSONObject();
		outputJsonObj.put("listComment", ja);
		return outputJsonObj.toJSONString();
	}
	public int countCommentOnTrip(String tripId){    //DANH_FIX
		DBCollection comments = TrafficDB.getCollection("tripComment");
		BasicDBObject queryComment = new BasicDBObject();
		queryComment.append("tripId", tripId);
		DBCursor cursorComment = comments.find(queryComment);
		
		return cursorComment.size();
	} 
}
