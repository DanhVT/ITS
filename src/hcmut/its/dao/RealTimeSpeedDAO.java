package hcmut.its.dao;

import hcmut.its.cache.RealTimeSpeedCache;
import hcmut.its.db.TrafficDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.QueryBuilder;

public class RealTimeSpeedDAO {

	private static final String SPEED_COLLECTION_NAME = "segmentspeed";

	public static double getRealTimeSpeedOf(String id, String date, int frame) {
		double sp = RealTimeSpeedCache.REAL_TIME_NOT_FOUND;
		DBCollection speedCollection = TrafficDB
				.getCollection(SPEED_COLLECTION_NAME);

		BasicDBObject idQuery = new BasicDBObject().append("segment_id",
				Integer.parseInt(id));
		BasicDBObject dateQuery = new BasicDBObject().append("date", date);
		BasicDBObject frameQuery = new BasicDBObject().append("frame", frame);
		QueryBuilder query = new QueryBuilder();
		query.and(idQuery, dateQuery, frameQuery);

		BasicDBObject speedBson = (BasicDBObject) speedCollection.findOne(query
				.get());
		if (speedBson != null)
			sp = speedBson.getDouble("speed");
		return sp;
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getRealTimeSpeedOf("525", "2014-03-24", 40)); }
	 */

}
