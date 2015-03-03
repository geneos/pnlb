package org.compiere.apps;

import java.beans.*;

/**
 *  Generated
 *
 *  @author Jorg Janke
 *  @version  $Id: StatusBarBeanInfo.java,v 1.2 2001/10/13 00:48:17 jjanke Exp $
 */
public class StatusBarBeanInfo extends SimpleBeanInfo
{
	private Class beanClass = StatusBar.class;
	private String iconColor16x16Filename;
	private String iconColor32x32Filename;
	private String iconMono16x16Filename;
	private String iconMono32x32Filename;

	public StatusBarBeanInfo()
	{
	}
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			PropertyDescriptor _info = new PropertyDescriptor("info", beanClass, null, "setInfo");
			PropertyDescriptor _statusDB = new PropertyDescriptor("statusDB", beanClass, null, "setStatusDB");
			PropertyDescriptor _statusLine = new PropertyDescriptor("statusLine", beanClass, "getStatusLine", "setStatusLine");
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_info,
				_statusDB,
				_statusLine};
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