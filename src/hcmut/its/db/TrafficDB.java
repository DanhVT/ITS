package hcmut.its.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class TrafficDB {

	public static DB db;
	public static Boolean check = false;
	public static String DB_NAME = "hcm_traffic";
	public static String HOST = "172.28.10.96";
	public static int PORT = 27017;

	private static void openConnection() {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(HOST, PORT);
			db = mongoClient.getDB(DB_NAME); 
			check = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static DBCollection getCollection(String name) {
		if (TrafficDB.check == false)
			TrafficDB.openConnection();
		DB db = TrafficDB.db;
		return db.getCollection(name);
	}

}