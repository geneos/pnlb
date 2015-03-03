package org.compiere.apps;

import java.beans.*;

/**
 *  Generated
 *
 *  @author Jorg Janke
 *  @version  $Id: ConfirmPanelBeanInfo.java,v 1.3 2002/01/11 06:11:38 jjanke Exp $
 */

public class ConfirmPanelBeanInfo extends SimpleBeanInfo
{
	private Class beanClass = ConfirmPanel.class;
	private String iconColor16x16Filename;
	private String iconColor32x32Filename;
	private String iconMono16x16Filename;
	private String iconMono32x32Filename;

	public ConfirmPanelBeanInfo()
	{
	}
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			PropertyDescriptor _cancelButton = new PropertyDescriptor("cancelButton", beanClass, "getCancelButton", null);
			PropertyDescriptor _cancelVisible = new PropertyDescriptor("cancelVisible", beanClass, "isCancelVisible", "setCancelVisible");
			PropertyDescriptor _customizeButton = new PropertyDescriptor("customizeButton", beanClass, "getCustomizeButton", null);
			PropertyDescriptor _enabled = new PropertyDescriptor("enabled", beanClass, null, "setEnabled");
			PropertyDescriptor _historyButton = new PropertyDescriptor("historyButton", beanClass, "getHistoryButton", null);
			PropertyDescriptor _OKButton = new PropertyDescriptor("OKButton", beanClass, "getOKButton", null);
			PropertyDescriptor _OKVisible = new PropertyDescriptor("OKVisible", beanClass, "isOKVisible", "setOKVisible");
			PropertyDescriptor _refreshButton = new PropertyDescriptor("refreshButton", beanClass, "getRefreshButton", null);
			PropertyDescriptor _zoomButton = new PropertyDescriptor("zoomButton", beanClass, "getZoomButton", null);
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_cancelButton,
				_cancelVisible,
				_customizeButton,
				_enabled,
				_historyButton,
				_OKButton,
				_OKVisible,
				_refreshButton,
				_zoomButton};
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