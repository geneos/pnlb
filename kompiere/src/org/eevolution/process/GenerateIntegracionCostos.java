package org.eevolution.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;


import javax.swing.JOptionPane;
import org.compiere.model.MCostElement;
import org.compiere.model.MDocType;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.eevolution.model.MMPCCostDetail;
import org.eevolution.model.MMPCOrderBOMLine;
import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCCostElement;
import org.eevolution.model.MMPCOrderNode;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.MUOM;
import org.compiere.model.X_AD_Process;
import org.compiere.model.X_M_InOutLine;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFNode;
import org.compiere.wf.MWorkflow;
import org.eevolution.model.MCost;
import org.eevolution.model.MInOutLine;
import org.eevolution.model.MInvoice;
import org.eevolution.model.MMPCCostCollector;
import org.eevolution.model.MMPCOrderCost;
import org.eevolution.model.MMPCOrderWorkflow;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.model.QueryDB;
import org.eevolution.tools.UtilProcess;
import org.eevolution.model.MMPCOrder;

import sun.net.www.http.Hurryable;

/** BISion - 26/03/2009 - Santiago IbaÃ±ez
 * Esta clase es utilizada por los procesos de:
 *      Reporte Detallado de Costos Integrados
 *      Reporte de Costos Integrados
 * El reporte de Costos Integrados recalcula item por item, esto se podria evitar
 * si se mantiene informacion que no varia de la OM (ej duracion del nodo, etc) en
 * una tabla y de esta forma solo se tendria que calcular el precio de la ultima compra
 * que es el unico atributo dinamico.
 * @author santiago
 */

public class GenerateIntegracionCostos extends SvrProcess{
			//Parametros a cargar
			//AD_Client_ID
			private int AD_Client_ID;
			//AD_Org_ID
			private int AD_Org_ID;
			//Producto
			private int M_Product_ID = 0;
			//Cantidad Teorica
			private BigDecimal CantidadTeorica = BigDecimal.ZERO;
			//Cantidad Real
			private BigDecimal CantidadReal = BigDecimal.ZERO;
			//Variacion de la Cantidad
			private BigDecimal VCantidad = BigDecimal.ZERO;
			//Variacion porcentual de la Cantidad
			private BigDecimal VPCantidad = BigDecimal.ZERO;
			//Precio Teorico
			private BigDecimal PrecioTeorico = BigDecimal.ZERO;
			//Precio Real
			private BigDecimal PrecioReal = BigDecimal.ZERO;
			//Variacion del Precio
			private BigDecimal VPrecio = BigDecimal.ZERO;
			//Variacion procentual del Precio
			private BigDecimal VPPrecio = BigDecimal.ZERO;
			//Valoracion de la variacion de la Cantidad
			private BigDecimal ValVCantidad = BigDecimal.ZERO;
			//Variacion del costo final
			private BigDecimal VCostoFinal = BigDecimal.ZERO;
            //Esquema Contable
            private BigDecimal p_C_AcctSchema_ID;

            private int precision = 3;


	       private BigDecimal       p_MPC_Order_ID = BigDecimal.ZERO;
	       private BigDecimal 		C_AcctSchema_ID = BigDecimal.ZERO;
           private BigDecimal       p_M_Product_Category_ID = BigDecimal.ZERO;
           private int              ElementoCostoRecursos = 0;
           private int              ElementoCostoMateriales = 0;
           private int              ElementoCostoIndirecto = 0;
           private int              ElementoCostoEstandarRecursos = 0;
           private int              ElementoCostoEstandarMateriales = 0;
           private int              ElementoCostoEstandarIndirecto = 0;

           private BigDecimal       RealTotal = BigDecimal.ZERO;

           /**
            * Para los Costos Totales
            */
           private  BigDecimal precioMat = BigDecimal.ZERO;
           private  BigDecimal precioRec = BigDecimal.ZERO;
           private  BigDecimal precioInd = BigDecimal.ZERO;
           private  BigDecimal precioMatT = BigDecimal.ZERO;
           private  BigDecimal precioRecT = BigDecimal.ZERO;
           private  BigDecimal precioIndT = BigDecimal.ZERO;
           

