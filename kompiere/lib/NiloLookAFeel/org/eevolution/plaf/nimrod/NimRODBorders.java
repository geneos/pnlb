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
 * Esta clase se utiliza como repositorio de borders. Esa es su unica utilidad.
 * @author Nilo J. Gonzalez
 */
 
package org.eevolution.plaf.nimrod;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;


public class NimRODBorders extends MetalBorders {
  private static Border butBorder;
  private static Border popupMenuBorder;
  private static Border rolloverButtonBorder;
  
  public static Border getPopupMenuBorder() {
    if ( popupMenuBorder == null) {
  	  popupMenuBorder = new BorderUIResource.CompoundBorderUIResource( new NimRODBorders.NimRODPopupMenuBorder(),
                                                                       new BasicBorders.MarginBorder());
    }
    return popupMenuBorder;
  }  
  
  public static Border getButtonBorder() {
    if ( butBorder == null) {
  	  butBorder = new BorderUIResource.CompoundBorderUIResource( new NimRODBorders.NimRODButtonBorder(),
                                                                 new BasicBorders.MarginBorder());
    }
    return butBorder;
  }

  public static Border getRolloverButtonBorder() {
    if ( rolloverButtonBorder == null) {
  	  rolloverButtonBorder = new BorderUIResource.CompoundBorderUIResource( new NimRODBorders.NimRODRolloverButtonBorder(),
                                                                            new BasicBorders.MarginBorder());
    }
    return rolloverButtonBorder;
  }
	
	public static class NimRODButtonBorder extends AbstractBorder implements UIResource {
    public void paintBorder( Component c, Graphics g, int x, int y, int w, int h) {
      g.translate( x, y);
      g.setColor( NimRODLookAndFeel.getControlDarkShadow() );
      g.drawRoundRect( 0,0, w-1,h-1, 8,8);
          
      if ( c instanceof JButton ) {
        JButton button = (JButton)c;
        ButtonModel model = button.getModel();
  
        if ( button.isDefaultButton() ) {
          g.setColor( NimRODLookAndFeel.getControlDarkShadow().darker());
          g.drawRoundRect( 1,1, w-3,h-3, 7,7);
        }
        /*else if ( model.isPressed() && model.isArmed() ) {
          g.translate( x, y);
          g.setColor( NimRODLookAndFeel.getControlDarkShadow() );
          g.drawRoundRect( 0,0, w-1,h-1, 8,8);
        }*/
      }
    }
  }
  
  public static class NimRODPopupMenuBorder extends AbstractBorder implements UIResource {
    protected static Insets borderInsets = new Insets( 2, 1, 5, 5);

    public void paintBorder( Component c, Graphics g, int x, int y, int w, int h ) {
	    g.translate( x, y);

      g.setColor( NimRODLookAndFeel.getPrimaryControlDarkShadow());
	    g.drawRect( 0, 0, w-5, h-5);
	   
	    g.setColor( new Color(0,0,0, 150));
	    g.drawLine( w-4,4, w-4,h-4);
	    g.drawLine( 4,h-4, w-5,h-4);
      g.setColor( new Color(0,0,0, 110));
      g.drawLine( w-3,4, w-3,h-3);
	    g.drawLine( 4,h-3, w-4,h-3);
      g.setColor( new Color(0,0,0, 70));
      g.drawLine( w-2,4, w-2,h-2);
	    g.drawLine( 4,h-2, w-3,h-2);
      g.setColor( new Color(0,0,0, 30));
      g.drawLine( w-1,4, w-1,h-1);
	    g.drawLine( 4,h-1, w-2,h-1);
	    
	    //g.fillRect( 5, 5, c.getWidth()-5,c.getHeight()-5);
	   
      g.translate( -x, -y);
    }

    public Insets getBorderInsets( Component c ) {
      return borderInsets;
    }

    public Insets getBorderInsets( Component c, Insets newInsets) {
	    newInsets.top = borderInsets.top;
	    newInsets.left = borderInsets.left;
	    newInsets.bottom = borderInsets.bottom;
	    newInsets.right = borderInsets.right;
	    return newInsets;
  	}
  }
  
  public static class NimRODRolloverButtonBorder extends AbstractBorder implements UIResource {
    public void paintBorder( Component c, Graphics g, int x, int y, int w, int h) {
      AbstractButton button = (AbstractButton)c;
	    ButtonModel model = ((AbstractButton)c).getModel();
	    
	    /*
	    if ( model.isRollover() && !( model.isPressed() && !model.isArmed() ) ) {
        g.translate( x, y);
        
  		}
  		*/
  		if ( model.isRollover() ) { //&& !( model.isPressed() && !model.isArmed() ) ) {
    		g.setColor( NimRODLookAndFeel.getControlDarkShadow());
  			g.drawRoundRect( 0,0, w-1,h-1, 8,8);
  			
    		RoundRectangle2D.Float boton = new RoundRectangle2D.Float(); 
        boton.x = 0;
        boton.y = 0;
        boton.width = c.getWidth();
        boton.height = c.getHeight();
        boton.arcwidth = 8;
        boton.archeight = 8;
        
        GradientPaint grad = null;
        if ( model.isPressed() ) {
        	grad = new GradientPaint( 0,0, NimRODLookAndFeel.sombra, 
                                    0,c.getHeight()/2, NimRODLookAndFeel.brillo);
    		}
    		else {
        	grad = new GradientPaint( 0,0, NimRODLookAndFeel.brillo, 
                                    0,c.getHeight(), NimRODLookAndFeel.sombra);
    		}
    		
    		Graphics2D g2D = (Graphics2D)g;
    		
        g2D.setPaint( grad);
        g2D.fill( boton);
      }
    }
  }
}

