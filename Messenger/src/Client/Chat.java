package Client;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Chat extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	private static JTextPane chatField;
	private static JList<String> list;
	private static DefaultListModel<String> model;
	public static String selectedUser;


	public Chat()  {

		selectedUser = Client.getUsername().toUpperCase();
		setTitle(Client.getUsername() + ". Chat with " + selectedUser + ".");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		chatField = new JTextPane();	
		chatField.setEditable(false);
		DefaultCaret caret = (DefaultCaret)chatField.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollTextPane = new JScrollPane(chatField);
		scrollTextPane.setBounds(12, 13, 435, 264);
		contentPane.add(scrollTextPane);
		
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					if (!textField.getText().isEmpty()) {
						Client.sendMessage(textField.getText(), selectedUser);
						textField.setText(null);
						refreshMessages();
					}
				}
			}
		});
		textField.setBounds(12, 290, 435, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!textField.getText().isEmpty()) {
					Client.sendMessage(textField.getText(), selectedUser);
					textField.setText(null);
					refreshMessages();
				}
			}
		});
		btnNewButton.setBounds(459, 290, 126, 25);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Users:");
		lblNewLabel.setBounds(459, 13, 56, 16);
		contentPane.add(lblNewLabel);
		
		model = new DefaultListModel<>();
		list = new JList<>( model );
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!list.isSelectionEmpty()) {
					selectedUser = list.getSelectedValue().split(" ")[0];
					setTitle(Client.getUsername() + ". Chat with " + selectedUser + ".");
					refreshMessages();
				}
			}
		});
		list.setBounds(459, 39, 126, 238);
		contentPane.add(list);
		
		addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			Client.sendToServer(XML.XMLParser.logout(Client.getUsername()));
			System.exit(0);
		}});
	}
	
	public static void refreshUsers(List<XML.User> users) {
		
		model.clear();
		
		for(XML.User user : users) {
			String status = " [online]";
			if (user.status.equals("offline")) {
				status = " .";
			}
			model.addElement(user.name + status);
		}
	}
	
    public static void refreshMessages() {
    	String response = Client.sendToServer(XML.XMLParser.getMessages(Client.getUsername(), selectedUser));
    	XML.Format format = XML.XMLParser.parse(response);
    	String newMessages = "";
    	for(XML.Message message : format.response.messages) {
    		newMessages += "[" + message.date.substring(11,19) + "] " + message.sender + ":   " + message.text + "\r\n";
    	}
    	if (!chatField.getText().equals(newMessages)) {
    		chatField.setText(newMessages);
    	}
    }
	
	public static void print(String message) {
		chatField.setText(chatField.getText() + message + '\n');
	}
}
