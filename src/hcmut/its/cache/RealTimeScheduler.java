package hcmut.its.cache;

import hcmut.its.db.TrafficDB;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class RealTimeScheduler implements Runnable {

	private static int PERIOD = 1; // minutes

	@Override
	public void run() {
		RealTimeSpeedCache cache = RealTimeSpeedCache.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -PERIOD);
		Date currentTime = calendar.getTime();
		DBCollection speedCollection = TrafficDB.getCollection("segmentspeed");
		BasicDBObject query = new BasicDBObject("_id", new BasicDBObject(
				"$gte", new ObjectId(currentTime)));
		DBCursor cursor = speedCollection.find(query);
		while (cursor.hasNext()) {
			BasicDBObject speedBSon = (BasicDBObject) cursor.next();
			int id = speedBSon.getInt("segment_id");
			//int sum = speedBSon.getInt("sum");
			double speed = speedBSon.getDouble("speed");
			cache.cacheRealTimeSpeedOf(id + "", speed);
		}
		//System.out.println("Cached realtime speed.");

	}

}
