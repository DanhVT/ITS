package hcmut.its.roadmap;

public interface Segment {

	public String id();

	public int cellX();

	public int cellY();

	public SegmentInfo info();

	public String toString();

	public boolean equals(Object obj);

}
