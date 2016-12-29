package hcmut.its.dao;

import hcmut.its.db.ITSDB;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.SpeedUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class SegmentDAO {

	
	private final String id;
	private BasicDBObject segmentBson = null;
	private static final String SEGMENT_COLLECTION_NAME = "segments";

	public SegmentDAO(String id) {
		this.id = id;
		DBCollection segmentCollection = ITSDB
				.getCollection(SEGMENT_COLLECTION_NAME);
		segmentBson = (BasicDBObject) segmentCollection
				.findOne(new BasicDBObject("segment_id", id));

	}

	public Vertex<Node> getSrc() {
		Vertex<Node> v = GraphFacotry.createVertex(
				segmentBson.getString("src_id"),
				((BasicDBObject) segmentBson.get("src")).getDouble("lat"),
				((BasicDBObject) segmentBson.get("src")).getDouble("lng"));
		return v;
	}

	public Vertex<Node> getTgt() {
		Vertex<Node> v = GraphFacotry.createVertex(
				segmentBson.getString("tgt_id"),
				((BasicDBObject) segmentBson.get("tgt")).getDouble("lat"),
				((BasicDBObject) segmentBson.get("tgt")).getDouble("lng"));
		return v;
	}

	public double getDistance() {
		return segmentBson.getDouble("distance");
	}

	public double getDefaultSpeed() {
		return segmentBson.getDouble("default_speed");
	}

	public int getCellX() {
		return segmentBson.getInt("cell_x");
	}

	public int getCellY() {
		return segmentBson.getInt("cell_y");
	}

	public String getStreet() {
		return segmentBson.getString("street_id");
	}

	public int getOneway() {
		return segmentBson.getInt("oneway");
	}

	public int hasProfile() {
		return segmentBson.getInt("profile");

	}

	public String getStreetType() {
		return segmentBson.getString("street_type");
	}

	public String getStreetName() {
		return segmentBson.getString("street_name");
	}
	
	public String getStreetId() {
		return segmentBson.getString("street_id");
	}

	public Weight getWeight() {
		double[] profile = null;
		if (hasProfile() == 1) {
			profile = (new SpeedProfileDAO(id)).getSpeedProfile();

		}
		Weight w = GraphFacotry.createWeight(profile,
				segmentBson.getDouble("distance"),
				//segmentBson.getDouble("default_speed"),
				getSpeed(),
				segmentBson.getInt("profile"));
		return w;
	}
	
	private double getSpeed() {
		String streetType = getStreetType();
		String name = getStreetName();
		double speed = SpeedUtils.COMMON;
		if (streetType.equals("")){
			speed = SpeedUtils.UNNAMED;
		} else if (streetType.equals("highway")) {
			speed = SpeedUtils.HIGHWAY;
		} 
		if (name.contains("Háº»m")) {
			speed = SpeedUtils.ALLEY;
		}
		return speed;
	}
}
