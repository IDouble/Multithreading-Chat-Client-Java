package Client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class ConnectGUI {

	protected Shell shell;
	protected Text server;
	protected Text port;
	private Client client;
	
	
	public ConnectGUI(Client c){
		client = c;
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
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
	
	public void close(){
		shell.close();
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.TITLE);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if(!client.getIsConnected()){ // Only closes the application,when user doesn't want to connect
					System.exit(0);
				}
			}
		});
		shell.setSize(450, 125);
		shell.setText("Connect");
		shell.setLayout(new GridLayout(2, false));
		
		Label lblServer = new Label(shell, SWT.NONE);
		lblServer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServer.setText("Server:");
		
		server = new Text(shell, SWT.BORDER);
		server.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					try{
						client.setHost(server.getText());
						client.setPortNumber(Integer.parseInt(port.getText()));
						client.tryConnect();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		});
		server.setText("CLIENTAYI");
		server.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(shell, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Port:");
		
		port = new Text(shell, SWT.BORDER);
		port.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					try{
						client.setHost(server.getText());
						client.setPortNumber(Integer.parseInt(port.getText()));
						client.tryConnect();
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		});
		port.setText("5000");
		port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Button btnConnect = new Button(shell, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try{
					client.setHost(server.getText());
					client.setPortNumber(Integer.parseInt(port.getText()));
					client.tryConnect();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		
		GridData gd_btnConnect = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnConnect.widthHint = 424;
		btnConnect.setLayoutData(gd_btnConnect);
		btnConnect.setText("Connect");

	}
	
}
