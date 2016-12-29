package hcmut.its.ws;

import hcmut.its.db.NodeBuilder;
import hcmut.its.db.SegmentBuilder;
import hcmut.its.db.SpeedProfileBuilder;
import hcmut.its.roadmap.RoadMap;
import hcmut.its.roadmap.RoadMapImpl;
import hcmut.its.util.DBUtils;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/db/")
public class ServicesDB {

	@Context
	ServletContext context;

	@GET
	@Produces("text/html")
	@Path("speed_profiles/{date}/{number}")
	public String speedprofiles(@PathParam("date") String d,
			@PathParam("number") int n) {
		long lStartTime = new Date().getTime();
		SpeedProfileBuilder speedProfileCollection = new SpeedProfileBuilder(
				DBUtils.getPreviousDaysOf(d, n));
		speedProfileCollection.build();

		RoadMap g = new RoadMapImpl();
		context.setAttribute("graph", g);
		System.out.println("The new context was create.");

		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		return "The \"speed_profiles\" collection for the date " + d
				+ " was created. Time elapsed: " + difference + " ms";
	}

	@GET
	@Produces("text/html")
	@Path("nodes")
	public String nodes() {
		long lStartTime = new Date().getTime();
		NodeBuilder nodeCollection = new NodeBuilder();
		nodeCollection.build();
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		return "The \"nodes\" collectoin was created. Time elapsed: "
				+ difference + " ms";
	}

	@GET
	@Produces("text/html")
	@Path("segments")
	public String segments() {
		long lStartTime = new Date().getTime();
		SegmentBuilder segmentCollection = new SegmentBuilder();
		segmentCollection.build();
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		return "The \"segments\" collectoin was created. Time elapsed: "
				+ difference + " ms";
	}

}
