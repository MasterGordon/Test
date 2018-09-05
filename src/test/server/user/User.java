package test.server.user;

import java.util.ArrayList;

import test.server.TestServer;
import test.util.Packet;

/**
 * 
 * Dies ist eine Klasse zur modellierung von Nutzern
 * 
 * @author Gordon
 * @version 30.08.2018 16:59:31
 *
 */
public class User {
	private String username = "";
	private String ipAdress = "";
	private int port;
	private int punkte;
	private boolean isLoggedIn = false;
	private ArrayList<String> testsDone = new ArrayList<String>();

	/**
	 * Konstruktor vom User jedoch wird der <ff>username</ff> noch nicht gesetzt, da
	 * der User sich erst einloggen muss.
	 * 
	 * @param ipAdress ip adresse des Users
	 * @param port     port des Users
	 */
	public User(String ipAdress, int port) {
		this.port = port;
		this.ipAdress = ipAdress;
	}

	public void send(Packet packet) {
		TestServer.instance.send(ipAdress, port, packet.save());
	}

	public String getUsername() {
		return username;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public int getPort() {
		return port;
	}

	public int getPunkte() {
		return punkte;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addTest(String code) {
		testsDone.add(code);
	}

	public boolean isTestDone(String code) {
		return testsDone.contains(code);
	}

	public void addPunkte(int punkte) {
		this.punkte += punkte;
	}

	public String getTestsDoneAsString() {
		String s = "";
		for (String test : testsDone) {
			s += test + ",";
		}
		return s;
	}

	public void setLoggedIn(boolean b) {
		isLoggedIn = true;
	}
}
