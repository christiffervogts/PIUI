package main;

public class Main {


	static Call_vars   cv;
	public static SSH_Startup ss;
	public static void main(String[] args) {
		
		cv = new Call_vars();//input you're info once then store it to a .txt file
		ss = new SSH_Startup();// make the SSH request to get acsess to the server
		GUI_Startup gs = new GUI_Startup();// start gui stuff here
	}

}
