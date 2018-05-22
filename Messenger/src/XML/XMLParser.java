package XML;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParser {

	public static Format parse(String XML) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		Handler handler = new Handler();
		try {
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setContentHandler(handler);	
		xmlReader.parse(new InputSource(new ByteArrayInputStream(XML.getBytes("utf-8"))));
		} catch (Exception e) {}
		return handler.getFormat();
	}
	
	
	//client
	public static String sendMessage(String senderID,
								     String addresseeID, 
								     String messageText) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
	
			
			out.writeStartElement("request");
			out.writeAttribute("type", "send_message");
			
			out.writeStartElement("sender");
			out.writeCharacters(senderID);
			out.writeEndElement();
			
			out.writeStartElement("message");
			
			out.writeStartElement("addressee");
			out.writeCharacters(addresseeID);
			out.writeEndElement();
			
			out.writeStartElement("text");
			out.writeCharacters(messageText);
			out.writeEndElement();
			
			out.writeStartElement("date");
			out.writeCharacters(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			out.writeEndElement();
			
			out.writeEndElement();
			
			out.writeEndElement();

	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}

	public static String getUsersList() {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "get_users_list");			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}

	public static String identifyUser(String user) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "identify_user");
			
			out.writeStartElement("sender");
			out.writeCharacters(user);
			out.writeEndElement();
			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String getMessages(String user, String addressee) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "get_messages");
			
			out.writeStartElement("sender");
			out.writeCharacters(user);
			out.writeEndElement();
			
			out.writeStartElement("addressee");
			out.writeCharacters(addressee);
			out.writeEndElement();
			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String logout(String user) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "logout");
			
			out.writeStartElement("sender");
			out.writeCharacters(user);
			out.writeEndElement();
			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String register(String login, String password) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "register");
			
			out.writeStartElement("sender");
			out.writeCharacters(login);
			out.writeEndElement();
			
			out.writeStartElement("password");
			out.writeCharacters(password);
			out.writeEndElement();
			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String login(String login, String password) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
			
			out.writeStartElement("request");
			out.writeAttribute("type", "login");
			
			out.writeStartElement("sender");
			out.writeCharacters(login);
			out.writeEndElement();
			
			out.writeStartElement("password");
			out.writeCharacters(password);
			out.writeEndElement();
			
			out.writeEndElement();
	
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	//server
	public static String sendUsersList(List<User> usersList) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
	
			
			out.writeStartElement("response");
			out.writeStartElement("list");
			
			for(User user : usersList) {
				out.writeStartElement("user");
				
				out.writeStartElement("name");
				out.writeCharacters(user.name);
				out.writeEndElement();
				
				out.writeStartElement("status");
				out.writeCharacters(user.status);
				out.writeEndElement();
				
				out.writeEndElement();
			}

			out.writeEndElement();
			out.writeEndElement();
			
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}
	
	public static String sendMessageList(List<Message> usersList) {
		try 
		{
			StringWriter stringOut = new StringWriter();
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
	
			out.writeStartDocument();
			out.writeStartElement("format");
	
			
			out.writeStartElement("response");
			out.writeStartElement("messages");
			
			for(Message user : usersList) {
				out.writeStartElement("message");
				
				out.writeStartElement("text");
				out.writeCharacters(user.text);
				out.writeEndElement();
				
				out.writeStartElement("date");
				out.writeCharacters(user.date);
				out.writeEndElement();
				
				out.writeStartElement("message_sender");
				out.writeCharacters(user.sender);
				out.writeEndElement();
				
				out.writeEndElement();
			}

			out.writeEndElement();
			out.writeEndElement();
			
			out.writeEndElement();
			out.writeEndDocument();
	
			out.close();
			return stringOut.toString();
		} catch (Exception e) {}
		return "";
	}

	
	
	//parser
	private static class Handler extends DefaultHandler {
		
		private Format format;
		
		private User currentUser;
		private Message currentMessage;
		
		private String currentTag;
		
		private String requestType = "undefined";
		
		public void startDocument() throws SAXException {

		}
		
		@Override
		public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
			currentTag = tag;
			
			switch(currentTag) {
			case "format": {
				format = new Format();
			} break;
			
			case "request": {
				format.request = new Request();
				format.request.type = attributes.getValue("type");
				requestType = format.request.type;
			} break;
			
			case "message": {
				currentMessage = new Message();
			} break;
			
			case "user": {
				currentUser = new User();
			} break;
			
			case "messages": {
				format.response.messages = new ArrayList<>();
			} break;
			
			case "list": {
				format.response.users = new ArrayList<>();
			} break;
			
			case "response": {
				format.response = new Response();
			} break;
			
			}
		}
		
		@Override
		public void characters(char[] characters, int start, int lenght) throws SAXException {
			String text = new String (characters, start, lenght);
		
			if (text.contains("<") || text.contains("\n") || currentTag == null) {
				return;
			}
			
			switch (currentTag) {
			case "text": {
				currentMessage.text = text;
			} break;
			
			case "date": {
				currentMessage.date = text;
			} break;
			
			case "name": {
				currentUser.name = text;
			} break;
			
			case "status": {
				currentUser.status = text;
			} break;
			
			case "sender": {
				format.request.user = text;
			} break;
			
			case "message_sender": {
				currentMessage.sender = text;
			} break;
			
			case "password": {
				format.request.password = text;
			} break;
			
			case "addressee": {
				format.request.addressee = text;
			} break;
			
			default: {}
			}
		}
		
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			switch(qName) {
			
			case "message": {
				if (requestType.equals("send_message"))
				{
					format.request.message = currentMessage;
					currentMessage = null;
				} else {
					format.response.messages.add(currentMessage);
					currentMessage = null;
				}
			} break;
			
			case "user": {
				format.response.users.add(currentUser);
				currentUser = null;
			} break;
			}
		}
		
		
		Format getFormat() {
			return format;
		}
	}
}
