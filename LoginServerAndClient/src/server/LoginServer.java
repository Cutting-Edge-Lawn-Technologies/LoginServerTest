/**
 * 
 */
package server;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 * @author Aaron
 *
 */
public class LoginServer {

	public static void main(String[] args) {
		try{
			final int PORT = 4444;
			boolean run = true;
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Ready for Clients");
			while(run){
				Socket sock = server.accept();
				LoginServerThread login = new LoginServerThread(sock);
				Thread x = new Thread(login);
				x.start();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}

	}

}
