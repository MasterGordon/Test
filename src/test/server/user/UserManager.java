package test.server.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import test.server.TestServer;
import test.util.Packet;

public class UserManager {
	private TestServer server;
	private Connection connection;
	private HashMap<String, User> userIP = new HashMap<String, User>();

	public UserManager(TestServer server) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + server.getPath() + "/users.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Verbindung zur Datenbank konnte nicht hergestellt werden!\nServer wird beendet...");
			System.exit(0);
		}
		this.server = server;
		String createUsers = "create table if not exists users ( `username` TEXT NOT NULL , `password` TEXT NOT NULL , `punkte` INT NOT NULL , `testsdone` TEXT NOT NULL);";
		Statement stat;
		try {
			stat = connection.createStatement();
			stat.executeUpdate(createUsers);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Users-Table konnte nicht erstellt werden!\nServer wird beendet...");
			System.exit(0);
		}
	}

	public void connect(String ipAdress, int port) {
		User user = new User(ipAdress, port);
		userIP.put(ipAdress + ":" + port, user);
	}

	public void disconnect(String ipAdress, int port) {
		User user = getUser(ipAdress, port);
		if (user != null) {
			try {
				Statement stat = connection.createStatement();
				stat.executeUpdate("update users SET punkte = " + user.getPunkte() + ", testsdone = '"
						+ user.getTestsDoneAsString() + "' WHERE username = '" + user.getUsername() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		userIP.remove(ipAdress + port);
		if (server.isConnectedTo(ipAdress, port))
			server.closeConnection(ipAdress, port);
	}

	public void login(User user, String username, String password) {
		String hashedPassword = hash(password);
		String dbHashedPassword = "error";
		Statement stat;
		int punkte = 0;
		ArrayList<String> testsdone = null;
		try {
			stat = connection.createStatement();
			ResultSet rs = stat.executeQuery("select * from users WHERE username = '" + username + "';");
			while (rs.next()) {
				dbHashedPassword = rs.getString("password");
				punkte = rs.getInt("punkte");
				testsdone = new ArrayList<String>();
				for (String test : rs.getString("testsdone").split(",")) {
					testsdone.add(test);
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (dbHashedPassword.equals("error")) {
			user.send(new Packet("loginerror").addData("type", "invalidusername"));
		}
		if (dbHashedPassword.equals(hashedPassword)) {
			user.setUsername(username);
			user.addPunkte(punkte);
			for (String test : testsdone) {
				if (!test.isEmpty())
					user.addTest(test);
			}
			user.setLoggedIn(true);
			user.send(new Packet("loginsuccess"));
		} else {
			user.send(new Packet("loginerror").addData("type", "invalidusername"));
		}
	}

	public void logout(User user) {
		user.setLoggedIn(false);
	}

	public void register(User user, String username, String password1, String password2) {
		String hashedPassword1 = hash(password1);
		String hashedPassword2 = hash(password2);
		Statement stat;
		if (!hashedPassword1.equals(hashedPassword2)) {
			user.send(new Packet("registererror").addData("type", "invalidpassword"));
			return;
		}
		if (username.length() > 15) {
			user.send(new Packet("registererror").addData("type", "invalidusername"));
			return;
		}
		try {
			stat = connection.createStatement();
			ResultSet rs = stat.executeQuery("select * from users WHERE username = '" + username + "';");
			if (rs.next()) {
				user.send(new Packet("registererror").addData("type", "invalidusername"));
				rs.close();
				return;
			}
			rs.close();
			stat.executeUpdate(
					"insert into users values ('" + username + "', '" + hashedPassword1 + "', " + 0 + ",'');");
			user.send(new Packet("registersuccess"));
		} catch (SQLException e) {
			e.printStackTrace();
			user.send(new Packet("registererror").addData("type", "dberror"));
		}
	}

	public static String hash(String passwordToHash) {
		passwordToHash += "test!2";
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passwordToHash.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public User getUser(String ipAdress, int port) {
		return userIP.get(ipAdress + ":" + port);
	}
}
