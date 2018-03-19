package raval.server;

import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Account {
	private String Username, Password, Password1, Name;

	public Account(String UN, String PassW, String PassW1, String NM) {
		Username = UN;
		Password = PassW;
		Password1 = PassW1;
		Name = NM;
	}

	public Account(String UN, String PassW) {
		Username = UN;
		Password = PassW;
	}

	public Account() {
	}

	public String signUp() {
		boolean done = !Username.equals("") && !Password.equals("");
		try {
			if (done) {
				DBConnection ToDB = new DBConnection(); // Have a connection to
														// the DB
				Connection DBConn = ToDB.openConn();
				Statement Stmt = DBConn.createStatement();
				String SQL_Command = "SELECT Username FROM Account WHERE Username ='" + Username + "'"; // SQL
																										// query
																										// command
				ResultSet Rslt = Stmt.executeQuery(SQL_Command); // Inquire if
																	// the
																	// username
																	// exsits.
				done = done && !Rslt.next();
				if (done) {
					SQL_Command = "INSERT INTO Account(Username, Password) VALUES ('" + Username + "','" + Password
							+ "')"; // Save the
									// username,
									// password and
									// Name
					System.out.println(SQL_Command);
					Stmt.executeUpdate(SQL_Command);
					Stmt.close();
					ToDB.closeConn();
					return Username;
				} else {
					Stmt.close();
					ToDB.closeConn();
					return "User already exists";
				}

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
			return "error";
		} catch (java.lang.Exception e) {
			done = false;
			System.out.println("Exception: " + e);
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

	public String signIn() {
		boolean done = !Username.equals("") && !Password.equals("");
		String Uname = "";
		try {
			if (done) {

				DBConnection ToDB = new DBConnection(); // Have a connection to
														// the DB
				Connection DBConn = ToDB.openConn();
				Statement Stmt = DBConn.createStatement();
				String SQL_Command = "SELECT Username FROM Account WHERE Username ='" + Username + "' and Password = '"
						+ Password + "'"; // SQL query command
				ResultSet Rslt = Stmt.executeQuery(SQL_Command);
				done = done && Rslt.next();
				if (done) {
					Uname = Rslt.getString("Username");
				}
				Stmt.close();
				ToDB.closeConn();
				return Uname;
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
		return Uname;
	}

	public boolean changePassword(String NewPassword) { // 5
		boolean done = false;
		try { // 20
			DBConnection ToDB = new DBConnection(); // Have a connection to the
													// DB
			Connection DBConn = ToDB.openConn();
			Statement Stmt = DBConn.createStatement();
			String SQL_Command = "SELECT * FROM Account WHERE Username ='" + Username + "'AND Password ='" + Password
					+ "'"; // SQL query command
			ResultSet Rslt = Stmt.executeQuery(SQL_Command); // Inquire if the
																// username
																// exsits.
			if (Rslt.next()) {
				SQL_Command = "UPDATE Account SET Password='" + NewPassword + "' WHERE Username ='" + Username + "'"; // Save
																														// the
																														// username,
																														// password
																														// and
																														// Name
				Stmt.executeUpdate(SQL_Command);
				Stmt.close();
				ToDB.closeConn();
				done = true;
			}
		} catch (java.sql.SQLException e) // 5
		{
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

	public Integer getTotalUser() {
		boolean done = !Username.equals("") && !Password.equals("");
		Integer count = 0;
		try {
			if (done) {

				DBConnection ToDB = new DBConnection(); // Have a connection to
														// the DB
				Connection DBConn = ToDB.openConn();
				Statement Stmt = DBConn.createStatement();
				String SQL_Command = "COUNT(Username) as total FROM Account";
				ResultSet Rslt = Stmt.executeQuery(SQL_Command);
				done = done && Rslt.next();
				if (done) {
					count = Rslt.getInt("total");
				}
				Stmt.close();
				ToDB.closeConn();
				return count;
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
		return count;
	}
}