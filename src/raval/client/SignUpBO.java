package raval.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class SignUpPanel extends JPanel implements ActionListener {
	private JButton RegisterButton;
	private JTextField UsernameField, NameField;
	private JPasswordField PasswordField, PasswordField1;
	private String UName, PsWord, PsWord1, Name;

	public SignUpPanel() {
		RegisterButton = new JButton("Register"); // initializing two button
													// references

		UsernameField = new JTextField(15);
		PasswordField = new JPasswordField(15);
		PasswordField1 = new JPasswordField(15);
		// NameField = new JTextField(15);

		JLabel UsernameLabel = new JLabel("Email: ");
		JLabel PasswordLabel = new JLabel("Password: ");
		JLabel PasswordLabel1 = new JLabel("Re-enter Password");
		// JLabel NameLabel = new JLabel("Name");

		JPanel UsernamePanel = new JPanel();
		JPanel PasswordPanel = new JPanel();
		JPanel PasswordPanel1 = new JPanel();
		// JPanel NamePanel = new JPanel();

		UsernamePanel.add(UsernameLabel);
		UsernamePanel.add(UsernameField);
		PasswordPanel.add(PasswordLabel);
		PasswordPanel.add(PasswordField);
		PasswordPanel1.add(PasswordLabel1);
		PasswordPanel1.add(PasswordField1);
		// NamePanel.add(NameLabel);
		// NamePanel.add(NameField);

		add(UsernamePanel);
		add(PasswordPanel);
		add(PasswordPanel1);
		// add(NamePanel);

		add(RegisterButton); // add the two buttons on to this panel
		RegisterButton.addActionListener(this); // event listener registration
	}

	public void actionPerformed(ActionEvent evt) // event handling
	{
		// Object source = evt.getSource(); //get who generates this event
		String arg = evt.getActionCommand();
		if (arg.equals("Register")) { // determine which button is clicked
			UName = UsernameField.getText(); // take actions
			PsWord = PasswordField.getText();
			PsWord1 = PasswordField1.getText();
			// Name = NameField.getText();

			if (!PsWord.equals(PsWord1)) {
				JOptionPane.showMessageDialog(null, "Please does not matched", "Confirmation",
						JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else {
				ClientApplication ca = new ClientApplication(UName, PsWord);
				try {
					ca.runClient("signup");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to Create EmailId", "Confirmation",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

		}
	}
}

public class SignUpBO extends JFrame {
	private SignUpPanel SU_Panel;

	public SignUpBO() {
		setTitle("Sign Up");
		setSize(340, 210);

		// get screen size and set the location of the frame
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		setLocation(screenWidth / 3, screenHeight / 4);

		addWindowListener(new WindowAdapter() // handle window event
		{
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		Container contentPane = getContentPane(); // add a panel to a frame
		SU_Panel = new SignUpPanel();
		contentPane.add(SU_Panel);
		show();
	}

	/*
	 * public static void main(String [] args) { JFrame frame = new SignUpBO();
	 * //initialize a JFrame object frame.show(); //display the frame }
	 */
}
