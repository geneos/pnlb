/** Generated Model - DO NOT CHANGE - Copyright (C) 1999-2005 Jorg Janke */
package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/**
 * Generated Model for AD_Action_Permission
 * 
 * @author BISION - Santiago Iba�ez
 * @version 1.0
 * @version 2008-09-22
 */
public class X_AD_Action_Permission extends PO {
	/** Standard Constructor */
	public X_AD_Action_Permission(Properties ctx, int AD_Action_Permission_ID,
			String trxName) {
		super(ctx, AD_Action_Permission_ID, trxName);
	}

	/** Load Constructor */
	public X_AD_Action_Permission(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** TableName=AD_Action_Permission */
	public static final String Table_Name = "AD_Action_Permission";

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
		StringBuffer sb = new StringBuffer("X_AD_Action_Permission[").append(
				get_ID()).append("]");
		return sb.toString();
	}

	/** set */
	public void setAD_Role_ID(int AD_Role_ID) {
		set_Value("AD_Role_ID", new Integer(AD_Role_ID));
	}

	/** set */
	public void setAD_Workflow_ID(int AD_Workflow_ID) {
		set_Value("AD_Workflow_ID", new Integer(AD_Workflow_ID));
	}

	/** TIPO_RET AD_Reference_ID=1000071 */
	public static final int Accion_Partidas_AD_Reference_ID = 1000071;

	/** Seleccionar_partida_existente = 1 */
	public static final int Accion_Partidas_Seleccionar_partida_existente = 1;

	/** Seleccionar_lote_existente = 2 */
	public static final int Accion_Partidas_Seleccionar_considerando_ubicacion = 2;

	/** Crear_partida_nueva = 3 */
	public static final int Accion_Partidas_Generar_Analisis = 3;

	/** Crear_lote_nuevo = 4 */
	public static final int Accion_Partidas_Generar_Lote = 4;

	/** Crear_lote_manual = 5 */
	public static final int Accion_Partidas_Generar_Lote_manual = 5;

	/** Seleccionar_partida_existente (excluyendo almacen) = 6 */
	public static final int Accion_Partidas_Seleccionar_excluyendo_Almacen = 6;

	/** Seleccionar_partida_existente (incluyendo almacen) = 7 */
	public static final int Accion_Partidas_Seleccionar_incluyendo_Almacen = 7;

	/** Seleccionar_considerando_almacen */
	public static final int Accion_Partidas_Seleccionar_considerando_Almacen_contexto = 8;

	/** set AD_Action_ID */
	public void setAction_ID(int Accion_Partidas) {
		set_Value("Action_ID", Accion_Partidas);
	}

	/** get AD_Action_ID */
	public int getAction_ID() {
		String act = (String) get_Value("ACTION_ID");
		Integer ii;
		try {
			ii = new Integer(act);
			return ii.intValue();
		} catch (Exception e) {
			return 0;
		}
		/*
		 * Integer ii = (Integer)get_Value("ACTION_ID"); if (ii == null) return
		 * 0; return ii.intValue();
		 */

	}

	/** get AD_Role_ID */
	public int getAD_Role_ID() {
		Integer ii = (Integer) get_Value("AD_Role_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** get AD_Workflow_ID */
	public int getAD_Workflow_ID() {
		Integer ii = (Integer) get_Value("AD_Workflow_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * Get Permission. Permission Identifier
	 */
	public int getAD_Action_Permission_ID() {
		Integer ii = (Integer) get_Value("AD_ACTION_PERMISSION_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/** TIPO_RET AD_Reference_ID=1000071 */
	public static final int Origen_Producto_AD_Reference_ID = 1000072;

	/** Seleccionar_partida_existente = 1 */
	public static final String Origen_Producto_Comprado = "C";

	public static final String Origen_Producto_Elaborado = "E";

	public static final String Origen_Producto_Todos = "T";

	public static final String Origen_Producto_InsumoProductivo = "P";

	public static final String Origen_Producto_InsumoNoProductivo = "N";

	public static final String Origen_Producto_ElaboradoNivel = "L";

	/** set AD_Action_ID */
	public void setTipoProducto(String Origen_Producto) {
		set_Value("TIPOPRODUCTO", Origen_Producto);
	}

	public String getTipoProducto() {
		return (String) get_Value("TIPOPRODUCTO");
	}

	public void setM_Warehouse_ID(int M_Warehouse_ID) {
		set_Value("M_Warehouse_ID", M_Warehouse_ID);
	}

	public int getM_Warehouse_ID() {
		Integer ii = (Integer) get_Value("M_Warehouse_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public int getNivel() {
		Integer ii = (Integer) get_Value("NIVEL");
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	public void setNivel(int l) {
		set_Value("NIVEL", l);
	}

	public int getAD_Table_ID() {
		String sql = "SELECT AD_Table_ID from AD_Table where name = 'AD_Action_Permission'";
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