package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB {
	private static final String url = "jdbc:mysql://localhost:3306/veselovmessenger?autoReconnect=true&useSSL=false";
    public static String user;
    public static  String password;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet queryResult;
    
    public static List<String> getUsersList() {
    	return select("select login, status from user", 2);
    }
    
    public static boolean auth(String login, String password) {
    	return !select("select * from user where login = \"" + login.toUpperCase() + "\"and password = \"" + password+"\"", 4).isEmpty();
    }
    
    public static boolean userExist(String login) {
    	return select("select * from user where login = \"" + login.toUpperCase() + "\"", 4).isEmpty();
    }
    
    public static List<String> getMessages(String sender, String addressee) {
    	return select("select sender, date, text from messages where (sender = '"+sender+"' and addressee = '"+addressee+"') or (sender = '"+addressee+"' and addressee = '"+sender+"')  order by date", 3);
    }
    
    public static List<String> select(String command, int columnCount) {
    	List<String> items = new ArrayList<>();
    	try {
             connection = DriverManager.getConnection(url, user, password);
             statement = connection.createStatement();
             queryResult = statement.executeQuery(command);


             while (queryResult.next()) {
            	 for(int column = 1; column <= columnCount; ++column) {
            		 items.add(queryResult.getString(column));
            	 }
             }

        } catch (SQLException e) {

        } finally {
             try { connection.close(); } catch(SQLException se) { }
             try { statement.close(); } catch(SQLException se) { }
             try { queryResult.close(); } catch(SQLException se) { }
        }
    	return items;
    }
    
    public static List<String> register(String login, String password) {
    	List<String> items = new ArrayList<>();
    	try {
             connection = DriverManager.getConnection(url, user, DB.password);

             statement = connection.createStatement();

             String command = String.format("insert into user(`login`, `password`, `status`) values(\"%s\", \"%s\", \"%s\")",
            		 login.toUpperCase(), password, "online");
             
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {

        } finally {
             try { connection.close(); } catch(SQLException se) { }
             try { statement.close(); } catch(SQLException se) { }
        }
    	return items;
    }
    
    public static void setStatus(String login, String status) {
    	try {
             connection = DriverManager.getConnection(url, user, DB.password);

             statement = connection.createStatement();

             String command ="update user set status = \""+status+"\" where login = '"+login+"'";
             
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {

        } finally {
             try { connection.close(); } catch(SQLException se) { }
             try { statement.close(); } catch(SQLException se) { }
        }
    }
   
    
    public static List<String> sendMessage(String sender, String addressee, String date, String message) {
    	List<String> items = new ArrayList<>();
    	try {
             connection = DriverManager.getConnection(url, user, DB.password);

             statement = connection.createStatement();

             String command = String.format("insert into messages values(\"%s\", \"%s\", \"%s\", \"%s\")",
            		 sender, addressee, date, message);
             
             ServerUI.chatWrite(command);
             
             
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {

        } finally {
             try { connection.close(); } catch(SQLException se) { }
             try { statement.close(); } catch(SQLException se) { }
        }
    	return items;
    }
    
    public static boolean tryConnectToDB(String login, String password) {
    	try {
        	connection = DriverManager.getConnection(url, login, password);
        	return true;
    	} catch (Exception e) {
    		return false;
    	}

    }
    
    
    
}
