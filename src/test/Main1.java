package test;

public class Main1 {

	public static void main(String[] args) {
		GenClass<GenData> o1 = new GenClass<GenData>(new GenData("1", "AAA"));
		GenClass<GenData> o2 = o1.clone();

		System.out.println(o1.equals(o2));
		System.out.println(o1 == o2);

		System.out.println(o1.v.equals(o2.v));
		System.out.println(o1.v == o2.v);

	}

}
