package hcmut.its.ws;

import java.util.ArrayList;
import java.util.List;

import hcmut.its.db.PreferenceDumb;
import hcmut.its.filter.CarFilter;
import hcmut.its.filter.Filter;
import hcmut.its.graph.Edge;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.RoadMap;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.JSONUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

@Path("/")
public class ServicesCommon {

	@GET
	@Path("graph_context")
	@Produces("text/html")
	public String setGraphContext() {
		GraphFacotry.loadMapInstance();
		return "The graph context was set.";
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("segment_id/{lat}/{lng}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSegment(@PathParam("lat") double lat,
			@PathParam("lng") double lng) {
		RoadMap g = GraphFacotry.getMapInstance();
		String id = g.getSegment(lat, lng);
		JSONObject obj = new JSONObject();
		if (!id.equals("")) {
			System.out.println("Segment Id: " + id);
			obj.put("segment_id", id);
		}
		return obj.toJSONString();

	}

	@GET
	@Path("testuser")
	@Produces(MediaType.APPLICATION_JSON)
	public String testuser() {
		PreferenceDumb.build("testuser");
		return "OK";
	}
	
	@GET
	@Path("get_segments/{x}/{y}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSegmentsInCell(@PathParam("x") int x, @PathParam("y") int y) {
		RoadMap g = GraphFacotry.getMapInstance();
		List<Edge<Node, Segment, Weight>> segmemts = g.getSegmentsInCell(x, y);
		JSONObject segmentJSON = new JSONObject();
		segmentJSON = JSONUtils.getJSONOfSegments(segmemts);
		return segmentJSON.toJSONString();

	}

	@GET
	@Path("get_searching_box/{lat1}/{lng1}/{lat2}/{lng2}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSearchingBox(@PathParam("lat1") double lat1,
			@PathParam("lng1") double lng1, @PathParam("lat2") double lat2,
			@PathParam("lng2") double lng2) {
		RoadMap g = GraphFacotry.getMapInstance();
		Filter filter = new CarFilter(lat1, lng1, lat2, lng2);
		List<Edge<Node, Segment, Weight>> segmemts = new ArrayList<Edge<Node, Segment, Weight>>(
				g.clone(filter, 100).edges());
		JSONObject segmentJSON = new JSONObject();
		segmentJSON = JSONUtils.getJSONOfSegments(segmemts);
		return segmentJSON.toJSONString();

	}
}
