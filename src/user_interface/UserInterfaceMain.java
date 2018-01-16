package user_interface;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import user_interface.InputFileForm.ConvertionStartedListener;
import xml_to_excel.XmlCatalogueToExcel;

/**
 * Start the application for xml=>xlsx
 * @author avonva
 *
 */
public class UserInterfaceMain {

	public static void main ( String[] args ) {
		
		// create a new shell for the ui
		Display display = new Display();
		Shell shell = new Shell ( display );

		// open the form
		InputFileForm form = new InputFileForm( shell, display );

		form.display( "Catalogue converter xml to xlsx - GUI", ".xml", ".xlsx" );

		form.setListener( new ConvertionStartedListener() {
			
			@Override
			public void started(String inputFilename, String outputFilename) {
				
				String title;
				String message;
				int style;
				
				XmlCatalogueToExcel converter = new XmlCatalogueToExcel( 
						inputFilename, outputFilename );
				
				setCursor ( shell, SWT.CURSOR_WAIT );
				
				try {

					converter.convertXmlToExcel();

					title = "Done!";
					message = "The conversion was correctly terminated";
					style = SWT.OK;

				} catch (TransformerException e) {
					
					title = "Error!";
					message = "Something went wrong during the conversion. Check the filenames!";
					style = SWT.ERROR;
				}
				
				setCursor ( shell, SWT.CURSOR_ARROW );
				
				MessageBox mb = new MessageBox( shell, style );
				mb.setText( title );
				mb.setMessage( message );
				mb.open();
			}
		});
		
		// show the shell
		shell.open();

		// loop for updating ui
		while ( !form.shell.isDisposed() ) {
			if ( !form.display.readAndDispatch() )
				form.display.sleep();
		}

		// dispose display
		display.dispose();
	}
	
	
	/**
	 * Update the shell cursor
	 * @param cursorType
	 */
	private static void setCursor ( Shell shell, int cursorType ) {
		
		// dispose the cursor if there is one
		if ( shell.getCursor() != null )
			shell.getCursor().dispose();

		// change the cursor to the new cursor
		shell.setCursor( new Cursor( shell.getDisplay() , cursorType ) );
	}
}
