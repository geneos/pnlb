package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;
/**
 * Clase creada para que al momento de seleccionar un producto, en la ventana 
 * orden de manufactura, se setee en forma automática la unidad para dicha 
 * ventana. (COMMITTED)
 * @author Santiago Ibañez- 12/09/2008
 */

public class CalloutMPCOrder extends CalloutEngine
{
    public String setUOM (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value)
    {
        if (isCalloutActive() || value == null)
			return "";
	setCalloutActive(true);
        
        //obtengo el ID de producto
        int M_Product_ID = ((Integer)value).intValue();
        
        //obtengo el producto asociado a la orden de manufactura
        MProduct p = new MProduct(ctx,M_Product_ID,null);
        
        //asigno la unidad del producto  a la orden de manufactura
        mTab.setValue("C_UOM_ID", new Integer(p.getC_UOM_ID()));
           
        setCalloutActive(false);
        return "";
    }
}
