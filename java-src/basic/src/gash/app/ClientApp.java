package gash.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import gash.socket.BasicClient;

/**
 * client - basic chat construct. This varies from our Python and C++ versions
 * as it prompts the use for messages.
 * 
 * @author gash
 * 
 */
public class ClientApp {
	private BasicClient myClient;

	public ClientApp() {
	}

	public static String gen(){
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
        
		for (int i = 0; i < 1000000; i++) {
				int index = random.nextInt(characters.length());
				char randomChar = characters.charAt(index);
				sb.append(randomChar);
		}
		return sb.toString();
	}

	public static void sendToJava(){
		String payload = gen();
		var javaClient = new BasicClient("app", "127.0.0.1", 3000);
		javaClient.connect();
		javaClient.join("pets/dogs");
		javaClient.sendMessage(payload);
	}

	public static void sendToCPP(){
		String payload = gen();
		var cppClient = new BasicClient("app", "127.0.0.1", 2000);
		cppClient.connect();
		cppClient.join("pets/dogs");
		cppClient.sendMessage(payload);
	}

	public static void sendToPython(){
		String payload = gen();
		var pythonClient = new BasicClient("app", "127.0.0.1", 4000);
		pythonClient.connect();
		pythonClient.join("pets/dogs");
		pythonClient.sendMessage(payload);
	}

	public static void main(String[] args) {
		sendToJava();
		sendToPython();
		sendToCPP();
	}
}
