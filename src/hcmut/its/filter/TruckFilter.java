package hcmut.its.filter;

import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.MapUtils;
import hcmut.its.vehicle.Truck;

public class TruckFilter extends BaseFilter {

	protected Truck vehicle;

	/*
	public TruckFilter(double lat1, double lng1, double lat2, double lng2) {
		super(lat1, lng1, lat2, lng2);
		searchingBox = MapUtils.getSearchingBox(lat1, lng1, lat2, lng2);
		vehicle = VehicleFacotry.createDefaultTruck();
	}
	*/
	
	public TruckFilter(double lat1, double lng1, double lat2, double lng2, Truck v) {
		super(lat1, lng1, lat2, lng2);
		searchingBox = MapUtils.getSearchingBox(lat1, lng1, lat2, lng2);
		vehicle = v;
	}

	@Override
	public boolean filter(Edge<Node, Segment, Weight> e, int time) {
	
		return e.data().info().allows(vehicle, time)
				&& (MapUtils.isEdgeInSearchingBox(searchingBox, e) && !(e
						.data().info().type().equals("residential") && (e
						.data().info().name().equals("") || e.data().info()
						.name().contains("Háº»m"))));
					
	}

	public void setVehicle(Truck v) {
		vehicle = v;
	}

}
