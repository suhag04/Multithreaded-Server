package raval.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Emails {
	private String Username, Password, Password1, Name, sender, receiver, subject, body, time;

	public Emails(String Username) {
		this.Username = Username;
	}

	public Emails(String sender, String receiver, String subject, String body) {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.body = body;
	}

	public List getSentEmail() {
		String SQL_Command = "";

		List<List> ls1 = new ArrayList<List>();
		Map<String, Object> mp = new HashMap<String, Object>();
		try {
			DBConnection ToDB = new DBConnection(); // Have a connection to
													// the DB
			Connection DBConn = ToDB.openConn();
			Statement Stmt = DBConn.createStatement();
			SQL_Command = " SELECT [id] as id " + "       ,[sender] as sender " + "       ,[receiver] as receiver "
					+ "       ,[subject] as subject" + "       ,[body] as body ,[time] as time "
					+ "   FROM [EmailSystem].[dbo].[emails] e " + "   where e.[sender] = '" + Username + "'; ";
			System.out.println(SQL_Command);
			ResultSet rs = Stmt.executeQuery(SQL_Command);
			while (rs.next()) {
				List<Object> ls = new ArrayList<Object>();
				// ls.add(rs.getLong("id"));
				ls.add(rs.getString("receiver"));
				ls.add(rs.getString("subject"));
				ls.add(rs.getString("body"));
				ls.add(rs.getString("time"));
				ls1.add(ls);
			}
			Stmt.close();
			ToDB.closeConn();
			return ls1;
		} catch (java.sql.SQLException e) {
			System.out.println("SQLException: " + e);
			while (e != null) {
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("Message: " + e.getMessage());
				System.out.println("Vendor: " + e.getErrorCode());
				e = e.getNextException();
				System.out.println("");
			}
		} catch (java.lang.Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
		return null;
	}

	public List getReceivedEmail() {
		String SQL_Command = "";
		List<List> ls1 = new ArrayList<List>();
		try {
			DBConnection ToDB = new DBConnection(); // Have a connection to
													// the DB
			Connection DBConn = ToDB.openConn();
			Statement Stmt = DBConn.createStatement();
			SQL_Command = " SELECT [id] as id " + "       ,[sender] as sender " + "       ,[receiver] as receiver "
					+ "       ,[subject] as subject" + "       ,[body] as body ,[time] as time "
					+ "   FROM [EmailSystem].[dbo].[emails] e " + "   where e.[receiver] = '" + Username + "'; ";
			System.out.println(SQL_Command);
			ResultSet rs = Stmt.executeQuery(SQL_Command);
			while (rs.next()) {
				List<Object> ls = new ArrayList<Object>();
				// ls.add(rs.getLong("id"));
				ls.add(rs.getString("sender"));
				ls.add(rs.getString("subject"));
				ls.add(rs.getString("body"));
				ls.add(rs.getString("time"));
				ls1.add(ls);
			}
			Stmt.close();
			ToDB.closeConn();
			return ls1;
		} catch (java.sql.SQLException e) {
			System.out.println("SQLException: " + e);
			while (e != null) {
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("Message: " + e.getMessage());
				System.out.println("Vendor: " + e.getErrorCode());
				e = e.getNextException();
				System.out.println("");
			}
		} catch (java.lang.Exception e) {
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertEmail() {
		boolean done = !sender.equals("") && !receiver.equals("");
		try {
			if (done) {
				DBConnection ToDB = new DBConnection(); // Have a connection to
														// the DB
				Date datetime = new Date();
				Connection DBConn = ToDB.openConn();
				Statement Stmt = DBConn.createStatement();
				String SQL_Command = "";
				if (done) {
					SQL_Command = "INSERT INTO emails(sender,receiver,subject,body,time) VALUES ('" + sender + "','"
							+ receiver + "','" + subject + "','" + body + "','" + datetime + "')"; // Save
					// the
					// username,
					// password and
					// Name
					System.out.println(SQL_Command);
					Stmt.executeUpdate(SQL_Command);
				}
				Stmt.close();
				ToDB.closeConn();
			}
		} catch (java.sql.SQLException e) {
			done = false;
			System.out.println("SQLException: " + e);
			while (e != null) {
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("Message: " + e.getMessage());
				System.out.println("Vendor: " + e.getErrorCode());
				e = e.getNextException();
				System.out.println("");
			}
		} catch (java.lang.Exception e) {
			done = false;
			System.out.println("Exception: " + e);
			e.printStackTrace();
		}
		return done;
	}
}
