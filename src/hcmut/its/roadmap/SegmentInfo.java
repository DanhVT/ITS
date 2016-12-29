package hcmut.its.roadmap;

import hcmut.its.rule.Rule;
import hcmut.its.vehicle.Vehicle;

import java.util.List;

public interface SegmentInfo {
	
	public String name();
	
	public String id();
	
	public String type();
	
	public boolean isTunel();
	
	public boolean isBridge();
	
	public List<Rule> rules();
	
	public void setRules(List<Rule> rules);
	
	public boolean allows(Vehicle v, int time);

}
