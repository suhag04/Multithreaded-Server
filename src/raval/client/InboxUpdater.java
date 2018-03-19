package raval.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InboxUpdater extends Thread implements Runnable {

	private JTable jt;
	private DefaultTableModel dfm;
	private DataInputStream dis;
	private DataOutputStream dos;

	public InboxUpdater(JTable tbl, DefaultTableModel dm, DataInputStream dis, DataOutputStream dos) {
		this.jt = tbl;
		this.dfm = dm;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run() {

		String received;
		while (true) {

			try {
				received = dis.readUTF();

				Vector rcvMatrix = new Vector<>();
				JSONObject jsonEmailDetails = (JSONObject) new JSONParser().parse(received);
				String sender = jsonEmailDetails.get("sender").toString();
				String emailSubject = jsonEmailDetails.get("subject").toString();
				String emailBody = jsonEmailDetails.get("body").toString();
				String emailTime = jsonEmailDetails.get("time").toString();
				rcvMatrix.addElement(sender);
				rcvMatrix.addElement(emailSubject);
				rcvMatrix.addElement(emailBody);
				rcvMatrix.addElement(emailTime);
				dfm.addRow(rcvMatrix);
				jt.validate();
				JOptionPane.showMessageDialog(null, "New Email!");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
