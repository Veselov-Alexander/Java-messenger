package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerConfig extends JFrame {

	private JPanel contentPane;
	private JTextField IP;
	private JTextField Port;
	private JTextField DBuser;
	private JTextField DBpassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerConfig frame = new ServerConfig();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerConfig() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 425, 202);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		IP = new JTextField();
		IP.setText("127.0.0.1");
		IP.setBounds(85, 20, 97, 22);
		contentPane.add(IP);
		IP.setColumns(10);
		
		Port = new JTextField();
		Port.setText("9090");
		Port.setBounds(85, 59, 97, 22);
		contentPane.add(Port);
		Port.setColumns(10);
		
		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(26, 23, 39, 16);
		contentPane.add(lblIp);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(26, 62, 56, 16);
		contentPane.add(lblPort);
		
		JButton Submit = new JButton("Run");
		Submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (IP.getText().isEmpty() ) {
					JOptionPane.showMessageDialog(contentPane,
		  					  					  "Empty IP field",
		  					  					  "Server error",
		  					  					  JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (DBuser.getText().isEmpty() ) {
					JOptionPane.showMessageDialog(contentPane,
		  					  					  "DB user login is empty",
		  					  					  "Server error",
		  					  					  JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (Port.getText().isEmpty()) {
					JOptionPane.showMessageDialog(contentPane,
		  					  					  "Empty port field",
		  					  					  "Server error",
		  					  					  JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (DB.tryConnectToDB(DBuser.getText(), DBpassword.getText())) {
					DB.user = DBuser.getText();
					DB.password = DBpassword.getText();

				} else {
					JOptionPane.showMessageDialog(contentPane,
		  					  "Can't connect to database",
		  					  "Database error",
		  					  JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				try {
					ServerUI UI = new ServerUI();
					
					new Thread(new Runnable() {
						Server server = new Server(IP.getText(), Integer.parseInt(Port.getText()), UI);
						public void run() {
							server.acceptClients();
						}
					}).start();
										
					UI.setVisible(true);
					dispose();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane,
		  					  					  "Ñan not create server",
		  					  					  "Server error",
		  					  					  JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		Submit.setBounds(288, 117, 97, 25);
		contentPane.add(Submit);
		
		JLabel lblDbUser = new JLabel("DB user:");
		lblDbUser.setBounds(204, 23, 56, 16);
		contentPane.add(lblDbUser);
		
		JLabel lblDbPassword = new JLabel("DB password:");
		lblDbPassword.setBounds(204, 62, 90, 16);
		contentPane.add(lblDbPassword);
		
		DBuser = new JTextField();
		DBuser.setText("root");
		DBuser.setBounds(288, 20, 97, 22);
		contentPane.add(DBuser);
		DBuser.setColumns(10);
		
		DBpassword = new JTextField();
		DBpassword.setText("1234567");
		DBpassword.setBounds(288, 59, 97, 22);
		contentPane.add(DBpassword);
		DBpassword.setColumns(10);
	}
}
