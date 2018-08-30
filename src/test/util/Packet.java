package test.util;

import java.util.HashMap;

/**
 * 
 * Dies ist eine Klasse für das vereinfachte versenden von Daten mit einer
 * leichten orientierung an JSON
 * 
 * Diese Klasse kann einfach Daten in Packet-Objekten Speichern, welche sich
 * einfach in versendbare Strings umwandelnlässt. Diese String lassen sich
 * wiederung im Packet-Ojekte umwandeln um einfach auf die Daten zugreifen zu
 * können
 * 
 * @author Gordon Goldbach
 * @version 30.08.2018 13:00:00
 *
 */

public class Packet {
	HashMap<String, String> data = new HashMap<String, String>();
	String action;

	/**
	 * Privater Konstruktor damit kein leeres Packet instanziert werden kann
	 */
	private Packet() {

	}

	/**
	 * Konstruktor für ein Packet
	 * 
	 * @param action Gibt den typ des packets an
	 */
	public Packet(String action) {
		this.action = action;
	}

	/**
	 * Erstellt ein Packet Objekt aus dem ingehenden String
	 * 
	 * @param input String welcher gesendet wurde
	 * @return ein Packet Objekt zu dem String
	 */
	public static Packet creatFromString(String input) {
		Packet p = new Packet();
		String[] parts = input.split("#");
		if (parts.length < 2)
			return null;
		p.action = parts[1];
		for (int i = 2; i < parts.length; i++) {
			p.data.put(parts[i].split(":")[0], parts[i].split(":")[1]);
		}
		return p;
	}

	/**
	 * Fügt daten zu einem Packet hinzu
	 * 
	 * @param key   namen von dem Wert
	 * @param value Wert den vom key aufgerufen werden kann
	 * @return Packet von welchem aus die methode aufgerufen wurde
	 */
	public Packet addData(String key, String value) {
		if (data.containsKey(key))
			return this;
		data.put(key, value);
		return this;
	}

	/**
	 * Wandelt Packet in einen String um, sodass dieser versendet werden kann
	 * 
	 * @return das Packet als String
	 */
	public String save() {
		String output = "#" + action + "#";
		for (String key : data.keySet()) {
			output += key + ":" + data.get(key) + "#";
		}

		return output;
	}

	/**
	 * Gibt die aktion des Packets zurück
	 * 
	 * @return Action/Typ des Packets
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Holt den Wert eines Keys
	 * 
	 * @param key Schlüssel dessen Wert gesucht ist
	 * @return Wert des angegebenen Keys
	 */
	public String get(String key) {
		return data.get(key);
	}
}