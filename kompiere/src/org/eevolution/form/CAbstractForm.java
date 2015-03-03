package org.eevolution.form;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.swing.CPanel;
import org.compiere.util.Env;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public abstract class CAbstractForm extends CPanel implements FormPanel {

	// Later it may inherit some functionallity. Therefore its included any longer.
	class FrameHandler extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {

			// Disposing isn't necessary here, because its handled by the form frame itself.
			//dispose();
		}
		
	}

    private FormFrame frame;
    private FrameHandler handler;
    
    
    public CAbstractForm() {
    	
    	handler = new FrameHandler();
    }
    
	public void init (int WindowNo, FormFrame frame) {
		
		this.frame = frame;
		frame.addWindowListener(handler);
	}
	
	public void dispose() {
		
		handler = null;
	}

	public int getWindowNo() {
		
		return Env.getWindowNo(frame);
	}
	
	public FormFrame getWindow() {
		
		return frame;
	}
}
