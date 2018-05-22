package XML;

public class Message {
	public Message () {}
	public Message(String text, String date, String sender) {
		this.text = text;
		this.date = date;
		this.sender = sender;
	}
	public String text;
	public String date;
	public String sender;
}
