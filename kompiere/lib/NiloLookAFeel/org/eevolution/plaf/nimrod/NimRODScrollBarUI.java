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
 * Esta clase implementa las barras de scroll.
 * @author Nilo J. Gonzalez
 */ 
 

package org.eevolution.plaf.nimrod;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class NimRODScrollBarUI extends MetalScrollBarUI {
  public static ComponentUI createUI( JComponent c)    {
    return new NimRODScrollBarUI();
  }

  protected JButton createDecreaseButton( int orientation) {
    decreaseButton = new NimRODScrollButton( orientation, scrollBarWidth, isFreeStanding);
    return decreaseButton;
  }

  protected JButton createIncreaseButton( int orientation) {
    increaseButton =  new NimRODScrollButton( orientation, scrollBarWidth, isFreeStanding);
    return increaseButton;
  }

  protected void paintThumb( Graphics g, JComponent c, Rectangle thumbBounds) {
    Color thumbColor = UIManager.getColor( "ScrollBar.thumb");
    Color thumbShadow = UIManager.getColor( "ScrollBar.thumbShadow");
    Color thumbHighlightColor = UIManager.getColor( "ScrollBar.thumbHighlight");
    
    int anchoDib;
    int xm;
    int ym;
    
    g.translate( thumbBounds.x, thumbBounds.y);
    
    if ( scrollbar.getOrientation() == JScrollBar.HORIZONTAL ) {
      anchoDib = thumbBounds.height - 6;
      xm = thumbBounds.width / 2;
      ym = thumbBounds.height / 2 - 1;
    }
    else {
      anchoDib = thumbBounds.width - 6;
      xm = thumbBounds.width / 2 - 1;
      ym = thumbBounds.height / 2;
    }
          
    int x1 = xm - anchoDib/2;
    int y1 = ym - anchoDib/2;
    int x2 = xm;
    int y2 = ym;
    int x3 = x1 + anchoDib;
    int y3 = y1 + anchoDib;

    g.setColor( thumbColor);
    g.fillRect( 0, 0, thumbBounds.width - 1, thumbBounds.height - 1 );
 
    g.setColor( thumbShadow );
    g.drawRect( 0, 0, thumbBounds.width - 1, thumbBounds.height - 1 );
    
    g.drawLine( x1,y1, x1,y1);
    g.drawLine( x1,y2, x2,y1);
    g.drawLine( x1,y3, x3,y1);
    g.drawLine( x2,y3, x3,y2);
    g.drawLine( x3,y3, x3,y3);

    g.setColor( thumbHighlightColor );
    //g.drawLine( 1, 1, thumbBounds.width - 3, 1 );
    //g.drawLine( 1, 1, 1, thumbBounds.height - 2 );

    x1++;y1++;
    x2++;y2++;
    x3++;y3++;
    
    g.drawLine( x1,y1, x1,y1);
    g.drawLine( x1,y2, x2,y1);
    g.drawLine( x1,y3, x3,y1);
    g.drawLine( x2,y3, x3,y2);
    g.drawLine( x3,y3, x3,y3);
    
    g.translate( -thumbBounds.x, -thumbBounds.y);
    
    Graphics2D g2D = (Graphics2D)g;
    GradientPaint grad = null;
    
    if ( scrollbar.getOrientation() == JScrollBar.HORIZONTAL ) {
      grad = new GradientPaint( thumbBounds.x,thumbBounds.y, NimRODLookAndFeel.brillo, 
                                thumbBounds.x,thumbBounds.height, NimRODLookAndFeel.sombra);
  	}
    else {
      grad = new GradientPaint( thumbBounds.x,thumbBounds.y, NimRODLookAndFeel.brillo, 
                                thumbBounds.width, thumbBounds.y, NimRODLookAndFeel.sombra);
    }
    g2D.setPaint( grad);
    g2D.fill( thumbBounds);
  }

  protected void paintTrack( Graphics g, JComponent c, Rectangle trackBounds) {
    super.paintTrack( g, c, trackBounds);
    
    Graphics2D g2D = (Graphics2D)g;
    GradientPaint grad = null;
    
    if ( scrollbar.getOrientation() == JScrollBar.HORIZONTAL ) {
      grad = new GradientPaint( trackBounds.x,trackBounds.y, NimRODLookAndFeel.sombra, 
                                trackBounds.x,trackBounds.y + trackBounds.height, NimRODLookAndFeel.brillo);
  	}
    else {
      grad = new GradientPaint( trackBounds.x,trackBounds.y, NimRODLookAndFeel.sombra, 
                                trackBounds.x + trackBounds.width, trackBounds.y, NimRODLookAndFeel.brillo);
    }
    
    g2D.setPaint( grad);
    g2D.fill( trackBounds);
  }
}
