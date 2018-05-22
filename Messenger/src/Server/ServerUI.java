package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;


public class ServerUI extends JFrame {

	private JPanel contentPane;
	private static JTextArea logArea;
	private static JTextArea chatArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI frame = new ServerUI();
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
	public ServerUI() {	
		
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		DefaultCaret logCaret = (DefaultCaret)logArea.getCaret();
		logCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		JScrollPane scrollLog= new JScrollPane(logArea);	
		scrollLog.setBounds(440, 38, 229, 324);
		contentPane.add(scrollLog);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		DefaultCaret chatCaret = (DefaultCaret)chatArea.getCaret();
		chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		JScrollPane scrollChat = new JScrollPane(chatArea);
		scrollChat.setBounds(12, 13, 420, 349);
		contentPane.add(scrollChat);
		
		JLabel lblLog = new JLabel("Log:");
		lblLog.setBounds(440, 13, 56, 16);
		contentPane.add(lblLog);
	}
	
	public static void logWrite(String message) {
		logArea.append(message + '\n');
	}
	
	public static void chatWrite(String message) {
		chatArea.append(message + '\n');
	}
	
	
}
