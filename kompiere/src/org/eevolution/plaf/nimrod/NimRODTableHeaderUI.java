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
 * Esta clase implementa las cabeceras de las tablas.
 * @author Nilo J. Gonzalez
 */ 
 
package org.eevolution.plaf.nimrod;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

public class NimRODTableHeaderUI extends BasicTableHeaderUI {
  
	public void NimRODTableHeaderUI() {
	}
	
	public static ComponentUI createUI( JComponent c) {
    return new NimRODTableHeaderUI();
  }
 
  public void installDefaults() {
    super.installDefaults();
  }
 
  public void paint( Graphics g, JComponent c) {
    super.paint( g, c);
    
    if ( !c.isOpaque() ) {
      return;
    }
    
    Graphics2D g2D = (Graphics2D)g;
    
    GradientPaint grad = new GradientPaint( 0,0, NimRODLookAndFeel.brillo, 
                                            0,c.getHeight(), NimRODLookAndFeel.sombra);
                                            
    g2D.setPaint( grad);
    g2D.fillRect( 0,0, c.getWidth(),c.getHeight());
  }
}
