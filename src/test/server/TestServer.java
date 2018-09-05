package test.server;

import java.io.File;

import test.api.Server;
import test.server.user.User;
import test.server.user.UserManager;
import test.util.Packet;

public class TestServer extends Server{

	public static TestServer instance;
	private UserManager userManager;
	private String path;
	
	public TestServer(int pPort) {
		super(pPort);
		path = System.getenv("USERPROFILE");
		if (path == null)
			path = "";
		else
			path += "/Documents";
		path += "/Test";
		new File(path + "/Tests").mkdirs();
		userManager = new UserManager(this);
	}

	@Override
	public void processNewConnection(String ipAdress, int port) {
		userManager.connect(ipAdress, port);
	}

	@Override
	public void processMessage(String ipAdress, int port, String message) {
		User user = userManager.getUser(ipAdress, port);
		user.send(new Packet("test"));
	}

	@Override
	public void processClosingConnection(String ipAdress, int port) {
		userManager.disconnect(ipAdress, port);
	}

	public String getPath() {
		return path;
	}
}
