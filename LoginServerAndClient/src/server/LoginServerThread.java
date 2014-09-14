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
			while((!successfulLogin && i < 3 ) && (message = br.readLine())!= null){
				String[] userNamePassWord=message.split(" ");
				successfulLogin = checkPasscodeUsername(userNamePassWord[0],userNamePassWord[1]);
				i++;
				if(successfulLogin){
					pw.println("Login Success");
					pw.flush();
				}
				else{
					pw.println("Login Failed");
					pw.flush();
				}
			}
			if(i==3){
				pw.println("Please Contact System Administrator: too many login attempts");
				pw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean checkPasscodeUsername(String username, String password){
		boolean passwordSuccess = false;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/server/file.txt")));
			ArrayList<String[]> usernamePasswords = new ArrayList<String[]>();
			String fileInput = null;
			while((fileInput = reader.readLine())!= null){
				usernamePasswords.add(fileInput.split(" "));
			}
			for(String[] credntials: usernamePasswords){
				if(username.equals(credntials[0])){
					if(password.equals(credntials[1])){
						passwordSuccess = true;
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
