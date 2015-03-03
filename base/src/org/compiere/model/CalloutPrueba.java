package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

public class CalloutPrueba extends CalloutEngine
{
    private boolean steps = false;
    
    public String DiscountQty (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
    {
        if (isCalloutActive() || value == null)
			return "";
	setCalloutActive(true);
        
        BigDecimal PriceEntered, DiscountQty, PriceList;
        
        PriceEntered = (BigDecimal)mTab.getValue("PriceEntered");
        DiscountQty = (BigDecimal)mTab.getValue("Discount_Qty");
        PriceList = (BigDecimal)mTab.getValue("PriceList");
        
        setCalloutActive(false);
        return "";
    }
}
