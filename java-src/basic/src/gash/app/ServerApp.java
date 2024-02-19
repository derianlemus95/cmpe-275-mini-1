package gash.app;

import gash.socket.BasicServer;

/**
 * server application
 * 
 * @author gash
 * 
 */
class ServerApp {
	public ServerApp() {
	}

	public void javaServer(String[] args) {
		var host = "192.168.1.201";
		var port = 2000;
		var server = new BasicServer(host,port);
		server.start();
	}

	public static pythonServer(String[] args) {
		var host = "192.168.1.201";
		var port = 4000;
		var server = new BasicServer(host,port);
		server.start();
	}

	public static void main(String[] args) {

	}
}
