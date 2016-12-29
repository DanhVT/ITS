package hcmut.its.roadmap;

public interface Node {
	
	public String id();

	public double lat();

	public double lng();
	
	public String toString();

	public boolean equals(Object obj);
	
	
}
