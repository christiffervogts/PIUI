package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.jcraft.jsch.*;

public class SSH_Startup {

    public String user;
    public String location;
    public String password;

    public void load_vars(String user_name, String location_name, String password_name) {
        user = user_name;
        location = location_name;
        password = password_name;
        call_SSH();
    }

    public void call_SSH() {
        JSch jsch = new JSch();
        Session session = null;

        try {
            session = jsch.getSession(user, location, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            System.out.println("Connected to " + location);

            ChannelShell channel = (ChannelShell) session.openChannel("shell");

            InputStream output = channel.getInputStream();
            OutputStream input = channel.getOutputStream();
           
            channel.connect();
            Thread.sleep(500); // wait for shell to load
            input.write("bind 'set enable-bracketed-paste off'\n".getBytes());
            input.flush();

            Thread printThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = output.read(buffer)) != -1) {
                        System.out.print(new String(buffer, 0, bytesRead));
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            printThread.setDaemon(true);
            printThread.start();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = consoleReader.readLine()) != null) {
                input.write((line + "\n").getBytes());
                input.flush();

                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            channel.disconnect();

        } catch (JSchException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
                System.out.println("Disconnected.");
            }
        }
    }
}