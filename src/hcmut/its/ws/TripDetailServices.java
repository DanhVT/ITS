package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@Path("/tripdetails/")
public class TripDetailServices {
	// Thuc hien cac chuc nang them, chinh sua, xoa cac diem tren lo trinh
	// (pointId, tripId, x, y, z, pointDescription, order ,dateTime, dateTimeModify)
	@POST
	@Path("CreatePointOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String CreatePointOnTrip(@FormParam("tokenId") String tokenId,
			@FormParam("tripId") String tripId,
			@FormParam("x") String x,@FormParam("y") String y, @FormParam("z") String z,@FormParam("locationName") String locationName,
			@FormParam("pointDescription") String pointDescription,@FormParam("order") String order)
	{
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		
		DBCollection tripDetail = TrafficDB.getCollection("tripDetail");
		BasicDBObject newTripDetail = new BasicDBObject();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String date_time = dateFormat.format(date);
		String pointId = String.valueOf(UUID.randomUUID());
		
		newTripDetail.append("tripId", tripId).append("pointId", pointId)
		.append("x", x).append("y", y).append("z", z)
		.append("pointDescription", pointDescription).append("locationName", locationName)
		.append("order", order).append("dateTime", date_time).append("dateTimeModify", date_time);
		
		tripDetail.insert(newTripDetail);
		System.out.println("PointId:" + pointId);
		
		String code = "1";
		String description = "Create point on trip successful!";
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("code", code);
		outputJsonObj.put("description", description);
		outputJsonObj.put("pointId", pointId);
		return outputJsonObj.toJSONString();
	}
	@POST
	@Path("GetListPointOnTrip")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetListPointOnTrip(@FormParam("tripId") String tripId){
		// Lay sanh sach cac diem thuoc trip
		DBCollection tripDetails = TrafficDB.getCollection("tripDetail");
		BasicDBObject queryTripDetail = new BasicDBObject();
		queryTripDetail.append("tripId", tripId);
		DBCursor cursorTripDetail = tripDetails.find(queryTripDetail);
		String x = "";
		String y = "";
		String z = "";
		String pointDescription = "";
		String order = "";
		String pointId = "";
		String locationName="";

		JSONArray ja = new JSONArray();
		while (cursorTripDetail.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorTripDetail.next();
			pointId = (String) data.get("pointId");
			x = (String) data.get("x");
			y = (String) data.get("y");
			z = (String) data.get("z");
			if(x != null && y!= null & z != null){
				
				// SRATRT Kiem tra cai nay vi du lieu tu di dong gui len sai, sau nay khi gui dung thi ko can kiem tra nua - phai XOA
				double t_x = Double.parseDouble(x);
				double t_y = Double.parseDouble(y);
				
				if(t_x > 11){
					x = x.substring(0, 2) + "." + x.substring(2, x.length());
					System.out.println("x: "+ x);
				}
				if(t_y > 107){
					y = y.substring(0, 3) + "." + y.substring(3, y.length());
					System.out.println("y: "+ y);
				}
				// END kiem tra du lieu sai
				
				// Lay them danh sach hinh tren point
				//.........................
				UploadServices uploadImg = new UploadServices();
				
				ArrayList <String>listFile = uploadImg.GetUrlFileId(pointId);
				
				JSONArray jaImg = new JSONArray();
				for(int i = 0; i < listFile.size(); i++){
					JSONObject joImg = new JSONObject();
					String imageId = listFile.get(i).split(",")[0];
					String url = listFile.get(i).split(",")[1];
					String type = listFile.get(i).split(",")[2];
					
					joImg.put("imageId", imageId);
					joImg.put("url", url);
					joImg.put("type", type);
					
					jaImg.add(joImg);
				}
				
				//......................................................
				ArrayList <String>listRating = uploadImg.GetDataRateInPoint(pointId);
				JSONArray jaRate = new JSONArray();
				for(int i = 0; i < listFile.size(); i++){
					JSONObject joRate = new JSONObject();
					String rateId = listFile.get(i).split("::")[0];
					String rate = listFile.get(i).split("::")[1];
					String message = listFile.get(i).split("::")[2];
					
					joRate.put("rateId", rateId);
					joRate.put("rate", rate);
					joRate.put("message", message);
					
					jaRate.add(joRate);
				}
				
				
				pointDescription = (String) data.get("pointDescription");
				order = (String) data.get("order");
				locationName = (String) data.get("locationName");
				
				JSONObject jo = new JSONObject();
				jo.put("pointId", pointId);
				jo.put("tripId", tripId);
				jo.put("x", x);
				jo.put("y", y);
				jo.put("z", z);
				jo.put("pointDescription", pointDescription);
				jo.put("locationName", locationName);
				jo.put("order", order);
				jo.put("listFile", jaImg);
				jo.put("listRate", jaRate);
				
				// Put element into Json Array
				ja.add(jo);
			}
			
		}
		System.out.println("Number of point: "+ ja.size());
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("listPoint", ja);
		return outputJsonObj.toJSONString();
	}
}
