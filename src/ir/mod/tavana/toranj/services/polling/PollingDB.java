package ir.mod.tavana.toranj.services.polling;

import ir.mod.tavana.toranj.entities.PollBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class PollingDB {

	private Connection conn = null;
	private SessionFactory factory;

	public PollingDB() {
		// create a connection
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			conn = java.sql.DriverManager
					.getConnection("jdbc:mysql://localhost/test?user=hamid&password=&wait_timeout=2147450");
			// System.out.println("MYSQL connection is established");
		} catch (Exception e) {
			System.out.println("*startMySQL has problem:");
			System.out.println(e.getMessage());
		}
		
/*		factory = new AnnotationConfiguration().
                configure().
                addAnnotatedClass(ir.mod.tavana.toranj.entities.PollBean.class).
                buildSessionFactory();
*/	}

	public int addPollingQuestion(String title, int status, String scope, double timeout, String options) {
		
		/*org.hibernate.classic.Session openSession = factory.openSession();
		Transaction tx = null;
		
		tx = openSession.beginTransaction();
		PollBean pollBean = new PollBean();*/
		
		
		int qid = -1;
		try {
			Statement stmt = conn.createStatement();
			String q = "INSERT polling_questions VALUES(NULL,'" + title + "'," + status + ",'" + scope + "',"
					+ timeout + ",'" + options + "')";
			stmt.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				qid = rs.getInt(1);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qid;
	}

	public void addPollingAnswer(int qid, String userID, String answers) {
		try {
			Statement stmt = conn.createStatement();
			String q = "INSERT polling_answers VALUES(NULL," + qid + ",'" + userID + "','" + answers + "')";
			stmt.executeUpdate(q);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePollingQuestion(int qid, int status) {
		try {
			Statement stmt = conn.createStatement();
			String q = "UPDATE polling_questions SET status=" + status + " WHERE ID=" + qid;
			stmt.executeUpdate(q);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPollingQuestion(String qid) {
		StringBuffer buffer = new StringBuffer();
		try {
			Statement stmt = conn.createStatement();
			String q = "SELECT polling_questions.ID, Title, Status, Scope, TimeOut, Options, UserID, answer FROM polling_questions INNER JOIN polling_answers ON polling_questions.ID=QID ";
			if (!qid.equals("*"))
				q += "where polling_questions.ID = " + qid;
			ResultSet rs = stmt.executeQuery(q);
			double[] vals = null;
			int curr_qid = -1;
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getNString(2);
				int status = rs.getInt(3);
				String scope = rs.getNString(4);
				double timeout = rs.getDouble(5);
				String options = rs.getNString(6);
				String userID = rs.getNString(7);
				String answers = rs.getNString(8);
				String[] options_array = options.split("\\s*;\\s*");
				String[] answers_array = answers.split("\\s*;\\s*");
				if (curr_qid != id) {
					if (curr_qid != -1) {
						buffer.append("\n-------summary-------\n");
						for (int i = 0; i < vals.length; i++) {
							buffer.append("\t" + vals[i] + "\n");
						}
					}
					vals = new double[answers_array.length];
					curr_qid = id;
				}
				for (int i = 0; i < answers_array.length; i++) {
					double val = Double.parseDouble(answers_array[i]);
					vals[i] += (val / answers_array.length);
				}

				buffer.append("\n");
				buffer.append("\ttitle: " + title + "\n");
				buffer.append("\tstatus: " + status + "\n");
				buffer.append("\tscope: " + scope + "\n");
				buffer.append("\ttimeout: " + timeout + "\n");
				buffer.append("\toptions: " + options + "\n");
				buffer.append("\tuserID: " + userID + "\n");
				buffer.append("\tanswers: " + answers + "\n");
				buffer.append("-----------------------");
				buffer.append("\n");
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public void shutdown() throws SQLException {
		conn.close();
	}

	public static void main(String[] args) throws Exception {
		PollingDB p = new PollingDB();
		// int qid = p.addPollingQuestion("aaa", 0, "sdsd", 10.2, "options");
		// p.updatePollingQuestion(qid, 1);
		p.getPollingQuestion("1");
		p.shutdown();
	}
}
