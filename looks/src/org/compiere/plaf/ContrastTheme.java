/*
 * @(#)ContrastTheme.java 1.10 04/07/26 Copyright (c) 2004 Sun Microsystems,
 * Inc. All Rights Reserved. Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met: -Redistribution of source code must retain the above
 * copyright notice, this list of conditions and the following disclaimer.
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. Neither the name of
 * Sun Microsystems, Inc. or the names of contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission. This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC.
 * ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. You
 * acknowledge that this software is not designed, licensed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 */
package org.compiere.plaf;

/*
 * @(#)ContrastTheme.java 1.10 04/07/26
 */
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * This class describes a higher-contrast Metal Theme.
 * 
 * @version 1.10 07/26/04
 * @author Michael C. Albers, Jorg Janke
 */
public class ContrastTheme extends DefaultMetalTheme
{

	public String getName ()
	{
		return "Contrast";
	}

	private final ColorUIResource primary1
		= new ColorUIResource (0, 0, 0);

	private final ColorUIResource primary2
		= new ColorUIResource (204, 204, 204);

	private final ColorUIResource primary3
		= new ColorUIResource (255, 255, 255);

	private final ColorUIResource primaryHighlight
		= new ColorUIResource (102, 102, 102);

	private final ColorUIResource secondary2
		= new ColorUIResource (204, 204, 204);

	private final ColorUIResource secondary3
		= new ColorUIResource (255, 255, 255);

	private final ColorUIResource controlHighlight
		= new ColorUIResource (102, 102, 102);

	protected ColorUIResource getPrimary1 ()
	{
		return primary1;
	}

	protected ColorUIResource getPrimary2 ()
	{
		return primary2;
	}

	protected ColorUIResource getPrimary3 ()
	{
		return primary3;
	}

	public ColorUIResource getPrimaryControlHighlight ()
	{
		return primaryHighlight;
	}

	protected ColorUIResource getSecondary2 ()
	{
		return secondary2;
	}

	protected ColorUIResource getSecondary3 ()
	{
		return secondary3;
	}

	public ColorUIResource getControlHighlight ()
	{
		return super.getSecondary3 ();
	}

	public ColorUIResource getFocusColor ()
	{
		return getBlack ();
	}

	public ColorUIResource getTextHighlightColor ()
	{
		return getBlack ();
	}

	public ColorUIResource getHighlightedTextColor ()
	{
		return getWhite ();
	}

	public ColorUIResource getMenuSelectedBackground ()
	{
		return getBlack ();
	}

	public ColorUIResource getMenuSelectedForeground ()
	{
		return getWhite ();
	}

	public ColorUIResource getAcceleratorForeground ()
	{
		return getBlack ();
	}

	public ColorUIResource getAcceleratorSelectedForeground ()
	{
		return getWhite ();
	}

	public void addCustomEntriesToTable (UIDefaults table)
	{
		Border blackLineBorder = new BorderUIResource 
			(new LineBorder(getBlack ()));
		Border whiteLineBorder = new BorderUIResource(
			new LineBorder(getWhite()));
		//
		Object textBorder = new BorderUIResource (new CompoundBorder 
			(blackLineBorder, new BasicBorders.MarginBorder()));

		//	Enhancements
		Object[] defaults = new Object[]
		{
			"ToolTip.border", blackLineBorder, 
			"TitledBorder.border", blackLineBorder, 
			"TextField.border", textBorder,
			"PasswordField.border", textBorder, 
			"TextArea.border", textBorder,
			"TextPane.border", textBorder, 
			"EditorPane.border", textBorder,
			//
			"ComboBox.background", getWindowBackground (),
			"ComboBox.foreground", getUserTextColor (),
			"ComboBox.selectionBackground", getTextHighlightColor (),
			"ComboBox.selectionForeground", getHighlightedTextColor (),
			"ProgressBar.foreground", getUserTextColor (),
			"ProgressBar.background", getWindowBackground (),
			"ProgressBar.selectionForeground", getWindowBackground (),
			"ProgressBar.selectionBackground", getUserTextColor (),
			"OptionPane.errorDialog.border.background", getPrimary1 (),
			"OptionPane.errorDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.errorDialog.titlePane.background", getPrimary1 (),
			"OptionPane.errorDialog.titlePane.shadow", getPrimary2 (),
			"OptionPane.questionDialog.border.background", getPrimary1 (),
			"OptionPane.questionDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.questionDialog.titlePane.background", getPrimary1 (),
			"OptionPane.questionDialog.titlePane.shadow", getPrimary2 (),
			"OptionPane.warningDialog.border.background", getPrimary1 (),
			"OptionPane.warningDialog.titlePane.foreground", getPrimary3 (),
			"OptionPane.warningDialog.titlePane.background", getPrimary1 (),
			"OptionPane.warningDialog.titlePane.shadow", getPrimary2 (),
		};
		table.putDefaults (defaults);
	}
}