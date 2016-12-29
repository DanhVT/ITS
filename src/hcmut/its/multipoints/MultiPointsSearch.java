package hcmut.its.multipoints;

import hcmut.its.algorithm.Algorithm;
import hcmut.its.algorithm.ITSPath;
import hcmut.its.algorithm.PreferenceLabel;
import hcmut.its.filter.Filter;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.RoadMap;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public class MultiPointsSearch {

	public static ITSPath<PreferenceLabel> route(
			final Algorithm<PreferenceLabel> algo, final Filter filter,
			int time, Query query, double initTime) {

		double lat1 = query.lat1;
		double lng1 = query.lng1;
		String id1 = query.id1;
		double lat2 = query.lat2;
		double lng2 = query.lng2;
		String id2 = query.id2;
		RoadMap g = GraphFacotry.getMapInstance();
		ITSPath<PreferenceLabel> p = null;

		if (g != null) {
			RoadMap gg = g.clone(filter, time);

			Edge<Node, Segment, Weight> e1 = gg.getEdgeById(id1);
			Edge<Node, Segment, Weight> e2 = gg.getEdgeById(id2);

			int e1ReverseId = Integer.parseInt(id1)
					+ GraphFacotry.SEGMENT_COUNT;
			int e2ReverseId = Integer.parseInt(id2)
					+ GraphFacotry.SEGMENT_COUNT;

			Edge<Node, Segment, Weight> e1Reverse = gg.getEdgeById(e1ReverseId
					+ "");
			Edge<Node, Segment, Weight> e2Reverse = gg.getEdgeById(e2ReverseId
					+ "");

			int oneway1 = 1;
			if (e1Reverse != null)
				oneway1 = 0;

			int oneway2 = 1;
			if (e2Reverse != null)
				oneway2 = 0;

			Vertex<Node> src = gg.edgePartition(e1, lat1, lng1, oneway1);
			Vertex<Node> dst = gg.edgePartition(e2, lat2, lng2, oneway2);

			p = algo.search(gg, src, dst, initTime);
		}
		return p;
	}
	
	public static ITSPath<PreferenceLabel> route(
			final Algorithm<PreferenceLabel> algo, final Filter filter,
			int time, double lat1, double lng1, String id1, double lat2, double lng2, String id2, double initTime) {

	
		RoadMap g = GraphFacotry.getMapInstance();
		ITSPath<PreferenceLabel> p = null;

		if (g != null) {
			RoadMap gg = g.clone(filter, time);

			Edge<Node, Segment, Weight> e1 = gg.getEdgeById(id1);
			Edge<Node, Segment, Weight> e2 = gg.getEdgeById(id2);

			int e1ReverseId = Integer.parseInt(id1)
					+ GraphFacotry.SEGMENT_COUNT;
			int e2ReverseId = Integer.parseInt(id2)
					+ GraphFacotry.SEGMENT_COUNT;

			Edge<Node, Segment, Weight> e1Reverse = gg.getEdgeById(e1ReverseId
					+ "");
			Edge<Node, Segment, Weight> e2Reverse = gg.getEdgeById(e2ReverseId
					+ "");

			int oneway1 = 1;
			if (e1Reverse != null)
				oneway1 = 0;

			int oneway2 = 1;
			if (e2Reverse != null)
				oneway2 = 0;

			Vertex<Node> src = gg.edgePartition(e1, lat1, lng1, oneway1);
			Vertex<Node> dst = gg.edgePartition(e2, lat2, lng2, oneway2);

			p = algo.search(gg, src, dst, initTime);
		}
		return p;
	}

	/*
	public static String route(
			List<Query> queries) {
		String rs = "";
		int initTime =  
		double t = initTime;
		for (Query q : queries) {
			 Algorithm<PreferenceLabel> algo;
			 Filter filter, 
			 int time, 
			ITSPath<PreferenceLabel> p = 
					MultiPointsSearch.route(algo, filter, time, q, t);
			rs += JSONUtils.getJSONOfTimePathMobileWithPreference(p)
					.toJSONString();
			t = p.getLabel().data().time();
		}
		return rs;

	}
	*/
}
