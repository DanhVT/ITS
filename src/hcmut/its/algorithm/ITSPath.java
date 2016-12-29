package hcmut.its.algorithm;

import hcmut.its.graph.Edge;
import hcmut.its.roadmap.Node;
import hcmut.its.roadmap.Segment;
import hcmut.its.roadmap.Weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ITSPath<L> {

	private Label<L> label = null;
	private List<Edge<Node, Segment, Weight>> path = new ArrayList<Edge<Node, Segment, Weight>>();

	public ITSPath() {
	}

	public ITSPath(Label<L> label) {
		this.label = label;
		// assert label != null;
		while (!label.isSource()) {
			path.add(label.edge());
			label = label.predecessor();
		}
		Collections.reverse(path);
	}

	public void setLabel(Label<L> label) {
		this.label = label;
		while (!label.isSource()) {
			path.add(label.edge());
			label = label.predecessor();
		}
		Collections.reverse(path);
	}

	public List<Edge<Node, Segment, Weight>> getPath() {
		return path;
	}

	public Label<L> getLabel() {
		return label;
	}

	public void print() {
		if (path.isEmpty())
			System.out.print("Cannot find the path.");
		for (Edge<Node, Segment, Weight> e : path) {
			System.out.print(e);
		}
		System.out.println();
	}

	public List<List<Edge<Node, Segment, Weight>>> split() {
		List<List<Edge<Node, Segment, Weight>>> list = new ArrayList<List<Edge<Node, Segment, Weight>>>();
		String previousStreetName = "null";
		int i = 0;
		for (Edge<Node, Segment, Weight> e : path) {
			String streetName = e.data().info().name();
			if (!streetName.equals(previousStreetName)) {
				List<Edge<Node, Segment, Weight>> segments = new ArrayList<Edge<Node, Segment, Weight>>();
				segments.add(e);
				list.add(i++, segments);
			} else {
				list.get(i - 1).add(e);
			}
			previousStreetName = streetName;
		}
		return list;
	}
}
