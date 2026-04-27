package main;

import java.io.*;

public class Call_vars {

	public String user;
	public String location;
	public String password;
	
	BufferedReader br;
	BufferedWriter bw;
	
	public Call_vars() {
		if(!check_for_save()) {
			make_save();
		}
	}
	public boolean check_for_save() {
		try {
			br = new BufferedReader(new FileReader("Saves.txt"));
			
			try {
				user = br.readLine();
				location = br.readLine();
				password = br.readLine();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			return false;
		}
		return false;
	}
	public void make_save() {
		try {
			bw = new BufferedWriter(new FileWriter("Saves.txt"));
			//find a way to get this in a window.
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
