package ir.mod.tavana.toranj.services.broadcasting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BroadcastingDB {
	
	private Connection conn = null;

	public BroadcastingDB() {
		// create a connection
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			conn = java.sql.DriverManager
					.getConnection("jdbc:mysql://localhost/defadb?user=maghbooli&password=13654243/broadcasting_schema");
			// System.out.println("MYSQL connection is established");
		} catch (Exception e) {
			System.out.println("*startMySQL has problem:");
			System.out.println(e.getMessage());
		}
	}
	
	public void addBroadcastingRegisterer(String qid) {
		try {
			Statement stmt = conn.createStatement();
			String q = "INSERT registerers VALUES('" + qid + "')";
			stmt.executeUpdate(q);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean existsBroadcastingRegisterers(String qid){
		Statement stmt;
		boolean returnVal = false;
		try {
			stmt = conn.createStatement();
			String q = "SELECT * FROM registerers where reg_id = '"+ qid+"'";
			ResultSet rs = stmt.executeQuery(q);
			while (rs.next()) {
				returnVal = true;
				break;
			}
			stmt.close();
			return returnVal;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}