package hcmut.its.db;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class StreetBuilder extends CollectionBuilder {

	public StreetBuilder() {
		super("streets");
	}

	@Override
	public void build(String name) {

		String URL = "jdbc:mysql://localhost:3306/truck";
		String USER = "root";
		String PASSWORD = "";
		String DRIVER = "com.mysql.jdbc.Driver";

		Connection con = null;
		try {
			Class.forName(DRIVER);

			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);

			DBCollection newCollection = ITSDB.getCollection(name);

			ResultSet rs1 = null;

			PreparedStatement rstm1 = (PreparedStatement) con
					.prepareStatement("SELECT * FROM ways");
			rs1 = (ResultSet) rstm1.executeQuery();

			while (rs1.next()) {

				DBObject newStreetBson = new BasicDBObject();
				String streetId = rs1.getString("id");
				String streetType = "";
				String streetName = "";
				String oneway = "no";
				double maxspeed = 30.0;

				ResultSet rs = null;

				PreparedStatement rstm = (PreparedStatement) con
						.prepareStatement("SELECT * FROM waytags WHERE id = ?");
				rstm.setString(1, streetId);
				rs = (ResultSet) rstm.executeQuery();

				while (rs.next()) {
					//streetId = rs.getString("id");
					String key = rs.getString("k");
					String value = rs.getString("v")
							.replaceAll("(\\r|\\n)", "");
					if (key.equals("oneway")) {
						oneway = value.trim();
					} else if (key.equals("maxspeed")) {
						try {
							maxspeed = Double.parseDouble(value.replaceAll(
									"(mph)", "").trim());
						} catch (NumberFormatException e) {
							System.out.println("stupid!");
							maxspeed = 40;
						}
					} else if (key.equals("highway")) {
						streetType = value.trim();
					} else if (key.equals("name")) {
						streetName = value;
					}
				}
				rs.close();

				if ((streetName.contains("Háº»m") || streetName.equals(""))
						&& streetType.equals("residential"))
					maxspeed = Math.min(15, maxspeed);

				// if (streetType.equals("service"))
				// maxspeed = 30.0;

				newStreetBson.put("street_id", streetId);
				newStreetBson.put("street_type", streetType);
				newStreetBson.put("street_name", streetName);
				newStreetBson.put("oneway", oneway);
				newStreetBson.put("maxspeed", maxspeed);

				newCollection.insert(newStreetBson);
				// System.out.println("Street " + newStreetBson.get("street_id")
				// + " is inserted.");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
		} finally {
			try {

				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void index(String name) {
		DBCollection coll = ITSDB.getCollection(name);
		coll.ensureIndex(new BasicDBObject("street_id", 1).append("unique",
				true));
	}

}
