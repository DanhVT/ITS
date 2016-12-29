package hcmut.its.vehicle;

public class TruckImpl implements Truck {

	private String id;
	private String owner;
	private double weight;
	private double load;
	private double totalLoad;

	public TruckImpl(String id, String owner, double weight, double load,
			double totalLoad) {
		super();
		this.id = id;
		this.owner = owner;
		this.weight = weight;
		this.load = load;
		this.totalLoad = totalLoad;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String owner() {
		return owner;
	}

	@Override
	public double weight() {
		return weight;
	}

	@Override
	public double load() {
		return load;
	}

	@Override
	public double totalLoad() {
		return totalLoad;
	}

}
