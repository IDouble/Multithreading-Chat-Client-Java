package Client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;

public class ChatRoomGUI {

	protected Shell shell;
	private Group group;
	
	private Button btnSend;
	private Text message;
	private Text txtChat;
	private String text = "";
	private List userList;
	private String selectedRCVRString = "";
	private static Client client;
	
	private Calendar cal;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public ChatRoomGUI(Client c){
		setClient(c);
	}
	/**
	 * Launch the application.
	 * @param args
	 */

	/**
	 * Open the window.
	 * @throws IOException 
	 */
	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() throws IOException {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @throws IOException 
	 */
	protected void createContents() throws IOException {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				client.sendEXIT();
				System.exit(0);
			}
		});
		shell.setSize(450, 300);
		shell.setText("Username " + "[" + client.getUsername() + "]" + " | Host " + "[" + client.getHost() + "]" + " | Port " + "[" + client.getPortNumber() + "]");
		shell.setLayout(new BorderLayout(0, 0));
		
		userList = new List(shell, SWT.BORDER);
		userList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectedItems = userList.getSelectionIndices();
		        for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++){
		        	selectedRCVRString = userList.getItem(selectedItems[loopIndex]);
		        }
		        System.out.println("Selected Items: " + selectedRCVRString);
			}
		});
		userList.setItems(new String[] {});
		userList.setLayoutData(BorderLayout.EAST);
		
		group = new Group(shell, SWT.NONE);
		group.setLayoutData(BorderLayout.SOUTH);
		group.setLayout(new BorderLayout(0, 0));
		
		message = new Text(group, SWT.BORDER);
		message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					client.sendMessage(selectedRCVRString,message.getText());
					Calendar cal = Calendar.getInstance();
					if(selectedRCVRString.equals("") || selectedRCVRString.equals("GLOBAL")){
						text += "(" + sdf.format(cal.getTime()) + ")" + "[GLOBAL] " + client.getUsername() + ": " + message.getText()  + " \n";
					}else{
						text += "(" + sdf.format(cal.getTime()) + ")" + "[TO " + "[" + selectedRCVRString + "]" + "] " + client.getUsername() + ": " + message.getText()  + " \n";
					}
	        		txtChat.setText(text);
					message.setText("");
				}
			}
		});
		message.setLayoutData(BorderLayout.CENTER);
		
		btnSend = new Button(group, SWT.NONE);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				client.sendMessage(selectedRCVRString,message.getText());
				Calendar cal = Calendar.getInstance();
				if(selectedRCVRString.equals("") || selectedRCVRString.equals("GLOBAL")){
					text += "(" + sdf.format(cal.getTime()) + ")" + "[GLOBAL] " + client.getUsername() + ": " + message.getText()  + " \n";
				}else{
					text += "(" + sdf.format(cal.getTime()) + ")" + "[TO " + "[" + selectedRCVRString + "]" + "] " + client.getUsername() + ": " + message.getText()  + " \n";
				}
        		txtChat.setText(text);
				message.setText("");
			}
		});
		btnSend.setLayoutData(BorderLayout.EAST);
		btnSend.setText("Send");
		
		txtChat = new Text(shell,  SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtChat.addListener(SWT.Modify, new Listener(){
		    public void handleEvent(Event e){
		        txtChat.setTopIndex(txtChat.getLineCount() - 1);
		    }
		});
		txtChat.setText(text);
		txtChat.setEditable(false);
		txtChat.setLayoutData(BorderLayout.CENTER);
		
	}
	
	public void addUserList(final ArrayList<String> users){
		new Thread(new Runnable() {
		      public void run() {
		            Display.getDefault().asyncExec(new Runnable() {
		               public void run() {
		            	   try{
		           			userList.add("");
		           			removeAllUsernames();
		           			for (int i = 0; i < users.size(); i++) {
		           				userList.add(users.get(i));
		           			}
		           			shell.layout();
		           			}catch(Exception e){
		           				e.printStackTrace();
		           			}
		               	}
		            });
		         
		      }
		   }).start();
	}
	
	public void updateChat(final String serverMSG){
		new Thread(new Runnable() {
		      public void run() {
		            Display.getDefault().asyncExec(new Runnable() {
		               public void run() {
		            	   try{
		            		text += serverMSG +" \n";
		            		txtChat.setText(text);
		           			shell.layout();
		           			}catch(Exception e){
		           				e.printStackTrace();
		           			}
		               	}
		            });
		      }
		   }).start();
	}
	
	private void removeAllUsernames(){
		userList.removeAll();
		userList.add("GLOBAL");
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		ChatRoomGUI.client = client;
	}
	
	
}
