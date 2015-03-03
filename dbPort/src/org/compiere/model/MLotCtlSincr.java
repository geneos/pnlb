/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.util.Iterator;
import java.util.Properties;
import org.compiere.util.QueryDB;

/**
 *
 * @author santiago
 */
public class MLotCtlSincr extends X_M_LotCtl_Sincr{
	
        /**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_LotCtl_Sincr_ID id
	 */
	private MLotCtlSincr (Properties ctx, int M_LotCtl_Sincr_ID, String trxName)
	{
		super (ctx, M_LotCtl_Sincr_ID, trxName);
	}	//	MLotCtlSincr
        /**
         * Método que retorna el id correspondiente para un control de  
         * secuencia de lotes dado
         * @param M_LotCtl_ID control de secuencia de lotes
         * @return
         */
        private static int getMLotCtlSincr_ID(int M_LotCtl_ID){
            QueryDB query = new QueryDB("org.compiere.model.X_M_LotCtl_Sincr");
            String filter = "M_LotCtl_ID = " + M_LotCtl_ID;
            java.util.List results = query.execute(filter);
            Iterator select = results.iterator();
            while (select.hasNext())
            {
             X_M_LotCtl_Sincr lotSincr =  (X_M_LotCtl_Sincr) select.next();                                          
             return lotSincr.getM_LotCtl_Sincr_ID();
            }
            return 0;
        }
        
        public synchronized static MLotCtlSincr getMLotCtlSincr(Properties ctx,int M_LotCtl_ID){
            int id =getMLotCtlSincr_ID(M_LotCtl_ID);
            MLotCtlSincr lot = new MLotCtlSincr(ctx,id,null);
            //creo uno nuevo si no existía
            if (id==0){
                lot.setIsDisponible(true);
                lot.setM_LotCtl_ID(M_LotCtl_ID);
                lot.save(); 
            }
            return lot;
        }
        
        /** BISion - 29/10/2008 - Santiago Ibañez
         * Método para chequear disponibilidad de un control de lote  y en caso de
         * estar disponible establecerlo como NO disponible.
         * Si Active = 'Y' entonces está disponible.
         * @return
         */
        public synchronized boolean estaDisponible(){
            //Si está disponible...
            if (isDisponible()){
                //lo bloquea para que nadie lo utilice.
                setIsDisponible(false);
                saveUpdate();
                return true;
            }
            return false;
        }
        
        public void desbloquear(){
            setIsDisponible(true);
            saveUpdate();
        }
}
