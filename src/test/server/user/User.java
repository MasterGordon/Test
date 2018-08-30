package test.server.user;

/**
 * 
 * Dies ist eine Klasse zur modellierung von Nutzern
 * 
 * @author Gordon Goldbach
 * @version 30.08.2018 16:59:31
 *
 */
public class User {
	String username;
	String ipAdress;
	int port;
	int punkte;

	/**
	 * Konstruktor vom User jedoch wird der <ff>username</ff> noch nicht gesetzt, da der User sich erst einloggen muss.
	 * 
	 * @param ipAdress ip adresse des Users
	 * @param port port des Users
	 */
	public User(String ipAdress, int port) {
		this.port = port;
		this.ipAdress = ipAdress;
	}
}
