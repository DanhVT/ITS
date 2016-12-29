package hcmut.its.ws;

import hcmut.its.algorithm.DistanceDijkstra;
import hcmut.its.filter.Filter;
import hcmut.its.filter.TruckFilter;
import hcmut.its.vehicle.Truck;
import hcmut.its.vehicle.TruckImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/truck/")
public class ServicesTruck {

	@GET
	@Path("distance_dijkstra/{srcLat}/{srcLng}/{srcEdge}/{dstLat}/{dstLng}/{dstEdge}/{time}/{weight}/{load}/{total}")
	@Produces(MediaType.APPLICATION_JSON)
	public String distanceDijkstra(@PathParam("srcLat") double lat1,
			@PathParam("srcLng") double lng1, @PathParam("srcEdge") String id1,
			@PathParam("dstLat") double lat2, @PathParam("dstLng") double lng2,
			@PathParam("dstEdge") String id2, @PathParam("time") int time,
			@PathParam("weight") double weight, @PathParam("load") double load,
			@PathParam("total") double totalLoad) {

		String id = "";
		String owner = "";

		Truck v = new TruckImpl(id, owner, weight, load, totalLoad);
		Filter filter = new TruckFilter(lat1, lng1, lat2, lng2, v);
		DistanceDijkstra algo = new DistanceDijkstra();
		return ServicesBase.route(algo, filter, time, lat1, lng1, id1, lat2,
				lng2, id2);
	}

}
