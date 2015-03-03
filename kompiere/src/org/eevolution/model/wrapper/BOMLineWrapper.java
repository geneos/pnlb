package org.eevolution.model.wrapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.PO;

import org.eevolution.model.MMPCOrderBOMLine;
import org.eevolution.model.MMPCProductBOMLine;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class BOMLineWrapper extends AbstractPOWrapper {

	public static String tableName(String type) {
		
		if(BOMWrapper.BOM_TYPE_PRODUCT.equals(type)) {
			
			return MMPCProductBOMLine.Table_Name;
		}
		else if(BOMWrapper.BOM_TYPE_ORDER.equals(type)) {
			
			return MMPCOrderBOMLine.Table_Name;
		}
		
		return "";
	}
	
	public static String idColumn(String type) {
		
		return tableName(type)+"_ID";
	}
	
	public BOMLineWrapper(Properties ctx, int id, String trxName, String type) {

		super(ctx, id, trxName, type);
	}
	
	protected PO receivePO(Properties ctx, int id, String trxName, String type) {

		PO po = null;
		if(BOMWrapper.BOM_TYPE_PRODUCT.equals(type)) {
			
			po = new MMPCProductBOMLine(ctx, id, trxName);
		}
		else if(BOMWrapper.BOM_TYPE_ORDER.equals(type)) {
			
			po = new MMPCOrderBOMLine(ctx, id, trxName);
		}
		
		return po;
	}
	
	public String getComponentType() {
		
		String type = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			type = ((MMPCProductBOMLine)get()).getComponentType();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			type = ((MMPCOrderBOMLine)get()).getComponentType();
		}
		
		return type;
	}

	public BigDecimal getAssay() {
		
		BigDecimal assay = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			assay = ((MMPCProductBOMLine)get()).getAssay();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			assay = ((MMPCOrderBOMLine)get()).getAssay();
		}
		
		return assay;
	}
        
        public int getM_ChangeNotice_ID() {
		
		int M_ChangeNotice_ID = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			M_ChangeNotice_ID = ((MMPCProductBOMLine)get()).getM_ChangeNotice_ID();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			M_ChangeNotice_ID = ((MMPCOrderBOMLine)get()).getM_ChangeNotice_ID();
		}
		
		return M_ChangeNotice_ID;
	}
        
        public String getHelp() {
		
		String Help = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			Help = ((MMPCProductBOMLine)get()).getHelp();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			Help = ((MMPCOrderBOMLine)get()).getHelp();
		}
		
		return Help;
	}


	public BigDecimal getQtyBatch() {
		
		BigDecimal qty = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			qty = ((MMPCProductBOMLine)get()).getQtyBatch();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			qty = ((MMPCOrderBOMLine)get()).getQtyBatch();
		}
		
		return qty;
	}

	public BigDecimal getForecast() {
		
		BigDecimal fc = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			fc = ((MMPCProductBOMLine)get()).getForecast();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			fc = ((MMPCOrderBOMLine)get()).getForecast();
		}
		
		return fc;
	}

	public Integer getLeadTimeOffset() {
		
		Integer offset = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			offset = ((MMPCProductBOMLine)get()).getLeadTimeOffset();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			offset = ((MMPCOrderBOMLine)get()).getLeadTimeOffset();
		}
		
		return offset;
	}

	public boolean isQtyPercentage() {
		
		boolean percentage = false;
		if(get() instanceof MMPCProductBOMLine) {
			
			percentage = ((MMPCProductBOMLine)get()).isQtyPercentage();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			percentage = ((MMPCOrderBOMLine)get()).isQtyPercentage();
		}
		
		return percentage;
	}

	public boolean isCritical() {
		
		boolean critical = false;
		if(get() instanceof MMPCProductBOMLine) {
			
			critical = ((MMPCProductBOMLine)get()).isCritical();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			critical = ((MMPCOrderBOMLine)get()).isCritical();
		}
		
		return critical;
	}
	
	public String getIssueMethod() {
		
		String issue = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			issue = ((MMPCProductBOMLine)get()).getIssueMethod();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			issue = ((MMPCOrderBOMLine)get()).getIssueMethod();
		}
		
		return issue;
	}

	public int getLine() {
		
		int line = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			line = ((MMPCProductBOMLine)get()).getLine();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			line = ((MMPCOrderBOMLine)get()).getLine();
		}
		
		return line;
	}
	
	public String getDescription() {
		
		String type = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			type = ((MMPCProductBOMLine)get()).getDescription();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			type = ((MMPCOrderBOMLine)get()).getDescription();
		}
		
		return type;
	}
	
	public int getM_Product_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			id = ((MMPCProductBOMLine)get()).getM_Product_ID();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			id = ((MMPCOrderBOMLine)get()).getM_Product_ID();
		}
		
		return id;
	}

	public int getMPC_Order_ID() {
		
		int id = 0;
		
		if(get() instanceof MMPCOrderBOMLine) {
			
			MMPCOrderBOMLine line = (MMPCOrderBOMLine)get();
			id = line.getMPC_Order_ID();
		}
		
		return id;
	}

	public int getMPC_BOM_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			id = ((MMPCProductBOMLine)get()).getMPC_Product_BOM_ID();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			id = ((MMPCOrderBOMLine)get()).getMPC_Order_BOM_ID();
		}
		
		return id;
	}

	public int getM_AttributeSetInstance_ID() {

		int id = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			id = ((MMPCProductBOMLine)get()).getM_AttributeSetInstance_ID();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			id = ((MMPCOrderBOMLine)get()).getM_AttributeSetInstance_ID();
		}
		
		return id;
	}

	public void setM_AttributeSetInstance_ID(int id) {

		if(get() instanceof MMPCProductBOMLine) {
			
			((MMPCProductBOMLine)get()).setM_AttributeSetInstance_ID(id);
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			((MMPCOrderBOMLine)get()).setM_AttributeSetInstance_ID(id);
		}
	}	
	
	public void setQtyBOM(BigDecimal qty) {

		if(get() instanceof MMPCProductBOMLine) {
			
			((MMPCProductBOMLine)get()).setQtyBOM(qty);
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			((MMPCOrderBOMLine)get()).setQtyBOM(qty);
		}
	}

	public BigDecimal getQtyBOM() {

		BigDecimal value = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getQtyBOM();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getQtyBOM();
		}
		
		return value;
	}

	public int getC_UOM_ID() {

		int value = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getC_UOM_ID();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getC_UOM_ID();
		}
		
		return value;
	}	
	
	public int getPo() {

		int value = 0;
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getLine();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getLine();
		}
		
		return value;
	}
	
	public BigDecimal getScrap() {

		BigDecimal value = new BigDecimal(0);
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getScrap();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getScrap();
		}
		
		return value;
	}
	
	public Timestamp getValidFrom() {

		Timestamp value = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getValidFrom();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getValidFrom();
		}
		
		return value;
	}

	public Timestamp getValidTo() {

		Timestamp value = null;
		if(get() instanceof MMPCProductBOMLine) {
			
			value = ((MMPCProductBOMLine)get()).getValidTo();
		}
		else if(get() instanceof MMPCOrderBOMLine) {
			
			value = ((MMPCOrderBOMLine)get()).getValidTo();
		}
		
		return value;
	}	
}
