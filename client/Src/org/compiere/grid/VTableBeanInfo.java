package org.compiere.grid;

import java.beans.*;

/**
 *  Generated
 *
 *  @author Jorg Janke
 *  @version  $Id: VTableBeanInfo.java,v 1.2 2001/10/16 21:35:41 jjanke Exp $
 */
public class VTableBeanInfo extends SimpleBeanInfo
{
	private Class beanClass = VTable.class;
	private String iconColor16x16Filename;
	private String iconColor32x32Filename;
	private String iconMono16x16Filename;
	private String iconMono32x32Filename;

	public VTableBeanInfo()
	{
	}
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		PropertyDescriptor[] pds = new PropertyDescriptor[] { };
		return pds;
	}
	public java.awt.Image getIcon(int iconKind)
	{
		switch (iconKind) {
		case BeanInfo.ICON_COLOR_16x16:
			  return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
		case BeanInfo.ICON_COLOR_32x32:
			  return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
		case BeanInfo.ICON_MONO_16x16:
			  return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
		case BeanInfo.ICON_MONO_32x32:
			  return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
								}
		return null;
	}
}