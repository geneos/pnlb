package org.eevolution.form.crp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.compiere.model.MProduct;
import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.eevolution.model.reasoner.CRPReasoner;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import org.eevolution.tools.DateTimeUtil;

import org.eevolution.model.MMPCOrder;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.model.MMPCOrderWorkflow;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public abstract class CRPDatasetFactory extends CRPReasoner implements CRPModel {

	protected JTree tree;
	protected DefaultCategoryDataset dataset;
	
	protected abstract BigDecimal convert(BigDecimal value);
	
	public static CRPModel get(Timestamp start, Timestamp end, MResource r) {
		
		MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
		// Hardcoded UOM ID - 'Minutes' is base unit
		final MUOM uom1 = new MUOM(Env.getCtx(), 103, null);
		// Target UOM is the resource type's UOM
		final MUOM uom2 = new MUOM(Env.getCtx(), t.getC_UOM_ID(), null);

		CRPDatasetFactory factory = new CRPDatasetFactory() {
			
			protected BigDecimal convert(BigDecimal value) {
				
				return MUOMConversion.convert(Env.getCtx(), uom1.get_ID(), uom2.get_ID(), value);
			}
		};
		factory.generate(start, end, r);
		
		return factory;
	}

	protected boolean generate(Timestamp start, Timestamp end, MResource r) {

		 if(start == null || end == null || r == null) {
			 
			 return false;
		 }
			  	
		 String labelActCap = Msg.translate(Env.getCtx(), "DailyCapacity");
		 String labelLoadAct = Msg.translate(Env.getCtx(), "ActualLoad");
		 
		 MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
		 
		 BigDecimal utilization = r.getPercentUtillization();
		 BigDecimal dailyCapacity = null;
		 if(BigDecimal.ZERO.compareTo(utilization) < 0) {
			 
                     //fjviejo e-evolution machineqty
			//dailyCapacity = r.getDailyCapacity().divide(utilization.divide(new BigDecimal(100)), 8, BigDecimal.ROUND_HALF_DOWN);
                     dailyCapacity = r.getDailyCapacity().multiply(r.getMachineQty()).divide(utilization.divide(new BigDecimal(100)), 8, BigDecimal.ROUND_HALF_DOWN);
                     // fjviejo e-evolution end
		 }
		 else {
			 
			 dailyCapacity = BigDecimal.ZERO;
		 }
		 
		 BigDecimal load = null;

	     String label = null;
	 	 DateFormat formatter = DateFormat.getDateInstance();

	     dataset = new DefaultCategoryDataset();
		 HashMap names = new HashMap();
		 DefaultMutableTreeNode root = new DefaultMutableTreeNode(r);
		 names.put(root, getTreeNodeRepresentation(null, root, r));
		 
		 Timestamp dateTime = start;
	     while(end.after(dateTime)) {

	    	label = formatter.format(dateTime);
	    	names.putAll(addTreeNodes(dateTime, root, r));

	    	load = isResourceAvailable(dateTime, r) ? calculateLoad(dateTime, r, null) : BigDecimal.ZERO;
	    	
	    	dataset.addValue(isResourceAvailable(dateTime, r) ? dailyCapacity : BigDecimal.ZERO ,labelActCap, label);
	 		dataset.addValue(isResourceAvailable(dateTime, r) ? load : BigDecimal.ZERO ,labelLoadAct, label);

	 		//dateTime = Date.TimeUtil.incrementDay(dateTime);
                        dateTime = org.compiere.util.TimeUtil.addDays(dateTime,1);
		 } 	 	

	     tree = new JTree(root);
	     tree.setCellRenderer(new DiagramTreeCellRenderer(names));
	     return true;
	}

	protected BigDecimal calculateLoad(Timestamp dateTime, MResource r, String docStatus) {
		
		MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
		MMPCOrderNode[] nodes = getMPCOrderNodes(dateTime, r);
		MUOM uom = new MUOM(Env.getCtx(), t.getC_UOM_ID(), null);

		MMPCOrder o = null;
		BigDecimal qtyOpen;
		long millis = 0l;
		for(int i = 0; i < nodes.length; i++) {                        
			o = new MMPCOrder(Env.getCtx(), nodes[i].getMPC_Order_ID(), null);
			if(docStatus != null && !o.getDocStatus().equals(docStatus)) {
			
				continue;
			}
			
			millis += calculateMillisForDay(dateTime, nodes[i], t);
		}
		
		// Pre-converts to minutes, because its the lowest time unit of compiere 
		BigDecimal scale = new BigDecimal(1000*60);
		BigDecimal minutes = new BigDecimal(millis).divide(scale, 2, BigDecimal.ROUND_HALF_UP);
		return convert(minutes);
	}
	
	protected Timestamp[] getDayBorders(Timestamp dateTime, MMPCOrderNode node, MResourceType t) {
		
		Timestamp endDayTime = null;
 		// The theoretical latest time on a day, where the work ends, dependent on
		// the resource type's time slot value
		if(t.isTimeSlot()) {
 			
 			endDayTime = DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotEnd(), true);
 		}
 		else {
 			
 			endDayTime = DateTimeUtil.getDayBorder(dateTime, null, true);
 		}	
		
		// Initialize the end time to the present, if the work ends at this day. Otherwise
		// the earliest possible start time for a day is set.
		endDayTime = (endDayTime.before(node.getDateFinishSchedule())) ? endDayTime : node.getDateFinishSchedule();
		
		Timestamp startDayTime = null;
 		// The theoretical earliest time on a day, where the work begins, dependent on
		// the resource type's time slot value
		if(t.isTimeSlot()) {
 			
 			startDayTime = DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotStart(), false);
 		}
 		else {
 			
 			startDayTime = DateTimeUtil.getDayBorder(dateTime, null, false);
 		}	

		// Initialize the end time to the present, if the work begins at this day. Otherwise
		// the earliest possible start time for a day is set.
		startDayTime = (startDayTime.after(node.getDateStartSchedule())) ? startDayTime : node.getDateStartSchedule();

		return new Timestamp[] {startDayTime, endDayTime};
	}
	
	protected long calculateMillisForDay(Timestamp dateTime, MMPCOrderNode node, MResourceType t) {
		
		Timestamp[] borders = getDayBorders(dateTime, node, t);
		return DateTimeUtil.getTimeDifference(borders[0], borders[1]);
	}
	
	protected HashMap addTreeNodes(Timestamp dateTime, DefaultMutableTreeNode root, MResource r) {

		HashMap names = new HashMap();
		
		MMPCOrder[] orders = getMPCOrders(dateTime, r);
		MMPCOrderNode[] nodes = null;

		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(dateTime);
		names.put(parent, getTreeNodeRepresentation(null, parent, r));
		
		DefaultMutableTreeNode child1 = null;
		DefaultMutableTreeNode child2 = null;
		root.add(parent);

		for(int i = 0; i < orders.length; i++) {
			
			child1 = new DefaultMutableTreeNode(orders[i]);
			parent.add(child1);

			names.put(child1, getTreeNodeRepresentation(dateTime, child1, r));
			
                            /*
                             *      VIT4B 11/12/2007
                             *      
                             *      Modificaci�n para que tome la referencia a los nodos de la 
                             *      orden en cuestion, sino lo que sucede hasta ahora es que toma 
                             *      todos los nodos en el rango de la fecha usada como parametro.    
                             *  
                             */    
                            
                            //nodes = getMPCOrderNodes(dateTime, r);
                            
                            nodes = getMPCOrderNodes2(dateTime, r, orders[i]);
                            
			
			for(int j = 0; j < nodes.length; j++) {
				
				child2 = new DefaultMutableTreeNode(nodes[j]);
				child1.add(child2);

				names.put(child2, getTreeNodeRepresentation(dateTime, child2, r));
			}
		}
		
		return names;
	}

    protected String getTreeNodeRepresentation(Timestamp dateTime, DefaultMutableTreeNode node, MResource r) {
    	
        String name = null;
        if(node.getUserObject() instanceof MResource) {
        
        	MResource res = (MResource) node.getUserObject();
       	
        	name = res.getName();
        }
        else if(node.getUserObject() instanceof Timestamp) {
        	
        	Timestamp d = (Timestamp)node.getUserObject();
    		SimpleDateFormat df = Env.getLanguage(Env.getCtx()).getDateFormat();

        	name = df.format(d);
       		if(!isResourceAvailable(d, r)) {
       			
       			name = "{"+name+"}";
       		}
        }
        else if(node.getUserObject() instanceof MMPCOrder) {
        	
        	MMPCOrder o = (MMPCOrder)node.getUserObject();
        	MProduct p = new MProduct(Env.getCtx(), o.getM_Product_ID(), null);
        	
        	name = o.getDocumentNo()+" ("+p.getName()+")";
        }
        else if(node.getUserObject() instanceof MMPCOrderNode) {
        	
        	MMPCOrderNode on = (MMPCOrderNode)node.getUserObject();
        	MMPCOrderWorkflow owf = new MMPCOrderWorkflow(Env.getCtx(), on.getMPC_Order_Workflow_ID(), null);
        	MResourceType rt = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);

        	// no function
        	//Env.getLanguage(Env.getCtx()).getTimeFormat();
        	SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        	Timestamp[] interval = getDayBorders(dateTime, on, rt);
       		name = df.format(interval[0])+" - "+df.format(interval[1])+" "+on.getName()+" ("+owf.getName()+")";
       	}
        
        return name;
    }
    
	protected BigDecimal getMaxRange(MResource r) {
		
            // fjviejo e-evolution machineqty
		 //return r.getDailyCapacity().divide(r.getPercentUtillization().divide(new BigDecimal(100)));
                 return r.getDailyCapacity().multiply(r.getMachineQty()).divide(r.getPercentUtillization().divide(new BigDecimal(100)));
            //fjviejo e-evolution end
	}
    
	public CategoryDataset getDataset() {
		
		return dataset;
	}

	public JTree getTree() {
		
		return tree;
	}
}
