package hcmut.its.util;

import hcmut.its.algorithm.DistanceLabel;
import hcmut.its.algorithm.ITSPath;
import hcmut.its.algorithm.PreferenceLabel;
import hcmut.its.algorithm.RealTimeDijkstra;
import hcmut.its.algorithm.TimeLabel;
import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class JSONUtils {

	@SuppressWarnings("unchecked")
	public static JSONObject getJSONOfSegments(
			List<Edge<Node, Segment, Weight>> edgeList) {
		JSONArray segmentJSON = new JSONArray();
		for (Edge<Node, Segment, Weight> e : edgeList) {
			JSONObject edgeJSON = new JSONObject();
			edgeJSON.put("lat1", e.source().data().lat());
			edgeJSON.put("lng1", e.source().data().lng());
			edgeJSON.put("lat2", e.target().data().lat());
			edgeJSON.put("lng2", e.target().data().lng());
			segmentJSON.add(edgeJSON);
		}
		JSONObject result = new JSONObject();
		result.put("segments", segmentJSON);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJSONOfPath(ITSPath<?> path) {
		List<Edge<Node, Segment, Weight>> edgeList = path.getPath();
		JSONArray pathJSON = new JSONArray();
		int i = 0;
		for (Edge<Node, Segment, Weight> e : edgeList) {

			if (i == edgeList.size() - 1) {
				JSONObject pointJSON = new JSONObject();
				pointJSON.put("lat", e.target().data().lat());
				pointJSON.put("lng", e.target().data().lng());
				pathJSON.add(pointJSON);
			} else {
				JSONObject pointJSON1 = new JSONObject();
				pointJSON1.put("lat", e.source().data().lat());
				pointJSON1.put("lng", e.source().data().lng());
				JSONObject pointJSON2 = new JSONObject();
				pointJSON2.put("lat", e.target().data().lat());
				pointJSON2.put("lng", e.target().data().lng());
				pathJSON.add(pointJSON1);
				pathJSON.add(pointJSON2);
			}
			i++;
		}
		JSONObject result = new JSONObject();
		result.put("path", pathJSON);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJSONOfTimePathMobile(ITSPath<TimeLabel> path) {
		double t = MapUtils.getCurrentTime();
		List<List<Edge<Node, Segment, Weight>>> edgeList = path.split();
		JSONArray pathJSON = new JSONArray();
		for (List<Edge<Node, Segment, Weight>> l : edgeList) {
			JSONObject streetJSON = new JSONObject();
			String name = l.get(0).data().info().name();
			String type = l.get(0).data().info().type();
			String streetId = l.get(0).data().info().id();
			JSONArray segmentJSON = new JSONArray();
			for (Edge<Node, Segment, Weight> e : l) {
				JSONObject edgeJSON = new JSONObject();
				edgeJSON.put("lat1", e.source().data().lat());
				edgeJSON.put("lng1", e.source().data().lng());
				edgeJSON.put("lat2", e.target().data().lat());
				edgeJSON.put("lng2", e.target().data().lng());
				// Type cast here
				edgeJSON.put("distance", ((Weight) e.weight()).distance());
				segmentJSON.add(edgeJSON);
				
				Weight w = e.weight();
				double newTime = w.distance()
						/ MapUtils.kph2Mpm(RealTimeDijkstra.getSpeedOf(e, t));
				
				t = t + newTime;
			}
			streetJSON.put("street_id", streetId);
			streetJSON.put("street_name", name);
			streetJSON.put("street_type", type);
			streetJSON.put("segments", segmentJSON);
			pathJSON.add(streetJSON);
		}
		JSONObject result = new JSONObject();
		result.put("path", pathJSON);
		result.put("distance", path.getLabel().data().distance());
		double time = path.getLabel().data().time();
		result.put("time", time);
		result.put("realtime", t);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJSONOfDistancePathMobile(
			ITSPath<DistanceLabel> path) {
		List<List<Edge<Node, Segment, Weight>>> edgeList = path.split();
		JSONArray pathJSON = new JSONArray();
		double t = MapUtils.getCurrentTime();
		for (List<Edge<Node, Segment, Weight>> l : edgeList) {
			JSONObject streetJSON = new JSONObject();
			String name = l.get(0).data().info().name();
			String streetId = l.get(0).data().info().id();
			String type = l.get(0).data().info().type();
			JSONArray segmentJSON = new JSONArray();
			for (Edge<Node, Segment, Weight> e : l) {
				JSONObject edgeJSON = new JSONObject();
				edgeJSON.put("lat1", e.source().data().lat());
				edgeJSON.put("lng1", e.source().data().lng());
				edgeJSON.put("lat2", e.target().data().lat());
				edgeJSON.put("lng2", e.target().data().lng());
				// Type cast here
				edgeJSON.put("distance", ((Weight) e.weight()).distance());
				segmentJSON.add(edgeJSON);
				
				
				Weight w = e.weight();
				double newTime = w.distance()
						/ MapUtils.kph2Mpm(RealTimeDijkstra.getSpeedOf(e, t));
				
				t = t + newTime;
			}
			streetJSON.put("street_id", streetId);
			streetJSON.put("street_name", name);
			streetJSON.put("street_type", type);
			streetJSON.put("segments", segmentJSON);
			pathJSON.add(streetJSON);
		}
		JSONObject result = new JSONObject();
		result.put("path", pathJSON);
		result.put("realtime", t);
		result.put("distance", path.getLabel().data().distance());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getJSONOfTimePathMobileWithPreference(ITSPath<PreferenceLabel> path) {
		double t = MapUtils.getCurrentTime();
		List<List<Edge<Node, Segment, Weight>>> edgeList = path.split();
		JSONArray pathJSON = new JSONArray();
		for (List<Edge<Node, Segment, Weight>> l : edgeList) {
			JSONObject streetJSON = new JSONObject();
			String name = l.get(0).data().info().name();
			String type = l.get(0).data().info().type();
			String streetId = l.get(0).data().info().id();
			JSONArray segmentJSON = new JSONArray();
			for (Edge<Node, Segment, Weight> e : l) {
				JSONObject edgeJSON = new JSONObject();
				edgeJSON.put("lat1", e.source().data().lat());
				edgeJSON.put("lng1", e.source().data().lng());
				edgeJSON.put("lat2", e.target().data().lat());
				edgeJSON.put("lng2", e.target().data().lng());
				// Type cast here
				edgeJSON.put("distance", ((Weight) e.weight()).distance());
				segmentJSON.add(edgeJSON);
				
				Weight w = e.weight();
				double newTime = w.distance()
						/ MapUtils.kph2Mpm(RealTimeDijkstra.getSpeedOf(e, t));
				
				t = t + newTime;
			}
			streetJSON.put("street_id", streetId);
			streetJSON.put("street_name", name);
			streetJSON.put("street_type", type);
			streetJSON.put("segments", segmentJSON);
			pathJSON.add(streetJSON);
		}
		JSONObject result = new JSONObject();
		result.put("path", pathJSON);
		result.put("distance", path.getLabel().data().distance());
		double time = path.getLabel().data().time();
		result.put("time", time);
		result.put("realtime", t);
		return result;
	}

}
