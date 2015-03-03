package org.eevolution.tools.swing;

import java.io.StreamTokenizer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public class CharDefinition {

    public CharDefinition() {
    }


    public static CharDefinition getDefaultDef() {
		
    	CharDefinition def = new CharDefinition();
		def.setAllowed("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 .,&-/+*$%");

        return def;
    }
    public static CharDefinition getNumberDef() {
		
    	CharDefinition def = new CharDefinition();
		def.setAllowed("1234567890");

        return def;
    }
    public static CharDefinition getLetterDef() {
		
    	CharDefinition def = new CharDefinition();
		def.setAllowed("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

        return def;
    }
    public static CharDefinition getCurrencyDef() {
		
    	CharDefinition def = new CharDefinition();
		def.setAllowed("1234567890,");

        return def;
    }

	public boolean contains(char c) {
		
		for(int i = 0; i < g_definition.length; i++) {
		
			if(c == g_definition[i]) return true;
		}
		
		return false;
	}

	public void setAllowed(String str) {
		
		g_definition = str.toCharArray();
	}


	private char[] g_definition;
}