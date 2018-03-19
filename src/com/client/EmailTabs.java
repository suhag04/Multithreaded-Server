package com.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class EmailTabs extends JPanel {
	static Vector<InboxUpdater> iu = new Vector<>();
	private String email;
	private DefaultTableModel defaultSentTableModel;
	private JTable sentJtable;

	public DefaultTableModel getDefaultSentTableModel() {
		return defaultSentTableModel;
	}

	public void setDefaultSentTableModel(DefaultTableModel defaultSentTableModel) {
		this.defaultSentTableModel = defaultSentTableModel;
	}

	public JTable getSentJtable() {
		return sentJtable;
	}

	public void setSentJtable(JTable sentJtable) {
		this.sentJtable = sentJtable;
	}

	public EmailTabs(Socket s, String email, DataInputStream dis, DataOutputStream dos)
			throws IOException, ParseException {
		super(new GridLayout(1, 1));
		this.email = email;
		String rcvData = dis.readUTF();
		StringTokenizer st = new StringTokenizer(rcvData, "#");
		String sent = st.nextToken();
		String rcv = st.nextToken();

		JSONParser parser = new JSONParser();
		Object objSent = parser.parse(sent);
		Object objRcv = parser.parse(rcv);
		JSONArray jsonA = (JSONArray) objSent;
		JSONArray jsonB = (JSONArray) objRcv;
		String[][] sentMatrix = new String[jsonA.size()][5];
		String[][] rcvMatrix = new String[jsonB.size()][5];

		for (int i = 0; i < jsonA.size(); i++) {
			JSONArray jsonC = (JSONArray) jsonA.get(i);
			for (int j = 0; j < jsonC.size(); j++) {
				sentMatrix[i][j] = jsonC.get(j).toString();
			}
		}
		for (int i = 0; i < jsonB.size(); i++) {
			JSONArray jsonD = (JSONArray) jsonB.get(i);
			for (int j = 0; j < jsonD.size(); j++) {
				rcvMatrix[i][j] = jsonD.get(j).toString();
			}
		}

		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = createImageIcon("images/middle.gif");

		JComponent panel1 = makeTextPanel("compose", dis, dos, sentMatrix);
		// panel1.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("Compose", icon, panel1, "Does nothing");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeTextPanel("inbox", dis, null, rcvMatrix);
		panel2.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("Inbox", icon, panel2, "Does twice as much nothing");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = makeTextPanel("sent", dis, null, sentMatrix);
		panel3.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("Sent", icon, panel3, "Still does nothing");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		// Add the tabbed pane to this panel.
		add(tabbedPane);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		JFrame frame = new JFrame("Logged In as " + email);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(this, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(500, 500);

		// get screen size and set the location of the frame
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setLocation(screenWidth / 3, screenHeight / 4);

	}

	protected JComponent makeTextPanel(String text, DataInputStream dis, DataOutputStream dos, String[][] dataMatrix)
			throws IOException, ParseException {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);

		if (text.equals("compose")) {
			JLabel labelTo = new JLabel("To:         ");
			JLabel labelSubject = new JLabel("Subject:");
			JLabel labelBody = new JLabel("Body:       ");
			JTextField fieldTo = new JTextField(30);
			JTextField fieldSubject = new JTextField(30);
			JButton buttonSend = new JButton("SEND");
			JTextArea textAreaMessage = new JTextArea(10, 30);

			JPanel toPanel = new JPanel();
			JPanel subjectPanel = new JPanel();
			JPanel areaPanel = new JPanel();
			JPanel sendPanel = new JPanel();

			toPanel.add(labelTo);
			toPanel.add(fieldTo);
			subjectPanel.add(labelSubject);
			subjectPanel.add(fieldSubject);
			areaPanel.add(labelBody);
			areaPanel.add(textAreaMessage);
			sendPanel.add(buttonSend);

			panel.add(toPanel);
			panel.add(subjectPanel);
			panel.add(areaPanel);
			panel.add(sendPanel);
			buttonSend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {

					Map<String, Object> hm = new HashMap<String, Object>();
					hm.put("sender", email);
					hm.put("receipent", fieldTo.getText());
					hm.put("subject", fieldSubject.getText());
					hm.put("body", textAreaMessage.getText());

					Gson gson = new Gson();
					String finalEmail = gson.toJson(hm);
					try {
						dos.writeUTF(finalEmail);
						DefaultTableModel dtm = null;
						JTable jtbl = new JTable();
						dtm = getDefaultSentTableModel();
						jtbl = getSentJtable();

						Vector sentVector = new Vector<>();
						sentVector.add(hm.get("receipent"));
						sentVector.add(hm.get("subject"));
						sentVector.add(hm.get("body"));
						sentVector.add(hm.get("time"));
						dtm.addRow(sentVector);
						jtbl.validate();
						if (dtm != null) {
							setDefaultSentTableModel(dtm);
						}
						if (jtbl != null) {
							setSentJtable(jtbl);
						}

						JOptionPane.showMessageDialog(null, "Email Sent Successfully!");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		}
		if (text.equals("inbox")) {

			DefaultTableModel defaultTableModel;
			JTable jt;

			String column[] = new String[4];
			column[0] = "From";
			column[1] = "Subject";
			column[2] = "Body";
			column[3] = "Time";

			defaultTableModel = new DefaultTableModel(dataMatrix, column);
			jt = new JTable(defaultTableModel);
			// final JTable jt = new JTable(dataMatrix, column);

			jt.setCellSelectionEnabled(true);
			ListSelectionModel select = jt.getSelectionModel();
			select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			select.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {

					String Data = null;
					int[] row = jt.getSelectedRows();
					int[] columns = jt.getSelectedColumns();
					for (int i = 0; i < row.length; i++) {
						for (int j = 0; j < columns.length; j++) {
							Data = (String) jt.getValueAt(row[i], columns[j]);
						}
					}
					System.out.println("Table element selected is: " + Data);
				}
			});
			JScrollPane sp = new JScrollPane(jt);
			panel.add(sp);

			InboxUpdater iupdater = new InboxUpdater(jt, defaultTableModel, dis, null);
			Thread t = new Thread(iupdater);
			// iu.add(iupdater);
			t.start();

		} else if (text.equals("sent")) {
			String column[] = new String[4];

			column[0] = "To";
			column[1] = "Subject";
			column[2] = "Body";
			column[3] = "Time";
			DefaultTableModel defaultTableModel;
			JTable jt;

			defaultTableModel = new DefaultTableModel(dataMatrix, column);
			jt = new JTable(defaultTableModel);

			setDefaultSentTableModel(defaultTableModel);
			setSentJtable(jt);

			jt.setCellSelectionEnabled(true);
			ListSelectionModel select = jt.getSelectionModel();
			select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			select.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					String Data = null;
					int[] row = jt.getSelectedRows();
					int[] columns = jt.getSelectedColumns();
					for (int i = 0; i < row.length; i++) {
						for (int j = 0; j < columns.length; j++) {
							Data = (String) jt.getValueAt(row[i], columns[j]);
						}
					}
					System.out.println("Table element selected is: " + Data);
				}
			});
			JScrollPane sp = new JScrollPane(jt);
			panel.add(sp);

		}
		return panel;
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = EmailTabs.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}