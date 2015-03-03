package org.eevolution.process;

import java.util.logging.Level;
import org.compiere.model.MProduct;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** BISion - 03/12/2008
 * Clase que integra los 3 procesos principales del MRP.
 * 1 - Calcular niveles inferiores
 * 2 - Crear registros MRP
 * 3 - Generar plan de materiales
 */
public class IntegrateMRPProcess extends SvrProcess {
	
	@Override
	protected String doIt() throws Exception {
                  /*
                   *  03/06/2013 Maria Jesus
                   *  Creacion de una variable del contexto que me indica que se esta corriendo
                   *  en MRP, para que se puedan borrar las OM.
                   */
                Env.setContext(Env.getCtx(), "RUN_MRP", "true");

		//Obtengo la transacci�n global definida en SvrProcess
                Trx m_trx = Trx.get(get_TrxName(), false);

                try
		{
                    //1- Actualizcion de Reservados
                    MRP mrp = new MRP();
                    if (mrp.executeUpdateValues()){
                        //3 - Calcular niveles inferiore
                        CalculateLowLevel cll = new CalculateLowLevel();
                        if (cll.startProcess(getCtx(), this.getProcessInfo(),null)){
                            //3 - Crear registros MRP
                            MRPUpdate mrpu = new MRPUpdate();
                            //if (mrpu.startProcess(getCtx(), this.getProcessInfo(),m_trx)){
                            if (mrpu.startProcess(getCtx(), this.getProcessInfo(),null)){
                                //4-Actualizar Ordenado.
                                if (mrp.actualizarOrdenado()){
                                    //5- Delete MRP
                                     if(mrp.deleteMRP()){
                                        //6 - Generar plan de materiales
                                        if (mrp.startProcess(getCtx(), this.getProcessInfo(),m_trx)){
                                             if ( mrpu.updateMPCOrdersSatisfechas() ){
                                                return "ok";
                                             }
                                             else
                                                 throw new RuntimeException("No se pudo completar el proceso: Actualizar ordenes satisfechas.");
                                        }
                                        else
                                            throw new RuntimeException("No se pudo completar el proceso: Generar Plan de Materiales.");
                                    }
                                    else
                                        throw new RuntimeException("No se pudo completar el proceso: Eliminar Registros MRP(OM).");
                                }
                               else
                                    throw new RuntimeException("No se pudo completar el proceso: Actualizar Ordenado.");

                            }
                            else
                                throw new RuntimeException("No se pudo completar el proceso: Crear registros MRP.");
                        }
                        else
                            throw new RuntimeException("No se pudo completar el proceso: Calculo de Niveles Inferiores.");
                    
                    }
                    else
                         throw new RuntimeException("No se pudo completar la actualización de reservados.");                
                }
		catch (Exception e)
		{
                    //ser� capturada por Svr para deshacer la transacci�n.
                    e.printStackTrace();
                    log.log(Level.SEVERE ,"IntegreateMRP - ", e);
                    throw e;
		}
	}

	@Override
	protected void prepare() {
		
	}
	
}
