package main;

public class Main {

	public static void main(String[] args) {
		
		Call_vars   cv = new Call_vars();//input you're info once then store it to a .txt file
		SSH_Startup ss = new SSH_Startup();// make the SSH request to get acsess to the server
		GUI_Startup gs = new GUI_Startup();// start gui stuff here
	}

}
