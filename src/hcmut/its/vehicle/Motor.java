package hcmut.its.vehicle;

public class Motor implements Vehicle {

	private String id;
	private String owner;

	public Motor(String id, String owner) {
		super();
		this.id = id;
		this.owner = owner;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String owner() {
		return owner;
	}
}
