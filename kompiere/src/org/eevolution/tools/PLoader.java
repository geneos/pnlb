package org.eevolution.tools;

import java.util.Properties;
import java.io.InputStream;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public class PLoader {

	protected Properties properties;

	public PLoader(String properties) {
		
		init(getClass(), properties);
	}

	public PLoader(Class clazz, String properties) {
		
		init(clazz, properties);
	}
	
	protected void init(Class clazz, String name) {
		
		properties = new Properties();
		InputStream is = clazz.getResourceAsStream(name);
		try {
		
			if(is != null) {
				
				properties.load(is);
			}
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		finally {
			
			try {
				
				if(is != null) {
					
					is.close();
				}
			}
			catch(Exception ee) {
				
				ee.printStackTrace();
			}
		}
	}
	
	public Properties getProperties() {
		
		return properties;
	}
}
