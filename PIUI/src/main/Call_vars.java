package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.swing.*;

public class Call_vars implements ActionListener{
	SSH_Startup ss = new SSH_Startup();// make the SSH request to get acsess to the server

	public String users;
	public String locations;
	public String passwords;
	
	public String user_name, location_name, password_name;
	
	public int user_index, location_index, password_index, state;
	public ArrayList<String> user = new ArrayList<>();
	public ArrayList<String> location = new ArrayList<>();
	public ArrayList<String> password = new ArrayList<>();
		
	JFrame window = new JFrame("Login");
	
	JButton new_user = new JButton("Add a User");
	JButton confirm_user = new JButton("Confirm?");
	JButton confirm_location = new JButton("Confirm?");
	JButton confirm_password = new JButton("Confirm?");
	JButton confirm_additon = new JButton("Confirm?");

	JTextField Username = new JTextField();
	JTextField Local = new JTextField();
	JTextField Password = new JTextField();

	JComboBox<String> user_box = new JComboBox<>();
	JComboBox<String> location_box = new JComboBox<>();
	JComboBox<String> password_box = new JComboBox<>();

	BufferedReader br;
	BufferedWriter bw;
	
	public Call_vars() {
		opening_stuff();
		if(!check_for_save()) {
			state = 3;
			pick_user();
		}
		else {
			pick_user();
			window.repaint();
		}
	}
	public void opening_stuff() {
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setLayout(null);
		window.setVisible(true);
	}
	public boolean check_for_save() {
		
		try {
			br = new BufferedReader(new FileReader("Saves.txt"));
			
			try {
				users = br.readLine();
				user = new ArrayList<>(Arrays.asList(users.split(",")));
				user.add(0, "---Selcet---");
				for(String u : user) {
				    user_box.addItem(u);
				}
				locations = br.readLine();
				location = new ArrayList<>(Arrays.asList(locations.split(",")));
				location.add(0, "---Selcet---");
				for(String u : location) {
					location_box.addItem(u);
				}
				passwords = br.readLine();
				password = new ArrayList<>(Arrays.asList(passwords.split(",")));
				password.add(0, "---Selcet---");
				for(String u : password) {
					password_box.addItem(u);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			return false;
		}
		
		return true;
	}
	public void add_information(String User, String Location, String ) {
		
	}
	public void pick_user() {
		switch(state) {
		case 0://chose user
			window.add(user_box);
			user_box.setSize(150, 50);
			user_box.setLocation(window.getWidth()/2-150/2-50, 100);
			user_box.setVisible(true);
			confirm_user.setSize(100,50);
			confirm_user.setVisible(true);
			confirm_user.addActionListener(this);
			confirm_user.setLocation(user_box.getX()+150, user_box.getY());
			window.add(confirm_user);
			break;
		case 1://chose location
			window.add(location_box);
			location_box.setSize(150, 50);
			location_box.setLocation(window.getWidth()/2-150/2-50, 150);
			location_box.setVisible(true);
			confirm_location.setSize(100,50);
			confirm_location.setVisible(true);
			confirm_location.addActionListener(this);
			confirm_location.setLocation(location_box.getX()+150, location_box.getY());
			window.add(confirm_location);
			break;
		case 2://chose password
			window.add(password_box);
			password_box.setSize(150, 50);
			password_box.setLocation(window.getWidth()/2-150/2-50, 200);
			password_box.setVisible(true);
			confirm_password.setSize(100,50);
			confirm_password.setVisible(true);
			confirm_password.addActionListener(this);
			confirm_password.setLocation(password_box.getX()+150, password_box.getY());
			window.add(confirm_password);
			break;
		case 3://add person
			username
			break;

		}

	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == confirm_user && user_box.getSelectedItem() != "---Selcet---") {
			user_name = (String) user_box.getSelectedItem();
			state++;
			pick_user();
			window.repaint();
		}
		else if(e.getSource() == confirm_location && location_box.getSelectedItem() != "---Selcet---") {
			location_name = (String) location_box.getSelectedItem();
			state++;
			pick_user();
			window.repaint();
		}
		else if(e.getSource() == confirm_password && password_box.getSelectedItem() != "---Selcet---") {
			password_name = (String) password_box.getSelectedItem();
			state++;
			window.dispose();
			ss.load_vars(user_name, location_name, password_name);
		}
		else if(e.getSource() == new_user) {
			state = 3;
			pick_user();
		}

	}
}
