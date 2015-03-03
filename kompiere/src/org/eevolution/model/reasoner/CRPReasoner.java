package org.eevolution.model.reasoner;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.MResourceUnAvailable;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.*;

import org.eevolution.tools.DateTimeUtil;

import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderWorkflow;


/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class CRPReasoner {

	/**
	 * All the below cases expect exactly two parameters: The (1) begin and the (2) end of a day
	 */

	/**
	 * Case 1: The time dependent process has already begun and ends at this day.
	 */
	public static final String RESTRICTION_DAY_CASE_1 =
		"(datestartschedule<=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 2: The time dependent process begins and ends at this day.
	 */
	public static final String RESTRICTION_DAY_CASE_2 =
		"(datestartschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datestartschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 3: The time dependent process begins at this day and ends few days later.
	 */
	public static final String RESTRICTION_DAY_CASE_3 =
		"(datestartschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datestartschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 4: The time dependent process has already begun and ends few days later.
	 */
	public static final String RESTRICTION_DAY_CASE_4 =
		"(datestartschedule<=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

        private static CLogger	log	= CLogger.getCLogger (CRPReasoner.class);


	private String getDayRestriction(Timestamp dateTime, MResource r) {

		Object[] params = { getBorderDayMin(dateTime, r).toString(), getBorderDayMax(dateTime, r).toString() };

		return
			MessageFormat.format(RESTRICTION_DAY_CASE_1, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_2, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_3, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_4, params);
	}

    public MMPCOrder[] getMPCOrdersNotCompleted(MResource r) {

        /**
         *      Vit4B 12/11/2007
         *
         *      Comentado para que no tome ordenes <> CL solamente porque tambien bajo este concepto
         *      toma las ordenes anuladas que no deberian ser consideradas.
         *
         *      Deberia de considerar: CO - AP - DR - IP
         *
         *
         *
         */

        /**
         *      Vit4B 20/11/2007
         *
         *      Agregado para que considere las ordenes que estan con cantidades pendientes.
         *      No tiene sentido si ya se recibió todo lo que la orden demandaba.
         *
         *      " QtyEntered - QtyDelivered > 0 "
         *
         */


         //String sql = "SELECT owf.MPC_Order_Workflow_ID , o.DateStartSchedule , o.DateFinishSchedule ,o.QtyOrdered - o.QtyDelivered - o.QtyScrap AS QtyOpen FROM MPC_Order o INNER JOIN MPC_Order_Workflow owf ON (owf.MPC_ORDER_ID = o.MPC_Order_ID) WHERE o.DocStatus <> 'CL' AND o.AD_Client_ID = ? AND o.S_Resource_ID= ? ORDER BY DatePromised" ;
     	 //String where =
      		 // Checks the requested resource id directly on order node, not on resource id of the order
      		 //"MPC_Order_ID IN (SELECT MPC_Order_ID FROM MPC_Order_Node on WHERE on.S_Resource_ID="+r.getID()+")"
                 ////////"S_Resource_ID="+r.get_ID() +" AND DocStatus <> 'CL' AND AD_Client_ID = " + r.getAD_Client_ID() ; //+ " AND MPC_Order_ID = 1000031" ;
      		 // ... and completed orders needn't to be observed

         String where = "S_Resource_ID = " + r.get_ID() +
                 " AND DocStatus <> 'CL' " +
                 " AND DocStatus <> 'VO' " +
                 " AND QtyEntered - QtyDelivered > 0 " +
                 " AND AD_Client_ID = " + r.getAD_Client_ID();




      	 int[] orderIds = PO.getAllIDs("MPC_Order", where, null);
      	 MMPCOrder[] orders = new MMPCOrder[orderIds.length];
      	 for(int i = 0; i < orderIds.length; i++) {

      		 orders[i] = new MMPCOrder(Env.getCtx(), orderIds[i], null);
      	 }

      	 return orders;
    }

    public Timestamp getBorderDayMin(Timestamp dateTime, MResource r) {

    	MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
    	Timestamp dMin = null;
    	return (t.isTimeSlot()) ?
    			DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotStart(), false) :
    			DateTimeUtil.getDayBorder(dateTime, null, false);
    }

    public Timestamp getBorderDayMax(Timestamp dateTime, MResource r) {

    	MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
    	Timestamp dMin = null;
    	return (t.isTimeSlot()) ?
    			DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotEnd(), true) :
    			DateTimeUtil.getDayBorder(dateTime, null, true);
    }

    public boolean isResourceAvailable(Timestamp dateTime, MResource r) {

    	MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);

    	return ( checkResourceAvailability(dateTime, r) && checkResourceTypeAvailability(dateTime, t) );
    }

    public MMPCOrder[] getMPCOrders(Timestamp dateTime, MResource r) {

    	if(!isResourceAvailable(dateTime, r)) {

    		return new MMPCOrder[0];
    	}

   	 	String where =
          		 // Checks the requested resource id directly on order node, not on resource id of the order
   	 			 "mpc_order_id in (select mpc_order_id from mpc_order_node where s_resource_id="+r.get_ID()
          		 // ... and only the orders running on given day
          		 +" AND ("+getDayRestriction(dateTime, r)+") ) AND AD_Client_ID =" + r.getAD_Client_ID();

         System.out.println("WHERE MPC_Order " + where);


       	 int[] orderIds = PO.getAllIDs("MPC_Order", where, null);
      	 MMPCOrder[] orders = new MMPCOrder[orderIds.length];
      	 for(int i = 0; i < orderIds.length; i++) {

      		 orders[i] = new MMPCOrder(Env.getCtx(), orderIds[i], null);
      	 }

       	 return orders;
    }

    public MMPCOrderNode[] getMPCOrderNodes(Timestamp dateTime, MResource r) {

    	if(!isResourceAvailable(dateTime, r)) {

    		return new MMPCOrderNode[0];
    	}

    	String where =
     		 "s_resource_id = "+r.get_ID()
     		 +" AND ("+getDayRestriction(dateTime, r)+") AND AD_Client_ID = " + r.getAD_Client_ID();
    	 log.log(Level.FINE,"getMPCOrderNodes --> Where:" + where);

         System.out.println("WHERE MPC_Order_Node " + where);

    	 int[] ids = PO.getAllIDs("MPC_Order_Node", where, null);

    	 MMPCOrderNode[] nodes = new MMPCOrderNode[ids.length];
    	 for(int i = 0; i < ids.length; i++) {

    		 nodes[i] = new MMPCOrderNode(Env.getCtx(), ids[i], null);
    	 }

    	 return nodes;
    }

    public MMPCOrderWorkflow getMPCOrderWorkflow(MMPCOrder o) {

     	 int[] ids = PO.getAllIDs("MPC_Order_Workflow", "MPC_Order_ID = "+o.get_ID() + " AND AD_Client_ID = " + o.getAD_Client_ID(), null);

      	 return (ids.length != 1) ? null : new MMPCOrderWorkflow(Env.getCtx(), ids[0], null);
   }

    public boolean checkResourceTypeAvailability(MResourceType t) {

		if(!t.isDateSlot()) {

			return true;
		}

		Timestamp dateTime = new Timestamp(System.currentTimeMillis());
		for(int i = 0; i < 7; i++) {

			if(checkResourceTypeAvailability(dateTime, t)) {

				return true;
			}

			//dateTime = DateTimeUtil.incrementDay(dateTime);
                        dateTime = org.compiere.util.TimeUtil.addDays(dateTime, 1);

		}

		return false;
	}

    public boolean checkResourceAvailability(Timestamp dateTime, MResource r) {

		int[] ids = PO.getAllIDs("S_ResourceUnAvailable", "S_Resource_ID = "+r.get_ID() + " AND AD_Client_ID = " + r.getAD_Client_ID(), null);

		Timestamp dateFrom = null;
		Timestamp dateTo = null;
		Timestamp dateActual = null;

		MResourceUnAvailable rua = null;
		for(int i = 0; i < ids.length; i++) {

			rua = new MResourceUnAvailable(Env.getCtx(), ids[i], null);

			dateFrom = DateTimeUtil.getDayBorder(rua.getDateFrom(), null, false);
			dateTo = DateTimeUtil.getDayBorder(rua.getDateTo(), null, true);
			dateActual = DateTimeUtil.getDayBorder(dateTime, null, false);

			if(dateFrom.compareTo(dateActual) <= 0 && dateTo.compareTo(dateActual) >= 0 ) {

				return false;
			}
		}

		return true;
	}

    public boolean checkResourceTypeAvailability(Timestamp dateTime, MResourceType t) {

		if(!t.isDateSlot()) {

			return true;
		}

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dateTime.getTime());

		boolean retValue = false;
	 	switch(gc.get(Calendar.DAY_OF_WEEK)) {

	 		case Calendar.SUNDAY:
	 			retValue = t.isOnSunday();
	 			break;

			case Calendar.MONDAY:
				retValue = t.isOnMonday();
				break;

			case Calendar.TUESDAY:
				retValue = t.isOnTuesday();
				break;

			case Calendar.WEDNESDAY:
				retValue = t.isOnWednesday();
				break;

			case Calendar.THURSDAY:
				retValue = t.isOnThursday();
				break;

			case Calendar.FRIDAY:
				retValue = t.isOnFriday();
				break;

			case Calendar.SATURDAY:
				retValue = t.isOnSaturday();
				break;
			}

	 	return retValue;
	}

    public MMPCOrderNode[] getMPCOrderNodes2(Timestamp dateTime, MResource r, MMPCOrder mMPCOrder) {

        if(!isResourceAvailable(dateTime, r)) {

    		return new MMPCOrderNode[0];
    	}

    	String where =
     		 "s_resource_id = "+r.get_ID()
     		 +" AND ("+getDayRestriction(dateTime, r)+") AND MPC_Order_ID = " + mMPCOrder.getMPC_Order_ID() + "  AND AD_Client_ID = " + r.getAD_Client_ID();
    	 log.log(Level.FINE,"getMPCOrderNodes --> Where:" + where);
    	 int[] ids = PO.getAllIDs("MPC_Order_Node", where, null);

    	 MMPCOrderNode[] nodes = new MMPCOrderNode[ids.length];
    	 for(int i = 0; i < ids.length; i++) {

    		 nodes[i] = new MMPCOrderNode(Env.getCtx(), ids[i], null);
    	 }

    	 return nodes;

    }
}