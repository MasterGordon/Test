package test.server;

import test.api.Server;

public class TestServer extends Server{

	public static TestServer instance;
	
	public TestServer(int pPort) {
		super(pPort);
	}

	@Override
	public void processNewConnection(String ipAdress, int port) {
		
	}

	@Override
	public void processMessage(String ipAdress, int port, String message) {
		
	}

	@Override
	public void processClosingConnection(String ipAdress, int port) {
		
	}

}
