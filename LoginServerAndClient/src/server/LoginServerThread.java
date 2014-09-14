/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Aaron
 *
 */
public class LoginServerThread implements Runnable {
	private Socket sock;
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	public LoginServerThread(Socket sock){
		this.sock = sock;
	}
	//****************************************************************************************************************************************
	@Override
	public void run() {
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
			InputStreamReader isr = new InputStreamReader(is);
			PrintWriter pw = new PrintWriter(os);
			br = new BufferedReader(isr);
			String message ="";
			boolean successfulLogin = false;
			int i = 0;
			while((!successfulLogin && i < 3 ) && (message = br.readLine())!= null){ //short circit of && to prevent getting stuck looking for input
				// if !successfully logged in and tries < 3 then look for input while loop form
				String[] userNamePassWord=message.split(" ");
				successfulLogin = checkPasscodeUsername(userNamePassWord[0],userNamePassWord[1]);//check login's txt file for logins
				i++;
				if(successfulLogin){ //if logged in then say so
					pw.println("Login Success");
					pw.flush();
				}
				else{
					pw.println("Login Failed");
					pw.flush();
				}
			}
			if(i==3){//outside loop, if you failed to log in in 3 tries print error message...Error is that doesn't accpet last login cridentials, even if right
				pw.println("Please Contact System Administrator: too many login attempts");
				pw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//****************************************************************************************************************************************
	private boolean checkPasscodeUsername(String username, String password){
		boolean passwordSuccess = false;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/server/file.txt")));//this gets the file
			ArrayList<String[]> usernamePasswords = new ArrayList<String[]>();// makes a magic array list of all the usernames and passwords
			String fileInput = null;
			while((fileInput = reader.readLine())!= null){
				usernamePasswords.add(fileInput.split(" "));//each line then splits the username and password to two strings
			}
			for(String[] credntials: usernamePasswords){//maybe should be a while loop
				if(username.equals(credntials[0])){
					if(password.equals(credntials[1])){//maybe should be passwordSuccess = passwordSuccess || password.equals(credntials[1]);  ????
						passwordSuccess = true;//check that shit like they do in the future
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		return passwordSuccess;
	}
}
