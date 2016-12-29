package hcmut.its.dao;

import hcmut.its.db.ITSDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class NodeDAO {
	
	private BasicDBObject nodeBson = null;
	private static final String NODE_COLLECTION_NAME = "nodes";
	
	public NodeDAO(String id) {
		DBCollection nodeCollection = ITSDB.getCollection(NODE_COLLECTION_NAME);
		nodeBson = (BasicDBObject) nodeCollection
				.findOne(new BasicDBObject("node_id", id));	
		}
	
	public NodeDAO(BasicDBObject nodeBson) {
		this.nodeBson = nodeBson;
	}

	public double getLat() {
		return nodeBson.getDouble("lat");
	}

	public double getLng() {
		return nodeBson.getDouble("lng");
	}

	/*
	public static void main(String[] args) {
		VertexDAO dao = new VertexDAO("366367342");
		System.out.println(dao.getLat());
		System.out.println(dao.getLng());
	}
	*/

}
