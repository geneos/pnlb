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
 * The main class for the NimROD Look&Feel.
 *
 * To use this Look&Feel, simply include these two lines into your code:<br>
 * <code>
 * NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
 * UIManager.setLookAndFeel( NimRODLF);
 * </code>
 * You can change the default theme color in two ways.
 * 
 * You can create theme and change de colours with this lines:<br>
 *   <code>
 *   NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
 *   NimRODTheme nt = new NimRODTheme();
 *   nt.setX( Color);
 *   ....
 *   nt.setX( Color);
 *   NimRODLF.setCurrentTheme( nt);
 *   UIManager.setLookAndFeel( NimRODLF);
 *   <code><br>
 * This way is good if you can change the sources and you want the program works *only* with NimRODLF. 
 *
 * If you don't have the sources you can change the theme color including same properties in your command line and the look and feel
 * will do its best... This couldn't work if the application changes the system properties, but, well, if you don't have the sources...<br>
 * For example:<br>
 *   <code>java -Dnimrodlf.selection=0x00cc00 XXX.YOUR.APP.XXX</code> will colour with green the selected widgets<br>
 *   <code>java java -Dnimrodlf.s1=0xdde8ee -Dnimrodlf.s2=0xb7daec -Dnimrodlf.s3=0x74bfe6 XXX.YOUR.APP.XXX</code> will colour with blue the background of the widgets<br>
 * The values are in the tipical HTML format (0xRRGGBB) with the red, green and blue values encoded in hexadecimal format.<br> 
 * These are the admited properties:
 * <ul>   
 * <li>nimrodlf.selection: this is the selection color</li>
 * <li>nimrodlf.background: this is the background color</li>
 * <li>nimrodlf.p1: this is the primary1 color (�Don't you understand? Patience?</li>
 * <li>nimrodlf.p2: this is the primary2 color</li>
 * <li>nimrodlf.p3: this is the primary3 color</li>
 * <li>nimrodlf.s1: this is the secondary1 color</li>
 * <li>nimrodlf.s2: this is the secondary2 color</li>
 * <li>nimrodlf.s3: this is the secondary3 color</li>
 * <li>nimrodlf.b: this is the black color</li>
 * <li>nimrodlf.w: this is the white color</li>
 * </ul>
 * �Primary color? �Secondary? �What the...? Cool. <a href='http://java.sun.com/products/jlf/ed1/dg/higg.htm#62001'>Here</a> you can learn what 
 * i'm talking about. Swing applications have only 8 colors, named PrimaryX, SecondaryX, White and Black, and <a href='http://java.sun.com/products/jlf/ed1/dg/higg.htm#62001'>here</a>
 * you hava a table with the who-is-who.<br>
 * You don't need to write all the values, you only must write those values you want to change. There are two shorthand properties, selection and background.
 * If you write nimrodlf.selection or nimrodlf.background the NimRODLF will calculate the colors around (darker and lighter) your choose.<br>
 * If nimrodlf.selection is writen, pX, sX, b and w are ignored. 
 * Ahh!! One more thing. 0xRRGGBB is equal #RRGGBB. 
 * @see NimRODTheme
 * @see http://java.sun.com/products/jlf/ed1/dg/higg.htm#62001
 * @author Nilo J. Gonzalez 
 */
 
package org.eevolution.plaf.nimrod;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.io.*;


public class NimRODLookAndFeel extends MetalLookAndFeel {
  public static Color brillo = new Color( 255,255,255, 64);
	public static Color sombra = new Color( 20,20,20, 50);
	
  public static Color negroOpaco = new Color(0,0,0, 64);
  public static Color negroTrans = new Color(0,0,0, 0);
  
  public static Color blancoOpaco = new Color(255,255,255, 128);
  public static Color blancoTrans = new Color(255,255,255, 0);
  
  public NimRODLookAndFeel() {
    super();
    
    NimRODTheme nt = new NimRODTheme();
    
    try {
      String base = System.getProperty( "nimrodlf.selection");
      String s = System.getProperty( "nimrodlf.background");
      
      if ( base != null ) {
        if ( s != null ) {
          nt = new NimRODTheme( Color.decode( base), Color.decode( s));
        }
        else {
          nt = new NimRODTheme( Color.decode( base));
        }
      }
      else {
        String p1 = System.getProperty( "nimrodlf.p1");
        String p2 = System.getProperty( "nimrodlf.p2");
        String p3 = System.getProperty( "nimrodlf.p3");
        
        String s1 = System.getProperty( "nimrodlf.s1");
        String s2 = System.getProperty( "nimrodlf.s2");
        String s3 = System.getProperty( "nimrodlf.s3");
        
        String w = System.getProperty( "nimrodlf.w");
        String b = System.getProperty( "nimrodlf.b");
        
        if ( p1 != null ) { nt.setPrimary1( Color.decode( p1)); }
        if ( p2 != null ) { nt.setPrimary2( Color.decode( p2)); }
        if ( p3 != null ) { nt.setPrimary3( Color.decode( p3)); }
        
        if ( s1 != null ) { nt.setSecondary1( Color.decode( s1)); }
        if ( s2 != null ) { nt.setSecondary2( Color.decode( s2)); }
        if ( s3 != null ) { nt.setSecondary3( Color.decode( s3)); }
        
        if ( w != null ) { nt.setWhite( Color.decode( w)); }
        if ( b != null ) { nt.setBlack( Color.decode( b)); }
      }
    }
    catch ( Exception ex ) {
      nt = new NimRODTheme();
    }
    
    this.setCurrentTheme( nt);
  }

  public String getID() {
    return "NimROD";
  }

  public String getName() {
    return "NimROD";
  }

  public String getDescription() {
    return "Look and Feel NimROD, 2005";
  }

  public boolean isNativeLookAndFeel() {
    return false;
  }

  public boolean isSupportedLookAndFeel() {
    return true;
  }



  protected void initClassDefaults( UIDefaults table) {
    super.initClassDefaults( table);
    //begin vpj-cd e-evolution 30 Dic 2005 
    table.put( "ComboBoxUI", "org.compiere.plaf.CompiereComboBoxUI");
    table.put( "LabelUI", "org.eevolution.plaf.nimrod.KompiereLabelUI");
    //end vpj-cd e-evolution 30 Dic 2005 
    table.put( "ButtonUI", "org.eevolution.plaf.nimrod.NimRODButtonUI");
    table.put( "ToggleButtonUI", "org.eevolution.plaf.nimrod.NimRODToggleButtonUI");
    table.put( "TextFieldUI", "org.eevolution.plaf.nimrod.NimRODTextFieldUI");
    table.put( "PasswordFieldUI", "org.eevolution.plaf.nimrod.NimRODPasswordFieldUI");
    table.put( "CheckBoxUI", "org.eevolution.plaf.nimrod.NimRODCheckBoxUI");
    table.put( "RadioButtonUI", "org.eevolution.plaf.nimrod.NimRODRadioButtonUI");
    table.put( "FormattedTextFieldUI", "org.eevolution.plaf.nimrod.NimRODFormattedTextFieldUI");
    table.put( "SliderUI", "org.eevolution.plaf.nimrod.NimRODSliderUI");
    
    table.put( "ListUI", "org.eevolution.plaf.nimrod.NimRODListUI");
    table.put( "ScrollBarUI", "org.eevolution.plaf.nimrod.NimRODScrollBarUI");
    table.put( "ToolBarUI", "org.eevolution.plaf.nimrod.NimRODToolBarUI");
    table.put( "ProgressBarUI", "org.eevolution.plaf.nimrod.NimRODProgressBarUI");
    
    table.put( "TabbedPaneUI", "org.eevolution.plaf.nimrod.NimRODTabbedPaneUI");
    table.put( "TableHeaderUI", "org.eevolution.plaf.nimrod.NimRODTableHeaderUI");
    table.put( "SplitPaneUI", "org.eevolution.plaf.nimrod.NimRODSplitPaneUI");
    
    // Todo esto, es para sacar un triste menu    
    table.put( "MenuBarUI", "org.eevolution.plaf.nimrod.NimRODMenuBarUI");
    table.put( "MenuUI", "org.eevolution.plaf.nimrod.NimRODMenuUI");
    table.put( "PopupMenuUI", "org.eevolution.plaf.nimrod.NimRODPopupMenuUI");
    table.put( "PopupMenuSeparatorUI", "org.eevolution.plaf.nimrod.NimRODPopupMenuSeparatorUI");
    table.put( "MenuItemUI", "org.eevolution.plaf.nimrod.NimRODMenuItemUI");
    table.put( "CheckBoxMenuItemUI", "org.eevolution.plaf.nimrod.NimRODCheckBoxMenuItemUI");
    table.put( "RadioButtonMenuItemUI", "org.eevolution.plaf.nimrod.NimRODRadioButtonMenuItemUI");
    
    /*
    for( Enumeration en = table.keys(); en.hasMoreElements(); ) {
      System.out.println( "[" + (String)en.nextElement() + "]");
    }
    */
  }


  protected void initSystemColorDefaults( UIDefaults table) {
    super.initSystemColorDefaults( table);
    
    // Esto es para que todo lo que este seleccionado tenga el mismo color.
    table.put( "textHighlight", getMenuSelectedBackground());
  }


  protected void initComponentDefaults( UIDefaults table) {
    super.initComponentDefaults( table);

    // Para el JTree
    table.put( "Tree.collapsedIcon", NimRODIconFactory.getTreeCollapsedIcon());
    table.put( "Tree.expandedIcon", NimRODIconFactory.getTreeExpandedIcon());
    table.put( "Tree.closedIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/TreeDirCerrado.png"));
    table.put( "Tree.openIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/TreeDirAbierto.png"));
    table.put( "Tree.leafIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/TreeFicheroIcon.png"));
    table.put( "Tree.EstructuraCollapsedIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/TreeCollapsedIcon.png"));
    table.put( "Tree.EstructuraExpandedIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/TreeExpandedIcon.png"));
    
    // Los dialogos de ficheros
    table.put( "FileView.directoryIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogDirCerrado.png"));
    table.put( "FileView.fileIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogFicheroIcon.png"));
    table.put( "FileView.floppyDriveIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogFloppyIcon.png"));
    table.put( "FileView.hardDriveIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogHDIcon.png"));
    table.put( "FileChooser.newFolderIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogNewDir.png"));
    table.put( "FileChooser.homeFolderIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogHome.png"));
    table.put( "FileChooser.upFolderIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogDirParriba.png"));
    table.put( "FileChooser.detailsViewIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogDetails.png"));
    table.put( "FileChooser.listViewIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/DialogList.png"));
    
    
    // Para los muchos CheckBox y RadioButtons
    table.put( "CheckBoxMenuItem.checkIcon", NimRODIconFactory.getCheckBoxMenuItemIcon());
    table.put( "RadioButtonMenuItem.checkIcon", NimRODIconFactory.getRadioButtonMenuItemIcon());
    
    table.put( "Menu.checkIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/bordeMenu.png"));
    table.put( "MenuItem.checkIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/bordeMenu.png"));
    table.put( "MenuCheckBox.iconBase", loadRes( "/org/eevolution/plaf/nimrod/icons/MenuCheckBoxBase.png"));
    table.put( "MenuCheckBox.iconTick", loadRes( "/org/eevolution/plaf/nimrod/icons/MenuCheckBoxTick.png"));
    table.put( "MenuRadioButton.iconBase", loadRes( "/org/eevolution/plaf/nimrod/icons/MenuRadioBase.png"));
    table.put( "MenuRadioButton.iconTick", loadRes( "/org/eevolution/plaf/nimrod/icons/MenuRadioTick.png"));
    table.put( "CheckBox.iconBase", loadRes( "/org/eevolution/plaf/nimrod/icons/CheckBoxBase.png"));
    table.put( "CheckBox.iconTick", loadRes( "/org/eevolution/plaf/nimrod/icons/CheckBoxTick.png"));
    table.put( "RadioButton.iconBase", loadRes( "/org/eevolution/plaf/nimrod/icons/RadioButtonBase.png"));
    table.put( "RadioButton.iconTick", loadRes( "/org/eevolution/plaf/nimrod/icons/RadioButtonTick.png"));
    
    // Los iconillos de los dialogos
    table.put( "OptionPane.errorIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/Error.png"));
    table.put( "OptionPane.informationIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/Inform.png"));
    table.put( "OptionPane.warningIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/Warn.png"));
    table.put( "OptionPane.questionIcon", loadRes( "/org/eevolution/plaf/nimrod/icons/Question.png"));
    
    // PAra el JSlider
    table.put( "Slider.horizontalThumbIcon", NimRODIconFactory.getSliderHorizontalIcon());
    table.put( "Slider.verticalThumbIcon", NimRODIconFactory.getSliderVerticalIcon());
    table.put( "Slider.horizontalThumbIconImage", loadRes( "/org/eevolution/plaf/nimrod/icons/HorizontalThumbIconImage.png"));
    table.put( "Slider.verticalThumbIconImage", loadRes( "/org/eevolution/plaf/nimrod/icons/VerticalThumbIconImage.png"));
    
    // Margenes de los botones
    table.put( "Button.margin", new InsetsUIResource( 5,14, 5,14));
    table.put( "ToggleButton.margin", new InsetsUIResource( 5,14, 5,14));
    
    /* Esto es solo para saber que hay en la tabla
    for( Enumeration en = table.keys(); en.hasMoreElements(); ) {
      System.out.println( "[" + (String)en.nextElement() + "]");
    }
    */
  }

  /****************************************
  UTILIDADES
  *****************************************/

  
  private ImageIcon loadRes( String fich) {
    try {
      return new ImageIcon( Toolkit.getDefaultToolkit().createImage( readStream( this.getClass().getResourceAsStream( fich))));
    }
    catch ( Exception ex) {
      ex.printStackTrace();
      System.out.println( "No se puede cargar el recurso " + fich);
      return null;
    }
  }

  private byte[] readStream( InputStream input) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    int read;
    byte[] buffer = new byte[256];
    
    while ( (read = input.read( buffer, 0, 256)) != -1 ) {
      bytes.write( buffer, 0, read);
    }
    
    return bytes.toByteArray();
  }
  
  
  /**
   * Esta funcion se usa para pintar la barra de seleccion de los menus. Esta aqui
   * para no repetirla en todas partes...
   */
  public static void pintaBarraMenu( Graphics g, JMenuItem menuItem, Color bgColor) {
    ButtonModel model = menuItem.getModel();
    Color oldColor = g.getColor();

    int menuWidth = menuItem.getWidth();
    int menuHeight = menuItem.getHeight();

    if ( menuItem.isOpaque() ) {
      g.setColor( menuItem.getBackground());
      g.fillRect( 0,0, menuWidth, menuHeight);
        
      if ( model.isArmed() || (menuItem instanceof JMenu && model.isSelected()) ) {
        RoundRectangle2D.Float boton = new RoundRectangle2D.Float(); 
        boton.x = 1;
        boton.y = 0;
        boton.width = menuWidth - 3;
        boton.height = menuHeight - 1;
        boton.arcwidth = 8;
        boton.archeight = 8;
        
        GradientPaint grad = new GradientPaint( 1,1, NimRODLookAndFeel.brillo, 
                                                0,menuHeight, NimRODLookAndFeel.sombra);
        
        Graphics2D g2D = (Graphics2D)g;
        
        g.setColor( bgColor);
        g2D.fill( boton);
        
        g.setColor( bgColor.darker());
        g2D.draw( boton);
        
        g2D.setPaint( grad);
        g2D.fill( boton);
      } 

      g.setColor( oldColor);
    }
  }
}