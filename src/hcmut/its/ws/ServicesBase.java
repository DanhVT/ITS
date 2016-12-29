package hcmut.its.ws;

import hcmut.its.algorithm.Algorithm;
import hcmut.its.algorithm.DistanceLabel;
import hcmut.its.algorithm.ITSPath;
import hcmut.its.algorithm.PreferenceLabel;
import hcmut.its.algorithm.TimeLabel;
import hcmut.its.filter.Filter;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.RoadMap;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.JSONUtils;
import hcmut.its.util.MapUtils;

import org.json.simple.JSONObject;

public final class ServicesBase {

	public static <L> String route(final Algorithm<L> algo,
			final Filter filter, int time, double lat1, double lng1,
			String id1, double lat2, double lng2, String id2) {

		RoadMap g = GraphFacotry.getMapInstance();
		JSONObject pathJSON = new JSONObject();

		if (g != null) {
			RoadMap gg = g.clone(filter, time);

			Edge<Node, Segment, Weight> e1 = gg.getEdgeById(id1);
			Edge<Node, Segment, Weight> e2 = gg.getEdgeById(id2);
			
			if (e1 == null || e2 == null) {
				return "{\"path\": \"null\"}";
			}

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

			int current = MapUtils.getCurrentTime();
			ITSPath<L> p = algo.search(gg, src, dst, current);

			pathJSON = JSONUtils.getJSONOfPath(p);
		}

		return pathJSON.toJSONString();
	}

	public static String route1(final Algorithm<DistanceLabel> algo,
			final Filter filter, int time, double lat1, double lng1,
			String id1, double lat2, double lng2, String id2) {

		RoadMap g = GraphFacotry.getMapInstance();
		JSONObject pathJSON = new JSONObject();

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

			int current = MapUtils.getCurrentTime();
			ITSPath<DistanceLabel> p = algo.search(gg, src, dst, current);

			pathJSON = JSONUtils.getJSONOfDistancePathMobile(p);
		}

		return pathJSON.toJSONString();
	}

	public static String route2(final Algorithm<TimeLabel> algo,
			final Filter filter, int time, double lat1, double lng1,
			String id1, double lat2, double lng2, String id2) {

		RoadMap g = GraphFacotry.getMapInstance();
		JSONObject pathJSON = new JSONObject();

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

			int current = MapUtils.getCurrentTime();
			ITSPath<TimeLabel> p = algo.search(gg, src, dst, current);

			pathJSON = JSONUtils.getJSONOfTimePathMobile(p);
		}

		return pathJSON.toJSONString();
	}
	
	/*
	 * for preference
	 */
	public static String route3(final Algorithm<PreferenceLabel> algo,
			final Filter filter, int time, double lat1, double lng1,
			String id1, double lat2, double lng2, String id2) {

		RoadMap g = GraphFacotry.getMapInstance();
		JSONObject pathJSON = new JSONObject();

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

			int current = MapUtils.getCurrentTime();
			ITSPath<PreferenceLabel> p = algo.search(gg, src, dst, current);

			pathJSON = JSONUtils.getJSONOfTimePathMobileWithPreference(p);
		}

		return pathJSON.toJSONString();
	}


}
