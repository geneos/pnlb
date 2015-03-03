/*
 *                 (C) Copyright 2005 Nilo J. Gonzalez
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 2 of the Licence, or (at your opinion) any
 * later version.
 * 
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html (Espa�ol)
 *
 *
 * Original author: Nilo J. Gonz�lez
 */

/**
 * Esta clase implementa los CheckBox.
 * En realidad lo unico que hace es asociar los iconos que se usaran en cada estado, y el trabajo
 * duro lo hace la clase NimRODIconFactory. 
 * @author Nilo J. Gonzalez
 */
 
package org.eevolution.plaf.nimrod;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;



public class NimRODCheckBoxUI extends MetalCheckBoxUI {
  public static ComponentUI createUI( JComponent c) {
    return new NimRODCheckBoxUI();
  }

  public void installDefaults( AbstractButton b) {
    super.installDefaults( b);

    b.setIcon( NimRODIconFactory.getCheckBoxIcon());
    b.setSelectedIcon( NimRODIconFactory.getCheckBoxIcon());
    
    b.setDisabledIcon( NimRODIconFactory.getCheckBoxIcon());
    b.setDisabledSelectedIcon( NimRODIconFactory.getCheckBoxIcon());
  } 
    //begin vpj-cd e-evolution 30 Dic 2005
	/**
	 * 	Create Button Listener
	 *	@param b button
	 *	@return listener
	 */
	protected BasicButtonListener createButtonListener (AbstractButton b)
	{
		return new KompiereButtonListener(b);
	}	//	createButtonListener
	//end vpj-cd e-evolution 30 Dic 2005
}

