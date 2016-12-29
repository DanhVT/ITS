package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Path("/user/")
public class UserServices {
	// co nen tao seesion cho user va service giong nhu trong WEB?
	// Neu ko tao session thi APP phai tu code phan Authentication, Web cung
	// vay!
	// Tuy nhien, cac service nay co lien quan den dich vu nguoi dung, nen can
	// phai co xac nhan!
	// "link": avatar
	// Xay dung dich vu nhan vao email, user_id, deviceId, token fb, type social
	// (fb, g+...)

	@POST
	@Path("Login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String POSTGETLoggin(@FormParam("userId") String userId,
			@FormParam("name") String name,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("birthday") String birthday,
			@FormParam("email") String email,
			@FormParam("updateTime") String updateTime,
			@FormParam("gender") String gender,
			@FormParam("local") String local,
			@FormParam("verified") String verified,
			@FormParam("timezone") String timezone,
			@FormParam("link") String link, 
			@FormParam("imei") String imei)
			throws Exception {

		System.out.println("userId: " + userId);
		// Check and input to user data
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);
		DBCursor cursor = users.find(query);

		System.out.println("Number of user!" + cursor.size());

		if (cursor.size() == 0 || cursor == null) {
			BasicDBObject newUser = new BasicDBObject();
			newUser.append("userId", userId).append("name", name)
					.append("firstName", firstName)
					.append("lastName", lastName).append("email", email)
					.append("updateTime", updateTime)
					.append("gender", gender).append("local", local)
					.append("verified", verified).append("timezone", timezone)
					.append("link", link).append("birthday", birthday);

			users.insert(newUser);
			
			System.out.println("userId:" + userId);
			
			System.out.println("Insert user ok!");
		}
		//cursor.close();

		// Check and input to imei data
		DBCollection devices = TrafficDB.getCollection("device");
		query = new BasicDBObject();
		query.append("imei", imei).append("userId", userId);
		cursor = devices.find(query);

		System.out.println("Number of device!" + cursor.size());

		// String uid = String.valueOf(UUID.randomUUID());
		if (cursor.size() == 0 | cursor == null) {
			BasicDBObject newDevice = new BasicDBObject();
			newDevice.append("imei", imei).append("userId", userId);
			devices.insert(newDevice);
			
			System.out.println("Insert device ok!");
		} else {
			// ..................
		}
		//cursor.close();

		// Create token for connecting service and client
		// tokenId, imei, userId, status, logon_date, expire_time(second)

		// Check and input to imei data
		DBCollection tokens = TrafficDB.getCollection("token");
		query = new BasicDBObject();
		query.append("imei", imei).append("userId", userId);
		cursor = tokens.find(query);

		String tokenId = String.valueOf(UUID.randomUUID());
		String status = "TRUE";

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String logonDate = dateFormat.format(date);

		String expireTime = "2592000";

		System.out.println("Number of token!" + cursor.size());

		if (cursor.size() == 0 | cursor == null) {

			BasicDBObject newToken = new BasicDBObject();

			newToken.append("tokenId", tokenId).append("imei", imei)
					.append("userId", userId).append("status", status)
					.append("logonDate", logonDate)
					.append("expireTime", expireTime);

			tokens.insert(newToken);
			
			System.out.println("tokenId:" + tokenId);
			System.out.println("Insert token ok!");
		} else {
			BasicDBObject querycheck = new BasicDBObject();
			querycheck.append("imei", imei).append("userId", userId).append("status", "TRUE");
			DBCursor cursorcheck = tokens.find(querycheck);
			
			System.out.println(cursorcheck.size());
			
			while (cursorcheck.hasNext()) {
				BasicDBObject data = (BasicDBObject) cursorcheck.next();
				System.out.println(data.get("status"));
				//data.remove("status");
				data.append("status","FALSE");
				tokens.save(data);
			}

			//cursorcheck.close();
			
			// Inser new
			BasicDBObject newToken = new BasicDBObject();

			newToken.append("tokenId", tokenId).append("imei", imei)
					.append("userId", userId).append("status", status)
					.append("logonDate", logonDate)
					.append("expireTime", expireTime);

			tokens.insert(newToken);
			
			System.out.println("tokenId:" + tokenId);
			System.out.println("Insert token ok!");
			
		}
		
		cursor.close();
		// Return data

