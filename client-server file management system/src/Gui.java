import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui {

	private JFrame frame;
	private JTextField username;
	private JLabel lblWelcomePleaseEnter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 349, 241);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(24, 84, 97, 25);
		frame.getContentPane().add(lblUsername);
		
		username = new JTextField();
		username.setBounds(90, 85, 195, 23);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String uname=username.getText();
			
				DcClient client = new DcClient();
				client.sendMessage("301"+uname);
				
				//frmFilesystemClient.dispose();
				FileDirectory.setUname(uname);
				FileDirectory.main(null);
				
			}
		});
		
		btnLogIn.setBounds(196, 150, 89, 23);
		frame.getContentPane().add(btnLogIn);
		
		lblWelcomePleaseEnter = new JLabel("Welcome Please Enter Username to Log In");
		lblWelcomePleaseEnter.setBounds(44, 26, 261, 14);
		frame.getContentPane().add(lblWelcomePleaseEnter);
	}
}
