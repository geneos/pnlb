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
 * Esta clase implementa los colores por defecto del NimRODLookAndFeel.
 * @author Nilo J. Gonzalez
 */ 
 
package org.eevolution.plaf.nimrod;


import java.awt.Color;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;


/**
 * Define un <I>tema</I> de color para el NimRODLookAndFeel.
 * En realidad, valen para cualquier Look&Feel que herede de MetalLookAndFeel.<BR>
 * Se usa asi:
 * <PRE>
 * NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
 * NimRODLF.setCurrentTheme( new NimRODTheme());
 * UIManager.setLookAndFeel(NimRODLF);
 * </PRE>
 * Con esto se pone un color gris oscuro. Tambien define temas partiendo de un color base, modificando
 * los valores primarios.
 * <PRE>
 * NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
 * NimRODLF.setCurrentTheme( new NimRODTheme( <I>unColor</I>));
 * UIManager.setLookAndFeel(NimRODLF);
 * </PRE>
 * o partiendo de dos colores base, uno para los valores primarios y otro para los secundarios.
 * <PRE>
 * NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
 * NimRODLF.setCurrentTheme( new NimRODTheme( <I>unColorPrimario</I>, <I>unColorSecundario</I>));
 * UIManager.setLookAndFeel(NimRODLF);
 * </PRE>
 * Para entender como va el temita de los colores, puede ayudar mucho consultar esta pagina:
 * <a href='http://java.sun.com/products/jlf/ed1/dg/higg.htm'>http://java.sun.com/products/jlf/ed1/dg/higg.htm</a> 
 */
public class NimRODTheme extends DefaultMetalTheme {

  // primarios
  private ColorUIResource primary1 = new ColorUIResource( 224, 184, 88);

  private ColorUIResource primary2 = new ColorUIResource( 244, 204, 108);

  private ColorUIResource primary3 = new ColorUIResource( 176, 145, 84);

  // secondarios
  private ColorUIResource secondary1 = new ColorUIResource( 205, 204, 190);

  private ColorUIResource secondary2 = new ColorUIResource( 225, 224, 210);

  private ColorUIResource secondary3 = new ColorUIResource( 245, 244, 230);
  
  private ColorUIResource black = new ColorUIResource( 0, 0, 0);
  
  private ColorUIResource white = new ColorUIResource( 255, 255, 255);

  public NimRODTheme() {
    super();
  }
  
  /**
   * Este constructor recibe por parametro el color que se desea utilizar como color principal de "fondo".
   * Es el color que se usara como fondo de los botones, dialogos, menus... El resto de los colores de fondo
   * se calculan oscureciendo este en diversa medida. 
   * @param base Color el color de fondo.
   */
  public NimRODTheme( Color base) {
    super();
    
    int r = base.getRed();
    int g = base.getGreen();
    int b = base.getBlue();
    
    primary1 = new ColorUIResource( new Color( (r>20 ? r-20 : 0), (g>20 ? g-20 : 0), (b>20 ? b-20 : 0)));
    primary2 = new ColorUIResource( new Color( (r>10 ? r-10 : 0), (g>10 ? g-10 : 0), (b>10 ? b-10 : 0)));
    primary3 = new ColorUIResource( base);
  }
  
  /**
   * Este constructor recibe por parametro los colores que se desea utilizar.
   * Base es el color que se usara como fondo de los botones, dialogos, menus... y prim es el color que se usara para
   * los objetos seleccionados. En palabras de Sun, Prim es el color que da "personalidad" al tema...
   * El resto de los colores  se calculan oscureciendo estos en diversa medida. 
   * @param prim Color el color a usar en las selecciones.
   * @param base Color el color de fondo.
   */
   public NimRODTheme( Color prim, Color sec) {
    super();
    
    int r = prim.getRed();
    int g = prim.getGreen();
    int b = prim.getBlue();
    
    primary1 = new ColorUIResource( new Color( (r>20 ? r-20 : 0), (g>20 ? g-20 : 0), (b>20 ? b-20 : 0)));
    primary2 = new ColorUIResource( new Color( (r>10 ? r-10 : 0), (g>10 ? g-10 : 0), (b>10 ? b-10 : 0)));
    primary3 = new ColorUIResource( prim);
    
    r = sec.getRed();
    g = sec.getGreen();
    b = sec.getBlue();
    
    secondary1 = new ColorUIResource( new Color( (r>20 ? r-20 : 0), (g>20 ? g-20 : 0), (b>20 ? b-20 : 0)));
    secondary2 = new ColorUIResource( new Color( (r>10 ? r-10 : 0), (g>10 ? g-10 : 0), (b>10 ? b-10 : 0)));
    secondary3 = new ColorUIResource( sec);
  }

  public String getName() { 
    return "NimROD"; 
  }


  protected ColorUIResource getPrimary1() { 
    return primary1; 
  }
  protected ColorUIResource getPrimary2() { 
    return primary2; 
  }
  protected ColorUIResource getPrimary3() { 
    return primary3; 
  }

  protected ColorUIResource getSecondary1() { 
    return secondary1; 
  }
  protected ColorUIResource getSecondary2() { 
    return secondary2; 
  }
  protected ColorUIResource getSecondary3() { 
    return secondary3; 
  }

  protected ColorUIResource getBlack() { 
    return black; 
  }
  
  protected ColorUIResource getWhite() { 
    return white; 
  }
  
  public void setPrimary1( Color col) { 
    primary1 = new ColorUIResource( col); 
  }
  public void setPrimary2( Color col) { 
    primary2 = new ColorUIResource( col); 
  }
  public void setPrimary3( Color col) { 
    primary3 = new ColorUIResource( col); 
  }

  public void setSecondary1( Color col) { 
    secondary1 = new ColorUIResource( col); 
  }
  public void setSecondary2( Color col) { 
    secondary2 = new ColorUIResource( col); 
  }
  public void setSecondary3( Color col) { 
    secondary3 = new ColorUIResource( col); 
  }
  
  public void setBlack( Color col) { 
    black = new ColorUIResource( col); 
  }
  public void setWhite( Color col) { 
    white = new ColorUIResource( col); 
  }
}
