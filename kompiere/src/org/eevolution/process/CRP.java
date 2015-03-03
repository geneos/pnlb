/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/

package org.eevolution.process;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderWorkflow;

import org.compiere.model.*;
import org.eevolution.model.MMPCProductPlanning;
import org.eevolution.model.reasoner.CRPReasoner;

import org.eevolution.tools.DateTimeUtil;

import java.math.BigDecimal;
import java.util.logging.*;
import java.sql.Timestamp;
import org.compiere.util.Trx;

/**
 * Capacity Requirement Planning
 * 
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany (Original by Victor Perez, e-Evolution, S.C.)
 * @version 1.0, October 14th 2005
 */
public class CRP extends SvrProcess {

	public static final String FORWARD_SCHEDULING = "F";
	public static final String BACKWARD_SCHEDULING = "B";
	
	private int p_S_Resource_ID;        
	private String p_ScheduleType;        
	private CRPReasoner reasoner;
	
 	public CRP() {
 		
 		super();
 		reasoner = new CRPReasoner();
 	}
       
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();

		if(para == null) {
			
			return;
		}
		
		for (int i = 0; i < para.length; i++) {
			
			if(para[i] == null) {
				
				continue;
			}

			String name = para[i].getParameterName();
			if (name.equals("S_Resource_ID")) {
            	
            	p_S_Resource_ID = ((BigDecimal)para[i].getParameter()).intValue();
                    
            }
			else if (name.equals("ScheduleType")) {
				
				p_ScheduleType = ((String)para[i].getParameter());				 		
			}
            else {
		
            	log.log(Level.SEVERE,"prepare - Unknown Parameter: " + name);
            }
		}
	}
       
     protected String doIt() throws Exception  {
         //Invoco al proceso que reestructura las transiciones de los nodos de las ordenes
         ActualizarTransicionesNodos proceso = new ActualizarTransicionesNodos();
         proceso.startProcess(getCtx(), getProcessInfo(),Trx.get(get_TrxName(),false));
         Trx.get(get_TrxName(), false).commit();
         return runCRP();
     } 
    
     private String runCRP() {

         log.warning("Comienzo del Proceso de Calcular Plan de Capacidad (CRP)");
 	MMPCOrderWorkflow owf = null;
	MMPCOrderNode node = null;
    	MResource resource = null;
        MResourceType resourceType = null;

        BigDecimal qtyOpen  = null;
    	
        Timestamp date = null; 
    	Timestamp dateStart = null;
    	Timestamp dateFinish = null;

	long nodeMillis = 0;
    	int nodeId = -1;
        Timestamp hoy = new Timestamp(System.currentTimeMillis());
	resource = new MResource(Env.getCtx(), p_S_Resource_ID, null);        
    	MMPCOrder[] orders = reasoner.getMPCOrdersNotCompleted(resource);
        //MMPCOrder[] orders = new MMPCOrder[1];
        //orders[0] = new MMPCOrder(getCtx(), 1052499, get_TrxName());// 1050183
        log.log(Level.INFO,"MMPCOrder[] : " + orders.length);
        for(int i = 0; i < orders.length; i++) {
                
                System.out.println(" Order Line " + orders[i].getMPC_Order_ID());
                
                /*      Vit4B 03/10/2007    Modificado para que tome la cantidad en funcion de lo requerido 
                 *      por la orden y lo entregado.
                 *      
                 *      POSIBLE ERROR !!!
                 *
                 *
                 */
                System.out.println("ORDEN Nº: "+orders[i].getDocumentNo());
                if(orders[i].getMPC_Order_ID() == 1025070)
                    System.out.println("ORDEN 961507");
                
                
                qtyOpen = orders[i].getQtyEntered().subtract(orders[i].getQtyDelivered()).subtract(orders[i].getQtyScrap());
        	
                if(qtyOpen.compareTo(Env.ZERO) < 0)
                    qtyOpen = Env.ZERO;
                
                
                owf = reasoner.getMPCOrderWorkflow(orders[i]);
			if(owf == null) {
				System.out.println("La orden "+orders[i].getDocumentNo()+" no tiene un flujo de trabajo asociado");
                log.warning("La orden "+orders[i].getDocumentNo()+" no tiene un flujo de trabajo asociado");
                continue;
				//return Msg.translate(Env.getCtx(), "Ok");
			}
        	
        	// Schedule Fordward
        	if (p_ScheduleType.equals(FORWARD_SCHEDULING)) {
        		log.log(Level.FINE,"MMPCOrder DocumentNo:" + orders[i].getDocumentNo());
                        log.log(Level.FINE,"MMPCOrder Workflow:" + owf.getName());
        		
                
                date = orders[i].getDateStartSchedule();
                if (date!=null&&date.compareTo(hoy)<0)
                    date = hoy;
        		nodeId = owf.getMPC_Order_Node_ID();
                if (nodeId==0)
                    log.warning("El flujo de trabajo: "+owf.getName()+" no tiene configurado un nodo de inicio");

                Timestamp dateStartScheduledOrder = date;
        		while(nodeId != 0) {
        			
        			node = new MMPCOrderNode(getCtx(),nodeId , get_TrxName());
                                log.log(Level.FINE,"MMPCOrder Node:" + node.getName() + " Description:" + node.getDescription());
        			resource = new MResource(Env.getCtx(), node.getS_Resource_ID(), null);
        			resourceType = new MResourceType(Env.getCtx(), resource.getS_ResourceType_ID(), null);
        			
    				// Checks, whether the resource type is principal available on one day a week.
        			// If not, process breaks with a Info message about.
        			if(!reasoner.checkResourceTypeAvailability(resourceType)) {
        				
        				return Msg.getMsg(Env.getCtx(), "ResourceNotInSlotDay");
        			}
        			
        			//nodeMillis = calculateMillisFor(node, resourceType, qtyOpen, owf.getDurationBaseSec());
        			nodeMillis = calculateMillisForLoteInclude(orders[i], node, resourceType, qtyOpen, owf.getDurationBaseSec());
                                dateFinish = scheduleForward(date, nodeMillis ,resource, resourceType);

        			node.setDateStartSchedule(date);
        			node.setDateFinishSchedule(dateFinish);	
        			node.save(get_TrxName());
				date = node.getDateFinishSchedule();
				nodeId = owf.getNext(nodeId);
                                 if (nodeId == 0)
                                     log.log(Level.FINE,"---------------MMPCOrder Node Next not exist:" );
        		}
                if (node!=null){
                    orders[i].setDateFinishSchedule(node.getDateFinishSchedule());
                    orders[i].setDateStartSchedule(dateStartScheduledOrder);
                }
        	}
        	// Schedule backward
        	else if (p_ScheduleType.equals(BACKWARD_SCHEDULING)) {
                    
                        log.log(Level.FINE,"MMPCOrder DocumentNo:" + orders[i].getDocumentNo());
                        log.log(Level.FINE,"MMPCOrder Workflow:" + owf.getName());
        		date = orders[i].getDateFinishSchedule(); 
        		nodeId = owf.getLast(0);
        		
    			while(nodeId != 0) {
                                
        			node = new MMPCOrderNode(getCtx(),nodeId , get_TrxName());
                                log.log(Level.FINE,"MMPCOrder Node:" + node.getName() + " Description:" + node.getDescription());
        			resource = new MResource(Env.getCtx(), node.getS_Resource_ID(), null);
        			resourceType = new MResourceType(Env.getCtx(), resource.getS_ResourceType_ID(), null);

        			// Checks, whether the resource type is principal available on one day a week.
        			// If not, process breaks with a Info message about.
        			if(!reasoner.checkResourceTypeAvailability(resourceType)) {
        				
        				return Msg.getMsg(Env.getCtx(), "ResourceNotInSlotDay");
        			}

        			nodeMillis = calculateMillisFor(node, resourceType, qtyOpen, owf.getDurationBaseSec());
        			dateStart = scheduleBackward(date, nodeMillis ,resource, resourceType);

					node.setDateStartSchedule(dateStart);
        			node.setDateFinishSchedule(date);
				node.save(get_TrxName());
        			
				date = node.getDateStartSchedule();
        			nodeId = owf.getPrevious(nodeId);
                                if (nodeId == 0)
                                     log.log(Level.FINE,"MMPCOrder Node Previos not exist:" );
                                    
        		}
                  if (node != null)      
                  orders[i].setDateStartSchedule(node.getDateStartSchedule()) ;                      
        	}

            orders[i].save(get_TrxName());
        }
        
     	return "OK";
     }
     
     private long calculateMillisFor(MMPCOrderNode node, MResourceType type, BigDecimal qty, long commonBase) {
    	 
	 		// A day of 24 hours in milliseconds
			double aDay24 = 24*60*60*1000;

			// Initializing available time as complete day in milliseconds.
			double actualDay = aDay24;
			
			// If resource type is timeslot, updating to available time of the resource. 
			if (type.isTimeSlot()) {
				
				actualDay = (double)DateTimeUtil.getTimeDifference(type.getTimeSlotStart(), type.getTimeSlotEnd());	
			}
			
			// Available time factor of the resource of the workflow node
			BigDecimal factorAvailablility = new BigDecimal((actualDay / aDay24));  

			// Total duration of workflow node (seconds) ...
			// ... its static single parts ...
			BigDecimal totalDuration = new BigDecimal(
					
					//node.getQueuingTime() 
					node.getSetupTimeRequiered() // Use the present required setup time to notice later changes  
					+ node.getMovingTime() 
					+ node.getWaitingTime()
			);
			// ... and its qty dependend working time ... (Use the present required duration time to notice later changes)
			//totalDuration = totalDuration.add(qty.multiply(new BigDecimal(node.getDurationRequiered()))); 
                        totalDuration = totalDuration.add(qty.multiply(new BigDecimal(node.getDuration()))); 
			// ... converted to common base.
			totalDuration = totalDuration.multiply(new BigDecimal(commonBase));

			// Returns the total duration of a node in milliseconds.
			return totalDuration.multiply(new BigDecimal(1000)).longValue(); 
     }

     
     /*     Vit4B - 09/01/2008
      *
      *     Agregado para calcular la duracion en funcion de los lotes en el caso que 
      *     el nodo este configurado para calculo por lotes.
      *
      *
      *
      */
     
     private long calculateMillisForLoteInclude(MMPCOrder order, MMPCOrderNode node, MResourceType type, BigDecimal qty, long commonBase) {
                        
                        
                        if(node.isBatchTime() == true)
                        {
                        
                            MMPCProductPlanning plan = MMPCProductPlanning.get(Env.getCtx(), order.getAD_Org_ID(),order.getM_Product_ID());
                            BigDecimal tamLote = plan.getOrder_Pack();
                            
                            // Saco la cantidad de lotes dividiendo la cantidad de la orden por el tama�o del lote y redondeando para arriba
                            BigDecimal numLotes = BigDecimal.ZERO;
                            if (!tamLote.equals(BigDecimal.ZERO))
                                numLotes = qty.divide(tamLote,BigDecimal.ROUND_UP);
                            
                            // A day of 24 hours in milliseconds
                            double aDay24 = 24*60*60*1000;

                            // Initializing available time as complete day in milliseconds.
                            double actualDay = aDay24;

                            // If resource type is timeslot, updating to available time of the resource. 
                            if (type.isTimeSlot()) {

                                    actualDay = (double)DateTimeUtil.getTimeDifference(type.getTimeSlotStart(), type.getTimeSlotEnd());	
                            }

                            // Available time factor of the resource of the workflow node
                            BigDecimal factorAvailablility = new BigDecimal((actualDay / aDay24));  

                            // Total duration of workflow node (seconds) ...
                            // ... its static single parts ...
                            BigDecimal totalDuration = new BigDecimal(

                                            //node.getQueuingTime() 
                                            node.getSetupTimeRequiered() // Use the present required setup time to notice later changes  
                                            + node.getMovingTime() 
                                            + node.getWaitingTime()
                            );
                            // ... and its qty dependend working time ... (Use the present required duration time to notice later changes)
                            //totalDuration = totalDuration.add(qty.multiply(new BigDecimal(node.getDurationRequiered()))); 
                            totalDuration = totalDuration.add(numLotes.multiply(new BigDecimal(node.getBATCHTIME()))); 
                            // ... converted to common base.
                            totalDuration = totalDuration.multiply(new BigDecimal(commonBase));

                            // Returns the total duration of a node in milliseconds.
                            return totalDuration.multiply(new BigDecimal(1000)).longValue(); 
                            
                            
                            
                        }
                        else
                        {

                            // A day of 24 hours in milliseconds
                            double aDay24 = 24*60*60*1000;

                            // Initializing available time as complete day in milliseconds.
                            double actualDay = aDay24;

                            // If resource type is timeslot, updating to available time of the resource. 
                            if (type.isTimeSlot()) {

                                    actualDay = (double)DateTimeUtil.getTimeDifference(type.getTimeSlotStart(), type.getTimeSlotEnd());	
                            }

                            // Available time factor of the resource of the workflow node
                            BigDecimal factorAvailablility = new BigDecimal((actualDay / aDay24));  

                            // Total duration of workflow node (seconds) ...
                            // ... its static single parts ...
                            BigDecimal totalDuration = new BigDecimal(

                                            //node.getQueuingTime() 
                                            node.getSetupTimeRequiered() // Use the present required setup time to notice later changes  
                                            + node.getMovingTime() 
                                            + node.getWaitingTime()
                            );
                            // ... and its qty dependend working time ... (Use the present required duration time to notice later changes)
                            //totalDuration = totalDuration.add(qty.multiply(new BigDecimal(node.getDurationRequiered()))); 
                            totalDuration = totalDuration.add(qty.multiply(new BigDecimal(node.getDuration()))); 
                            // ... converted to common base.
                            totalDuration = totalDuration.multiply(new BigDecimal(commonBase));

                            // Returns the total duration of a node in milliseconds.
                            return totalDuration.multiply(new BigDecimal(1000)).longValue(); 
                        }

    }

     private Timestamp scheduleForward(Timestamp start, long nodeDuration, MResource r, MResourceType t)	{

 		// Checks, whether the resource is available at this day and recall with 
 		// next day, if not.
 		if(!reasoner.checkResourceAvailability(start, r)) {
 			
			//return scheduleForward(Util.incrementDay(start), nodeDuration, r, t);
                        return scheduleForward(org.compiere.util.TimeUtil.addDays(start, 1) , nodeDuration, r, t);
 			
 		}
 		// Checks, whether the resource type (only if date slot) is available at
 		// this day and recall with next day, if not.
 		else if(t.isDateSlot()) {
			  
			if(!reasoner.checkResourceTypeAvailability(start, t)) {
				
				//return scheduleForward(DateTimeUtil.incrementDay(start), nodeDuration, r, t);
                                return scheduleForward(org.compiere.util.TimeUtil.addDays(start, 1), nodeDuration, r, t);
			}
		}

		Timestamp dayStart = null;
 		// Retrieve the principal days start time, dependent on timeslot or not
 		if(t.isTimeSlot()) {
 			
 			dayStart = DateTimeUtil.getDayBorder(start, t.getTimeSlotStart(), false);
 		}
 		else {
 			
 			dayStart = DateTimeUtil.getDayBorder(start, null, false);
 		}

 		Timestamp dayEnd = null;
 		// Retrieve the days end time, dependent on timeslot or not
 		if(t.isTimeSlot()) {
 			
 			dayEnd = DateTimeUtil.getDayBorder(start, t.getTimeSlotEnd(), true);
 		}
 		else {
 			
 			dayEnd = DateTimeUtil.getDayBorder(start, null, true);
 		}

 		// If working has already begon at this day and the value is in the range of the 
 		// resource's availability, switch start time to the given again
		if(start.after(dayStart) && start.before(dayEnd)) {

 			dayStart = start;
 		}
 		
 		// The available time at this day in milliseconds
 		long availableDayDuration = DateTimeUtil.getTimeDifference(dayStart, dayEnd);
 		
 		Timestamp retValue = null;
 		// The work can be finish on this day.
 		if(availableDayDuration >= nodeDuration) {
 			
 			retValue = new Timestamp(dayStart.getTime()+nodeDuration);
 		}
 		// Otherwise recall with next day and the remained node duration.
 		else {
 			
			//retValue = scheduleForward(DateTimeUtil.incrementDay(DateTimeUtil.getDayBorder(start, null, false)), nodeDuration-availableDayDuration, r, t);
                      retValue = scheduleForward(org.compiere.util.TimeUtil.addDays(DateTimeUtil.getDayBorder(start, null, false),1), nodeDuration-availableDayDuration, r, t);
 		}
 		
 		return retValue;
 	}  	
 	
     private Timestamp scheduleBackward(Timestamp end, long nodeDuration, MResource r, MResourceType t)	{
                
                log.log(Level.FINE,"scheduleBackward --> end " +end);
                log.log(Level.FINE,"scheduleBackward --> nodeDuration " +nodeDuration);
                log.log(Level.FINE,"scheduleBackward --> ResourceType " + t);

 		// Checks, whether the resource is available at this day and recall with 
 		// next day, if not.
 		if(!reasoner.checkResourceAvailability(end, r)) {
 			
			//return scheduleBackward(DateTimeUtil.decrementDay(end), nodeDuration, r, t);
                        return scheduleBackward(org.compiere.util.TimeUtil.addDays(end , -1), nodeDuration, r, t);
 			
 		}

 		// Checks, whether the resource type (only if date slot) is available on 
 		// this day and recall with next day, if not.
		if(t.isDateSlot()) {
			  
			if(!reasoner.checkResourceTypeAvailability(end, t)) {
				
				//return scheduleBackward(DateTimeUtil.decrementDay(end), nodeDuration, r, t);
                                return scheduleBackward(org.compiere.util.TimeUtil.addDays(end , -1), nodeDuration, r, t);
			}
		}

		Timestamp dayEnd = null;
 		// Retrieve the principal days end time, dependent on timeslot or not
 		if(t.isTimeSlot()) {
 			
 			dayEnd = DateTimeUtil.getDayBorder(end, t.getTimeSlotEnd(), true);
 		}
 		else {
 			
 			dayEnd = DateTimeUtil.getDayBorder(end, null, true);
 		}

                log.log(Level.FINE,"scheduleBackward --> dayEnd " + dayEnd);
                 
 		Timestamp dayStart = null;
 		// Retrieve the start end time, dependent on timeslot or not
 		if(t.isTimeSlot()) {
 			
 			dayStart = DateTimeUtil.getDayBorder(end, t.getTimeSlotStart(), false);
 		}
 		else {
 			
 			dayStart = DateTimeUtil.getDayBorder(end, null, false);
 		}

                log.log(Level.FINE,"scheduleBackward --> dayStart " + dayStart);
                
 		// If working has already begon at this day and the value is in the range of the 
 		// resource's availability, switch end time to the given again
 		if(end.before(dayEnd) && end.after(dayStart)) {

 			dayEnd = end;
 		}
 		
                
                 
 		// The available time at this day in milliseconds
 		long availableDayDuration = DateTimeUtil.getTimeDifference(dayStart, dayEnd);
                
                log.log(Level.FINE,"scheduleBackward --> availableDayDuration  " + availableDayDuration );
                
 		Timestamp retValue = null;
 		// The work can be finish on this day.
 		if(availableDayDuration >= nodeDuration) {
 			log.log(Level.FINE,"scheduleBackward --> availableDayDuration >= nodeDuration true " + availableDayDuration + "|" + nodeDuration );
 			retValue = new Timestamp(dayEnd.getTime()-nodeDuration);
 		}
 		// Otherwise recall with previous day and the remained node duration.
 		else {
 			
			//retValue = scheduleBackward(DateTimeUtil.getDayBorder(end, null, true)), nodeDuration-availableDayDuration, r, t);
                        log.log(Level.FINE,"scheduleBackward --> availableDayDuration >= nodeDuration false " + availableDayDuration + "|" + nodeDuration );
                        log.log(Level.FINE,"scheduleBackward --> nodeDuration-availableDayDuration " + (nodeDuration-availableDayDuration) );
                        retValue = scheduleBackward(org.compiere.util.TimeUtil.addDays(DateTimeUtil.getDayBorder(end, null, true), -1), nodeDuration-availableDayDuration, r, t);
 		}
 		
                log.log(Level.FINE,"scheduleBackward -->  retValue  " +  retValue);
 		return retValue;
 	}  	

}
  
