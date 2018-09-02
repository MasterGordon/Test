package test;

import test.server.TestServer;

public class MainServer {
	public static void main(String[] args) {
		TestServer.instance = new TestServer(1001);
	}
}