		JSONObject outputJsonObj = new JSONObject();
		//Test response://outputJsonObj.put("tokenId", userId + " " + link);
		outputJsonObj.put("tokenId", tokenId);
		outputJsonObj.put("status", status);
		return outputJsonObj.toJSONString();
	}
	
	public Boolean CheckValidToken(String tokenId){
		return true;
	}
	
	public String GetUserId(String tokenId){	
		String userId = "NULL";
		DBCollection tokens = TrafficDB.getCollection("token");
		BasicDBObject query = new BasicDBObject();
		query.append("tokenId", tokenId);	
		DBCursor cursor = tokens.find(query);
		System.out.println("Number of result: " + cursor.size());
		while (cursor.hasNext()) {
			System.out.println("Find out userId");
			BasicDBObject data = (BasicDBObject) cursor.next();
			userId = (String) data.get("userId");
			
		}
		cursor.close();
		return userId;
	}
	
//	GET NAME OF USER AND LINK
	public String GetUserNameAndLink(String userId){
		String userName = "";
		String link = "";
		
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);
		DBCursor cursor = users.find(query);
		
		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			userName = (String) data.get("name");
			link = (String) data.get("link");
		}
		
		cursor.close();
		if("".equals(link)){
			link = "NO_LINK";
		}
		String result = userName+"::"+link;
		return result;
	}
	
	@POST
	@Path("GetUserInfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetUserInfo(@FormParam("userId") String userId){
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);
		DBCursor cursor = users.find(query);
		
		System.out.println("There is "+ cursor.count() + " users!");
		
		JSONObject outputJsonObj = new JSONObject();
		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			String firstName = (String) data.get("firstName");
			String lastName = (String) data.get("lastName");
			String name = (String) data.get("name");
			String link = (String) data.get("link");
			outputJsonObj.put("firstName", firstName);
			outputJsonObj.put("lastName", lastName);
			outputJsonObj.put("name", name);
			outputJsonObj.put("link", link);
			outputJsonObj.put("userId", userId);
			break;
		}
		cursor.close();
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("GetUserInfoFromToken")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetUserInfoFromToken(@FormParam("tokenId") String tokenId){
		
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);	
		
		String userId = GetUserId(tokenId);
		
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);
		DBCursor cursor = users.find(query);
		
		System.out.println("There is "+ cursor.count() + " users!");
		
		JSONObject outputJsonObj = new JSONObject();
		while (cursor.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursor.next();
			String firstName = (String) data.get("firstName");
			String lastName = (String) data.get("lastName");
			String name = (String) data.get("name");
			String link = (String) data.get("link");
			outputJsonObj.put("firstName", firstName);
			outputJsonObj.put("lastName", lastName);
			outputJsonObj.put("name", name);
			outputJsonObj.put("link", link);
			outputJsonObj.put("userId", userId);
			break;
		}
		cursor.close();
		return outputJsonObj.toJSONString();
	}
	
	public boolean IsMember(String userId){	
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);	
		DBCursor cursor = users.find(query);
		if(cursor.count() == 0){
			return false;
		}
		cursor.close();
		return true;
	}
	
	@POST
	@Path("Logout")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public void POSTLogout(@FormParam("tokenId") String tokenId){
		DBCollection tokens = TrafficDB.getCollection("token");
		BasicDBObject query = new BasicDBObject();
		query.append("tokenId", tokenId);	
		DBCursor cursor = tokens.find(query);
		System.out.println("Number of result: " + cursor.size());
		while (cursor.hasNext()) {
			System.out.println("Find out tokenId");
			BasicDBObject data = (BasicDBObject) cursor.next();
			data.put("status", "FALSE");
			tokens.save(data);
		}
		cursor.close();
		System.out.println("Logout ok!");
	}
	
	@POST
	@Path("TestUserId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public void TestUserId(@FormParam("userId") String userId){
		DBCollection users = TrafficDB.getCollection("user");
		BasicDBObject query = new BasicDBObject();
		query.append("userId", userId);	
		DBCursor cursor = users.find(query);
		System.out.println("Number of result: " + cursor.size());
		while (cursor.hasNext()) {
			System.out.println("Find out userId");
			BasicDBObject data = (BasicDBObject) cursor.next();
			String tempUserId = (String) data.get("userId");
			System.out.println("Find out userId: " + tempUserId);
		}
		cursor.close();
		System.out.println("Finish!");
	}
}
