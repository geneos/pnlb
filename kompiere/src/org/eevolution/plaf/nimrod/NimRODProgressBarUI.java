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
 * Esta clase implementa las barras de progreso.
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

public class NimRODProgressBarUI extends MetalProgressBarUI {
	public static ComponentUI createUI( JComponent c) {
    return new NimRODProgressBarUI();
  }
	
  public void paintDeterminate( Graphics g, JComponent c) {
	  super.paintDeterminate( g,c);
	  
	  if ( !(g instanceof Graphics2D) ) {  // Realmente no entiendo esto, pero esta asi en MetalProgressBarUI,
      return;                            // asi que tiene que estar aqui tambien para no completar algo que 
    }                                    // la clase base no hace...
    
	  Graphics2D g2D = (Graphics2D)g;
	  
    Insets b = progressBar.getInsets();
    int largo = progressBar.getWidth() - (b.left + b.right);
	  int alto = progressBar.getHeight() - (b.top + b.bottom);
	  int len = getAmountFull(b, largo, alto);
    
    int xi = b.left;
    int yi = b.top;
    int xf = xi + largo;
    int yf = yi + alto;
    int xm = xi + len - 1;
    int ym = yf - len ;
    
	  if ( progressBar.getOrientation() == JProgressBar.HORIZONTAL ) {
      GradientPaint grad = new GradientPaint( xi,yi, NimRODLookAndFeel.brillo, 
                                              xi,yf, NimRODLookAndFeel.sombra);
      g2D.setPaint( grad);
      g2D.fillRect( xi,yi, xm,yf);
      
    	grad = new GradientPaint( xm+1,yi, NimRODLookAndFeel.sombra, 
                                xm+1,yf, NimRODLookAndFeel.brillo);
  		g2D.setPaint( grad);
      g2D.fillRect( xm+1,yi, xf,yf);
  	}
  	else {
      GradientPaint grad = new GradientPaint( xi,yi, NimRODLookAndFeel.sombra, 
                                              xf,yi, NimRODLookAndFeel.brillo);
  		g2D.setPaint( grad);
      g2D.fillRect( xi,yi, xf,ym);
      
      grad = new GradientPaint( xi,ym, NimRODLookAndFeel.brillo, 
                                xf,ym, NimRODLookAndFeel.sombra);
      g2D.setPaint( grad);
      g2D.fillRect( xi,ym, xf,yf);
    }
	}
	
	public void paintIndeterminate( Graphics g, JComponent c) {
    super.paintIndeterminate( g, c);
    
	  if ( !(g instanceof Graphics2D) ) {  // Realmente no entiendo esto, pero esta asi en MetalProgressBarUI,
      return;                            // asi que tiene que estar aqui tambien para no completar algo que 
    }                                    // la clase base no hace...
    
	  Graphics2D g2D = (Graphics2D)g;
	  
	  Rectangle rec = new Rectangle();
    rec = getBox( rec);
    
    Insets b = progressBar.getInsets();
    int xi = b.left;
    int yi = b.top;
    int xf = c.getWidth() - b.right;
    int yf = c.getHeight() - b.bottom;
    
    if ( progressBar.getOrientation() == JProgressBar.HORIZONTAL ) {
      GradientPaint grad = new GradientPaint( rec.x,rec.y, NimRODLookAndFeel.brillo, 
                                              rec.x,rec.height, NimRODLookAndFeel.sombra);
      g2D.setPaint( grad);
      g2D.fill( rec);
      
      grad = new GradientPaint( xi,yi, NimRODLookAndFeel.sombra, 
                                xi,yf, NimRODLookAndFeel.brillo);
  		g2D.setPaint( grad);
      g2D.fillRect( xi,yi, rec.x,yf);
      g2D.fillRect( rec.x + rec.width,yi, xf,yf);
    }
    else {
      GradientPaint grad = new GradientPaint( rec.x,rec.y, NimRODLookAndFeel.brillo, 
                                              rec.width,rec.y, NimRODLookAndFeel.sombra);
      g2D.setPaint( grad);
      g2D.fill( rec);

      
      grad = new GradientPaint( xi,yi, NimRODLookAndFeel.sombra, 
                                xf,yi, NimRODLookAndFeel.brillo);
  		g2D.setPaint( grad);
      g2D.fillRect( xi,yi, xf,rec.y);
      g2D.fillRect( xi,rec.y+rec.height, xf,yf);
    }
  }
}