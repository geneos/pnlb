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
 * Esta clase dibuja varios de los iconos que se usan a lo largo de todo el LookAndFeel.
 * Esta es la clase que hace el trabajo duro de pintar checkboxes, radios, sliders... Consta de 
 * varias inner clases privadas y de funciones que las dan acceso. Las inner clases mas o menos
 * son todas iguales: pintan un icono base leido de un archivo PNG, segun cual sea el estado del
 * objeto (seleccionado, inactivo...) le dan una capa de color y si es necesario vuelven a pintar 
 * otro icono.
 * Podria hacerse mas sencillo pintando un unico icono segun el estado del componente, teniendo
 * un PNG por cada estado, pero entonces no se podria usar colores en ellos porque si cambiara el
 * color de seleccion o foco, por ejemplo a verde, todos los objetos de la aplicacion se pintarian
 * seleccionados en verde menos los iconos, que se pintarian con el color con que hubieramos pintado
 * el PNG, dando un aspecto inconsistente. Por eso se pinta un icono trasparente y una capa de color
 * cuando hace falta, para decidir en tiempo real cual es el color apropiado.
 * @author Nilo J. Gonzalez
 */
 
 
package org.eevolution.plaf.nimrod;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.io.Serializable;

public class NimRODIconFactory implements Serializable {
  private static Icon checkBoxIcon;
  private static Icon radioButtonIcon;
  private static Icon checkBoxMenuItemIcon;
  private static Icon radioButtonMenuItemIcon;
  private static Icon sliderHorizIcon;
  private static Icon sliderVertIcon;
  private static Icon treeCollapsedIcon;
  private static Icon treeExpandedIcon;

  public static Icon getCheckBoxIcon() {
  	if ( checkBoxIcon == null ) {
	    checkBoxIcon = new CheckBoxIcon();
  	}
    return checkBoxIcon;
  }
  
  public static Icon getRadioButtonIcon() {
  	if ( radioButtonIcon == null ) {
	    radioButtonIcon = new RadioButtonIcon();
  	}
    return radioButtonIcon;
  }
  
  public static Icon getCheckBoxMenuItemIcon() {
  	if ( checkBoxMenuItemIcon == null ) {
	    checkBoxMenuItemIcon = new CheckBoxMenuItemIcon();
  	}
    return checkBoxMenuItemIcon;
  }

  public static Icon getRadioButtonMenuItemIcon() {
  	if ( radioButtonMenuItemIcon == null ) {
  	  radioButtonMenuItemIcon = new RadioButtonMenuItemIcon();
  	}
  	return radioButtonMenuItemIcon;
  }
  
  public static Icon getSliderVerticalIcon() {
  	if ( sliderVertIcon == null ) {
  	  sliderVertIcon = new SliderVerticalIcon();
  	}
  	return sliderVertIcon;
  }
  
  public static Icon getSliderHorizontalIcon() {
  	if ( sliderHorizIcon == null ) {
  	  sliderHorizIcon = new SliderHorizontalIcon();
  	}
  	return sliderHorizIcon;
  }
 
  public static Icon getTreeCollapsedIcon() {
  	if ( treeCollapsedIcon == null ) {
  	  treeCollapsedIcon = new TreeCollapsedIcon();
  	}
  	return treeCollapsedIcon;
  }
  
  public static Icon getTreeExpandedIcon() {
  	if ( treeExpandedIcon == null ) {
  	  treeExpandedIcon = new TreeExpandedIcon();
  	}
  	return treeExpandedIcon;
  }
  
  /******************************************************************************************/
  private static class CheckBoxIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public CheckBoxIcon() {
      w = 21;
      h = 21;
      
      Color aux = NimRODLookAndFeel.getFocusColor();
      selectColor = new Color( aux.getRed(), aux.getGreen(), aux.getBlue());//, 210);
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JCheckBox b = (JCheckBox) c;
	    ButtonModel model = b.getModel();

	    boolean isEnabled = model.isEnabled();
      boolean isOn = model.isSelected() || model.isPressed();
	    
	    Icon icono = UIManager.getIcon( "CheckBox.iconBase");
      icono.paintIcon( c, g, x, y);

      if ( !isEnabled ) {
  	    g.setColor( new Color( 0,0,0, 63));
        g.fillRect( x+3,y+3, 15,15);
      }
      else if ( isOn ) {
        g.setColor( selectColor);
        g.fillRect( x+3,y+3, 15,15);
      }
      
      if ( model.isArmed() && isEnabled ) {
        g.setColor( new Color( 255,255,155, 127));
        g.fillRect( x+5,y+5, 11,11);
      }
      
      if ( isOn ) {
        icono = UIManager.getIcon( "CheckBox.iconTick");
        icono.paintIcon( c, g, x, y);
      }
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /******************************************************************************************/
  private static class RadioButtonIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public RadioButtonIcon() {
      w = 21;
      h = 21;
      
      Color aux = NimRODLookAndFeel.getFocusColor();
      selectColor = new Color( aux.getRed(), aux.getGreen(), aux.getBlue());//, 210);
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JRadioButton b = (JRadioButton) c;
	    ButtonModel model = b.getModel();

	    boolean isEnabled = model.isEnabled();
      boolean isOn = model.isSelected() || model.isPressed();
	    
	    Icon icono = UIManager.getIcon( "RadioButton.iconBase");
      icono.paintIcon( c, g, x, y);

      if ( !isEnabled ) {
  	    g.setColor( new Color( 0,0,0, 63));
        g.fillOval( x+3,y+3, 15,15);
      }
      else if ( isOn ) {
        g.setColor( selectColor);
        g.fillOval( x+3,y+3, 15,15);
      }
      
