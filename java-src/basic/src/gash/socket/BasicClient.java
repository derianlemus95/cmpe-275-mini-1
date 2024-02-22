package gash.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

import gash.payload.BasicBuilder;
import gash.payload.Message;

public class BasicClient {
	private String name;
	private String ipaddr;
	private int port;
	private String group = "public";

	private Socket clt;

	public BasicClient(String name) {
		this(name, "127.0.0.1", 2000);
	}

	public BasicClient(String name, String ipaddr, int port) {
		this.name = name;
		this.ipaddr = ipaddr;
		this.port = port;
	}

	public void stop() {
		if (this.clt != null) {
			try {
				this.clt.close();
			} catch (IOException e) {
				// TODO better error handling? yes!

				//@Author: Armaghan

				System.err.println("Error closing the socket: " + e.getMessage());
				// Optionally log this error to a logging framework or error monitoring service
			} finally {
				this.clt = null;
			}
		}
		this.clt = null;
	}

	public void join(String group) {
		this.group = group;
	}

	public void connect() {
		if (this.clt != null) {
			return;
		}

		try {
			this.clt = new Socket(this.ipaddr, this.port);
			System.out.println("Connected to " + clt.getInetAddress().getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void sendMessage(String message) {
		if (this.clt == null) {
			System.out.println("no connection, text not sent");
			return;
		}

		try {
			BasicBuilder builder = new BasicBuilder();
			byte[] msg = builder.encode(new Message(name, group, message)).getBytes();
			this.clt.getOutputStream().write(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	// Modification to run for Server.cpp:

	public void sendMessage(String message) {
	    if (this.clt == null) {
				System.out.println("no connection, text not sent");
				return;
	    }

	    try {
				BasicBuilder builder = new BasicBuilder();
				Message msg = new Message(name, group, message);
				String encodedMsg = builder.encode(msg);
				byte[] msgBytes = encodedMsg.getBytes("UTF-8");
				int start = 0;
				int end = 10000;
				int len = msgBytes.length;
				while(end < len){
					this.clt.getOutputStream().write(Arrays.copyOfRange(msgBytes, start, end));
					long sentTime = System.nanoTime();
					System.out.println("Sent message to server at"+ sentTime);
					while(this.clt != null){
						int i = this.clt.getInputStream().read();
						if(i <=0 ){
							continue;
						} 
						System.out.println("Received ACK from server: in"+ (System.nanoTime() - sentTime));
						break;
					}
					end+=10000;
					start+=10000;
				}
				if(start < len){
					this.clt.getOutputStream().write(Arrays.copyOfRange(msgBytes, start, len));
					long sentTime = System.nanoTime();
					System.out.println("Sent message to server at"+ sentTime);
					while(this.clt != null){
						int i = this.clt.getInputStream().read();
						if(i <=0 ){
							continue;
						} 
						System.out.println("Received ACK from server: in"+ (System.nanoTime() - sentTime));
						break;
					}
				}
				// System.arraycopy(lengthPrefixBytes, 0, finalMsg, 0, lengthPrefixBytes.length);
				// System.arraycopy(msgBytes, 0, finalMsg, lengthPrefixBytes.length, msgBytes.length);
			} catch (Exception e) {
		e.printStackTrace();
	    }
	}
}
