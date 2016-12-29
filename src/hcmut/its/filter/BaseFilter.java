package hcmut.its.filter;

import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;
import hcmut.its.util.MapUtils;

public abstract class BaseFilter implements Filter {

	protected int[] searchingBox;
	protected double[] srcBox;
	protected double[] destBox;

	public BaseFilter(double lat1, double lng1, double lat2, double lng2) {
		super();
		searchingBox = MapUtils.getSearchingBox(lat1, lng1, lat2, lng2);
		srcBox = MapUtils.getBox(lat1, lng1);
		destBox = MapUtils.getBox(lat2, lng2);
	}

	@Override
	public abstract boolean filter(Edge<Node, Segment, Weight> e, int time);
}
