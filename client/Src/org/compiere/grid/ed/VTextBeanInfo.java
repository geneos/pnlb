package org.compiere.grid.ed;

import java.beans.*;

/**
 *  Generated
 *
 *  @author Jorg Janke
 *  @version  $Id: VTextBeanInfo.java,v 1.2 2001/10/16 03:02:12 jjanke Exp $
 */

public class VTextBeanInfo extends SimpleBeanInfo
{
	private Class beanClass = VText.class;
	private String iconColor16x16Filename;
	private String iconColor32x32Filename;
	private String iconMono16x16Filename;
	private String iconMono32x32Filename;

	public VTextBeanInfo()
	{
	}
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, null, "setBackground");
			PropertyDescriptor _border = new PropertyDescriptor("border", beanClass, null, "setBorder");
			PropertyDescriptor _display = new PropertyDescriptor("display", beanClass, "getDisplay", null);
			PropertyDescriptor _editable = new PropertyDescriptor("editable", beanClass, "isEditable", "setEditable");
			PropertyDescriptor _font = new PropertyDescriptor("font", beanClass, null, "setFont");
			PropertyDescriptor _foreground = new PropertyDescriptor("foreground", beanClass, null, "setForeground");
			PropertyDescriptor _mandatory = new PropertyDescriptor("mandatory", beanClass, "isMandatory", "setMandatory");
			PropertyDescriptor _value = new PropertyDescriptor("value", beanClass, "getValue", "setValue");
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_background,
				_border,
				_display,
				_editable,
				_font,
				_foreground,
				_mandatory,
				_value};
			return pds;








}
		catch(IntrospectionException ex)
		{
			ex.printStackTrace();
			return null;
		}
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
	public BeanInfo[] getAdditionalBeanInfo()
	{
		Class superclass = beanClass.getSuperclass();
		try
		{
			BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
			return new BeanInfo[] { superBeanInfo };
		}
		catch(IntrospectionException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}