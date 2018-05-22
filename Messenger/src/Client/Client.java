package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

class Client extends Thread
{
	private static String login, IP;
	private static int port;
	private static String userList;
	private int clock = -100;
	
	private static final int CLOCKS_PER_REFRESH = 400000000;

    Client(String _name, String _address, int _port) throws Exception
    {
    	 login = _name.toUpperCase();
    	 IP = _address;
    	 port = _port; 
    	 
    	sendToServer(XML.XMLParser.identifyUser(login));
    }
    
    public static String getUsername()
    {
    	return login;
    }  
    
    public void listenMessages() {
		 
		 try {     
			 while(true) {
				if (++clock % CLOCKS_PER_REFRESH == 0) {
					refreshUsers();
	            	Chat.refreshMessages();
					clock = 0;
				}			
			 }
		 } catch (Exception e) {
	   		 
	   	 }
    }
    
    public static void refreshUsers() {
    	String response = sendToServer(XML.XMLParser.getUsersList());
    	XML.Format format = XML.XMLParser.parse(response);
		if (!response.equals(userList)) {
        	Chat.refreshUsers(format.response.users);
        	userList = response;
		}
    }
    
    public static void sendMessage(String message, String addressee) {
    	sendToServer(XML.XMLParser.sendMessage(login.toUpperCase(), addressee, message));
    }
    
    
    public static String sendToServer(String message) {
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

    	return "server do not respond";
    }
}
