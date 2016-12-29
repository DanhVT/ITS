package hcmut.its.vehicle;

public class CarImpl implements Car{
	
	private String id;
	private String owner;
	private int seat;
	
	public CarImpl(String id, String owner, int seat) {
		super();
		this.id = id;
		this.owner = owner;
		this.seat = seat;
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
	public int seat() {
		return seat;
	}

}
