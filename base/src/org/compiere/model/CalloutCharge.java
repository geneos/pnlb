/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

     

/*      
*      Bision 22/04/2008
*      CallOut para la exclusion de los campos ISPERCEPADUANERA y ISRETBANK
*      Nadia
*
*/
public class CalloutCharge extends CalloutEngine
{
   public String checkEsPercepcionAduanera (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean espercepcion = (Boolean)mField.getValue();
                       
            if(espercepcion){
                mTab.setValue("ISPERCEPADUANERA", new Boolean(false));                
            }
            return "";
   }    
    
    public String checkEsRetencionBancaria (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean esretencion = (Boolean)mField.getValue();
            if(esretencion){
                mTab.setValue("ISRETBANK", new Boolean(false));                
            }
            return "";
   }    
    
    public String checkEsMontoFijo (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean esMontoFijo = (Boolean)mField.getValue();
            if(esMontoFijo){
                mTab.setValue("PORCENTAJE", null); 
                mTab.setValue("ISPORCENTAJE", new Boolean(false));                 
            }
            return "";
   }    
    
    public String checkEsPorcentaje (Properties ctx, int WindowNo, MTab mTab, MField mField, Object value){
            boolean esPorcentaje = (Boolean)mField.getValue();
            if(esPorcentaje){
                mTab.setValue("MONTOMAXIMO", null);
                mTab.setValue("ISMONTOFIJO", new Boolean(false));               
            }
            return "";
   }        
    
}	