      if ( model.isArmed() && isEnabled ) {
        g.setColor( new Color( 255,255,155, 127));
        g.fillOval( x+5,y+5, 11,11);
      }
      
      if ( isOn ) {
        icono = UIManager.getIcon( "RadioButton.iconTick");
        icono.paintIcon( c, g, x, y);
      }
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /******************************************************************************************/
  private static class CheckBoxMenuItemIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public CheckBoxMenuItemIcon() {
      w = 21;
      h = 21;
      
      Color aux = NimRODLookAndFeel.getFocusColor();
      selectColor = new Color( aux.getRed(), aux.getGreen(), aux.getBlue());//, 210);
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JMenuItem b = (JMenuItem) c;
	    ButtonModel model = b.getModel();

	    boolean isEnabled = model.isEnabled();
      boolean isOn = model.isSelected() || model.isPressed();
	   
      Icon icono = UIManager.getIcon( "MenuCheckBox.iconBase");
      icono.paintIcon( c, g, x, y);

      if ( !isEnabled ) {
  	    g.setColor( new Color( 0,0,0, 63));
        g.fillRect( x+3,y+3, 15,15);
      }
      else if ( isOn ) {
        g.setColor( selectColor);
        g.fillRect( x+3,y+3, 15,15);
      }
      
      if ( model.isArmed() && isEnabled ) {
        g.setColor( new Color( 255,255,155, 127));
        g.fillRect( x+5,y+5, 11,11);
      }
      
      if ( isOn ) {
        icono = UIManager.getIcon( "MenuCheckBox.iconTick");
        icono.paintIcon( c, g, x, y);
      }
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /******************************************************************************************/
  private static class RadioButtonMenuItemIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public RadioButtonMenuItemIcon() {
      w = 21;
      h = 21;
      
      Color aux = NimRODLookAndFeel.getFocusColor();
      selectColor = new Color( aux.getRed(), aux.getGreen(), aux.getBlue());//, 210);
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JMenuItem b = (JMenuItem) c;
	    ButtonModel model = b.getModel();

	    boolean isEnabled = model.isEnabled();
      boolean isOn = model.isSelected() || model.isPressed();
	    
	    Icon icono = UIManager.getIcon( "MenuRadioButton.iconBase");
      icono.paintIcon( c, g, x, y);

      if ( !isEnabled ) {
  	    g.setColor( new Color( 0,0,0, 63));
        g.fillOval( x+3,y+3, 15,15);
      }
      else if ( isOn ) {
        g.setColor( selectColor);
        g.fillOval( x+3,y+3, 15,15);
      }
      
      if ( model.isArmed() && isEnabled ) {
        g.setColor( new Color( 255,255,155, 127));
        g.fillOval( x+5,y+5, 11,11);
      }
      
      if ( isOn ) {
        icono = UIManager.getIcon( "MenuRadioButton.iconTick");
        icono.paintIcon( c, g, x, y);
      }
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /***************************************************************************************************************/
  private static class SliderVerticalIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public SliderVerticalIcon() {
      w = 21;
      h = 19;
      
      selectColor = NimRODLookAndFeel.getFocusColor();
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JSlider b = (JSlider)c;
      
      if ( c.hasFocus() ) {
        g.setColor( selectColor);
        g.fillOval( x+1,y+4, 17,11);
      }
      else if ( !c.isEnabled() ) {
        g.setColor( Color.gray);
        g.fillOval( x+1,y+4, 17,11);
      }
      
      Icon icono = UIManager.getIcon( "Slider.verticalThumbIconImage");
      icono.paintIcon( c, g, x, y);
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /***************************************************************************************************************/
  private static class SliderHorizontalIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    private Color selectColor;
    
    public SliderHorizontalIcon() {
      w = 19;
      h = 21;
      
      selectColor = NimRODLookAndFeel.getFocusColor();
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JSlider b = (JSlider)c;
      
      if ( c.hasFocus() ) {
        g.setColor( selectColor);
        g.fillOval( x+3,y+2, 11,17);
      }
      else if ( !c.isEnabled() ) {
        g.setColor( Color.gray);
        g.fillOval( x+3,y+2, 11,17);
      }
      
      Icon icono = UIManager.getIcon( "Slider.horizontalThumbIconImage");
      icono.paintIcon( c, g, x, y);
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  
  /***************************************************************************************************************/
  private static class TreeCollapsedIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    
    public TreeCollapsedIcon() {
      w = 18;
      h = 18;
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JTree b = (JTree)c;
      
      g.setColor( NimRODLookAndFeel.getFocusColor());
      g.fillOval( x+2,y+2, 13,13);
      
      Icon icono = UIManager.getIcon( "Tree.EstructuraCollapsedIcon");
      icono.paintIcon( c, g, x, y);
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
  
  /***************************************************************************************************************/
  private static class TreeExpandedIcon implements Icon, UIResource, Serializable { 
    private int w, h;
    
    public TreeExpandedIcon() {
      w = 18;
      h = 18;
    }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
	    JTree b = (JTree)c;
      
      g.setColor( NimRODLookAndFeel.getFocusColor());
      g.fillOval( x+2,y+2, 13,13);
      
      Icon icono = UIManager.getIcon( "Tree.EstructuraExpandedIcon");
      icono.paintIcon( c, g, x, y);
  	}

	  public int getIconWidth() { 
      return w;
    } 

	  public int getIconHeight() { 
      return h; 
    }
  }
}
