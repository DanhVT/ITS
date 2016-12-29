package hcmut.its.roadmap;

import hcmut.its.rule.Rule;
import hcmut.its.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class SegmentInfoImpl implements SegmentInfo {
	
	private String id;

	private String name;

	private String type;

	private boolean tunel;

	private boolean bridge;

	private List<Rule> rules;

	public SegmentInfoImpl(String id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		tunel = false;
		bridge = false;
		rules = new ArrayList<Rule>();
	}

	public SegmentInfoImpl(String id, String name, String type, boolean tunel,
			boolean bridge) {
		this(id, name, type);
		this.tunel = tunel;
		this.bridge = bridge;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public boolean isTunel() {
		return tunel;
	}

	@Override
	public boolean isBridge() {
		return bridge;
	}

	@Override
	public List<Rule> rules() {
		return rules;
	}

	@Override
	public boolean allows(Vehicle v, int time) {
		for (Rule r : rules()) {
			//System.out.println("Yeah");
			if (!r.allows(v, time))
				return false;
		}
		//System.out.println("Stupid");

		return true;
		//return false;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

}
