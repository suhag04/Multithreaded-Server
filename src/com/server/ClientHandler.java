package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

//ClientHandler class
class ClientHandler extends Thread implements Runnable {
	private String email;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isloggedin;

	// constructor
	public ClientHandler(Socket s, String email, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.email = email;
		this.s = s;
		this.isloggedin = true;
	}

	public void run() {
		System.out.println("Inside run() function... ");
		String received;
		while (true) {
			try {
				// receive the string
				received = dis.readUTF();
				if (received.equals("logout")) {
					this.isloggedin = false;
					this.s.close();
					break;
				}
				if (received.contains("@")) {

					// JSONObject jsonData = (JSONObject) new
					// JSONParser().parse(received);
					JSONObject jsonEmailDetails = (JSONObject) new JSONParser().parse(received);

					// break the string into message and recipient part
					String sender = jsonEmailDetails.get("sender").toString();
					String recipient = jsonEmailDetails.get("receipent").toString();
					String emailSubject = jsonEmailDetails.get("subject").toString();
					String emailBody = jsonEmailDetails.get("body").toString();

					// search for the recipient in the connected devices list.
					// ar is the vector storing client of active users
					for (ClientHandler mc : ServerApplication.ar) {
						// if the recipient is found, write on its
						
						
						// output stream
						if (mc.email.trim().equals(recipient.trim()) && mc.isloggedin == true) {
							// mc.dos.writeUTF(this.email + " : " + MsgToSend);
							Emails e = new Emails(sender.trim(), recipient.trim(), emailSubject, emailBody);
							e.insertEmail();
							Map<String, Object> hm = new HashMap<>();
							hm.put("sender", sender);
							hm.put("subject", emailSubject);
							hm.put("body", emailBody);
							hm.put("time", new Date().toString());

							Gson gson = new Gson();
							String replay = "";
							replay = gson.toJson(hm);
							mc.dos.writeUTF(replay);

							break;
						}
					}
				}
			} catch (IOException | ParseException e) {

				e.printStackTrace();
			}

		}
		try {
			// closing resources
			this.dis.close();
			this.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}