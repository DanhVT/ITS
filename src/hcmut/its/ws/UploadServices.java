package hcmut.its.ws;

import hcmut.its.db.TrafficDB;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


@Path("/upload/")

public class UploadServices {
	public static final String ROOT_PATH = System.getProperty("catalina.base") + File.separator + "webapps";
	public static final String FOLDER_PATH = "/uploadImage/";
	public static final String FULL_PATH = ROOT_PATH + File.separator + FOLDER_PATH;
	private static String codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	public static final String URL_WEB = "traffic.hcmut.edu.vn";
	
	@POST
	@Path("UploadTextRateToPoint")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String UploadTextRateToPoint(
			@FormParam("tokenId") String tokenId,
			@FormParam("pointId") String pointId,
			@FormParam("rate") String rate,
			@FormParam("message") String message){
		
		System.out.println("Have a request to rate");
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		String userId = usr.GetUserId(tokenId);
		try {
			String rateId = String.valueOf(UUID.randomUUID());
			DBCollection rateMaker = TrafficDB.getCollection("rateMaker");
			String status = "TRUE";	
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String timestamp = dateFormat.format(date);
	
	
			BasicDBObject newImage = new BasicDBObject();
			newImage.append("rateId", rateId)
			.append("userId", userId)
			.append("status", status)
			.append("timestamp", timestamp)
			.append("pointId", pointId)
			.append("rate",rate)
			.append("message", message);
			
			rateMaker.insert(newImage);	
			
			System.out.println("Insert token ok!");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("description", "Upload rating successful!");		
		outputJsonObj.put("status", "1");
		outputJsonObj.put("rate", rate);
		outputJsonObj.put("message", message);
		System.out.println("Upload sucess: "+ message);
		return outputJsonObj.toJSONString();		
	}
	
	
	@POST
	@Path("UploadImageToPoint")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String UploadImageToPoint(
			@FormParam("tokenId") String tokenId,
			@FormParam("pointId") String pointId, 
			@FormParam("dataImage") String encryptedFile, 
			@FormParam("filename") String filename,
			@FormParam("type") String type)
	{
		
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		String userId = usr.GetUserId(tokenId);
		
		System.out.println("File Input: " + encryptedFile);
		System.out.println("File name: " + filename);
		//System.out.println("File Name: " + filename);
    	
		
		if (encryptedFile.length() % 4 != 0)    {
            System.err.println("Invalid base64 encryptedImage");
            return null;
        }
        byte decoded[] = new byte[((encryptedFile.length() * 3) / 4) - (encryptedFile.indexOf('=') > 0 ? (encryptedFile.length() - encryptedFile.indexOf('=')) : 0)];
        char[] inChars = encryptedFile.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4)     {
            // This could be made faster (but more complicated) by precomputing these index locations
            b[0] = codes.indexOf(inChars[i]);
            b[1] = codes.indexOf(inChars[i + 1]);
            b[2] = codes.indexOf(inChars[i + 2]);
            b[3] = codes.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64)      {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64)  {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
	        }
            }
        }
        
        String outPutLink = "";
        
        try {
        	String storedFilename = String.valueOf(UUID.randomUUID());
        	String storedPathFilename = FULL_PATH + storedFilename + filename;
        	//convert array of bytes into file
    	    FileOutputStream fileOuputStream = 
                      new FileOutputStream(storedPathFilename); 
    	    fileOuputStream.write(decoded);
    	    fileOuputStream.close();
    	    
    	    outPutLink = FOLDER_PATH + storedFilename + filename;
        	System.out.println("Done: " + FOLDER_PATH + storedFilename + filename);
        	
        	// Luu vao image table
        	String imageId = String.valueOf(UUID.randomUUID());
        	DBCollection imagesCollection = TrafficDB.getCollection("imageMarker");
    		String status = "TRUE";

    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		String timestamp = dateFormat.format(date);


    		BasicDBObject newImage = new BasicDBObject();

    		newImage.append("imageId", imageId)
    				.append("userId", userId)
					.append("status", status)
					.append("timestamp", timestamp)
					.append("type", type)
					.append("pointId", pointId)
					.append("url",outPutLink);

    		imagesCollection.insert(newImage);
			
			System.out.println("Insert token ok!");
        	
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("description", "Upload successful!");
		outputJsonObj.put("status", "1");
		outputJsonObj.put("link",  outPutLink);
		return outputJsonObj.toJSONString();
	}
	
	@POST
	@Path("UploadImageWithLatLon")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String UploadImageWithLatLon(@FormParam("tokenId") String tokenId,@FormParam("tripId") String tripId, @FormParam("x") String x,@FormParam("y") String y, @FormParam("z") String z,@FormParam("locationName") String locationName, @FormParam("pointDescription") String pointDescription, @FormParam("order") String order, @FormParam("dataImage") String encryptedImage, @FormParam("filename") String filename){
		UserServices usr = new UserServices();
		// kiem tra valid token
		usr.CheckValidToken(tokenId);
		String userId = usr.GetUserId(tokenId);
		
		// Create Point
		TripDetailServices detailPoint = new TripDetailServices();
		String strDataPoint = detailPoint.CreatePointOnTrip(tokenId, tripId, x, y, z, locationName, pointDescription, order);
		JSONObject jsonObjPoint = (JSONObject)JSONValue.parse(strDataPoint);
		
		String pointId = (String)jsonObjPoint.get("pointId");
		
		// Upload Image
		System.out.println("File Input: " + encryptedImage);
		System.out.println("File name: " + filename);
		//System.out.println("File Name: " + filename);
    	
		
		if (encryptedImage.length() % 4 != 0)    {
            System.err.println("Invalid base64 encryptedImage");
            return null;
        }
        byte decoded[] = new byte[((encryptedImage.length() * 3) / 4) - (encryptedImage.indexOf('=') > 0 ? (encryptedImage.length() - encryptedImage.indexOf('=')) : 0)];
        char[] inChars = encryptedImage.toCharArray();
        int j = 0;
        int b[] = new int[4];
        for (int i = 0; i < inChars.length; i += 4)     {
            // This could be made faster (but more complicated) by precomputing these index locations
            b[0] = codes.indexOf(inChars[i]);
            b[1] = codes.indexOf(inChars[i + 1]);
            b[2] = codes.indexOf(inChars[i + 2]);
            b[3] = codes.indexOf(inChars[i + 3]);
            decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
            if (b[2] < 64)      {
                decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                if (b[3] < 64)  {
                    decoded[j++] = (byte) ((b[2] << 6) | b[3]);
	        }
            }
        }
        
        String outPutLink = "";
        
        try {
        	String storedFilename = String.valueOf(UUID.randomUUID());
        	String storedPathFilename = FULL_PATH + storedFilename + filename;
        	//convert array of bytes into file
    	    FileOutputStream fileOuputStream = 
                      new FileOutputStream(storedPathFilename); 
    	    fileOuputStream.write(decoded);
    	    fileOuputStream.close();
    	    
    	    outPutLink = FOLDER_PATH + storedFilename + filename;
        	System.out.println("Done: " + FOLDER_PATH + storedFilename + filename);
        	
        	// Luu vao image table
        	String imageId = String.valueOf(UUID.randomUUID());
        	DBCollection images = TrafficDB.getCollection("imageMarker");
    		String status = "TRUE";

    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		String timestamp = dateFormat.format(date);


    		BasicDBObject newImage = new BasicDBObject();

    		newImage.append("imageId", imageId)
    				.append("userId", userId)
					.append("status", status)
					.append("timestamp", timestamp)
					.append("pointId", pointId).append("url",outPutLink);

    		images.insert(newImage);
			
			System.out.println("Insert token ok!");
        	
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        JSONObject outputJsonObj = new JSONObject();
		outputJsonObj.put("description", "Upload successful!");
		outputJsonObj.put("status", "1");
		outputJsonObj.put("pointId",  pointId);
		outputJsonObj.put("link",  outPutLink);
		
		return outputJsonObj.toJSONString();
	
	}


	public ArrayList<String> GetUrlFileId(String pointId){
		ArrayList<String> listImage = new ArrayList<String>();
		
		DBCollection imageMarker = TrafficDB.getCollection("imageMarker");
		BasicDBObject queryImageMarker = new BasicDBObject();
		queryImageMarker.append("pointId", pointId);
		DBCursor cursorImageMarker = imageMarker.find(queryImageMarker);
		
		while (cursorImageMarker.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorImageMarker.next();
			String imageId = (String) data.get("imageId");
			//String url = URL_WEB + (String) data.get("url");
			String type = data.getString("type");
			String url = "";
			if(type == "3"){
				url = (String) data.get("data");
			} else{
				url = (String) data.get("url");
			}
		
			String item = imageId+"::"+url+"::"+type;
			listImage.add(item);
		}
		
		cursorImageMarker.close();
		return listImage;
	}
	public ArrayList<String> GetDataRateInPoint(String pointId){
		ArrayList<String> listRate = new ArrayList<String>();
		DBCollection rateMaker = TrafficDB.getCollection("rateMaker");
		BasicDBObject rateMarker = new BasicDBObject();
		rateMarker.append("pointId", pointId);
		DBCursor cursorRateMarker = rateMaker.find(rateMarker);
		while (cursorRateMarker.hasNext()) {
			BasicDBObject data = (BasicDBObject) cursorRateMarker.next();
			String rateId = (String) data.get("rateId");
			//String url = URL_WEB + (String) data.get("url");		
			String rate = (String) data.get("rate");
			String message = (String) data.get("message");
			String item = rateId+"::"+rate+"::"+message;
			listRate.add(item);
		}
		cursorRateMarker.close();
		return listRate;
		
	}
	
}
