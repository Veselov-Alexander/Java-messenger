package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Server extends Thread
{
    public Socket socket;
    
    private static boolean isRunning = false;
    
    private String IP;
    private int port;
    
    private ServerSocket server;
    private ServerUI UI;
    

    public Server(String _IP, int _port, ServerUI _UI) throws Exception
    {
    	IP = _IP;
    	port = _port;
    	UI = _UI;
    	server = new ServerSocket(port, 0, InetAddress.getByName(IP));
    	isRunning = true;
    	ServerUI.logWrite("Server is started at: " + getPath()+'\n');
    }
    
    public String getPath()
    {
    	return server.getInetAddress().getHostAddress() +
    		   ":" +
    		   server.getLocalPort();
    }
    
    
    public Server(Socket _socket, ServerUI _UI)
    {
    	socket = _socket;	
    	UI = _UI;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }
    
    public void acceptClients() {
    	try
        {
            while(isRunning)
            {
            	try {
            		new Server(server.accept(), UI);
            	} catch (Exception e) {
            		
            	}
            }
        } catch(Exception e) {
        	
        }
    }
   

    public void run()
    { 	
        try
        {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            
            byte data[] = new byte[64*1024];
            String message = new String(data, 0, inputStream.read(data));

            parse(message, outputStream);
            socket.close();
        }
        catch(Exception e) {
        	System.out.println("Initialization error: " + e);
        }
    }
    
    public void parse(String message, OutputStream outputStream) throws IOException {
    	XML.Format format = XML.XMLParser.parse(message);
    	
    	if (format.request != null)
    	{
    		switch (format.request.type) {
    		
    		case "send_message": {
    			DB.sendMessage(format.request.user, format.request.addressee, format.request.message.date, format.request.message.text);
    			ServerUI.logWrite("["+format.request.message.date+"]\r\n" + format.request.user + " sended message to " + format.request.addressee + "\r\n"+format.request.message.text + "\r\n");
    	    	ServerUI.chatWrite(message);
    			outputStream.write("success".getBytes());
    		} break;
    		
    		case "identify_user": {
    			DB.setStatus(format.request.user, "online");
    			outputStream.write("success".getBytes());
    		} break;
    		
    		case "logout": {
    			DB.setStatus(format.request.user, "offline");
    			ServerUI.logWrite(format.request.user + " has disconnected.\r\n");
    	    	 ServerUI.chatWrite(message);
    			outputStream.write("success".getBytes());
    		} break;
    		
    		case "get_users_list": {
    			List<XML.User> users = new ArrayList<>();
    			List<String> list = DB.getUsersList();
    			for(int user = 0; user < list.size() / 2; ++user) {
    				users.add(new XML.User(list.get(user*2), list.get(user*2 + 1)));
    			}
    			String usersList = XML.XMLParser.sendUsersList(users);
                outputStream.write(usersList.getBytes());
    		} break;
    		
    		case "login": {
    			if (format.request.password == null) {
					format.request.password = "";
				}
    	    	ServerUI.chatWrite(message);
    			if (DB.auth(format.request.user, format.request.password)) {
    				ServerUI.logWrite(format.request.user.toUpperCase() + " has connected.\r\n");
    				
        			outputStream.write("success".getBytes());
    			} else {
    				outputStream.write("error".getBytes());
    			}

    		} break;
    		
    		case "register": {
    	    	ServerUI.chatWrite(message);
    			if (DB.userExist(format.request.user)) {
    				if (format.request.password == null) {
    					format.request.password = "";
    				}
    				DB.register(format.request.user, format.request.password);
    				ServerUI.logWrite(format.request.user.toUpperCase() + " has registered.\r\n");
        			outputStream.write("success".getBytes());
    			} else {
    				outputStream.write("error".getBytes());
    			}
    		} break;
    		
    		case "get_messages": {
    			List<String> list = DB.getMessages(format.request.user, format.request.addressee);
    			List<XML.Message> messageList = new ArrayList<>();
    			for(int mess = 0; mess < list.size() / 3; ++mess) {
    				messageList.add(new XML.Message(list.get(mess * 3 + 2), list.get(mess * 3 + 1), list.get(mess * 3)));
    			}
    			outputStream.write(XML.XMLParser.sendMessageList(messageList).getBytes());
    		} break;
    		
    		}
    	} else {
    		
    	}
    }
}
