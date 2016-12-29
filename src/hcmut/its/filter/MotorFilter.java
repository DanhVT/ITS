package hcmut.its.filter;

import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.MapUtils;

public class MotorFilter extends BaseFilter {

	public MotorFilter(double lat1, double lng1, double lat2, double lng2) {
		super(lat1, lng1, lat2, lng2);
		searchingBox = MapUtils.getSearchingBox(lat1, lng1, lat2, lng2);
	}

	@Override
	public boolean filter(Edge<Node, Segment, Weight> e, int time) {
		return MapUtils.isEdgeInSearchingBox(searchingBox, e);
	}

}
