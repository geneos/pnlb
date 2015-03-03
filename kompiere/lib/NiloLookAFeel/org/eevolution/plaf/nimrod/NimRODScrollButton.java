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
 * Esta clase implementa los botones de las barras de scroll.
 * @author Nilo J. Gonzalez
 */ 
 

package org.eevolution.plaf.nimrod;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class NimRODScrollButton extends MetalScrollButton {
  public NimRODScrollButton( int direction, int width, boolean freeStanding) {
    super( direction, width, freeStanding);
  }

  public void paint( Graphics g) {
    super.paint( g);

    Rectangle rec = new Rectangle( 0,0, getWidth(),getHeight());
    
    GradientPaint grad = null;
    
    if (getDirection() == EAST || getDirection() == WEST) {
      grad = new GradientPaint( 0,0, NimRODLookAndFeel.brillo, 
                                0,rec.height, NimRODLookAndFeel.sombra);
    } 
    else {
      grad = new GradientPaint( 0,0, NimRODLookAndFeel.brillo, 
                                rec.width,0, NimRODLookAndFeel.sombra);
    }
    
    Graphics2D g2D = (Graphics2D)g;
    g2D.setPaint( grad);
    g2D.fillRect( rec.x, rec.y, rec.width-1, rec.height);
    
    //NimRODBorders.NimRODButtonBorder.paintBorder( this,g, 0,0, getWidth(),getHeight());
  }
}
