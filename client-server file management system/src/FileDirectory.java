import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JList;

public class FileDirectory {

	private JFrame frame;
	private JTextField textField;
	private static String uname;
	private File fileToUpload;
	private File fileToDownload;
	private DefaultListModel demoList = new DefaultListModel();
	private JList list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileDirectory window = new FileDirectory();
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
	public FileDirectory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DcClient user = new DcClient();
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 311);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Button to choose a file for upload
		JButton bttnchooseFile = new JButton("Choose File");
		bttnchooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
			      int returnValue = fileChooser.showOpenDialog(null);
			      if (returnValue == JFileChooser.APPROVE_OPTION) {
			        File selectedFile = fileChooser.getSelectedFile();
			        fileToUpload = selectedFile;
			        //java.awt.Desktop.getDesktop().open(selectedFile);
			        //if (fileChooser.showOpenDialog(bttnchooseFile) == JFileChooser.APPROVE_OPTION){
		                textField.setText(selectedFile.getAbsolutePath());
			        //}
			      }
			}
		});
		bttnchooseFile.setBounds(53, 46, 122, 23);
		frame.getContentPane().add(bttnchooseFile);
		
		JButton btnupload = new JButton("Upload to Server");
		btnupload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//byte[] array = Files.readAllBytes(new File(fileToUpload.getAbsolutePath()).toPath());
				byte[] fileInBytes;
				try {
					DcClient client = new DcClient();
					fileInBytes = client.fileToByteArray(fileToUpload.getAbsolutePath(), fileToUpload.length());
					String fileContents = new String(fileInBytes);
					client.sendMessage("501 " +fileToUpload.getName());	// Send the name
					client.sendMessage("502 " +fileContents);	// Send the file
					updateFileList();
				} catch (IOException ex) {
					ex.printStackTrace( );
				}
			       
			}
		});
		btnupload.setBounds(53, 80, 178, 23);
		frame.getContentPane().add(btnupload);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DcClient client = new DcClient();
				client.sendMessage("302" + uname);
				frame.dispose();
				Gui.main(null);
			}
		});
		btnLogOut.setBounds(360, 238, 89, 23);
		frame.getContentPane().add(btnLogOut);
		
		JLabel lblNewLabel = new JLabel("Upload To Server");
		lblNewLabel.setBounds(53, 21, 122, 14);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(185, 47, 225, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btndownload = new JButton("Download");
		btndownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedFile = list.getSelectedValue().toString();
				System.out.println(selectedFile);
				DcClient client = new DcClient();

				client.sendMessage("503 " +selectedFile);	// Send the name

				// client.lastMessage should contain the file
				try {
					byte[] fileInBytes = client.lastMessage.getBytes();
					File newFile = new File("C:\\DsFiles\\downloads\\"+selectedFile);
					newFile.createNewFile();
					FileOutputStream f=new FileOutputStream("C:/DsFiles/downloads/"+ selectedFile);
					f.write(fileInBytes);
					f.close();
				} catch (IOException ex) {
					ex.printStackTrace( );
				}

			}
		});
		btndownload.setBounds(271, 198, 178, 23);
		frame.getContentPane().add(btndownload);
		demoList.addElement("No Files Found");
		list = new JList(demoList);
		list.setBounds(53, 114, 178, 132);
		frame.getContentPane().add(list);
		updateFileList();
	}
	
	private void updateFileList() {
		DcClient client = new DcClient();
		client.sendMessage("504 listFiles");
		String fileList = client.lastMessage;
		
		demoList.clear();
		
		String[] files = fileList.split(";");
		for (int i = 0; i < files.length; i++) {
			demoList.addElement(files[i]);
		}
	}
	
	public static void setUname(String Uname) 
	{
		uname = Uname;
	}
}
