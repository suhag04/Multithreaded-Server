package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;

public class ServerApplication {
	static Vector<ClientHandler> ar = new Vector<>();
	static int i = 0;
	static String email;

	public static void main(String args[]) throws Exception {

		ServerSocket sc = new ServerSocket(2021);

		while (true) {
			Socket s = null;
			try {
				s = sc.accept();
				System.out.println("Notification: New Client Joined");
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				String temp = dis.readUTF().trim();
				String credentials[] = temp.split(",");
				Account acc;
				String Password;

				email = credentials[0];
				Password = credentials[1];
				acc = new Account(email, Password);
				if (temp.contains("signup")) {
					email = new String(acc.signUp());
					if (!email.contains("@")) {
						dos.writeUTF("failed");
						break;
					}
				} else {
					email = new String(acc.signIn());
				}

				if (email != "" || !email.equals("")) {
					System.out.println("Notification: Assigning new thread for this client for " + email);
					dos.writeUTF("successful");
					Gson gson = new Gson();

					Emails em = new Emails(email);
					List l1 = em.getSentEmail();
					List l2 = em.getReceivedEmail();

					String sentEmails = gson.toJson(l1);
					String receivedEmails = gson.toJson(l2);
					dos.writeUTF(sentEmails + "#" + receivedEmails);
					System.out.println("Notification: Email String Found");
					// Create a new handler object for handling this
					// request.
					ClientHandler mtch = new ClientHandler(s, email, dis, dos);
					// Create a new Thread with this object.
					Thread t = new Thread(mtch);

					System.out.println("Notification: Adding this client to active client list");
					// add this client to active clients list
					ar.add(mtch);
					// start the thread.
					t.start();
					// increment i for new client.
					// i is used for naming only, and can be replaced
					// by any naming scheme
					i++;
					System.out.println("Notification: Online Users: " + i);

				} else {
					dos.writeUTF("failed");
				}

			} catch (Exception e) {
				s.close();
				e.printStackTrace();
			}
		}

	}
}
