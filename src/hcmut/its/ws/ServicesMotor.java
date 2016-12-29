package hcmut.its.ws;

import hcmut.its.algorithm.DistanceDijkstra;
import hcmut.its.algorithm.ITSPath;
import hcmut.its.algorithm.PreferenceLabel;
import hcmut.its.algorithm.ProfileDijkstra;
import hcmut.its.algorithm.RealTimeDijkstra;
import hcmut.its.algorithm.RealTimeDijkstraWithPreferences;
import hcmut.its.algorithm.StaticTimeDijkstra;
import hcmut.its.filter.Filter;
import hcmut.its.filter.MotorFilter;
import hcmut.its.multipoints.MultiPointsSearch;
import hcmut.its.multipoints.Query;
import hcmut.its.util.JSONUtils;
import hcmut.its.util.MapUtils;

import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/motor/")
public class ServicesMotor {

	@GET
	@Path("distance_dijkstra/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String distanceDijkstra(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		DistanceDijkstra algo = new DistanceDijkstra();
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("static_time_dijkstra/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String staticTimeDijkstra(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		StaticTimeDijkstra algo = new StaticTimeDijkstra();
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("profile_time_dijkstra/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String profileTimeDijkstra(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		ProfileDijkstra algo = new ProfileDijkstra();
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("real_time_dijkstra/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String realTimeDijkstra(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		RealTimeDijkstra algo = new RealTimeDijkstra();
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("preference_real_time_dijkstra/{user}/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String realTimeDijkstraWithPreference(
			@PathParam("user") String user, @PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
				user);
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("distance_dijkstra1/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String distanceDijkstra1(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		DistanceDijkstra algo = new DistanceDijkstra();
		return ServicesBase.route1(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("static_time_dijkstra1/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String staticTimeDijkstra1(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dumb value
		StaticTimeDijkstra algo = new StaticTimeDijkstra();
		return ServicesBase.route2(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("profile_time_dijkstra1/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String profileTimeDijkstra1(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dumb value
		ProfileDijkstra algo = new ProfileDijkstra();
		return ServicesBase.route2(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	@GET
	@Path("real_time_dijkstra1/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String realTimeDijkstra1(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dump value
		RealTimeDijkstra algo = new RealTimeDijkstra();
		return ServicesBase.route2(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	/*
	 * with preference
	 */
	@GET
	@Path("preference_real_time_dijkstra1/{user}/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}")
	@Produces(MediaType.APPLICATION_JSON)
	public String realTimeDijkstraWithPreference1(
			@PathParam("user") String user, @PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2) {
		Filter filter = new MotorFilter(lat1, lng1, lat2, lng2);
		int time = 100; // 100 is dumb value
		RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
				user);
		return ServicesBase.route3(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

	/*
	 * multiple points
	 * example parameters: [
		{lat=10.796379859206985,lng=106.6732120513916,id=89355},
	   {lat=10.784154456566771,lng=106.66226863861084,id=1935},
	   {lat=10.753293645253464,lng=106.65407180786133,id=86877}]

	 * ]
	 */
	
	/*
	 * http://localhost:8080/ITS/rest/motor/multiple_points/testuser/[{lat=10.796379859206985,lng=106.6732120513916,id=89355},%20%7Blat=10.784154456566771,lng=106.66226863861084,id=1935%7D,%20%7Blat=10.753293645253464,lng=106.65407180786133,id=86877%7D]
	 */
	
	@GET
	@Path("multiple_points/{user}/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String mulitpointSearch(@PathParam("user") String user,
			@PathParam("param") String URIJSON) {
		
		//parameters = "[{\"lat\":10.796379859206985,\"lng\":106.6732120513916,\"id\":\"89355\"},{\"lat\":10.784154456566771,\"lng\":106.66226863861084,\"id\":\"1935\"},{\"lat\":10.753293645253464,\"lng\":106.65407180786133,\"id\":\"86877\"} ]";
		
		@SuppressWarnings("deprecation")
		String parameters = URLDecoder.decode(URIJSON);
		List<Query> queries = Query.queries(parameters);
				
		String rs = "[";

		
		//List<Query> queries = new ArrayList<>();
		//queries.add(new Query(10.796379859206985, 106.6732120513916, "89355", 10.784154456566771, 106.66226863861084, "1935"));
		//queries.add(new Query( 10.784154456566771, 106.66226863861084, "1935", 10.753293645253464, 106.65407180786133, "86877"));
		
		int initTime = MapUtils.getCurrentTime();
		
		//int initTime = 60*7;

		double t = initTime;
		for (Query q : queries) {

			RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
					user);
			Filter filter = new MotorFilter(q.lat1, q.lng1, q.lat2, q.lng2);
			int time = 100; // dumb value,
			ITSPath<PreferenceLabel> p = MultiPointsSearch.route(algo, filter,
					time, q, t);
			//mismatch in JSONArray 
			t = p.getLabel().data().time();
			String sep = "";
			if (!rs.equals("["))
				sep = ",";
			rs = rs + sep + JSONUtils.getJSONOfTimePathMobileWithPreference(p)
					.toJSONString();
		}
		
		
		/*
		Query q = new Query(10.796379859206985,106.6732120513916,"89355",10.753293645253464,106.65407180786133,"86877");
		int initTime = MapUtils.getCurrentTime();
		
		//double lat1 = 10.796379859206985;
		//double lng1 = 106.6732120513916;
		//String id1 = "89355";
		//double lat2 = 10.753293645253464;
		//double lng2 = 106.65407180786133;
		//String id2 = "86877";
		RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
				user);
		Filter filter = new MotorFilter(q.lat1, q.lng1, q.lat2, q.lng2);
		int time = 100; // dump value,
		ITSPath<PreferenceLabel> p = MultiPointsSearch.route(algo, filter,
				time, q, initTime);
		//mismatch in JSONArray 
		/*rs = rs + JSONUtils.getJSONOfTimePathMobileWithPreference(p)
				.toJSONString();
				*/
		
		return rs + "]";

	}
	
	@POST
	@Path("multiple_pointsPOST")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String multiplePointsPOST(@FormParam("user") String user,
			@FormParam("param") String URIJSON) {
		
		//parameters = "[{\"lat\":10.796379859206985,\"lng\":106.6732120513916,\"id\":\"89355\"},{\"lat\":10.784154456566771,\"lng\":106.66226863861084,\"id\":\"1935\"},{\"lat\":10.753293645253464,\"lng\":106.65407180786133,\"id\":\"86877\"} ]";
		
		System.out.println("user is: " + user);		
		System.out.println("urijson is: " + URIJSON);
		
		@SuppressWarnings("deprecation")
		String parameters = URLDecoder.decode(URIJSON);
		List<Query> queries = Query.queries(parameters);
				
		String rs = "[";

		
		//List<Query> queries = new ArrayList<>();
		//queries.add(new Query(10.796379859206985, 106.6732120513916, "89355", 10.784154456566771, 106.66226863861084, "1935"));
		//queries.add(new Query( 10.784154456566771, 106.66226863861084, "1935", 10.753293645253464, 106.65407180786133, "86877"));
		
		int initTime = MapUtils.getCurrentTime();
		
		//int initTime = 60*7;

		double t = initTime;
		for (Query q : queries) {

			RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
					user);
			Filter filter = new MotorFilter(q.lat1, q.lng1, q.lat2, q.lng2);
			int time = 100; // dumb value,
			ITSPath<PreferenceLabel> p = MultiPointsSearch.route(algo, filter,
					time, q, t);
			//mismatch in JSONArray 
			t = p.getLabel().data().time();
			String sep = "";
			if (!rs.equals("["))
				sep = ",";
			rs = rs + sep + JSONUtils.getJSONOfTimePathMobileWithPreference(p)
					.toJSONString();
		}
		
		
		/*
		Query q = new Query(10.796379859206985,106.6732120513916,"89355",10.753293645253464,106.65407180786133,"86877");
		int initTime = MapUtils.getCurrentTime();
		
		//double lat1 = 10.796379859206985;
		//double lng1 = 106.6732120513916;
		//String id1 = "89355";
		//double lat2 = 10.753293645253464;
		//double lng2 = 106.65407180786133;
		//String id2 = "86877";
		RealTimeDijkstraWithPreferences algo = new RealTimeDijkstraWithPreferences(
				user);
		Filter filter = new MotorFilter(q.lat1, q.lng1, q.lat2, q.lng2);
		int time = 100; // dump value,
		ITSPath<PreferenceLabel> p = MultiPointsSearch.route(algo, filter,
				time, q, initTime);
		//mismatch in JSONArray 
		/*rs = rs + JSONUtils.getJSONOfTimePathMobileWithPreference(p)
				.toJSONString();
				*/
		
		return rs + "]";

	}
}
