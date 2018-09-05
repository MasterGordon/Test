package test.server.tests;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import test.server.TestServer;
import test.server.user.User;

public class TestManager {
	private TestServer server;
	private ArrayList<String> codes = null;
	private HashMap<User, Test> currentTests;
	private HashMap<User, Test> currentCreatingTests;
	private static final char[] CODECHARACTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	@SuppressWarnings("unchecked")
	public TestManager(TestServer server) {
		this.server = server;
		currentTests = new HashMap<User, Test>();
		currentCreatingTests = new HashMap<User, Test>();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(server.getPath()));
			Object result = ois.readObject();
			if (result instanceof ArrayList) {
				if (result.getClass().getComponentType().isAssignableFrom(String.class))
					codes = (ArrayList<String>) result;
			}
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (codes == null) {
			codes = new ArrayList<String>();
		}
	}

	public String generateUniqueCode() {
		String code;
		do {
			code = "";
			for (int i = 0; i < 6; i++) {
				code += CODECHARACTERS[(int) ((CODECHARACTERS.length - 1) * Math.random())];
			}
		} while (codes.contains(code));
		return code;
	}
}
