package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

public class ClientApplication {

	private static String Username, Password;

	ClientApplication(String uname, String psw) {
		this.Username = uname;
		this.Password = psw;
	}

	public void runClient(String type) throws Exception {
		URL myPublicIP = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(myPublicIP.openStream()));
		String ip = in.readLine(); // you get the IP as a String
		System.out.println(ip);
		Socket clientSocket = new Socket("localhost", 2021);
		DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
		DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
		// send user name to server
		if (type.endsWith("signup")) {
			dos.writeUTF(Username + "," + Password + ",signup");
		} else {
			dos.writeUTF(Username + "," + Password);
		}
		if (dis.readUTF().equals("successful")) {
			// open email window
			System.out.println("Notification: Successfully Login to System");
			EmailTabs e = new EmailTabs(clientSocket, Username, dis, dos);

		} else if (dis.readUTF().equals("failed")) {
			System.out.println("EmailID not Found");
			JOptionPane.showMessageDialog(null, "Email/Password Not Found!");
		} else {
			JOptionPane.showMessageDialog(null, dis.readUTF());
		}
	}
}