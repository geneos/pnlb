package org.compiere.minigrid;

import java.beans.*;

/**
 *  Generated
 */

public class MiniTableBeanInfo extends SimpleBeanInfo
{
	private Class beanClass = MiniTable.class;
	private String iconColor16x16Filename;
	private String iconColor32x32Filename;
	private String iconMono16x16Filename;
	private String iconMono32x32Filename;

	public MiniTableBeanInfo()
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