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

public class UsernameGUI {

	public ErrorBoxGUI eBX = new ErrorBoxGUI("You need to Type a Username");
	protected Shell shell;
	private Text username;
	private Client client;
	
	public UsernameGUI(Client c){
		client = c;
	}
	/**
	 * Open the window.
	 */
	public void close(){
		shell.close();
	}
	
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

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell(Display.getDefault(), SWT.CLOSE | SWT.TITLE);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if(username.getText().equals("")){ // Only closes the application,when user doesn't typed a username and closed it
					System.exit(0);
				}
			}
		});
		shell.setSize(450, 100);
		shell.setText("Username");
		shell.setLayout(new GridLayout(2, false));
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText("Username:");
		username = new Text(shell, SWT.BORDER);
		username.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR){
					try{
						if(!username.getText().equals("")){
							client.setUsername(username.getText());
							close();
						}else{
							eBX.open();
						}
					}catch(Exception ex){
						
					}
				}
			}
		});
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnUsernameChose = new Button(shell, SWT.NONE);
		btnUsernameChose.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!username.getText().equals("")){
						client.setUsername(username.getText());
						close();
					}else{
						eBX.open();
					}
				}catch(Exception ex){
					
				}
			}
		});
		GridData gd_btnUsernameChose = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnUsernameChose.widthHint = 424;
		btnUsernameChose.setLayoutData(gd_btnUsernameChose);
		btnUsernameChose.setText("Chose");

	}
}
