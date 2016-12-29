package hcmut.its.db;

import hcmut.its.util.MapUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class SegmentBuilder extends CollectionBuilder {

	public SegmentBuilder() {
		super("segments");
	}

	public static BasicDBList orderedMerge(BasicDBList a, BasicDBList b) {
		/*
		 * if (a == null) a = new BasicDBList(); if (b == null) b = new
		 * BasicDBList();
		 */
		BasicDBList result = new BasicDBList();
		result.addAll(a);
		result.addAll(b);
		return result;
	}

	@Override
	public void build(String name) {

		String URL = "jdbc:mysql://localhost:3306/truck";
		String USER = "root";
		String PASSWORD = "";
		String DRIVER = "com.mysql.jdbc.Driver";

		int SEGMENT_COUNT = 200000;

		Connection con = null;
		try {
			Class.forName(DRIVER);

			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);

			DBCollection segmentCollection = ITSDB.getCollection("segments");

			ResultSet rs = null;

			PreparedStatement rstm = (PreparedStatement) con
					.prepareStatement("SELECT * FROM graph");
			rs = (ResultSet) rstm.executeQuery();

			while (rs.next()) {

				int segmentId = SEGMENT_COUNT++;
				String srcId = rs.getString("source");
				String tgtId = rs.getString("target");

				double srcLat = rs.getDouble("x1");
				double srcLng = rs.getDouble("y1");
				double tgtLat = rs.getDouble("x2");
				double tgtLng = rs.getDouble("y2");

				int cellX = MapUtils.getCellX(srcLat, srcLng);
				int cellY = MapUtils.getCellY(srcLat, srcLng);

				BasicDBObject segmentBson = new BasicDBObject();
				segmentBson.put("segment_id", segmentId + "");

				segmentBson.put("cell_x", cellX);
				segmentBson.put("cell_y", cellY);

				BasicDBObject srcBson1 = new BasicDBObject();
				srcBson1.put("lat", srcLat);
				srcBson1.put("lng", srcLng);

				segmentBson.put("src_id", srcId);
				segmentBson.put("src", srcBson1);

				BasicDBObject tgtBson1 = new BasicDBObject();
				tgtBson1.put("lat", tgtLat);
				tgtBson1.put("lng", tgtLng);
				segmentBson.put("tgt_id", tgtId);
				segmentBson.put("tgt", tgtBson1);

				String streetId = rs.getString("wayid");

				DBCollection streetCollection = ITSDB.getCollection("streets");

				DBObject streetBson = streetCollection
						.findOne(new BasicDBObject("street_id", streetId));

				int oneway = 0;

				String oneway1 = ((BasicDBObject) streetBson)
						.getString("oneway");

				if (oneway1.equals("yes"))
					oneway = 1;

				String streetType = ((BasicDBObject) streetBson)
						.getString("street_type");

				String streetName = ((BasicDBObject) streetBson)
						.getString("street_name");

				double streetSpeed = ((BasicDBObject) streetBson)
						.getDouble("maxspeed");

				segmentBson.put("profile", 0);

				segmentBson.put("oneway", oneway);

				segmentBson.put("street_id", streetId);

				segmentBson.put("street_type", streetType);

				segmentBson.put("street_name", streetName);

				segmentBson.put("distance",
						MapUtils.distance(srcLat, srcLng, tgtLat, tgtLng));

				segmentBson.put("default_speed", streetSpeed);

				segmentCollection.insert(segmentBson);

				System.out.println("Segment " + segmentId);

			}
			rs.close();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} finally {
			try {

				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void build22(String name) {

		DBCollection newSegmentCollection = ITSDB.getCollection(name);

		DBCollection nodeCollection = ITSDB.getCollection("nodes");

		DBCollection segmentCollection = TrafficDB.getCollection("segment");
		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {
			BasicDBObject segmentBson = (BasicDBObject) cursor.next();

			int segmentId = segmentBson.getInt("segment_id");
			String srcId = segmentBson.getString("node_id_start");
			String tgtId = segmentBson.getString("node_id_end");

			BasicDBObject srcBson = (BasicDBObject) nodeCollection
					.findOne(new BasicDBObject("node_id", srcId));
			BasicDBObject tgtBson = (BasicDBObject) nodeCollection
					.findOne(new BasicDBObject("node_id", tgtId));

			double srcLat = srcBson.getDouble("lat");
			double srcLng = srcBson.getDouble("lng");

			double tgtLat = tgtBson.getDouble("lat");
			double tgtLng = tgtBson.getDouble("lng");

			int cellX = MapUtils.getCellX(srcLat, srcLng);
			int cellY = MapUtils.getCellY(srcLat, srcLng);

			BasicDBObject newSegmentBson = new BasicDBObject();
			newSegmentBson.put("segment_id", segmentId + "");

			newSegmentBson.put("cell_x", cellX);
			newSegmentBson.put("cell_y", cellY);

			BasicDBObject srcBson1 = new BasicDBObject();
			srcBson1.put("lat", srcLat);
			srcBson1.put("lng", srcLng);

			newSegmentBson.put("src_id", srcId);
			newSegmentBson.put("src", srcBson1);

			BasicDBObject tgtBson1 = new BasicDBObject();
			tgtBson1.put("lat", tgtLat);
			tgtBson1.put("lng", tgtLng);
			newSegmentBson.put("tgt_id", tgtId);
			newSegmentBson.put("tgt", tgtBson1);

			String streetId = segmentBson.getString("street_id");

			DBCollection streetCollection = ITSDB.getCollection("streets");

			DBObject streetBson = streetCollection.findOne(new BasicDBObject(
					"street_id", streetId));

			int oneway = 0;

			String oneway1 = ((BasicDBObject) streetBson).getString("oneway");

			if (oneway1.equals("yes"))
				oneway = 1;

			String streetType = ((BasicDBObject) streetBson)
					.getString("street_type");

			String streetName = ((BasicDBObject) streetBson)
					.getString("street_name");

			double streetSpeed = ((BasicDBObject) streetBson)
					.getDouble("maxspeed");

			newSegmentBson.put("profile", 0);

			newSegmentBson.put("oneway", oneway);

			newSegmentBson.put("street_id", streetId);

			newSegmentBson.put("street_type", streetType);

			newSegmentBson.put("street_name", streetName);

			newSegmentBson.put("distance",
					MapUtils.distance(srcLat, srcLng, tgtLat, tgtLng));

			newSegmentBson.put("default_speed", streetSpeed);

			newSegmentCollection.insert(newSegmentBson);

			System.out.println("Segment " + segmentId);

		}

	}

	public void build11(String name) {

		int unknown = 0;

		DBCollection segmentCollection = TrafficDB.getCollection("segment");
		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {
			DBObject baseSegmentBson = cursor.next();
			DBCollection newSegmentCollection = ITSDB.getCollection(name);

			DBCollection segmentDetailCollection = TrafficDB
					.getCollection("segment_cell_details");
			DBObject segmentBson = segmentDetailCollection
					.findOne(new BasicDBObject("segment_id", baseSegmentBson
							.get("segment_id")));

			if (segmentBson == null) {
				System.out.println("Segment "
						+ baseSegmentBson.get("segment_id"));
				unknown++;

			} else {

				BasicDBObject newSegmentBson = new BasicDBObject();
				newSegmentBson.put("segment_id", segmentBson.get("segment_id")
						+ "");
				// newSegmentBson.put("cell_id", segmentBson.get("cell_id"));
				newSegmentBson.put("cell_x", segmentBson.get("cell_x"));
				newSegmentBson.put("cell_y", segmentBson.get("cell_y"));

				DBCollection nodeCollection = ITSDB.getCollection("nodes");

				double srcLat = (double) ((BasicDBObject) segmentBson
						.get("node_s")).get("node_lat");
				double srcLng = (double) ((BasicDBObject) segmentBson
						.get("node_s")).get("node_lon");

				BasicDBObject srcIdQuery1 = new BasicDBObject().append("lat",
						srcLat);
				BasicDBObject srcIdQuery2 = new BasicDBObject().append("lng",
						srcLng);
				QueryBuilder srcQuery = new QueryBuilder();
				srcQuery.and(srcIdQuery1, srcIdQuery2);

				BasicDBObject srcId = (BasicDBObject) nodeCollection
						.findOne(srcQuery.get());

				BasicDBObject srcBson = new BasicDBObject();
				srcBson.put("lat", srcLat);
				srcBson.put("lng", srcLng);
				// srcBson.put("in", srcId.getInt("in"));
				// srcBson.put("out", srcId.getInt("out"));
				newSegmentBson.put("src_id", srcId.get("node_id"));
				newSegmentBson.put("src", srcBson);

				double tgtLat = (double) ((BasicDBObject) segmentBson
						.get("node_e")).get("node_lat");
				double tgtLng = (double) ((BasicDBObject) segmentBson
						.get("node_e")).get("node_lon");

				BasicDBObject tgtIdQuery1 = new BasicDBObject().append("lat",
						tgtLat);
				BasicDBObject tgtIdQuery2 = new BasicDBObject().append("lng",
						tgtLng);
				QueryBuilder tgtQuery = new QueryBuilder();
				tgtQuery.and(tgtIdQuery1, tgtIdQuery2);

				BasicDBObject tgtId = (BasicDBObject) nodeCollection
						.findOne(tgtQuery.get());

				BasicDBObject tgtBson = new BasicDBObject();
				tgtBson.put("lat", tgtLat);
				tgtBson.put("lng", tgtLng);
				// tgtBson.put("in", tgtId.getInt("in"));
				// tgtBson.put("out", tgtId.getInt("out"));
				newSegmentBson.put("tgt_id", tgtId.get("node_id"));
				newSegmentBson.put("tgt", tgtBson);

				/*
				 * BasicDBObject streetBson = new BasicDBObject();
				 * streetBson.put("street_id", ((BasicDBObject) segmentBson
				 * .get("street")).get("street_id"));
				 * streetBson.put("street_type", ((BasicDBObject) segmentBson
				 * .get("street")).get("street_type"));
				 * newSegmentBson.put("street", streetBson);
				 */

				String streetId = ((BasicDBObject) segmentBson.get("street"))
						.getString("street_id");

				DBCollection streetCollection = TrafficDB
						.getCollection("street");

				DBObject streetBson = streetCollection
						.findOne(new BasicDBObject("street_id", streetId));

				String streetType = ((BasicDBObject) streetBson)
						.getString("street_type");

				newSegmentBson.put("street_id", streetId);

				newSegmentBson.put("street_type", streetType);

				// newSegmentBson.put("segment_type", "primary");

				newSegmentBson.put("distance",
						MapUtils.distance(srcLat, srcLng, tgtLat, tgtLng));

				newSegmentBson.put("default_speed", 40.0);

				/*
				 * BasicDBList subSegments = new BasicDBList();
				 * subSegments.add(segmentBson.get("segment_id"));
				 * newSegmentBson.put("sub_segments", subSegments);
				 */

				// System.out.println("Saving segment "
				// + newSegmentBson.getInt("segment_id"));

				newSegmentCollection.insert(newSegmentBson);

			}
		}

		System.out.println("Number of unknown segments: " + unknown);

	}

	public void updateProfile() {

		DBCollection segmentCollection = ITSDB.getCollection("segments");
		DBCollection speedProfileCollection = ITSDB
				.getCollection("speed_profiles");

		DBCursor cursor = speedProfileCollection.find();

		while (cursor.hasNext()) {

			BasicDBObject profileBson = (BasicDBObject) cursor.next();

			BasicDBObject newDocument = new BasicDBObject();
			newDocument
					.append("$set", new BasicDBObject().append("profile", 1));

			BasicDBObject searchQuery = new BasicDBObject().append(
					"segment_id", profileBson.getString("segment_id"));

			segmentCollection.update(searchQuery, newDocument);
		}

	}

	public void updateId() {
		
		DBCollection segmentCollection = ITSDB.getCollection("segments");
		DBCollection tgtSegmentCollection = TrafficDB.getCollection("segment");

		DBCursor cursor = segmentCollection.find();

		while (cursor.hasNext()) {
			BasicDBObject segmentBson = (BasicDBObject) cursor.next();

			BasicDBObject query = new BasicDBObject().append("node_id_start",
					segmentBson.get("src_id")).append("node_id_end",
					segmentBson.get("tgt_id"));

			BasicDBObject tgtDocument = (BasicDBObject) tgtSegmentCollection
					.findOne(query);

			if (tgtDocument != null) {
				String newId = tgtDocument.getInt("segment_id") + "";

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set",
						new BasicDBObject().append("segment_id", newId));

				BasicDBObject searchQuery = new BasicDBObject().append(
						"segment_id", segmentBson.getString("segment_id"));

				segmentCollection.update(searchQuery, newDocument);
				
				System.out.println("Segment: " + newId);

			}
		}
	}

	public void updateWay() {

		String URL = "jdbc:mysql://localhost:3306/roadmap";
		String USER = "root";
		String PASSWORD = "";
		String DRIVER = "com.mysql.jdbc.Driver";

		Connection con = null;
		try {
			Class.forName(DRIVER);

			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);

			DBCollection segmentCollection = ITSDB.getCollection("segments");
			DBCursor cursor = segmentCollection.find();

			while (cursor.hasNext()) {
				DBObject segmentBson = cursor.next();
				String srcId = (String) segmentBson.get("src_id");
				String tgtId = (String) segmentBson.get("tgt_id");

				ResultSet rs = null;

				PreparedStatement rstm = (PreparedStatement) con
						.prepareStatement("SELECT * FROM graph WHERE source = ? and target = ?");
				rstm.setDouble(1, new Double(srcId));
				rstm.setDouble(2, new Double(tgtId));

				rs = (ResultSet) rstm.executeQuery();

				while (rs.next()) {
					int oneway = rs.getInt("oneway");
					// int oneway1 = (int) segmentBson.get("oneway");
					if (oneway == 1) {
						BasicDBObject newDocument = new BasicDBObject();
						newDocument.append("$set",
								new BasicDBObject().append("oneway", 1));
						System.out.println(segmentBson.get("segment_id")
								+ " is set");

						BasicDBObject searchQuery = new BasicDBObject().append(
								"segment_id", segmentBson.get("segment_id"));

						segmentCollection.update(searchQuery, newDocument);
					}

				}
				rs.close();
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} finally {
			try {

				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void index(String name) {
		DBCollection coll = ITSDB.getCollection(name);
		coll.ensureIndex(new BasicDBObject("segment_id", 1).append("unique",
				true));
		coll.ensureIndex(new BasicDBObject("cell_x", 1).append("cell_y", 1));
		coll.ensureIndex(new BasicDBObject("src_id", 1));
		coll.ensureIndex(new BasicDBObject("tgt_id", 1));
	}

}
