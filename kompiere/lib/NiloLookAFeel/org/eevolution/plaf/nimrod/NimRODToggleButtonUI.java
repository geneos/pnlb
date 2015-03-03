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
 * Esta clase implementa los botones que se quedan pulsados.
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

public class NimRODToggleButtonUI extends MetalToggleButtonUI {
  protected RoundRectangle2D.Float boton;
  
	public static ComponentUI createUI( JComponent c) {
    return new NimRODToggleButtonUI();
  }
  
  public void installDefaults( AbstractButton button) {
    super.installDefaults( button);
    
    button.setBorder( NimRODBorders.getButtonBorder());
    selectColor = UIManager.getColor( "ScrollBar.thumb");
  }	

  
  protected void paintButtonPressed( Graphics g, AbstractButton b) {
    if ( b.isContentAreaFilled() ) {
      Graphics2D g2D = (Graphics2D)g;
	    g2D.setColor( selectColor);
	    hazBoton( b);
      g2D.fill( boton);
	  }
  }
  
	protected void paintFocus( Graphics g, AbstractButton b,
														 Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
    if( b.getParent() instanceof JToolBar ) {
      return;  // No se pintael foco cuando estamos en una barra
    }
    
		g.setColor( getFocusColor());
		g.drawRoundRect( 2,2, b.getWidth()-6,b.getHeight()-6, 5,5);
    g.drawRoundRect( 3,3, b.getWidth()-6,b.getHeight()-6, 4,4);
  }
  
  public void paint( Graphics g, JComponent c) {
    super.paint( g, c);
    
    if ( !c.isOpaque()) {
      return;
    }

		Graphics2D g2D = (Graphics2D)g;
		ButtonModel mod = ((AbstractButton)c).getModel();
		
    if( ( c.getParent() instanceof JToolBar ) && ( !mod.isRollover() ) ) {
      return;
    }
    
    hazBoton( c);
    
    GradientPaint grad = null;
    if ( mod.isPressed() || mod.isSelected() ) {
    	grad = new GradientPaint( 0,0, NimRODLookAndFeel.sombra, 
                                0,c.getHeight(), NimRODLookAndFeel.brillo);
		}
		else {
    	grad = new GradientPaint( 0,0, NimRODLookAndFeel.brillo, 
                                0,c.getHeight(), NimRODLookAndFeel.sombra);
		}
		
		g2D.setPaint( grad);
    g2D.fill( boton);
  }
  
  private void hazBoton( JComponent c) {
    boton = new RoundRectangle2D.Float(); 
    boton.x = 0;
    boton.y = 0;
    boton.width = c.getWidth();
    boton.height = c.getHeight();
    boton.arcwidth = 8;
    boton.archeight = 8;
  }
}
