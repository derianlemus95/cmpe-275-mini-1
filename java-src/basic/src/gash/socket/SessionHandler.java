package gash.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;

import gash.payload.BasicBuilder;
import gash.payload.Message;


/**
 * 
 * @author gash
 * 
 */
class SessionHandler extends Thread {
	private Socket connection;
	private String name;
	private boolean forever = true;
	

	public SessionHandler(Socket connection) {
		this.connection = connection;
		
		// allow server to exit if
		this.setDaemon(true);
	}

	/**
	 * stops session on next timeout cycle
	 */
	public void stopSession() {
		forever = false;
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		connection = null;
	}

	/**
	 * process incoming data
	 */
	public void run() {
		System.out.println("Session " + this.getId() + " started");

		try {
			connection.setSoTimeout(2000);
			var in = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());

			if (in == null)
				throw new RuntimeException("Unable to get input streams");

			byte[] raw = new byte[10000];
			BasicBuilder builder = new BasicBuilder();
			while (forever) {
				try {
					int len = in.read(raw);
					if (len == 0)
						continue;
					else if (len == -1){
						break;
					}
					Message msg = builder.decode(new String(raw, 0, len).getBytes());
					System.out.println(msg);

					out.write(new byte[] {'A','C','K'});
					out.flush();
				} catch (InterruptedIOException | SocketException e) {
					if(e.getMessage() == "Connection reset"){
						System.out.println("Connection to client lost | finished");
						break;
					} else{
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Session " + this.getId() + " ending");
				System.out.flush();
				stopSession();
			} catch (Exception re) {
				re.printStackTrace();
			}
		}
	}

} // class SessionHandler