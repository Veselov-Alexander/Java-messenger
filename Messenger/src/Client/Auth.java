package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPasswordField;

public class Auth extends JFrame {

	private JPanel contentPane;
	private JTextField IP;
	private JTextField Login;
	private JTextField Port;
	private JPasswordField Password;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Auth frame = new Auth();
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
	public Auth() {
		setTitle("Auth");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 239);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("IP-address:");
		lblIp.setBounds(34, 52, 68, 16);
		contentPane.add(lblIp);
		
		IP = new JTextField();
		IP.setText("127.0.0.1");
		IP.setBounds(114, 49, 98, 22);
		contentPane.add(IP);
		IP.setColumns(10);
		
		JLabel lblNickname = new JLabel("Login:");
		lblNickname.setBounds(34, 94, 68, 16);
		contentPane.add(lblNickname);
		
		Login = new JTextField();
		Login.setBounds(114, 91, 98, 22);
		contentPane.add(Login);
		Login.setColumns(10);
		
		JButton Submit = new JButton("Login");
		Submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Login.getText().isEmpty()) {
					JOptionPane.showMessageDialog(contentPane,
												  "Login field is empty",
												  "Login required",
												  JOptionPane.ERROR_MESSAGE);
				} else {
					String request = XML.XMLParser.login(Login.getText(), Password.getText());
					String status = sendToServer(request, IP.getText(), Integer.parseInt(Port.getText()));
					if (status.equals("success")) {
						login();
					} else if (status.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane,
								  					  "Server do not respond",
								  					  "Connection error",
								  					  JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(contentPane,
			  					  "Incorrect login or password",
			  			          "Auth error",
			                      JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		Submit.setBounds(222, 133, 90, 25);
		contentPane.add(Submit);
		
		Port = new JTextField();
		Port.setText("9090");
		Port.setForeground(Color.BLACK);
		Port.setBounds(271, 49, 41, 22);
		contentPane.add(Port);
		Port.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(222, 52, 56, 16);
		contentPane.add(lblPort);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 137, 68, 16);
		contentPane.add(lblPassword);
		
		Password = new JPasswordField();
		Password.setToolTipText("");
		Password.setBounds(114, 136, 98, 22);
		contentPane.add(Password);
		
		JButton Register = new JButton("Register");
		Register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String request = XML.XMLParser.register(Login.getText(), Password.getText());
				String status = sendToServer(request, IP.getText(), Integer.parseInt(Port.getText()));
				if (status.equals("success")) {
					login();
				} else if (status.isEmpty()) {
					JOptionPane.showMessageDialog(contentPane,
		  					  					  "Server do not respond",
		  					  					  "Connection error",
		  					  					  JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(contentPane,
		  					  "This user already exist",
		  			          "Register error",
		                      JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		Register.setBounds(222, 90, 90, 25);
		contentPane.add(Register);
	}
	
	public void login() {
		try
		{							
			new Thread(new Runnable() {	
				Client client = new Client(Login.getText(),
					   	   				   IP.getText(),
					   	   				   Integer.parseInt(Port.getText()));
				public void run() {
					client.listenMessages();
				}
			}).start();
			
			Chat chat = new Chat();
			chat.setVisible(true);
			dispose();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPane,
					  					  "Server do not respond",
					  			          "Connection error",
					                      JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String sendToServer(String message, String IP, int port) {
    	byte buffer[] = new byte[64 * 1024];
    	try {

        	Socket socket = new Socket(IP, port);
			socket.getOutputStream().write(message.getBytes());

			int length = socket.getInputStream().read(buffer);

			String response = new String(buffer, 0, length);
			socket.close();
			return response;
		} catch (IOException e) {

		}

    	return "";
    }
}
