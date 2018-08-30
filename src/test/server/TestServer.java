package test.server;

import test.api.Server;

public class TestServer extends Server{

	public TestServer(int pPort) {
		super(pPort);
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort) {
		
	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage) {
		
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort) {
		
	}

}
