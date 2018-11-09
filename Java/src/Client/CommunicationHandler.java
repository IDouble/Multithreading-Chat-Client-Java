package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommunicationHandler extends Thread {
	
	private Boolean isActive = true;
	
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<String> message = new ArrayList<String>();
	
	private Socket s = new Socket();
	private PrintWriter s_out = null;
	private BufferedReader s_in = null;
	private String response = "";
	private Client client;
	private Boolean isConnected = false;
	
	public CommunicationHandler(){
		super.start();
	}
	
	public void run(){
		try {
			client = new Client(this);
			while(isActive){
				if(getIsConnected()){
					while((response = s_in.readLine()) != null){
						if(response.equals("/GAU")){
							String uList;
							while((uList = s_in.readLine()) != null){
								System.out.println("TEST Userlist");
								userList = setUserList(uList);
								System.out.println("Response:" + userList);
								client.updateUserListChatGUI(userList);
						    	if(uList.contains("[END]")){
						    		break;
						    	}
							}
						}
						else if(response.equals("/M")){
							String messageResponse;
							while((messageResponse = s_in.readLine()) != null){
								client.updateMessageChatGUI(messageResponse);
						    	break;
							}
							System.out.println("Message Test");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> setUserList(String rspUserList){
		ArrayList<String> parts = new ArrayList<String>(Arrays.asList(rspUserList.split(":")));
		parts.remove("[END]"); // Arraylist
		return parts;
	}
	
	public synchronized Boolean getIsConnected() {
		return isConnected;
	}

	public synchronized void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public synchronized Socket getS() {
		return s;
	}

	public synchronized void setS(Socket s) {
		this.s = s;
	}
	
	public synchronized PrintWriter getS_out() {
		return s_out;
	}

	public synchronized void setS_out(PrintWriter s_out) {
		this.s_out = s_out;
	}

	public synchronized BufferedReader getS_in() {
		return s_in;
	}

	public synchronized void setS_in(BufferedReader s_in) {
		this.s_in = s_in;
	}
	
	public synchronized Client getClient() {
		return client;
	}

	public synchronized void setClient(Client client) {
		this.client = client;
	}
}
