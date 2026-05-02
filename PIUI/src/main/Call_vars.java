package main;

import java.awt.*;
import java.awt.List;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Scanner;

public class Call_vars implements ActionListener{
	SSH_Startup ss = new SSH_Startup();// make the SSH request to get acsess to the server
	NetworkInterface network;
	byte[] mac;
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

	JLabel Text = new JLabel("Go To Consol");
	
	JComboBox<String> user_box = new JComboBox<>();
	JComboBox<String> location_box = new JComboBox<>();
	JComboBox<String> password_box = new JComboBox<>();

	BufferedReader br;
	BufferedWriter bw;
	
	public Call_vars() {
		networkVerification();
		opening_stuff();
		if(!check_for_save()) {
			state = 3;
			pick_user();
			state = 1;
			check_for_save();
			window.repaint();
		}
		else {
			pick_user();
			window.repaint();
		}
	}
	public void opening_stuff() {
		networkVerification();
		window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setLayout(null);
		window.setVisible(true);
		
		window.add(new_user);
		new_user.setSize(200, 50);
		new_user.setLocation(window.getWidth()/2-100, 340);
		new_user.setVisible(true);
		new_user.addActionListener(this);
	}
	public boolean check_for_save() {
		networkVerification();
		try {
			br = new BufferedReader(new FileReader("Saves.piui"));
			try {
				br.mark(1000);
				if(br.readLine() == null) {
					return false;
				}
				br.reset();
				users = br.readLine();
				user = new ArrayList<>(Arrays.asList(users.split(",")));
				for(String u : user) {
					user_box.addItem(decrypt(u));
				}
				user.add(0, "---Selcet---");
				locations = br.readLine();
				location = new ArrayList<>(Arrays.asList(locations.split(",")));
				for(String u : location) {
					location_box.addItem(decrypt(u));
				}
				location.add(0, "---Selcet---");
				passwords = br.readLine();
				password = new ArrayList<>(Arrays.asList(passwords.split(",")));
				for(String u : password) {
					password_box.addItem(decrypt(u));
				}
				password.add(0, "---Selcet---");
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			return false;
		}
		
		return true;
	}
	public void networkVerification() {
        
        
		try {
			InetAddress ip = InetAddress.getLocalHost(); 
			network = NetworkInterface.getByInetAddress(ip);
			mac = network.getHardwareAddress();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
        

	}
	public String encrypt(String input) {
	    char[] output_chars = input.toCharArray();
	    
	    for (int i = 0; i < mac.length; i++) {
	        for (int j = 0; j < output_chars.length; j++) {
	            if (i % 2 == 0) {
	                output_chars[j] += mac[i];
	            } else {
	                output_chars[j] -= mac[i];
	            }
	        }
	    }
	    
	    byte[] bytes = new String(output_chars).getBytes(StandardCharsets.UTF_16BE);
	    String output = HexFormat.of().formatHex(bytes);
	    System.out.println(output);
	    return output;
	}
	public String decrypt(String input) {
		byte[] bytes = HexFormat.of().parseHex(input);
		String result = new String(bytes, StandardCharsets.UTF_16BE);
		char[] output_chars = result.toCharArray();
	    
	    for (int i = mac.length - 1; i >= 0; i--) {
	        for (int j = 0; j < output_chars.length; j++) {
	            if (i % 2 == 0) {
	                output_chars[j] -= mac[i];
	            } else {
	                output_chars[j] += mac[i];
	         }
	        }
	    }
	    
	    String output = new String(output_chars);
	    System.out.println(output);
	    return output;
	}
	public void add_information(String User, String Location, String password) {
	    String[] newData = { User, Location, password };
	    String filePath = "Saves.piui";
	
	    java.util.List<String> existingLines = new ArrayList<>();
	    File file = new File(filePath);
	    if (file.exists()) {
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                existingLines.add(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
	        for (int i = 0; i < 3; i++) {
	            String existing = i < existingLines.size() ? existingLines.get(i) : null;
	            if (existing == null) {
	                bw.write(newData[i]);
	            } else {
	                bw.write(existing + newData[i]);
	            }
	            bw.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}	
	public void pick_user() {
		networkVerification();
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
			
			window.getContentPane().removeAll();			
		    Scanner myObj = new Scanner(System.in);
		    
		    System.out.println("Enter username");
		    String userName = myObj.nextLine(); 
		    System.out.println("Username is: " + userName);
		    
		    System.out.println("Enter Location");
		    String Location = myObj.nextLine();
		    System.out.println("Location: " + Location);
		    
		    System.out.println("Enter Password");
		    String PassWord = myObj.nextLine();
		    System.out.println("Location: " + PassWord);

		    userName = encrypt(userName);
		    Location = encrypt(Location);
		    PassWord = encrypt(PassWord);

		    add_information(userName, Location, PassWord);
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
			System.out.println("Hello");

			state = 3;
			pick_user();
		}

	}
}
