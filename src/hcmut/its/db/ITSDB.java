package hcmut.its.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class ITSDB {

	public static DB db;
	public static Boolean check = false;
	public static String DB_NAME = "its";
	public static String HOST = "localhost";
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
		if (ITSDB.check == false)
			ITSDB.openConnection();
		DB db = ITSDB.db;
		return db.getCollection(name);
	}

}