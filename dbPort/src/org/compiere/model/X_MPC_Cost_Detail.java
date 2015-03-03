/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * 
 * @author santiago
 */
public class X_MPC_Cost_Detail extends PO {
	/** Standard Constructor */
	public X_MPC_Cost_Detail(Properties ctx, int MPC_Material_Cost_Detail_ID,
			String trxName) {
		super(ctx, MPC_Material_Cost_Detail_ID, trxName);
	}

	/** Load Constructor */
	public X_MPC_Cost_Detail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Action_Permission */
	public static final String Table_Name = "MPC_COST_DETAIL";

	/** AD_Table_ID */
	public int Table_ID;

	protected KeyNamePair Model;

	/** Load Meta Data */
	protected POInfo initPO(Properties ctx) {
		POInfo info = initPO(ctx, Table_Name);
		Table_ID = info.getAD_Table_ID();
		Model = new KeyNamePair(Table_ID, Table_Name);
		return info;
	}

	protected BigDecimal accessLevel = new BigDecimal(1);

	/** AccessLevel 1 - Org */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("MPC_Cost_Detail[").append(get_ID())
				.append("]");
		return sb.toString();
	}

	public void setMPC_Order_ID(int MPC_Order_ID) {
		set_Value("MPC_Order_ID", new Integer(MPC_Order_ID));
	}

	public void setM_Product_ID(int M_Product_ID) {
		set_Value("M_Product_ID", new Integer(M_Product_ID));
	}

	public void setM_AttributeSetInstance(int M_AttributeSetInstance_ID) {
		set_Value("M_AttributeSetInstance_ID", new Integer(
				M_AttributeSetInstance_ID));
	}

	public void setCOSTO_UNITARIO(BigDecimal costo) {
		set_Value("COSTO_UNITARIO", costo);
	}

	public void setCOSTO_TOTAL(BigDecimal costo) {
		set_Value("COSTO_TOTAL", costo);
	}

	public void setCANTIDAD(BigDecimal qty) {
		set_Value("CANTIDAD", qty);
	}

	public void setMPC_Cost_Element_ID(int MPC_Cost_Element_ID) {
		set_Value("MPC_Cost_Element_ID", MPC_Cost_Element_ID);
	}

	public void setMPC_Order_Node_ID(int MPC_Order_Node) {
		set_Value("MPC_Order_Node_ID", MPC_Order_Node);
	}

	public void setC_UOM_ID(int C_UOM_ID) {
		set_Value("C_UOM_ID", C_UOM_ID);
	}

	public void setUNIDAD_RECURSO(String u) {
		set_Value("UNIDAD_RECURSO", u);
	}

	public int getMPC_Order_ID() {
		Integer i = (Integer) get_Value("MPC_Order_ID");
		return i != null ? i.intValue() : 0;
	}

	public int getM_Product_ID() {
		Integer i = (Integer) get_Value("M_Product_ID");
		return i != null ? i.intValue() : 0;
	}

	public int getM_AttributeSetInstance() {
		Integer i = (Integer) get_Value("M_AttributeSetInstance_ID");
		return i != null ? i.intValue() : 0;
	}

	public BigDecimal getCOSTO_UNITARIO() {
		BigDecimal i = (BigDecimal) get_Value("COSTO_UNITARIO");
		return i != null ? i : BigDecimal.ZERO;
	}

	public BigDecimal getCOSTO_TOTAL() {
		BigDecimal i = (BigDecimal) get_Value("COSTO_TOTAL");
		return i != null ? i : BigDecimal.ZERO;
	}

	public BigDecimal getCANTIDAD() {
		BigDecimal i = (BigDecimal) get_Value("CANTIDAD");
		return i != null ? i : BigDecimal.ZERO;
	}

	public int getMPC_Cost_Element_ID() {
		Integer i = (Integer) get_Value("MPC_Cost_Element_ID");
		return i != null ? i.intValue() : 0;
	}

	public int getMPC_Order_Node_ID() {
		Integer i = (Integer) get_Value("MPC_Order_Node_ID");
		return i != null ? i.intValue() : 0;
	}

	public int getC_UOM_ID() {
		Integer i = (Integer) get_Value("C_UOM_ID");
		return i != null ? i.intValue() : 0;
	}

	public int getMPC_Cost_Detail_ID() {
		Integer i = (Integer) get_Value("MPC_Cost_Detail_ID");
		return i != null ? i.intValue() : 0;
	}

	public String getUNIDAD_RECURSO() {
		return (String) get_Value("UNIDAD_RECURSO");
	}

	public int getAD_Table_ID() {
		String sql = "SELECT AD_Table_ID from AD_Table where name = 'MPC_COST_DETAIL'";
		PreparedStatement ps = DB.prepareStatement(sql, "TRX");
		ResultSet rs = null;
		int tableID = 0;
		try {
			rs = ps.executeQuery();
			while (rs.next())
				tableID = rs.getInt(1);
		} catch (Exception ex) {

		}
		return tableID;
	}

}
