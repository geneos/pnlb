package org.eevolution.model.wrapper;

import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.PO;
import org.eevolution.model.reasoner.StorageReasoner;

import org.eevolution.model.MMPCOrderBOM;
import org.eevolution.model.MMPCProductBOM;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class BOMWrapper extends AbstractPOWrapper {

	final public static String BOM_TYPE_PRODUCT = "productBOM";
	final public static String BOM_TYPE_ORDER = "orderBOM";
	
	public static String tableName(String type) {
		
		if(BOM_TYPE_PRODUCT.equals(type)) {
			
			return MMPCProductBOM.Table_Name;
		}
		else if(BOM_TYPE_ORDER.equals(type)) {
			
			return MMPCOrderBOM.Table_Name;
		}
		
		return "";
	}

	public static String idColumn(String type) {
		
		String value = null;
		if(BOM_TYPE_PRODUCT.equals(type)) {
			
			value = MMPCProductBOM.Table_Name;
		}
		else if(BOM_TYPE_ORDER.equals(type)) {
			
			value = MMPCOrderBOM.Table_Name;
		}
		
		return value+"_ID";
	}
	
	public BOMWrapper(Properties ctx, int id, String trxName, String type) {

		super(ctx, id, trxName, type);
	}
	
	protected PO receivePO(Properties ctx, int id, String trxName, String type) {

		PO po = null;
		if(BOM_TYPE_PRODUCT.equals(type)) {
			
			po = new MMPCProductBOM(ctx, id, trxName);
		}
		else if(BOM_TYPE_ORDER.equals(type)) {
			
			po = new MMPCOrderBOM(ctx, id, trxName);
		}
		
		return po;
	}
	
	public String getName() {
		
		String name = null;
		if(get() instanceof MMPCProductBOM) {
			
			name = ((MMPCProductBOM)get()).getName();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			name = ((MMPCOrderBOM)get()).getName();
		}
		
		return name;
	}
	
	public String getDescription() {
		
		String name = null;
		if(get() instanceof MMPCProductBOM) {
			
			name = ((MMPCProductBOM)get()).getDescription();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			name = ((MMPCOrderBOM)get()).getDescription();
		}
		
		return name;
	}

	public String getRevision() {
		
		String name = null;
		if(get() instanceof MMPCProductBOM) {
			
			name = ((MMPCProductBOM)get()).getRevision();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			name = ((MMPCOrderBOM)get()).getRevision();
		}
		
		return name;
	}
	
	public String getDocumentNo() {
		
		String value = null;
		if(get() instanceof MMPCProductBOM) {
			
			value = ((MMPCProductBOM)get()).getDocumentNo();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			value = ((MMPCOrderBOM)get()).getDocumentNo();
		}
		
		return value;
	}
	
	public int getM_Product_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOM) {
			
			id = ((MMPCProductBOM)get()).getM_Product_ID();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			id = ((MMPCOrderBOM)get()).getM_Product_ID();
		}
		
		return id;
	}

	public int getM_AttributeSetInstance_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOM) {
			
			id = ((MMPCProductBOM)get()).getM_AttributeSetInstance_ID();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			id = ((MMPCOrderBOM)get()).getM_AttributeSetInstance_ID();
		}
		
		return id;
	}

	public int getC_UOM_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOM) {
			
			id = ((MMPCProductBOM)get()).getC_UOM_ID();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			id = ((MMPCOrderBOM)get()).getC_UOM_ID();
		}
		
		return id;
	}

	public Timestamp getValidFrom() {

		Timestamp value = null;
		if(get() instanceof MMPCProductBOM) {
			
			value = ((MMPCProductBOM)get()).getValidFrom();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			value = ((MMPCOrderBOM)get()).getValidFrom();
		}
		
		return value;
	}

	public Timestamp getValidTo() {

		Timestamp value = null;
		if(get() instanceof MMPCProductBOM) {
			
			value = ((MMPCProductBOM)get()).getValidTo();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			value = ((MMPCOrderBOM)get()).getValidTo();
		}
		
		return value;
	}	
	
	public String getValue() {
		
		String value = null;
		if(get() instanceof MMPCProductBOM) {
			
			value = ((MMPCProductBOM)get()).getValue();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			value = ((MMPCOrderBOM)get()).getValue();
		}
		
		return value;
	}
	
	public String getBOMType() {
		
		String value = null;
		if(get() instanceof MMPCProductBOM) {
			
			value = ((MMPCProductBOM)get()).getBOMType();
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			value = ((MMPCOrderBOM)get()).getBOMType();
		}
		
		return value;
	}
	
	public int getMPC_Order_ID() {
		
		int id = 0;
		
		if(get() instanceof MMPCOrderBOM) {
			
			MMPCOrderBOM bom = (MMPCOrderBOM)get();
			id = bom.getMPC_Order_ID();
		}
		
		return id;
	}
	
	public BOMLineWrapper[] getLines() {
		
		int[] ids = null;

		String type = null;
		if(get() instanceof MMPCProductBOM) {
			
			type = BOM_TYPE_PRODUCT;
		}
		else if(get() instanceof MMPCOrderBOM) {
			
			type = BOM_TYPE_ORDER;
		}
		
		StorageReasoner mr = new StorageReasoner();
		ids = mr.getPOIDs(BOMLineWrapper.tableName(type), idColumn(type)+" = "+getID(), null);

		BOMLineWrapper[] lines = new BOMLineWrapper[ids.length];
		
		
		for(int i = 0; i < ids.length; i++) {
			
			lines[i] = new BOMLineWrapper(getCtx(), ids[i], null, type);
		}
		return lines;
	}
}
