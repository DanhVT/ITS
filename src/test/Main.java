package test;

import hcmut.its.algorithm.DistanceDijkstra;
import hcmut.its.algorithm.DistanceLabel;
import hcmut.its.algorithm.ITSPath;
import hcmut.its.filter.Filter;
import hcmut.its.filter.MotorFilter;
import hcmut.its.graph.Edge;
import hcmut.its.graph.Vertex;
import hcmut.its.roadmap.GraphFacotry;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.RoadMap;
import hcmut.its.roadmap.RoadMapImpl;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

public class Main {

	public static void main(String[] args) {
		RoadMap g = new RoadMapImpl();
		g.build();

		String srcEdgeId = "100344";
		String dstEdgeId = "237557";

		double srcLat = 10.794356378625087;
		double srcLng = 106.65450096130371;
		double dstLat = 10.757341144831328;
		double dstLng = 106.67338371276855;

		ITSPath<DistanceLabel> p = null;

		if (g != null) {
			Filter filter = new MotorFilter(10.794356378625087,
					106.65450096130371, 10.757341144831328, 106.67338371276855);
			RoadMap gg = g.clone(filter, 100);// 100 is dump value

			System.out.println(g.vertices().size());
			System.out.println(g.edges().size());

			System.out.println(gg.vertices().size());
			System.out.println(gg.edges().size());

			Edge<Node, Segment, Weight> srcEdge = gg.getEdgeById(srcEdgeId);
			Edge<Node, Segment, Weight> dstEdge = gg.getEdgeById(dstEdgeId);

			int srcReId = Integer.parseInt(srcEdgeId)
					+ GraphFacotry.SEGMENT_COUNT;
			int dstReId = Integer.parseInt(dstEdgeId)
					+ GraphFacotry.SEGMENT_COUNT;

			Edge<Node, Segment, Weight> srcReverseEdge = gg.getEdgeById(srcReId
					+ "");
			Edge<Node, Segment, Weight> dstReverseEdge = gg.getEdgeById(dstReId
					+ "");

			int onewaySrc = 1;
			if (srcReverseEdge != null)
				onewaySrc = 0;
			int onewayDest = 1;
			if (dstReverseEdge != null)
				onewayDest = 0;

			Vertex<Node> src = gg.edgePartition(srcEdge, srcLat, srcLng,
					onewaySrc);
			Vertex<Node> dst = gg.edgePartition(dstEdge, dstLat, dstLng,
					onewayDest);

			DistanceDijkstra alg = new DistanceDijkstra();
			p = alg.search(gg, src, dst, 0);
		}

		p.print();
	}

}
