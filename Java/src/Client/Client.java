package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Client extends Thread{
	
	ConnectGUI connectWindow;
	private String host = "Admin-PC";
	private int portNumber = 5000;
	private Socket s;
	private PrintWriter s_out = null;
	private BufferedReader s_in = null;
	private String username = "";
	private CommunicationHandler communicationHandler;
	private Boolean isConnected = false;
	public ChatRoomGUI window;
	
	public ErrorBoxGUI eBCS;
	public ErrorBoxGUI eBUH;
	
	private Calendar cal;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public Client(CommunicationHandler cH){
		super.start();
		communicationHandler = cH;
	}
	
	public void run(){
		try{
			connect();
			if(isConnected){
				choseUsername();
				openChat();
			}else{
				run();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void openChat(){
		try {
			window = new ChatRoomGUI(this);
			communicationHandler.setIsConnected(true);
            setCHConfiguration();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connect() throws IOException{
		try {
			connectWindow = new ConnectGUI(this);
			connectWindow.open();
		} catch (Exception e) {
			e.printStackTrace();
			connect();
		}
	}
	
	public void tryConnect() throws IOException{
		try{
			s = new Socket();
	        s.connect(new InetSocketAddress(host , portNumber));
	        System.out.println("Connected");
        	//writer for socket
            s_out = new PrintWriter( s.getOutputStream(), true);
            //reader for socket
            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            isConnected = true;
            connectWindow.close();
        }catch(ConnectException cE){
        	System.err.println("Cannot Connect");
        	eBCS = new ErrorBoxGUI("Cannot Connect");
        	eBCS.open();
        }
		catch (UnknownHostException e){//Host not found
            System.err.println("Don't know about host : " + host);
            eBUH = new ErrorBoxGUI("Don't know about host : " + host);
            eBUH.open();
        }
	}
	
	public void choseUsername(){
		try {
			UsernameGUI window = new UsernameGUI(this);
			window.open();
			if(!username.equals("")){
				s_out.println(username);
			}else{
				choseUsername();
			}
		} catch (Exception e) {
			e.printStackTrace();
			choseUsername();
		}
	}
	
	private void setCHConfiguration(){
		communicationHandler.setS_in(s_in);
        communicationHandler.setS_out(s_out);
        communicationHandler.setS(s);
	}
	
	public void updateUserListChatGUI(ArrayList<String> users){
		window.addUserList(users);
	}
	
	public void updateMessageChatGUI(String msg){
		try{
			if(msg.substring(0,8).equals("[GLOBAL]")){
				System.out.println(msg.substring(9, 9 + username.length()));
				if(!msg.substring(9, 9 + username.length()).equals(username)){
					Calendar cal = Calendar.getInstance();
					msg = "(" + sdf.format(cal.getTime()) + ")" + msg;
					window.updateChat(msg);
				}
			}else{
				Calendar cal = Calendar.getInstance();
				msg = "(" + sdf.format(cal.getTime()) + ")" + msg;
				window.updateChat(msg);
			}
			
		}catch(StringIndexOutOfBoundsException SIOBE){
			Calendar cal = Calendar.getInstance();
			msg = "(" + sdf.format(cal.getTime()) + ")" + msg;
			window.updateChat(msg);
		}
	}

	public void sendMessage(String receiver,String msg){
		s_out.println("/M");
		s_out.println((receiver.equals("")) ? "GLOBAL" : receiver);
		s_out.println(msg);
	}
	
	public void sendEXIT(){
		s_out.println("/EXIT");
	}
	
	public synchronized Boolean getIsConnected() {
		return isConnected;
	}

	public synchronized void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	public synchronized String getHost() {
		return host;
	}

	public synchronized void setHost(String host) {
		this.host = host;
	}

	public synchronized int getPortNumber() {
		return portNumber;
	}

	public synchronized void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public synchronized String getUsername() {
		return username;
	}

	public synchronized void setUsername(String username) {
		this.username = username;
	}

	public ChatRoomGUI getWindow() {
		return window;
	}
}