           private Timestamp from = null;
           private Timestamp to = null;
		/**
		 *  Prepare - e.g., get Parameters.
		 */
		protected void prepare()
		{
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				if (name.equals("MPC_Order_ID")){
					p_MPC_Order_ID = ((BigDecimal)para[i].getParameter());
				}
                else if (name.equals("C_AcctSchema_ID"))
                    p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter());
	            else if (name.equals("M_Product_Category_ID"))
                    p_M_Product_Category_ID = ((BigDecimal)para[i].getParameter());
                else if (name.equals("DateOrdered")){
                    from = ((Timestamp)para[i].getParameter());
                    to = ((Timestamp)para[i].getParameter_To());
                }
                else
					log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
			}
            //AD_Client_ID
			AD_Client_ID = Env.getAD_Client_ID(getCtx());
			//AD_Org_ID
			AD_Org_ID = Env.getAD_Org_ID(getCtx());
            //Esquema contable
            int esquema = Env.getContextAsInt(getCtx(), "$C_AcctSchema_ID");
            p_C_AcctSchema_ID = new BigDecimal(esquema);

            try{
                ElementoCostoRecursos = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
                ElementoCostoMateriales = MMPCCostElement.getCostElementByName("Costo Real de Materiales").getMPC_Cost_Element_ID();
                ElementoCostoEstandarRecursos = MCostElement.getCostElementByName("Costo Estandar de Recursos").getM_CostElement_ID();
                ElementoCostoIndirecto = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
                ElementoCostoEstandarMateriales = MCostElement.getCostElementByName("Costo Estandar de Materiales").getM_CostElement_ID();
                ElementoCostoEstandarIndirecto = MCostElement.getCostElementByName("Costo Estandar Indirecto").getM_CostElement_ID();
            }
            catch(Exception e){
                ElementoCostoRecursos = 0;
                ElementoCostoMateriales = 0;
                ElementoCostoEstandarRecursos = 0;
            }
		}	//	prepare

		private void resetParametros(){
            /**
             * Parametros especificos del Costo de Materiales
             */
			//Producto
			M_Product_ID = 0;
			//Cantidad Teorica
			CantidadTeorica = BigDecimal.ZERO;
			//Cantidad Real
			CantidadReal = BigDecimal.ZERO;
			//Variacion de la Cantidad
			VCantidad = BigDecimal.ZERO;
			//Variacion porcentual de la Cantidad
			VPCantidad = BigDecimal.ZERO;
			//Precio Teorico
			PrecioTeorico = BigDecimal.ZERO;
			//Precio Real
			PrecioReal = BigDecimal.ZERO;
			//Variacion del Precio
			VPrecio = BigDecimal.ZERO;
			//Variacion procentual del Precio
			VPPrecio = BigDecimal.ZERO;
			//Valoracion de la variacion de la Cantidad
			ValVCantidad = BigDecimal.ZERO;
			//Variacion del costo final
			VCostoFinal = BigDecimal.ZERO;
            /**
             * Parametros especificos del Costo de Recursos
             */
		}

		private void vaciarTablas() throws Exception{
			PreparedStatement ps = DB.prepareStatement("delete from T_INTEGRACIONCOSTMAT", null);
			ps.executeQuery();
            ps.close();
            ps = DB.prepareStatement("delete from T_INTEGRACIONCOSTREC", null);
			ps.executeQuery();
            ps.close();
            ps = DB.prepareStatement("delete from T_INTEGRACIONCOSTIND", null);
			ps.executeQuery();
            ps.close();
            ps = DB.prepareStatement("delete from T_COSTOSTOTALES", null);
			ps.executeQuery();
            ps.close();
		}

		private void insertarFilaEnTablaRecursos()throws Exception{
            setPrecision(4,BigDecimal.ROUND_HALF_UP);
            PreparedStatement ps = DB.prepareStatement("insert into T_INTEGRACIONCOSTREC values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", null);
            ps.setInt(1, AD_Client_ID);
			ps.setInt(2, AD_Org_ID);
			ps.setString(3, "Y");
			ps.setInt(4, M_Product_ID);
			ps.setFloat(5, CantidadTeorica.floatValue());
			ps.setFloat(6, CantidadReal.floatValue());
			ps.setFloat(7, VCantidad.floatValue());
			ps.setFloat(8, VPCantidad.floatValue());
			ps.setFloat(9, PrecioTeorico.floatValue());
			ps.setFloat(10, ValVCantidad.floatValue());
			ps.setBigDecimal(11, p_MPC_Order_ID);
            ps.setBigDecimal(12, p_C_AcctSchema_ID);
            ps.setBigDecimal(13, null);
            ps.setBigDecimal(14, null);
            ps.setBigDecimal(15, null);
            ps.setBigDecimal(16, VCostoFinal);
            ps.executeUpdate();
            ps.close();
        }

        /**
         * Metodo que setea la precision de los importes y cantidades
         * @param scale
         */
        private void setPrecision(int scale, int roundingMode){
			//Cantidad Teorica
			CantidadTeorica = CantidadTeorica.setScale(scale,roundingMode);
			//Cantidad Real
			CantidadReal = CantidadReal.setScale(scale,roundingMode);
			//Variacion de la Cantidad
			VCantidad = VCantidad.setScale(scale,roundingMode);
			//Variacion porcentual de la Cantidad
			VPCantidad = VPCantidad.setScale(scale,roundingMode);
			//Precio Teorico
			PrecioTeorico = PrecioTeorico.setScale(scale,roundingMode);
			//Precio Real
			PrecioReal = PrecioReal.setScale(scale,roundingMode);
			//Variacion del Precio
			VPrecio = VPrecio.setScale(scale,roundingMode);
			//Variacion procentual del Precio
			VPPrecio = VPPrecio.setScale(scale,roundingMode);
			//Valoracion de la variacion de la Cantidad
			ValVCantidad = ValVCantidad.setScale(scale,roundingMode);
			//Variacion del costo final
			VCostoFinal = VCostoFinal.setScale(scale,roundingMode);
        }

        private void insertarFilaEnTablaMateriales(int MPC_Order_ID) throws Exception{
			setPrecision(precision,BigDecimal.ROUND_DOWN);
            PreparedStatement ps = DB.prepareStatement("insert into T_INTEGRACIONCOSTMAT values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", null);
			ps.setInt(1, AD_Client_ID);
			ps.setInt(2, AD_Org_ID);
			ps.setString(3, "Y");
			ps.setInt(4, M_Product_ID);
			ps.setFloat(5, CantidadTeorica.floatValue());
			ps.setFloat(6, CantidadReal.floatValue());
			ps.setFloat(7, VCantidad.floatValue());
			ps.setFloat(8, VPCantidad.floatValue());
			ps.setFloat(9, PrecioTeorico.floatValue());
			ps.setFloat(10, PrecioReal.floatValue());
			ps.setFloat(11, VPrecio.floatValue());
			ps.setFloat(12, VPPrecio.floatValue());
			ps.setFloat(13, ValVCantidad.floatValue());
			ps.setFloat(14, VCostoFinal.floatValue());
			ps.setInt(15, MPC_Order_ID);
            ps.setBigDecimal(16, p_C_AcctSchema_ID);
			ps.executeUpdate();
            ps.close();
		}

        private void insertarFilaEnTablaIndirecto(BigDecimal qtyDelivered) throws Exception{
            setPrecision(4,BigDecimal.ROUND_DOWN);
            PreparedStatement ps = DB.prepareStatement("insert into T_INTEGRACIONCOSTIND values(?,?,?,?,?,?,?,?,?,?,?,?,?)", null);
			ps.setInt(1, AD_Client_ID);
			ps.setInt(2, AD_Org_ID);
			ps.setString(3, "Y");
			ps.setFloat(4, PrecioTeorico.floatValue());
			
			ps.setFloat(6, VPrecio.floatValue());
			ps.setFloat(7, VPPrecio.floatValue());
			ps.setBigDecimal(8, p_MPC_Order_ID);
            ps.setBigDecimal(9, p_C_AcctSchema_ID);

            BigDecimal totalTeorico = PrecioTeorico.multiply(qtyDelivered).setScale(2,BigDecimal.ROUND_DOWN);
            
            BigDecimal VprecioTotal = BigDecimal.ZERO;
            BigDecimal VprecioPorcentualTotal = BigDecimal.ZERO;

            if (PrecioTeorico.equals(PrecioReal))
                VprecioTotal = BigDecimal.ZERO;
            else
                VprecioTotal = RealTotal.subtract(totalTeorico);

            ps.setFloat(5, PrecioReal.floatValue());

            //VARIACION PORCENTUAL TOTAL DEL INDIRECTO
            float ct = totalTeorico.floatValue();
            float cr = RealTotal.floatValue();
            if (ct==cr)
                VprecioPorcentualTotal = BigDecimal.ZERO;
            else if (ct==0||cr==0){
                VprecioPorcentualTotal = new BigDecimal(100);
                VprecioPorcentualTotal = ct == 0? VprecioPorcentualTotal : VprecioPorcentualTotal.negate();
            }
            else
                VprecioPorcentualTotal = VprecioTotal.multiply(new BigDecimal(100)).divide(totalTeorico,precision,BigDecimal.ROUND_HALF_UP);

            ps.setFloat(10, totalTeorico.floatValue());
			ps.setFloat(11, RealTotal.floatValue());
			ps.setFloat(12, VprecioTotal.floatValue());
			ps.setFloat(13, VprecioPorcentualTotal.floatValue());
			ps.executeUpdate();
            ps.close();
        }

        private void cargarTablaRecursos() throws Exception{
            System.out.println("***Cargando Costos de Recursos...***");
            if (ElementoCostoEstandarRecursos==0)
                JOptionPane.showMessageDialog(null, "Falta el Elemento de Costo: Costo Estandar de Recursos", "Elemento de costo faltante", JOptionPane.ERROR_MESSAGE);
            else{
                //Obtengo los nodos de ordenes de manufactura
                MMPCOrderNode[] nodes=null;
                if (p_MPC_Order_ID.equals(BigDecimal.ZERO))
                    nodes = getNodosOrdenesCerradas(0);
                else{

                    nodes = getNodosOrdenesCerradas(p_MPC_Order_ID.intValue());
                }

                for (int i=0;i<nodes.length;i++){
                    resetParametros();

                    //Seteo la orden de manufactura
                    p_MPC_Order_ID = new BigDecimal(nodes[i].getMPC_Order_ID());
                    //Obtengo la OM asociada
                    MMPCOrder order = new MMPCOrder(getCtx(),p_MPC_Order_ID.intValue(),null);
                    System.out.println("Orden de Manufactura: "+order.toString());
                    System.out.println("Nodo: "+nodes[i].toString());
                    MWFNode nodoPlan = new MWFNode(getCtx(), nodes[i].getAD_WF_Node_ID(), null);
                    MResource recurso = new MResource(getCtx(),nodoPlan.getS_Resource_ID(),null);
                    /** BISion - 25/03/2009 - Santiago Ibañez
                     * Modificacion realizada para tomar bien las cantidades
                     */

                    //Calculo la Cantidad Teorica (en funcion del Workflow)
                    MMPCOrderWorkflow wf = new MMPCOrderWorkflow(getCtx(), nodes[i].getMPC_Order_Workflow_ID(), null);
                    //Obtengo la duracion en segundos del workflow
                    BigDecimal duracionWF = new BigDecimal(wf.getDurationBaseSec());
                    //CantidadTeorica = duracionWF.multiply(new BigDecimal(nodes[i].getDurationRequiered()));
                    
                    //Duracion Teorica = Duracion nodo * Cantidad Rendida
                    BigDecimal durTeorica = new BigDecimal(nodes[i].getDuration());

                    

                    durTeorica = durTeorica.multiply(order.getQtyDelivered());
                    
                    //08-04-2011 Camarzana Mariano descomentada la siguiente linea para que asigne la CantidadTeorica
                    CantidadTeorica = duracionWF.multiply(durTeorica);
                    
                    setCantidadTeoricaRecursos(order.getM_Product_ID(), nodes[i], order.getQtyDelivered());
                    if (CantidadTeorica.floatValue()==0)
                        CantidadTeorica = BigDecimal.ZERO;
                    //Obtengo el tipo de recurso
                    MResourceType tipoRecurso= new MResourceType(getCtx(), recurso.getS_ResourceType_ID(), null);
                    
                    /**
                     * Calculo la Cantidad Real (en funcion del Registro de Actividad)
                     */
                    BigDecimal duracionNodo = BigDecimal.ZERO;
                    //Obtengo el control de actividad asociado al nodo
                    MMPCCostCollector cc = getControlActividad(nodes[i].getMPC_Order_Node_ID());
                    if (cc!=null){
                        //CantidadReal = cc.getDurationReal().multiply(tipoRecurso.getSeconds(cc.getDurationUnit()));
                        //Paso a horas el registro de actividad
                        duracionNodo = new BigDecimal(cc.getDurationBaseSec());
                        //paso la duracion a horas
                        duracionNodo = duracionNodo.divide(new BigDecimal(3600),precision,BigDecimal.ROUND_HALF_UP);
                        //Duracion del Flujo * Duracion Registro Actividad
                        CantidadReal = duracionNodo.multiply(cc.getDurationReal());
                        if (CantidadReal.floatValue()==0)
                             CantidadReal = BigDecimal.ZERO;
                           
                    }//fin cantidad real

                    /**
                     * Calculo el Precio del Recurso
                     */

                    if (recurso.getProduct()!=null){
                        //CantidadTeorica = tipoRecurso.getSeconds().multiply(new BigDecimal(nodes[i].getDuration()).multiply(order.getQtyDelivered()));
                        //if (!CantidadTeorica.equals(BigDecimal.ZERO))
                            //CantidadTeorica = CantidadTeorica.divide(new BigDecimal(3600),8,BigDecimal.ROUND_HALF_UP);

                        //Obtengo el producto dados el recurso y tipo obtenidos
                        M_Product_ID = recurso.getProduct().getM_Product_ID();

                        //Obtengo el costo por unidad de tiempo del recurso
                        BigDecimal costo = BigDecimal.ZERO;
                        MCost cos[] = MCost.getElements(M_Product_ID, p_C_AcctSchema_ID.intValue());
                        for (int c=0;c<cos.length;c++){
                            if (cos[c].getM_CostElement_ID()==ElementoCostoEstandarRecursos){
                                costo = cos[c].getCurrentCostPrice();
                                break;
                            }
                        }
                        //El precio teorico es el precio por unidad del recurso (lo normalizo a horas)
                        PrecioTeorico = costo.multiply(new BigDecimal(3600)).divide(tipoRecurso.getSeconds(),precision,BigDecimal.ROUND_HALF_UP);
                        
                        //PrecioTeorico = CantidadReal.multiply(costo).divide(tipoRecurso.getSeconds());
                    }
                    setVariacionCantidad();
                    setVariacionPorcentualCantidad();
                    
                    //07-04-2011 Camarzana Mariano comentado por cambio en el calculo
                    //ValVCantidad = VCantidad.multiply(PrecioTeorico);
                    
                    ValVCantidad = CantidadReal.subtract(CantidadTeorica).multiply(PrecioTeorico);
                    VCostoFinal = PrecioReal.subtract(PrecioTeorico).multiply(CantidadReal);
                    insertarFilaEnTablaRecursos();
                }
            }
        }

   /** BISion - 17/03/2009 - Santiago Ibañez
    * Metodo que retorna para un nodo dado el primer registro de control de
    * actividad asociado que haya.
    * @param MPC_Order_Node_ID
    * @return
    */
   private MMPCCostCollector getControlActividad(int MPC_Order_Node_ID){
       int[] cc = MMPCCostCollector.getAllIDs("MPC_Cost_Collector", "MPC_Order_Node_ID = "+MPC_Order_Node_ID+" and mpc_order_id = "+p_MPC_Order_ID, null);
       MMPCCostCollector col = null;
       for(int i=0;i<cc.length;i++){
          col = new MMPCCostCollector(getCtx(), cc[i], null);
          MDocType doc = MDocType.get(getCtx(),col.getC_DocType_ID());
          if (doc.getDocBaseType().equals(MDocType.DOCBASETYPE_ManufacturingOperationActivity))
            return col;
       }
       return col;
    }

      private void cargarTablaMateriales(boolean calculaReales) throws Exception{
			System.out.println("****Carga de Costos de Materiales****");
            //Obtengo las formulas de ordenes de manufactura
			MMPCOrderBOMLine[] bomlines=null;
			if (p_MPC_Order_ID.equals(BigDecimal.ZERO))
				bomlines = getLineasOrdenesCerradas();
			else{
				MMPCOrder order = new MMPCOrder(getCtx(),p_MPC_Order_ID.intValue(),null);
				bomlines = order.getLines(true, null);
			}
			for (int i=0;i<bomlines.length;i++){
				resetParametros();
				//Seteo el producto
				M_Product_ID = bomlines[i].getM_Product_ID();

				//Obtengo la Orden de Manufactura asociada
				MMPCOrder order = new MMPCOrder(getCtx(),bomlines[i].getMPC_Order_ID(),null);
                System.out.println("Orden de Manufactura: "+order.toString());
				//Cantidad Teorica = Cantidad Entregada de la orden * Cantidad de la formula
				
                /*
                 *23-03-2011 Camarzana Mariano
                 *Modificado ya que no tenia en cuenta si la cantidad estaba expresada en porcentaje 
                 */
                BigDecimal cantidad = BigDecimal.ZERO;
                if (bomlines[i].isQtyPercentage())
                	cantidad = bomlines[i].getQtyBatch().divide(new BigDecimal(100));
                else
                	cantidad = bomlines[i].getQtyBOM();
                
                if (!order.getQtyDelivered().equals(BigDecimal.ZERO)&&! cantidad.equals(BigDecimal.ZERO))
                    	CantidadTeorica = order.getQtyDelivered().multiply(cantidad);
				
				//Cantidad Real = Cantidad entregada de la formula
				CantidadReal = bomlines[i].getQtyDelivered();

                setVariacionCantidad();
                setVariacionPorcentualCantidad();
				
                //COSTO TEORICO (se consideran productos elaborados y de terceros)
                
                //Obtengo el producto involucrado para comprobar si se trata de un insumo o material elaborado
                MProduct producto = new MProduct(getCtx(), M_Product_ID, null);
                if (!producto.isPurchased()){
                    PrecioTeorico = getCostoTeorico(M_Product_ID);
                }
                else{
                    //Precio Teorico = Precio de la linea de la factura / Cantidad linea de factura * Cantidad entregada formula;
                    MInvoiceLine invoiceLine = getUltimaLineaFacturaCompra(M_Product_ID);
                    //Si no existen facturas entonces el precio es 0
                    /**
                     * BISion - 17/02/2010 - Santiago Ibañez
                     * Modificacion realizada para tomar en cuenta tambien la ultima OC en caso de no haber facturas
                     */
                    if (invoiceLine == null){
                        PrecioTeorico = getPrecioUltimaOC(M_Product_ID);
                        //07-04-2011 Camarzana Mariano comentado por cambio en el calculo
                        //ValVCantidad = PrecioTeorico;
                    }
                    else if (!invoiceLine.getQtyEntered().equals(BigDecimal.ZERO)&&!invoiceLine.getLineNetAmt().equals(BigDecimal.ZERO)){
                        //Precio
                        //PrecioTeorico = invoiceLine.getLineNetAmt().divide(invoiceLine.getQtyEntered(),precision,BigDecimal.ROUND_HALF_UP);
                    	//28-02-2011 Camarzana Mariano
                    	//Modificado para que tenga en cuenta la cotizacion -> multiply(inv.getCotizacion()
                    	MInvoice inv = new MInvoice(Env.getCtx(),invoiceLine.getC_Invoice_ID(),null);
                    	PrecioTeorico = invoiceLine.getPriceEntered().multiply(inv.getCotizacion());
                    	//07-04-2011 Camarzana Mariano comentado por cambio en el calculo
                    	//ValVCantidad = PrecioTeorico;
                    }
                }
				//Precio Real =
				//Se modifico el precio real para que se considere el unitario
                //PrecioReal = getPrecioMateriaPrima(M_Product_ID,bomlines[i].getM_AttributeSetInstance_ID()).multiply(bomlines[i].getQtyDelivered());

                /**
                 * Calculo el precio real solo en el proceso de Detalle de costos integrados
                 * porque en el de totales directamente lo obtengo de los registros de la OM
                 */
                if (calculaReales){
                    
                	//obtengo el id correspondiente al elemento de costo real de materiales
                    int costoMateriales_ID = MMPCCostElement.getCostElementByName("Costo Real de Materiales").getMPC_Cost_Element_ID();
                    int C_AcctSchema_ID = Env.getContextAsInt(getCtx(),"$C_AcctSchema_ID");
                    PrecioReal = getMaterialsCost(order.getMPC_Order_ID(),M_Product_ID, costoMateriales_ID, C_AcctSchema_ID,CantidadReal);
                    
                    if (CantidadReal.intValue() != 0)
                    	PrecioReal = PrecioReal.divide(CantidadReal,4,BigDecimal.ROUND_HALF_UP);
                	
                    /*
                     * 24-02-2011 Camarzana Mariano
                     * Modificado para que tenga en cuenta las partidas a la hora de calcular los costos
                     *  
                      //Producto Comprado
                     if (producto.isPurchased())
                       PrecioReal = getPrecioMateriaPrima(M_Product_ID,bomlines[i].getM_AttributeSetInstance_ID());
                     else
                       PrecioReal = getCostoReal(M_Product_ID,bomlines[i].getM_AttributeSetInstance_ID());
                      */
                }
				setVariacionPrecio();
                setVariacionPorcentualPrecio();

                //Valoracion de la variacion de la cantidad = Precio ultima compra * variacion cantidad
                
                //07-04-2011 Camarzana Mariano comentado por cambio en el calculo VVC = CantidadReal - CantidadTeorica * PrecioTeorico 
                //ValVCantidad = ValVCantidad.multiply(VCantidad);
                
                ValVCantidad = CantidadReal.subtract(CantidadTeorica).multiply(PrecioTeorico);

				//Variacion del Costo final = Valoracion de la variacion de la cantidad + variacion del precio
                //07-04-2011 Camarzana Mariano comentado por cambio en el calculo
                //VCostoFinal = ValVCantidad.add(VPrecio);
                VCostoFinal = PrecioReal.subtract(PrecioTeorico).multiply(CantidadReal);

                /** BISion - 26/03/2009 - Santiago Ibañez
                 * Modificacion Realizada para tomar el precio unitario de los materiales utilizados
                 */
                //fin modificacion BISion
                insertarFilaEnTablaMateriales(bomlines[i].getMPC_Order_ID());
			}
		}
      
  	/**
  	 * 24-02-2011 Camarzana Mariano
     * Metodo realizado para calcular el costo real de materiales teniendo en cuenta que un material
     * puede haber sido provisto desde varias partidas, de modo tal que se debe calcular el porcentaje de cada partida
     * Costo Materia Prima = Porcentaje de materia prima entregada * Precio de la materia Prima
     * 
  	 */
      private BigDecimal calcularPorcentajePartida(BigDecimal importe, BigDecimal cantidadTotal,BigDecimal cantidadEntregada){
    	  BigDecimal porcentaje = BigDecimal.ZERO;
    	  if (cantidadTotal.intValue() == 0)
    		  return BigDecimal.ZERO;
    	  porcentaje = cantidadEntregada.multiply(new BigDecimal(100)).divide(cantidadTotal,4,BigDecimal.ROUND_HALF_UP);
    	  porcentaje = porcentaje.multiply(importe);
  		return porcentaje.divide(new BigDecimal(100),4,BigDecimal.ROUND_HALF_UP);
      	
      }  
      
      /**
       * 24-02-2011 Camarzana Mariano
       * Metodo realizado para calcular el costo real de materiales de una materia prima dada
       * Costo Materia Prima = Precio * Cantidad del Movimiento
       * Similar al metodo getMaterialsCost de MMPCOrder
       *
       */
      
      public BigDecimal getMaterialsCost(int MPC_Order_ID,int productId,int MPC_Cost_Element_ID,int C_AcctSchema_ID,BigDecimal CantidadReal){
          System.out.println("Calculando costo de materiales...");
          BigDecimal cost = BigDecimal.ZERO;
          MMPCOrder order = new MMPCOrder(Env.getCtx(),MPC_Order_ID,null);
          MMPCCostCollector[] ra = order.getMMPCCostCollector(MPC_Order_ID);
          for (int i=0;i<ra.length;i++){
              //chequeo que el tipo de documento sea surtimiento o recepcion, usando el doctype asociado
              MDocType doc = MDocType.get(getCtx(),ra[i].getC_DocType_ID());
              if (doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderIssue)
                      ||doc.getDocBaseType().equalsIgnoreCase(MDocType.DOCBASETYPE_ManufacturingOrderReceipt)){
                  //Si el producto de la línea NO es el producto terminado
                  if (ra[i].getM_Product_ID()!=order.getM_Product_ID() && productId == ra[i].getM_Product_ID())
                      //si es materia prima...
                      if (order.esMateriaPrima(ra[i].getM_Product_ID())) {
                    	  
	                    	  
	                          //obtengo el precio de la materia prima desde la OC
	                          System.out.println(ra[i].getM_Product_ID()+" es Materia Prima");
	                          BigDecimal price = order.getPrecioMateriaPrima(ra[i].getM_Product_ID(),ra[i].getM_AttributeSetInstance_ID());
	
	                          //obtengo la cantidad del movimiento desde el reporte de actividades
	                          BigDecimal qty = ra[i].getMovementQty();
	                          
	                          //acumulo el costo
	                          cost = cost.add(price.multiply(qty));
	                          
	                          //price = calcularPorcentajePartida(price, CantidadReal, qty);
	                          
	                          cost = cost.add(price);
                    	  }
                         
                    
                      //si no es materia prima
                      else{
                          System.out.println(ra[i].getM_Product_ID()+" NO es Materia Prima");
                          //obtengo la orden de manufactura para el producto de la línea y su partida
                          MMPCOrder orderProducto = order.getMMPCOrderByProduct(ra[i].getM_Product_ID(), ra[i].getM_AttributeSetInstance_ID());
                          if (orderProducto!=null){
                              System.out.println("Orden de Manufactura: "+orderProducto.getDocumentNo());
                          BigDecimal costo = BigDecimal.ZERO;
                          //si no tiene registros de costo real de material asociado
                          if (!order.tieneRegistroCosto(MPC_Cost_Element_ID,orderProducto.getMPC_Order_ID())){
                              //obtengo recursivamente el costo real de materiales de la OM
                              //costo = order.getMaterialsCost(order.getMPC_Order_ID(),MPC_Cost_Element_ID,C_AcctSchema_ID);
                              //creo el registro de costo real de material para la OM order
                              //order.generateMPC_Cost_Order(generarDetalleCosto);
                              costo = order.getCostoOM(orderProducto.getMPC_Order_ID(),MPC_Cost_Element_ID);
                              //le agrego ademas el costo real de recursos
                              int costoRecursos_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
                              costo = costo.add(order.getCostoOM(order.getMPC_Order_ID(),costoRecursos_ID));
                              int costoIndirecto_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
                              //le agrego el costo indirecto en caso de que lo tenga
                              costo = costo.add(order.getCostoOM(orderProducto.getMPC_Order_ID(),costoIndirecto_ID));
                          }
                          //si tiene registro de costo real de materiales asociado
                          else{
                              costo = order.getCostoOM(orderProducto.getMPC_Order_ID(),MPC_Cost_Element_ID);
                              //le agrego además el costo real de recursos
                              int costoRecursos_ID = MMPCCostElement.getCostElementByName("Costo Real de Recursos").getMPC_Cost_Element_ID();
                              costo = costo.add(order.getCostoOM(orderProducto.getMPC_Order_ID(),costoRecursos_ID));
                              int costoIndirecto_ID = MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID();
                              //le agrego en caso de que tenga el costo real indirecto
                              costo = costo.add(order.getCostoOM(orderProducto.getMPC_Order_ID(),costoIndirecto_ID));
                              System.out.println("Costo Real: "+costo);
                          }
                          //CALCULO EL COSTO UNITARIO
                          BigDecimal unitario = BigDecimal.ZERO;
                          //Si la cantidad entregada es diferente a 0 calculo el costo unitario
                          if (!orderProducto.getQtyDelivered().equals(BigDecimal.ZERO)){
                              //obtengo la cantidad real que se utilizo de la OM de un producto elaborado (Ej PL..,GL... etc)
                              BigDecimal qty = ra[i].getMovementQty();
                              System.out.println("Cantidad de Movimiento: "+qty);
                              //para guardar el costo unitario
                              unitario = costo.divide(orderProducto.getQtyDelivered(),12,BigDecimal.ROUND_HALF_UP);
                              System.out.println("Costo Unitario: $"+unitario);
                              //Si la cantidad entregada de la OM es diferente a la cantidad real que se utilizo de la OM
                              if (orderProducto.getQtyDelivered()!=qty){
                                  //al costo de la OM lo divido por la cantidad de esa OM (costo por unidad de OM)
                                  costo = costo.divide(orderProducto.getQtyDelivered(),12,BigDecimal.ROUND_HALF_UP);
                                  //al costo le multiplico la cantidad real que se utilizo de la OM
                                  costo = costo.multiply(qty);
                              }
                              System.out.println("Costo Total: $"+costo);
                          //Considero costo = 0
                          }
                          else{
                              System.out.println("Cantidad Entregada de la Orden en cero");
                              costo = BigDecimal.ZERO;
                          }

                          //acumulo el costo
                          cost = cost.add (costo);
                      }
                          else
                        	  JOptionPane.showMessageDialog(null,"No se encontro la OM para el producto elaborado: " + ra[i].getM_Product_ID(),"Mensage",JOptionPane.INFORMATION_MESSAGE);
                    }

              }
          }
          System.out.println("Costo Real de Materiales: $"+cost);
          return cost;
      }

      /** BISion - 30/03/2009 - Santiago Ibañez
       * Metodo que retorna la suma de cada uno de los tres costos teoricos:
       * Materiales + Recursos + Indirecto para un producto dado
       * @param M_Product_ID
       * @return
       */
      private BigDecimal getCostoTeorico(int M_Product_ID){
            BigDecimal costo = BigDecimal.ZERO;
            MCost[] costos = MCost.getElements(M_Product_ID, p_C_AcctSchema_ID.intValue());
            for (int i=0;i<costos.length;i++)
                if (costos[i].getM_CostElement_ID()==ElementoCostoEstandarIndirecto
                    || costos[i].getM_CostElement_ID()==ElementoCostoEstandarRecursos
                    || costos[i].getM_CostElement_ID()==ElementoCostoEstandarMateriales)
                    costo = costo.add(costos[i].getCurrentCostPrice());
            return costo;
      }

      /** BISion - 30/03/2009 - Santiago Ibañez
       * Metodo que dado un producto y partida retorna la suma de los costos reales
       * (Indirecto, Materiales y Recursos) a traves de los registros de costos de la OM
       * @param M_Product_ID
       * @param M_AttributeSetInstance_ID
       * @return
       */
      private BigDecimal getCostoReal(int M_Product_ID, int M_AttributeSetInstance_ID){
          BigDecimal costo = BigDecimal.ZERO;

          //Obtengo la OM para el producto y partida
          int MPC_Order_ID = 0;
          PreparedStatement ps = DB.prepareStatement("SELECT MPC_Order_ID FROM MPC_Order WHERE M_Product_ID = "+M_Product_ID+" and M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID, null);
          try{
              ResultSet rs = ps.executeQuery();
              if (rs.next())
                  MPC_Order_ID = rs.getInt(1);
          }
          catch(Exception e){
                System.out.println("No se pudo obtener la Orden de manufactura para el producto: "+M_Product_ID+" y la partida: "+M_AttributeSetInstance_ID);
                System.out.println(e.getMessage());
          }

          //Obtengo los registros de costos de la OM obtenida
          if (MPC_Order_ID!=0){
              String sql = "SELECT nvl(sum(costcumqty),0) FROM MPC_Order_Cost " +
                           " where (MPC_Cost_Element_ID = "+ElementoCostoRecursos+
                           " or MPC_Cost_Element_ID = "+ElementoCostoMateriales+
                           " or MPC_Cost_Element_ID = "+ElementoCostoIndirecto+
                           " ) and C_AcctSchema_ID = "+p_C_AcctSchema_ID.intValue()+
                           " and MPC_Order_ID = "+MPC_Order_ID;
              ps = DB.prepareStatement(sql, null);
              try{
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                    costo = rs.getBigDecimal(1);

                	/*
	                 * 21-10-2010 Camarzana Mariano 
	                 * Faltaba cerrar los cursores - ORA-01000:	maximum open cursors exceeded
	                 */
                	rs.close();
                	ps.close();
              }
              catch(Exception e){
                System.out.println("No se pudo obtener el costo para ");

              }
          }
          return costo;
      }

        protected String doIt() throws Exception{
            vaciarTablas();
            X_AD_Process process = new X_AD_Process(getCtx(), getProcessInfo().getAD_Process_ID(), null);
            boolean calculaReales = process.getName().equals("Detalle Costos Integrados") ? true : true ;
            if ((to==null || from == null)&&process.getName().equals("Costos Integrados")){
                JOptionPane.showMessageDialog(null, "Falta especificar una fecha", "Fecha faltante", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Error: Fecha faltante");
            }
            cargarTablaMateriales(calculaReales);
            try{
                cargarTablaRecursos();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            

            cargarTablaIndirecto(process.getName());

            

            /**
             * Chequeo desde donde se llamo la clase
             * Esta puede ser llamada desde:
             *  1- Reporte de Costos integrados detallado
             *  2- Reporte de Costos integrados
             */
            
            if (process.getName().equals("Detalle Costos Integrados"))
                UtilProcess.initViewer("Formato Integracion de Costos Detallado",getAD_PInstance_ID(),getProcessInfo());
            else if (process.getName().equals("Costos Integrados")){
                cargarTablaTotales();
                UtilProcess.initViewer("Formato Integracion de Costos",getAD_PInstance_ID(),getProcessInfo());
            }
			return "ok";
		}

        private void setVariacionCantidad(){
            CantidadTeorica = CantidadTeorica.setScale(precision, BigDecimal.ROUND_DOWN);
            CantidadReal = CantidadReal.setScale(precision, BigDecimal.ROUND_DOWN);
            if (CantidadReal.equals(CantidadTeorica))
                VCantidad = BigDecimal.ZERO;
            else
                //Variacion de la Cantidad
                VCantidad = CantidadReal.subtract(CantidadTeorica).setScale(precision,BigDecimal.ROUND_DOWN);
            //La Variacion siempre es positiva
            //VCantidad = VCantidad.signum()==1 ? VCantidad : VCantidad.negate();
        }

        private void setVariacionPorcentualCantidad(){
            try{
                if (CantidadTeorica.equals(CantidadReal))
                    VPCantidad = BigDecimal.ZERO;
                else{
                    //Variacion porcentual de la cantidad = Variacion de la cantidad * 100 / CantidadTeorica
                    float ct = CantidadTeorica.floatValue();
                    float cr = CantidadReal.floatValue();
                    if (ct==0&&cr==0)
                        VPCantidad = BigDecimal.ZERO;
                    else if (ct==0||cr==0){
                        VPCantidad = new BigDecimal(100);
                        VPCantidad = ct == 0? VPCantidad : VPCantidad.negate();
                    }
                    else
                        VPCantidad = VCantidad.multiply(new BigDecimal(100)).divide(CantidadTeorica,precision,BigDecimal.ROUND_DOWN);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        /** BISion - 18/03/2009 - Santiago IbaÃ±ez
         * Metodo que es comun a los tres tipos de costos y que setea la variacion
         * del precio.
         */
        private void setVariacionPrecio(){
            PrecioTeorico = PrecioTeorico.setScale(precision,BigDecimal.ROUND_DOWN);
            PrecioReal = PrecioReal.setScale(precision,BigDecimal.ROUND_DOWN);
            RealTotal = RealTotal.setScale(precision, BigDecimal.ROUND_DOWN);
            if (PrecioTeorico.equals(PrecioReal))
                VPrecio = BigDecimal.ZERO;
            else
                VPrecio = PrecioReal.subtract(PrecioTeorico).setScale(precision,BigDecimal.ROUND_DOWN);
            //La Variacion del precio siempre es positiva
            //VPrecio = VPrecio.signum()==1 ? VPrecio : VPrecio.negate();
        }

        private void setVariacionPorcentualPrecio(){
            //Si son iguales los precios no hay variacion
            if (PrecioTeorico.equals(PrecioReal))
                VPPrecio = BigDecimal.ZERO;
            else{
                //Variacion porcentual del precio = Variacion del precio * 100 / PrecioTeorica
                float pt = PrecioTeorico.floatValue();
                float pr = PrecioReal.floatValue();
                if (pt==0&&pr==0)
                    VPPrecio = BigDecimal.ZERO;
                else if (pr==0||pt==0)
                    VPPrecio = new BigDecimal(100);
                else
                    VPPrecio = VPrecio.multiply(new BigDecimal(100)).divide(PrecioTeorico,precision,BigDecimal.ROUND_HALF_UP);
            }
        }

        /** BISion - 16/03/2009 - Santiago IbaÃ±ez
         * Metodo que retorna todos los nodos de workflows asociados a ordenes
         * de manufactura en estado cerrado
         * @return
         * @throws java.lang.Exception
         */
        private MMPCOrderNode[] getNodosOrdenesCerradas(int MPC_Order_ID) throws Exception{
            ArrayList<MMPCOrderNode> nodes = new ArrayList<MMPCOrderNode>();
            String sql;
            PreparedStatement ps;
            if (MPC_Order_ID!=0){
                sql = "SELECT * FROM MPC_Order_Node where MPC_Order_ID = "+MPC_Order_ID;
                //Selecciono los nodos de las ordenes de manufacturas completas
                ps = DB.prepareStatement(sql,null);
            }
            else{
                if (p_M_Product_Category_ID.equals(BigDecimal.ZERO)){
                    sql = "SELECT MPC_Order_Node_ID from MPC_Order_Node node join MPC_Order o on (node.mpc_order_id = o.mpc_order_id) where o.docstatus = '"+DocAction.STATUS_Closed+"'";
                    ps = DB.prepareStatement(sql,null);
                }
                else{
                    //16-02-2011 Camarzana Mariano
                	//Modificado para que tome la fecha de comienzo de la orden en vez de la fecha de la misma
                	//o.dateordered -> o.datestart
                	sql = "SELECT MPC_Order_Node_ID from MPC_Order_Node node " +
                          "inner join MPC_Order o on (node.mpc_order_id = o.mpc_order_id) " +
                          "inner join M_Product p on (o.M_Product_ID = p.M_Product_ID) " +
                          "where o.docstatus = '"+DocAction.STATUS_Closed+"' AND " +
                          "p.M_Product_Category_ID = "+p_M_Product_Category_ID+
                          " and o.datestart BETWEEN ? and ?";

                	ps = DB.prepareStatement(sql,null);
                    ps.setTimestamp(1, from);
                    ps.setTimestamp(2, to);
                }
            }
            
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                MMPCOrderNode nodo = new MMPCOrderNode(getCtx(),rs.getInt(1),null);
                nodes.add(nodo);
            }
            MMPCOrderNode[] nodos = new MMPCOrderNode[nodes.size()];
            rs.close();
            ps.close();
            return nodes.toArray(nodos);
        }

        //Metodo que retorna todas las lineas de formulas para ordenes de manufactura cerradas
		private MMPCOrderBOMLine[] getLineasOrdenesCerradas() throws Exception{
            //16-02-2011 Camarzana Mariano
        	//Modificado para que tome la fecha de comienzo de la orden en vez de la fecha de la misma
        	//o.dateordered -> o.datestart
			String sql = "SELECT bom.mpc_order_bomline_id FROM mpc_order_bomline bom "+
                         " join mpc_order o on (o.mpc_order_id = bom.mpc_order_id)" +
                         " join m_product p on (o.m_product_id = p.m_product_id)" +
                         " where o.docstatus = 'CL'"+
                         " and p.m_product_category_id = "+p_M_Product_Category_ID+
                         " and o.datestart between ? and ?";
            PreparedStatement ps = DB.prepareStatement(sql, null);
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
            ResultSet rs = ps.executeQuery();
            ArrayList<MMPCOrderBOMLine> lines = new ArrayList<MMPCOrderBOMLine>();
            while (rs.next()){
                MMPCOrderBOMLine line = new MMPCOrderBOMLine(getCtx(), rs.getInt(1), null);
                lines.add(line);
            }

            /*
             * 21-10-2010 Camarzana Mariano 
             * Faltaba cerrar los cursores - ORA-01000:	maximum open cursors exceeded
             */
            
            ps.close();
            rs.close();


            /*int orders[] = MMPCOrder.getAllIDs("MPC_Order", "docstatus = '"+DocAction.STATUS_Closed+"'", null);
            ArrayList<MMPCOrderBOMLine> lines = new ArrayList<MMPCOrderBOMLine>();
			for (int i=0;i<orders.length;i++){
				MMPCOrder order = new MMPCOrder(getCtx(),orders[i],null);
                //Si se ingreso una categoria entonces tomo en cueta las OM unicamente de dicha categoria
                if (!p_M_Product_Category_ID.equals(BigDecimal.ZERO)){
                    //Obtengo el producto asociado
                    MProduct p = new MProduct(getCtx(), order.getM_Product_ID(), null);
                    if (p.getM_Product_Category_ID()!=p_M_Product_Category_ID.intValue())
                        //continuo porque a esta orden no la voy a considerar por no tener un producto de la categoria dada...
                        continue;
                    else{
                        //Chequeo el rango de fechas
                        if (!(order.getDateOrdered().compareTo(from)>-1 && order.getDateOrdered().compareTo(to)<1)){
                            //continuo porque a esta orden no la voy a considerar por no entrar en el rango de fechas...
                            continue;
                        }
                    }
                }
                MMPCOrderBOMLine lineas[] = order.getLines(true, null);
				for(int j=0;j<lineas.length;j++){
                    lines.add(lineas[j]);
                }
			}*/
			MMPCOrderBOMLine[] bomlines = new MMPCOrderBOMLine[lines.size()];
			return lines.toArray(bomlines);

		}

		/** BISion - 13/03/2009 - Santiago IbaÃ±ez
		 * Metodo que retorna la ultima linea de factura de compra dado un producto
		 * @param M_Product_ID
		 * @return
		 */
		private MInvoiceLine getUltimaLineaFacturaCompra(int M_Product_ID) throws Exception{
			MInvoiceLine invoiceLine = null;
			//Como actualmente no se pueden completar Facturas por un problema en Panalab se quito la consideracion de facturas en estado 'CO'
            //PreparedStatement ps = DB.prepareStatement("SELECT il.C_InvoiceLine_ID FROM C_Invoiceline il join C_Invoice i on (i.C_Invoice_ID = il.C_Invoice_ID) WHERE i.docstatus = 'CO' AND il.M_Product_ID = "+M_Product_ID+"AND i.issotrx = 'N' ORDER BY i.dateinvoiced DESC", null);
            PreparedStatement ps = DB.prepareStatement("SELECT il.C_InvoiceLine_ID FROM C_Invoiceline il join C_Invoice i on (i.C_Invoice_ID = il.C_Invoice_ID) WHERE il.M_Product_ID = "+M_Product_ID+"AND i.issotrx = 'N' ORDER BY i.dateinvoiced DESC", null);
			//try{
				ResultSet rs = ps.executeQuery();
				if (rs.next())//{
					invoiceLine = new MInvoiceLine(getCtx(),rs.getInt(1),null);
			//	}
                rs.close();
                ps.close();
			/*}
			catch(Exception e){
                e.printStackTrace();
			}*/
			return invoiceLine;
		}

        /** BISion - 17/02/2010 - Santiago Ibañez
         * Metodo que retorna el precio de la ultima orden de compra (sin considerar estado)
         * del producto dado.
         * @param M_Product_ID
         * @return
         * @throws java.lang.Exception
         */
        private BigDecimal getPrecioUltimaOC(int M_Product_ID) throws Exception{
			BigDecimal qty = BigDecimal.ZERO;
            PreparedStatement ps = DB.prepareStatement("SELECT priceentered, o.c_order_id " +
            		" FROM c_orderline ol join c_order o on (o.c_order_id = ol.c_order_id)" +
            		" WHERE ol.M_Product_ID = "+M_Product_ID+"AND o.issotrx = 'N' ORDER BY o.dateordered DESC", null);
			//try{
				ResultSet rs = ps.executeQuery();
				MOrder order = null ;
				if (rs.next()){
			        //16-02-2011 Camarzana Mariano 
			         //Modificacion para tomar la cotizacion del importe de la orden
					order = new MOrder(Env.getCtx(),rs.getInt(2), null);
					qty = rs.getBigDecimal(1).multiply(order.getCotizacion());
				}
                rs.close();
                ps.close();
			/*}
			catch(Exception e){
                e.printStackTrace();
			}*/
			return qty;
		}

		/** BISion - 13/03/2009 - Santiago IbaÃ±ez
		 * Dado un producto y la orden de manufactura retorna el precio real del mismo
		 * @param M_Product_ID
		 * @param MPC_Order_ID
		 * @return
		 */
		private BigDecimal getPrecioReal(int M_Product_ID,int MPC_Order_ID){
			BigDecimal precio = BigDecimal.ZERO;
			//obtengo todos los detalles de costo real de materiales porque se pueden haber utilizado varias partidas del mismo producto

			int detalleCosto[] = MMPCCostDetail.getAllIDs("MPC_Cost_Detail", "MPC_Order_ID = "+MPC_Order_ID+" and M_Product_ID = "+M_Product_ID+" AND MPC_Cost_Element_ID = "+ElementoCostoMateriales, null);
			for (int i=0;i<detalleCosto.length;i++){
				//Obtengo el detalle de costo (se carga al cierre de la OM)
				MMPCCostDetail detalle = new MMPCCostDetail(getCtx(),detalleCosto[i],null);
				precio = precio.add(detalle.getCOSTO_TOTAL());
			}
			return precio;
		}

        private BigDecimal getPrecioMateriaPrima(int M_Product_ID,int M_AttributeSetInstance_ID){
        //obtengo la linea del recibo de materiales dados un producto y su partida
        MInOutLine receiptLine = getMInOutLine(M_Product_ID,M_AttributeSetInstance_ID);
        BigDecimal price = BigDecimal.ZERO;
        if (receiptLine!=null){
            /** BISion - 25/03/2009 - Santiago Ibañez
             * Modificacion realizada para obtener el precio desde la factura y no desde la OC
             */
            int[] facturas =  MInvoiceLine.getAllIDs("C_InvoiceLine", "M_InoutLine_ID = "+receiptLine.getM_InOutLine_ID()+" AND M_Product_ID = "+receiptLine.getM_Product_ID(), null);
            //tomo la primer factura que encuentro
            if (facturas!=null && facturas.length>0){
                System.out.println("Tiene factura");
                MInvoiceLine linea = new MInvoiceLine(getCtx(), facturas[0], null);
                
                MInvoice invoice = new MInvoice(Env.getCtx(), linea.getC_Invoice_ID(),null);
                
                if (!linea.getQtyInvoiced().equals(BigDecimal.ZERO))
                    //16-02-2011 Camarzana Mariano
                	//Se le agrego la tasa de conversion
                	price = linea.getLineNetAmt().divide(linea.getQtyInvoiced(),precision,BigDecimal.ROUND_HALF_UP).multiply(invoice.getCotizacion());
            }
            //Si no tiene facturas asociadas chequeo por la orden de compra
            else{
                int C_OrderLine = receiptLine.getC_OrderLine_ID();
                if (C_OrderLine != 0){
                    //Obtengo la OC
                    MOrderLine orderLine = new MOrderLine(getCtx(),C_OrderLine, get_TrxName());
                    if (orderLine !=null){
                    	MOrder order = new MOrder(getCtx(),orderLine.getC_Order_ID(), null);
                        System.out.println("Precio obtenido de una Orden de Compra");
                        
                        //16-02-2011 Idem
                        price = orderLine.getPriceEntered().multiply(order.getCotizacion());
                    }
                }
            }
            //fin modificacion BISion
        }
        else{
            System.out.println("No Hay recepciones");
        }
        System.out.println("Precio Obtenido: $"+price);
        return price;
        }
        /* Bision - 17/07/2008 - Santiago Ibañez
         * Metodo que retorna la linea de recepcion de materiales para un
         * producto y partida dados.
         * */
        private MInOutLine getMInOutLine(int M_Product_ID, int M_AttributeSetInstance_ID){
            QueryDB query = new QueryDB("org.compiere.model.X_M_InOutLine");
            String filter = "M_Product_ID = " + M_Product_ID + " AND M_AttributeSetInstance_ID = "+M_AttributeSetInstance_ID;
            java.util.List results = query.execute(filter);
            Iterator select = results.iterator();
            while (select.hasNext())
            {
             X_M_InOutLine xm = (X_M_InOutLine)select.next();
             MInOutLine receipt = new MInOutLine(getCtx(),xm.getM_InOutLine_ID(),get_TrxName());
             return receipt;
            }
            return null;
        }

        /**
         * COSTO INDIRECTO INTEGRADO
         */


        /**
         * Metodo que carga en  la tabla de costos indirectos aquellos datos
         * para la OM de manufactura ingresada (o todas aquellas que esten cerradas)
         * @throws java.lang.Exception
         */
        private void cargarTablaIndirecto(String proceso) throws Exception{
            resetParametros();
             if (MMPCCostElement.getCostElementByName("Costo Real Indirecto")==null)
                JOptionPane.showMessageDialog(null, "Falta el Elemento de Costo: Costo Real Indirecto", "Elemento de costo faltante", JOptionPane.ERROR_MESSAGE);
            else if (MCostElement.getCostElementByName("Costo Estandar Indirecto")==null)
                JOptionPane.showMessageDialog(null, "Falta el Elemento de Costo: Costo Estandar Indirecto", "Elemento de costo faltante", JOptionPane.ERROR_MESSAGE);
            else if (proceso.equals("Detalle Costos Integrados")){
                cargarIndirectoManufactura();
            }
            else if (proceso.equals("Costos Integrados")){
                //16-02-2011 Camarzana Mariano
            	//Modificado para que tome la fecha de comienzo de la orden en vez de la fecha de la misma
            	//o.dateordered -> o.datestart
            	String sqlSelect = "select distinct(mpc_order_id) from mpc_order o " +
                               "inner join m_product p on (p.m_product_id = o.m_product_id) " +
                               "where o.docstatus = '"+DocAction.STATUS_Closed+"' " +
                               "and o.datestart between ? and ?";
                if (p_M_Product_Category_ID!=BigDecimal.ZERO)
                    sqlSelect+= "and p.m_product_category_id = "+p_M_Product_Category_ID;
                PreparedStatement ps = DB.prepareStatement(sqlSelect, null);
                ps.setTimestamp(1, from);
                ps.setTimestamp(2, to);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    resetParametros();
                    p_MPC_Order_ID = new BigDecimal(rs.getInt(1));
                    cargarIndirectoManufactura();
                }
                rs.close();
                ps.close();
            }
        }
        
        /** BISion - 19/03/2009 - Santiago IbaÃ±ez
         * Carga los datos de costo indirecto para una orden de manufactura en
         * particular (p_MPC_Order_ID)
         */
        private void cargarIndirectoManufactura() throws Exception{
            //Obtengo la orden de manufactura
            MMPCOrder order = new MMPCOrder(getCtx(), p_MPC_Order_ID.intValue(), null);
            //Obtengo el registro de Costo Real cuyo elemento de Costo sea Real Indirecto
            int[] cost = MMPCOrderCost.getAllIDs("MPC_Order_Cost", "MPC_Cost_Element_ID = "+MMPCCostElement.getCostElementByName("Costo Real Indirecto").getMPC_Cost_Element_ID()+" and MPC_Order_ID = "+p_MPC_Order_ID, null);
            if (cost.length>0){
                MMPCOrderCost oc = new MMPCOrderCost(getCtx(), cost[0], null);
                RealTotal = oc.getCostCumQty();
                //Paso el precio real a precio unitario
                if (!RealTotal.equals(BigDecimal.ZERO)&&!order.getQtyDelivered().equals(BigDecimal.ZERO))
                    PrecioReal = RealTotal.divide(order.getQtyDelivered(), precision, BigDecimal.ROUND_DOWN);
                else
                    PrecioReal = BigDecimal.ZERO;
            }
            M_Product_ID = order.getM_Product_ID();
            //Obtengo el costo Estandar Indirecto
            MCost[] costosEstandar = MCost.getElements(M_Product_ID, p_C_AcctSchema_ID.intValue());
            for(int j=0;j<costosEstandar.length;j++)
                if (costosEstandar[j].getM_CostElement_ID() == MCostElement.getCostElementByName("Costo Estandar Indirecto").getM_CostElement_ID())
                    PrecioTeorico = costosEstandar[j].getCurrentCostPrice();
            setVariacionPrecio();
            setVariacionPorcentualPrecio();
            insertarFilaEnTablaIndirecto(order.getQtyDelivered());
        }

        public void insertarFilaTotales(int MPC_Order_ID,Timestamp DateOrdered, BigDecimal QtyDelivered) throws Exception{
            String sqlInsert = "insert into T_COSTOSTOTALES values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = DB.prepareStatement(sqlInsert, null);
            ps.setInt(1, AD_Client_ID);
			ps.setInt(2, AD_Org_ID);
			ps.setString(3, "Y");
			ps.setInt(4, M_Product_ID);
			ps.setBigDecimal(5, p_M_Product_Category_ID);
			ps.setTimestamp(6, DateOrdered);
            BigDecimal totalReal = precioMat.add(precioRec).add(precioInd);
            BigDecimal totalTeorico = precioMatT.add(precioRecT).add(precioIndT);
            ps.setBigDecimal(7, totalTeorico.setScale(precision,BigDecimal.ROUND_DOWN));
			ps.setBigDecimal(8, totalReal.setScale(precision,BigDecimal.ROUND_DOWN));
            if (totalTeorico.equals(totalReal))
                ps.setBigDecimal(9, BigDecimal.ZERO);
            else
                ps.setBigDecimal(9, totalReal.subtract(totalTeorico));
			if (totalReal.floatValue() == totalTeorico.floatValue())
                ps.setBigDecimal(10, BigDecimal.ZERO);
            else if (totalReal.floatValue()==0 || totalTeorico.floatValue()==0){
                BigDecimal cien = totalReal.floatValue() == 0 ? new BigDecimal(-100):new BigDecimal(100);
                ps.setBigDecimal(10, cien);
            }
            else  //variacion * 100 / total teorico
                ps.setBigDecimal(10, (totalReal.subtract(totalTeorico)).multiply(new BigDecimal(100)).divide(totalTeorico,precision,BigDecimal.ROUND_HALF_UP));
			ps.setInt(11, MPC_Order_ID);
			ps.setBigDecimal(12, QtyDelivered);
			ps.setBigDecimal(13, p_C_AcctSchema_ID);
            ps.setInt(14, getAD_PInstance_ID());
            if (!QtyDelivered.equals(BigDecimal.ZERO)){
                ps.setBigDecimal(15, totalTeorico.divide(QtyDelivered,precision,BigDecimal.ROUND_DOWN));
                ps.setBigDecimal(16, totalReal.divide(QtyDelivered,precision,BigDecimal.ROUND_DOWN));
            }
            else{
                ps.setBigDecimal(15, BigDecimal.ZERO);
                ps.setBigDecimal(16, BigDecimal.ZERO);
            }

            BigDecimal ut = BigDecimal.ZERO;
            BigDecimal ur = BigDecimal.ZERO;

            ps.setBigDecimal(17, ur.subtract(ut));
            
            //Variacion del Precio Unitario
            if (!QtyDelivered.equals(BigDecimal.ZERO)){
                ut =totalTeorico.divide(QtyDelivered,precision,BigDecimal.ROUND_DOWN);
                ur =totalReal.divide(QtyDelivered,precision,BigDecimal.ROUND_DOWN);
            }
            if (ut.equals(ur))
                ps.setBigDecimal(17, BigDecimal.ZERO);
            else
                ps.setBigDecimal(17, ur.subtract(ut));

            //Variacion Porcentual del Precio Unitario
            if (ur.floatValue() == ut.floatValue())
                ps.setBigDecimal(18, BigDecimal.ZERO);
            else if (ur.floatValue()==0 || ut.floatValue()==0){
                BigDecimal cien = ur.floatValue() == 0 ? new BigDecimal(-100):new BigDecimal(100);
                ps.setBigDecimal(18, cien);
            }
            else  //variacion * 100 / total teorico
                ps.setBigDecimal(18, (ur.subtract(ut)).multiply(new BigDecimal(100)).divide(ut,precision,BigDecimal.ROUND_HALF_UP));
			ps.executeUpdate();
            ps.close();
        }

        private void setTotalesOM(int MPC_Order_ID) throws Exception{
            //precios reales
            int k=0;
            precioMat = BigDecimal.ZERO;
            precioRec = BigDecimal.ZERO;
            precioInd = BigDecimal.ZERO;
            //precios teoricos
            precioMatT = BigDecimal.ZERO;
            precioRecT = BigDecimal.ZERO;
            precioIndT = BigDecimal.ZERO;
            MMPCOrder order = new MMPCOrder(getCtx(), MPC_Order_ID, null);

            //No tomo en cuenta los costos existentes de OM

            /*if (!order.getQtyDelivered().equals(BigDecimal.ZERO)){
                BigDecimal costoOM = order.getCostoOM(MPC_Order_ID, ElementoCostoRecursos);
                if (costoOM.signum()!=-1)
                    precioMat = costoOM;
            }*/
            PreparedStatement ps;
            ResultSet rs;

            //Calculo el costo real de materiales solo si no lo tiene
            if (precioMat.floatValue()==0){
                ps = DB.prepareStatement("select nvl(sum(precioreal*cantidadreal),0) from t_integracioncostmat where mpc_order_id = "+MPC_Order_ID,null);
                rs = ps.executeQuery();

                if(rs.next()){
                    precioMat = rs.getBigDecimal(1);
                }
                ps.close();
                rs.close();
            }
            /*if (!order.getQtyDelivered().equals(BigDecimal.ZERO)){
                BigDecimal costoOM = order.getCostoOM(MPC_Order_ID, ElementoCostoRecursos);
                if (costoOM.signum()!=-1)
                    precioRec = costoOM;
            }*/
            //Calculo el costo real de recursos solo si no lo tiene
            if (precioRec.floatValue()==0){
                ps = DB.prepareStatement("select nvl(sum(preciorecurso*cantidadreal),0) from t_integracioncostrec where mpc_order_id = "+MPC_Order_ID,null);
                rs = ps.executeQuery();
                if(rs.next()){
                    precioRec = rs.getBigDecimal(1);
                }
                ps.close();
                rs.close();
            }

            //Calculo el costo real indirecto solo si no lo tiene
            /*if (!order.getQtyDelivered().equals(BigDecimal.ZERO))
                precioInd = order.getCostoOM(MPC_Order_ID, ElementoCostoIndirecto);*/
            if (precioInd.floatValue()==0){
                ps = DB.prepareStatement("select nvl(sum(realtotal),0) from t_integracioncostind where mpc_order_id = "+MPC_Order_ID,null);
                rs = ps.executeQuery();
                if(rs.next()){
                    precioInd = rs.getBigDecimal(1);
                }
                ps.close();
                rs.close();
            }
            ps = DB.prepareStatement("select nvl(sum(precioteorico*cantidadteorica),0) from t_integracioncostmat where mpc_order_id = "+MPC_Order_ID,null);
            rs = ps.executeQuery();
            if(rs.next()){
                precioMatT = rs.getBigDecimal(1);
            }
            ps.close();
            rs.close();

            ps = DB.prepareStatement("select nvl(sum(preciorecurso*cantidadteorica),0) from t_integracioncostrec where mpc_order_id = "+MPC_Order_ID,null);
            rs = ps.executeQuery();
            if(rs.next()){
                precioRecT = rs.getBigDecimal(1);
            }
            ps.close();
            rs.close();

            ps = DB.prepareStatement("select nvl(sum(teoricototal),0) from t_integracioncostind where mpc_order_id = "+MPC_Order_ID,null);
            rs = ps.executeQuery();
            if(rs.next()){
                precioIndT = rs.getBigDecimal(1);
            }
            System.out.println("Precio Teorico Materiales: "+precioMatT);
            System.out.println("Precio Teorico de Recursos: "+precioRecT);
            System.out.println("Precio Teorico Indirecto: "+precioIndT);
            System.out.println("Precio Real Materiales: "+precioMat);
            System.out.println("Precio Real de Recursos: "+precioRec);
            System.out.println("Precio Real Indirecto: "+precioInd);

            ps.close();
            rs.close();
            //insertarFilaTotales();
        }

        private void cargarTablaTotales(){
            System.out.println("****Carga de Costos Totales****");
            //Obtengo cada una de las ordenes involucradas
            System.out.println("Obteniendo todas las ordenes de manufactura involucradas...");
            //16-02-2011 Camarzana Mariano
        	//Modificado para que tome la decha de comienzo de la orden en vez de la fecha de la misma
        	//o.dateordered -> o.datestart
            String sqlSelect = "select distinct(mpc_order_id) from mpc_order o " +
                               "inner join m_product p on (p.m_product_id = o.m_product_id) " +
                               "where o.docstatus = '"+DocAction.STATUS_Closed+"' " +
                               "and o.datestart between ? and ? ";
            if (p_M_Product_Category_ID!=BigDecimal.ZERO)
                sqlSelect+= "and p.m_product_category_id = "+p_M_Product_Category_ID;
            PreparedStatement ps = DB.prepareStatement(sqlSelect, null);
            try{
                ps.setTimestamp(1, from);
                ps.setTimestamp(2, to);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    MMPCOrder order = new MMPCOrder(getCtx(),rs.getInt(1),null);
                    M_Product_ID = order.getM_Product_ID();
                    System.out.println("Cargando totales de: "+order.toString());
                    setTotalesOM(order.getMPC_Order_ID());
                    
                    //16-02-2011 Idem 
                    //insertarFilaTotales(order.getMPC_Order_ID(), order.getDateOrdered(), order.getQtyDelivered());
                    insertarFilaTotales(order.getMPC_Order_ID(), order.getDateStart(), order.getQtyDelivered());
                }

                /*
                 * 21-10-2010 Camarzana Mariano 
                 * Faltaba cerrar los cursores - ORA-01000:	maximum open cursors exceeded
                 */
                ps.close();
                rs.close();
            }
            catch(Exception e){
                System.out.println("No se pudieron obtener las OM");
                System.out.println(e.getMessage());
            }
            
        }

        private BigDecimal getCostoRecurso(int M_Product_ID){
            BigDecimal cost = BigDecimal.ZERO;
            RollupWorkflow procesoRecursos = new RollupWorkflow();
            //procesoRecursos.setParamFromOut(p_AD_Org_ID, p_M_Warehouse_ID , M_Product_ID, M_CostType_ID, C_AcctSchema_ID,costElementType);
            procesoRecursos.startProcess(getCtx(), this.getProcessInfo(),null/*Trx.get("update",true)*/);
            return cost;
        }

        private void setCantidadTeoricaRecursos(int M_Product_ID, MMPCOrderNode node,BigDecimal qtyDelivered){
            CantidadTeorica = BigDecimal.ZERO;
            BigDecimal time = BigDecimal.ONE;
            MWFNode nodo = new MWFNode(getCtx(), node.getAD_WF_Node_ID(), null);
            MMPCProductPlanning pp = MMPCProductPlanning.get(getCtx(), AD_Org_ID , M_Product_ID,-1);
            //tomo el flujo de trabajo para el producto M_Product_ID
            int AD_Workflow_ID = pp.getAD_Workflow_ID();
            MWorkflow Workflow = new MWorkflow(getCtx(),AD_Workflow_ID,null);
            //BigDecimal rate = getCostoTeorico(M_Product_ID);
            //obtengo la unidad de medida del recurso asociado al nodo
            MUOM uom = getResourceUOM(nodo.getS_Resource_ID());
            //obtengo la unidad de duraciï¿½n del nodo
            String wfDuration = Workflow.getDurationUnit();
            BigDecimal time_resource = BigDecimal.ONE;
            BigDecimal costo_resource = BigDecimal.ZERO;
            MResource resource = new MResource(getCtx(),nodo.getS_Resource_ID(),null);
            MResourceType type = resource.getResourceType();
            if (type==null)
                return;
            boolean normalizar = false;
            //Normalizo a segundos si no tienen la misma unidad de tiempo
            if (!igualUnidadTiempo(wfDuration,uom)){
                normalizar = true;
                //convierto la unidad de medida del recurso a segundos
                time_resource=type.getSeconds();
                //convierto la unidad de tiempo del nodo a segundos
                time = type.getSeconds(wfDuration);
            }
            //calculo el costo por unidad de tiempo para el recurso [$/t]
            //if (!time_resource.equals(BigDecimal.ZERO))
                //costo_resource=rate.divide(time_resource,12,BigDecimal.ROUND_HALF_UP);

            //es tiempo por lote?
            if ( nodo.isBatchTime() ){
                if ( ! pp.getOrder_Pack().equals(Env.ZERO) ){
                    BigDecimal size_lote=(BigDecimal)pp.getOrder_Pack();
                    //obtengo la duracion del nodo
                    BigDecimal duracionNodo = new BigDecimal(nodo.getBATCHTIME());
                    time = time.multiply(duracionNodo);
                    time = time.divide(size_lote,precision,BigDecimal.ROUND_HALF_UP);
                    costo_resource=costo_resource.multiply(time);
                }

            }else{
                //obtengo la duracion del nodo
                BigDecimal duracionNodo = new BigDecimal(nodo.getDuration());
                time = time.multiply(duracionNodo);
            }
            CantidadTeorica = time.multiply(qtyDelivered);
            if (normalizar)
                CantidadTeorica = CantidadTeorica.divide(new BigDecimal(3600),precision,BigDecimal.ROUND_HALF_UP);
        }

        /* Bision - 23/02/2010 - Santiago Ibañez
         * Funcion que dado un recurso retorna la unidad de medida
         * */
        private MUOM getResourceUOM(int S_Resource_ID){
            int C_UOM_ID = DB.getSQLValue(null,"SELECT C_UOM_ID FROM M_Product WHERE S_Resource_ID = ? " , S_Resource_ID);
            MUOM uom = new MUOM(getCtx(),C_UOM_ID,null);
            return uom;
        }

        private boolean igualUnidadTiempo(String unit,MUOM uom){
          if (unit.equals("h")&&uom.isHour())
            return true;
          else if (unit.equals("m")&&uom.isMinute())
            return true;
          else if (unit.equals("D")&&uom.isDay())
            return true;
          else if (unit.equals("M")&&uom.isMonth())
            return true;
          else if (unit.equals("Y")&&uom.isYear())
            return true;
          return false;
        }
}
