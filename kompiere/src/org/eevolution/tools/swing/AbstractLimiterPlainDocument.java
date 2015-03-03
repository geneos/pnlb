package org.eevolution.tools.swing;

import java.math.BigDecimal;

import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public abstract class AbstractLimiterPlainDocument extends PlainDocument {

	protected int charCount;
	protected BigDecimal maxNumber;
	protected BigDecimal minNumber;

	protected CharDefinition charDefinition;

	public abstract void action(String value);
	
    public AbstractLimiterPlainDocument(CharDefinition def){
     
    	super();

		charDefinition = def;
		
		charCount = -1;
    }
    
    public int getCharCount() {
    	
		return charCount;
	}

	public void setCharCount(int charCount) {
		
		this.charCount = charCount;
	}

	public BigDecimal getMaxNumber() {
		
		return maxNumber;
	}

	public BigDecimal getMinNumber() {
		
		return minNumber;
	}

	public void setMaxNumber(BigDecimal number) {
		
		this.maxNumber = number;
	}

	public void setMinNumber(BigDecimal number) {
		
		this.minNumber = number;
	}

	public CharDefinition getCharDefinition() {
		
		return charDefinition;
	}

	public void insertString (int offset, String str, AttributeSet attr) throws BadLocationException {
    	
        if ((str == null) || !charDefinition.contains(str.charAt(0)) ) {
        	
        	return;
        }

        if(getCharCount() > 0) {
        	
            if ((getLength() + str.length()) > charCount){
                
            	return;
            }
        }
        String value = getText(0, offset)+str;
        if(!isValidNumber(value)) {
        	
        	return;
        }

        action(value);
        super.insertString(offset, str, attr);
	}
	
	protected boolean isValidNumber(String str) {

		if(str == null || str.length() == 0) {
			
			return true;
		}
		
		boolean valid = true;

		if(maxNumber != null) {
			
			BigDecimal actual = new BigDecimal(str);

			valid = maxNumber.compareTo(actual) >= 0;
			if(valid && minNumber != null) {
			
				valid = minNumber.compareTo(actual) < 0;
			}
		}
		
        return valid;
	}
}

