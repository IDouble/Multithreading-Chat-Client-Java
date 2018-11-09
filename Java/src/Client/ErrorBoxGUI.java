package Client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ErrorBoxGUI {
		
	String errorMessage;
	
	Display display;
	Shell shell = new Shell(display);
	public ErrorBoxGUI(String errorMessage){
		this.errorMessage = errorMessage;
	}
	
	public void open() {
		new Thread(new Runnable() {
		      public void run() {
		            Display.getDefault().asyncExec(new Runnable() {
		               public void run() {
		            	   try{
		            		    createContents();
		            			shell.open();
		            			shell.layout();
		            			while (!shell.isDisposed()) {
		            				if (!display.readAndDispatch()) {
		            					display.sleep();
		            				}
		            			}
		           			}catch(Exception e){
		           				e.printStackTrace();
		           			}
		               	}
		            });
		         
		      }
		   }).start();
		
	}
	
	public void close(){
		shell.close();
	}
	
	protected void createContents() {
		display = Display.getDefault();
		int style = SWT.ICON_ERROR;
	    //SWT.ICON_QUESTION | SWT.YES | SWT.NO
		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setMessage(errorMessage);
		int rc = messageBox.open();
		switch (rc) {
			case SWT.OK:
			  System.out.println("SWT.OK");
			  break;
			case SWT.CANCEL:
			  System.out.println("SWT.CANCEL");
			  break;
			case SWT.YES:
			  System.out.println("SWT.YES");
			  break;
			case SWT.NO:
			  System.out.println("SWT.NO");
			  break;
			case SWT.RETRY:
			  System.out.println("SWT.RETRY");
			  break;
			case SWT.ABORT:
			  System.out.println("SWT.ABORT");
			  break;
			case SWT.IGNORE:
			  System.out.println("SWT.IGNORE");
			  break;
			}
		close();
	  }
}	

