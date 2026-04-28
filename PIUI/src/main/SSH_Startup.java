package main;

import java.io.IOException;

public class SSH_Startup {

	public String user;
	public String location;
	public String password;

	public String command = "dir";
	
	public void load_vars(String user_name, String location_name, String password_name) {
		user = user_name;
		location = location_name;
		password = password_name;

		call_SSH();
	}
	
	public void call_SSH() {
		try {
		    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
		    pb.inheritIO();
		    Process process = pb.start();
		    process.waitFor();
		    pb.command("ssh "+user+"@"+location);
		    pb.start();
		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		}	}
}
