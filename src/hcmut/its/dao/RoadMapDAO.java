package hcmut.its.dao;

import hcmut.its.db.ITSDB;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Graph;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.SegmentInfo;
import hcmut.its.roadmap.Weight;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class RoadMapDAO {

	public static void build(Graph<Node, Segment, Weight> g) {
		DBCollection segmentColelction = ITSDB.getCollection("segments");
		DBCursor cursor = segmentColelction.find();
		while (cursor.hasNext()) {
			BasicDBObject segmentBson = (BasicDBObject) cursor.next();
			/**
			 * assert segmentBson != null : "segment is null";
			 */
			String id = segmentBson.getString("segment_id");
			/**
			 * create the DAO Obj for filling obj fields
			 */
			SegmentDAO dao = new SegmentDAO(id);
			Vertex<Node> src = dao.getSrc();
			Vertex<Node> tgt = dao.getTgt();
			Weight w = dao.getWeight();
			int x = dao.getCellX();
			int y = dao.getCellY();
			String type = dao.getStreetType();
			String name = dao.getStreetName();
			String streetId = dao.getStreetId();

			SegmentInfo info = GraphFacotry.createSegmentInfo(streetId, name,
					type);

			RulesDAO rdao = new RulesDAO(Integer.parseInt(id));
			
			if (!rdao.getRules().isEmpty())
				System.out.println("Not empty rules");

			info.setRules(rdao.getRules());

			int oneway = dao.getOneway();

			if (oneway == 1) {
				Edge<Node, Segment, Weight> e = GraphFacotry.createEdge(id, x,
						y, info, src, tgt, w);
				g.addEdge(e);

			} else {

				Edge<Node, Segment, Weight> e1 = GraphFacotry.createEdge(id, x,
						y, info, src, tgt, w);
				g.addEdge(e1);

				int cId = Integer.parseInt(id) + GraphFacotry.SEGMENT_COUNT;
				Edge<Node, Segment, Weight> e2 = GraphFacotry.createEdge(cId
						+ "", x, y, info, tgt, src, w);
				g.addEdge(e2);

			}
		}
	}

}
