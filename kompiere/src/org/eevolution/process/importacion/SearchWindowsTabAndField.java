package org.eevolution.process.importacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.compiere.util.Env;

/**
 * @author daniel
 */

public class SearchWindowsTabAndField {

	private static Hashtable<String, Object> compararWindow(
			Connection cInicial, Connection cFuente, ResultSet rsInicial,
			ResultSet rsFuente) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try {

			String sql = "Select w.name, trl.NAME as TRLNAME, trl.DESCRIPTION as TRLDESCTRIPTION, "
					+ "trl.help as TRLHELP, trl.ISTRANSLATED "
					+ "From ad_window w "
					+ "LEFT JOIN ad_window_trl trl ON (w.ad_window_id = trl.ad_window_id) "
					+ "Where w.ad_window_id = ? ";
			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,	ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_Window_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();

			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_Window_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();

			rsExtFuente.next();
			rsExtInicial.next();
			/*
			 * ISACTIVE NAME DESCRIPTION HELP WINDOWTYPE ISSOTRX ENTITYTYPE
			 * PROCESSING AD_IMAGE_ID AD_COLOR_ID ISDEFAULT WINHEIGHT WINWIDTH
			 * ISBETAFUNCTIONALITY
			 */

			addToHashString(ht, "ISACTIVE", rsInicial, rsFuente);
			addToHashString(ht, "NAME", rsInicial, rsFuente);
			addToHashString(ht, "DESCRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "HELP", rsInicial, rsFuente);
			addToHashString(ht, "WINDOWTYPE", rsInicial, rsFuente);
			addToHashString(ht, "ISSOTRX", rsInicial, rsFuente);
			addToHashString(ht, "ENTITYTYPE", rsInicial, rsFuente);
			addToHashString(ht, "PROCESSING", rsInicial, rsFuente);
			addToHashString(ht, "AD_IMAGE_ID", rsInicial, rsFuente);
			addToHashString(ht, "AD_COLOR_ID", rsInicial, rsFuente);
			addToHashString(ht, "ISDEFAULT", rsInicial, rsFuente);
			addToHashString(ht, "WINHEIGHT", rsInicial, rsFuente);
			addToHashString(ht, "WINWIDTH", rsInicial, rsFuente);
			addToHashString(ht, "ISBETAFUNCTIONALITY", rsInicial, rsFuente);
			addToHashString(ht, "TRLNAME", rsExtInicial, rsExtFuente);
			addToHashString(ht, "TRLDESCTRIPTION", rsExtInicial, rsExtFuente);
			addToHashString(ht, "TRLHELP", rsExtInicial, rsExtFuente);
			addToHashString(ht, "ISTRANSLATED", rsExtInicial, rsExtFuente);

			rsExtFuente.close();
			rsExtInicial.close();
			psFuente.close();
			psInicial.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new Hashtable<String, Object>();
		}

		return ht;
	}

	protected static void addToHashString(Hashtable<String, Object> ht,
			String campo, ResultSet rsInicial, ResultSet rsFuente)
			throws Exception {
		if (rsInicial.getString(campo) != null
				&& rsFuente.getString(campo) == null)
			ht.put(campo, "");
		if ((rsInicial.getString(campo) == null && rsFuente.getString(campo) != null)
				|| (rsInicial.getString(campo) != null
						&& rsFuente.getString(campo) != null && !rsInicial
						.getString(campo).equals(rsFuente.getString(campo))))
			ht.put(campo, rsFuente.getString(campo));
	}

	protected static void addToHashInteger(Hashtable<String, Object> ht,
			String campo, ResultSet rsInicial, ResultSet rsFuente)
			throws Exception {
		if (rsInicial.getInt(campo) != rsFuente.getInt(campo))
			ht.put(campo, Integer.toString(rsFuente.getInt(campo)));
	}

	private static Hashtable<String, Object> CompareTabs(ResultSet rsInicial,
			ResultSet rsFuente) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try {

			// AD_TAB_ID
			// AD_CLIENT_ID
			// AD_ORG_ID
			// ISACTIVE CREATED
			// CREATEDBY
			// UPDATED
			// UPDATEDBY
			// NAME
			// DESCRIPTION
			// HELP
			// AD_TABLE_ID AD_WINDOW_ID
			// SEQNO
			// TABLEVEL
			// ISSINGLEROW
			// ISINFOTAB
			// ISTRANSLATIONTAB
			// ISREADONLY
			// AD_COLUMN_ID
			// HASTREE
			// WHERECLAUSE
			// ORDERBYCLAUSE
			// COMMITWARNING
			// AD_PROCESS_ID
			// PROCESSING
			// AD_IMAGE_ID
			// IMPORTFIELDS
			// AD_COLUMNSORTORDER_ID AD_COLUMNSORTYESNO_ID
			// ISSORTTAB
			// ENTITYTYPE
			// INCLUDED_TAB_ID
			// DISPLAYLOGIC
			// READONLYLOGIC
			// ISADVANCEDTAB
			// ISINSERTRECORD

			addToHashString(ht, "ISACTIVE", rsInicial, rsFuente);
			addToHashString(ht, "NAME", rsInicial, rsFuente);
			addToHashString(ht, "DESCRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "HELP", rsInicial, rsFuente);
			addToHashString(ht, "SEQNO", rsInicial, rsFuente);
			addToHashString(ht, "TABLEVEL", rsInicial, rsFuente);
			addToHashString(ht, "ISSINGLEROW", rsInicial, rsFuente);
			addToHashString(ht, "ISINFOTAB", rsInicial, rsFuente);
			addToHashString(ht, "ISTRANSLATIONTAB", rsInicial, rsFuente);
			addToHashString(ht, "ISREADONLY", rsInicial, rsFuente);
			addToHashString(ht, "HASTREE", rsInicial, rsFuente);
			addToHashString(ht, "WHERECLAUSE", rsInicial, rsFuente);
			addToHashString(ht, "ORDERBYCLAUSE", rsInicial, rsFuente);
			addToHashString(ht, "COMMITWARNING", rsInicial, rsFuente);
			addToHashString(ht, "PROCESSING", rsInicial, rsFuente);
			addToHashString(ht, "AD_IMAGE_ID", rsInicial, rsFuente);
			addToHashString(ht, "IMPORTFIELDS", rsInicial, rsFuente);
			addToHashString(ht, "ISSORTTAB", rsInicial, rsFuente);
			addToHashString(ht, "ENTITYTYPE", rsInicial, rsFuente);
			addToHashString(ht, "DISPLAYLOGIC", rsInicial, rsFuente);
			addToHashString(ht, "READONLYLOGIC", rsInicial, rsFuente);
			addToHashString(ht, "ISADVANCEDTAB", rsInicial, rsFuente);
			addToHashString(ht, "ISINSERTRECORD", rsInicial, rsFuente);

			addToHashString(ht, "TRLNAME", rsInicial, rsFuente);
			addToHashString(ht, "TRLDESCTRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "TRLHELP", rsInicial, rsFuente);
			addToHashString(ht, "ISTRANSLATED", rsInicial, rsFuente);
			addToHashString(ht, "Columname1", rsInicial, rsFuente);
			addToHashString(ht, "ColumnameSorted", rsInicial, rsFuente);
			addToHashString(ht, "ColumnameSortedYesNo", rsInicial, rsFuente);
			addToHashString(ht, "ProcesName", rsInicial, rsFuente);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new Hashtable<String, Object>();
		}
		return ht;
	}

	private static Hashtable<String, Object> compararTab(Connection cInicial,
			Connection cFuente, ResultSet rsInicial, ResultSet rsFuente) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try {

			String sql = "select adt.*, adtrl.NAME as TRLNAME, adtrl.DESCRIPTION as TRLDESCTRIPTION,"
					+ " adtrl.help as TRLHELP, adtrl.ISTRANSLATED, adc.name as Columname1,adcs.name as ColumnameSorted,"
					+ " adcsy.name as ColumnameSortedYesNo, adp.name as ProcesName, "
					+ " adTab.name as TabName"
					+ " From ad_tab adt"
					+ " LEFT JOIN ad_tab_trl adtrl ON (adtrl.ad_tab_id = adt.ad_tab_id)"
					+ " LEFT JOIN AD_TABLE ad ON (ad.AD_TABLE_ID = adt.AD_TABLE_ID)"
					+ " LEFT JOIN AD_COLUMN adc ON (adc.AD_COLUMN_ID = adt.AD_COLUMN_ID)"
					+ " LEFT JOIN AD_PROCESS adp ON (adp.AD_PROCESS_ID = adt.AD_PROCESS_ID)"
					+ " LEFT JOIN AD_COLUMN adcs ON (adcs.AD_COLUMN_ID = adt.AD_COLUMNSORTORDER_ID)"
					+ " LEFT JOIN AD_COLUMN adcsy ON (adcsy.AD_COLUMN_ID = adt.AD_COLUMNSORTYESNO_ID)"
					+ " LEFT JOIN AD_TAB adTab ON (adTab.AD_PROCESS_ID = adt.INCLUDED_TAB_ID)"
					+ " Where adt.AD_Window_ID = ? "
					+ "	AND adt.AD_Client_ID = "
					+ Env.getAD_Client_ID(Env.getCtx()) + "	Order By adt.NAME";

			PreparedStatement psFuente = cFuente
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1, rsFuente.getInt("AD_Window_ID"));
			ResultSet rsExtFuente = psFuente.executeQuery();

			PreparedStatement psInicial = cInicial
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1, rsInicial.getInt("AD_Window_ID"));
			ResultSet rsExtInicial = psInicial.executeQuery();

			boolean continuar = false;
			if (rsExtInicial.next())
				if (rsExtFuente.next())
					continuar = true;
				else
					do {
						ht.put("Tab <-- " + rsExtInicial.getString("NAME"),new Hashtable<String, Object>());
					} while (rsExtInicial.next());
			else if (rsExtFuente.next())
				do {
					ht.put("Tab --> " + rsExtFuente.getString("NAME"),new Hashtable<String, Object>());
				} while (rsExtFuente.next());

			while (continuar) {
				String rsI = rsExtInicial.getString("NAME");
				String rsF = rsExtFuente.getString("NAME");
				if (rsI.compareTo(rsF) == 0) {
					//Hashtable<String, Object> SUBht = CompareTabs(rsExtFuente,
					//		rsExtInicial);
					Hashtable<String, Object> SUBht = CompareTabs(rsExtInicial,	rsExtFuente);
					SUBht = compararField(SUBht,rsExtInicial.getInt("AD_TAB_ID"),rsExtFuente.getInt("AD_TAB_ID"), cInicial,cFuente, rsInicial, rsFuente);
					if (!SUBht.isEmpty())
						ht.put("Tab -- " + rsI, SUBht);

					rsExtFuente.next();
					rsExtInicial.next();
				}

				else if (rsI.compareTo(rsF) < 0) {
					ht.put("Tab <-- " + rsI, new Hashtable<String, Object>());
					rsExtInicial.next();
				} else {
					ht.put("Tab --> " + rsF, new Hashtable<String, Object>());
					rsExtFuente.next();
				}
				if (rsExtInicial.isAfterLast() || rsExtFuente.isAfterLast())
					continuar = false;
			}
			try	{
				if (!rsExtFuente.isAfterLast())
					do {
						ht.put("Tab --> " + rsExtFuente.getString("NAME"),new Hashtable<String, Object>());
					} while (rsExtFuente.next());
	
				else
					if (!rsExtInicial.isAfterLast())
						do {
							ht.put("Tab <-- " + rsExtInicial.getString("NAME"),new Hashtable<String, Object>());
						} while (rsExtInicial.next());
			} catch (SQLException ex) {
				return ht;
			}
			rsExtFuente.close();
			rsExtInicial.close();
			psFuente.close();
			psInicial.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return new Hashtable<String, Object>();
		}
		return ht;
	}

	private static Hashtable<String, Object> CompareFields(ResultSet rsInicial,ResultSet rsFuente) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try {
			addToHashString(ht, "ISACTIVE", rsInicial, rsFuente);
			addToHashString(ht, "NAME", rsInicial, rsFuente);
			addToHashString(ht, "DESCRIPTION", rsInicial, rsFuente);
			addToHashString(ht, "HELP", rsInicial, rsFuente);
			addToHashString(ht, "ISCENTRALLYMAINTAINED", rsInicial, rsFuente);
			addToHashString(ht, "ISDISPLAYED", rsInicial, rsFuente);
			addToHashString(ht, "DISPLAYLOGIC", rsInicial, rsFuente);
			addToHashString(ht, "DISPLAYLENGTH", rsInicial, rsFuente);
			addToHashString(ht, "ISREADONLY", rsInicial, rsFuente);
			addToHashString(ht, "SEQNO", rsInicial, rsFuente);
			addToHashString(ht, "SORTNO", rsInicial, rsFuente);
			addToHashString(ht, "ISSAMELINE", rsInicial, rsFuente);
			addToHashString(ht, "ISHEADING", rsInicial, rsFuente);
			addToHashString(ht, "ISFIELDONLY", rsInicial, rsFuente);
			addToHashString(ht, "ISENCRYPTED", rsInicial, rsFuente);
			addToHashString(ht, "ENTITYTYPE", rsInicial, rsFuente);
			addToHashString(ht, "OBSCURETYPE", rsInicial, rsFuente);
			addToHashString(ht, "TabName", rsInicial, rsFuente);
			addToHashString(ht, "ColumName", rsInicial, rsFuente);
			addToHashString(ht, "GroupName", rsInicial, rsFuente);
			addToHashString(ht, "TabName2", rsInicial, rsFuente);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new Hashtable<String, Object>();
		}
		return ht;
	}

	private static Hashtable<String, Object> compararField(Hashtable<String, Object> ht,int idTabInicial,int idTabFuente,Connection cInicial, Connection cFuente, ResultSet rsInicial,ResultSet rsFuente) {
		//Hashtable<String, Object> ht = new Hashtable<String, Object>();
		try {

			// AD_FIELD_ID
			// AD_CLIENT_ID AD_ORG_ID
			// ISACTIVE
			// CREATED CREATEDBY UPDATED UPDATEDBY
			// NAME
			// DESCRIPTION
			// HELP
			// ISCENTRALLYMAINTAINED
			// AD_TAB_ID AD_COLUMN_ID AD_FIELDGROUP_ID
			// ISDISPLAYED DISPLAYLOGIC
			// DISPLAYLENGTH
			// ISREADONLY
			// SEQNO
			// SORTNO
			// ISSAMELINE
			// ISHEADING
			// ISFIELDONLY
			// ISENCRYPTED
			// ENTITYTYPE
			// OBSCURETYPE
			// INCLUDED_TAB_ID

			String sql = " select adf.*, adftrl.NAME as TRLNAME, adftrl.DESCRIPTION as TRLDESCTRIPTION, "
					+ " adftrl.help as TRLHELP, adftrl.ISTRANSLATED, adt.name as TabName,adc.name as ColumName, "
					+ " adfg.name as GroupName,adt2.name as TabName2 "
					+ " From ad_field adf"
					+ " LEFT JOIN AD_Field_Trl adftrl ON (adftrl.ad_field_id = adf.ad_field_id) "
					+ " LEFT JOIN AD_TAB adt ON (adt.AD_TAB_ID = adf.AD_TAB_ID)"
					+ " LEFT JOIN AD_COLUMN adc ON (adc.AD_COLUMN_ID =  adf.AD_COLUMN_ID) "
					+ " LEFT JOIN ad_fieldgroup adfg ON (adfg.AD_fieldgroup_ID =  adf.AD_fieldgroup_ID) "
					+ " LEFT JOIN AD_tab adt2 ON (adt2.AD_TAB_ID = adf.INCLUDED_TAB_ID) "
					+ " where adf.ad_tab_id= ? "
					+ "	AND adt.AD_Client_ID = "
					+ Env.getAD_Client_ID(Env.getCtx()) + "	Order By adf.NAME";

			PreparedStatement psFuente = cFuente.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			psFuente.setInt(1,idTabFuente);
			ResultSet rsExtFuente = psFuente.executeQuery();

			PreparedStatement psInicial = cInicial.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			psInicial.setInt(1,idTabInicial);
			ResultSet rsExtInicial = psInicial.executeQuery();

			boolean continuar = false;
			if (rsExtInicial.next())
				if (rsExtFuente.next())
					continuar = true;
				else
					do {
						ht.put("Field <-- " + rsExtInicial.getString("NAME"),new Hashtable<String, Object>());
					} while (rsExtInicial.next());
			else if (rsExtFuente.next())
				do {
					ht.put("Field --> " + rsExtFuente.getString("NAME"),new Hashtable<String, Object>());
				} while (rsExtFuente.next());

			while (continuar) {
				String rsI = rsExtInicial.getString("NAME");
				String rsF = rsExtFuente.getString("NAME");
				if (rsI.compareTo(rsF) == 0) {
					Hashtable<String, Object> SUBht = CompareFields(rsExtInicial,rsExtFuente);
					if (!SUBht.isEmpty())
						ht.put("Field -- " + rsI, SUBht);
					rsExtFuente.next();
					rsExtInicial.next();
				} else if (rsI.compareTo(rsF) < 0) {
					ht.put("Field <-- " + rsI, new Hashtable<String, Object>());
					rsExtInicial.next();
				} else {
					ht.put("Field --> " + rsF, new Hashtable<String, Object>());
					rsExtFuente.next();
				}
				if (rsExtInicial.isAfterLast() || rsExtFuente.isAfterLast())
					continuar = false;
			}
			try	{
				if (!rsExtFuente.isAfterLast())
					do {
						ht.put("Field --> " + rsExtFuente.getString("NAME"),new Hashtable<String, Object>());
					} while (rsExtFuente.next());
				else 
					if (!rsExtInicial.isAfterLast())
						do {
							ht.put("Field <-- " + rsExtInicial.getString("NAME"),new Hashtable<String, Object>());
						} while (rsExtInicial.next());
			} catch (SQLException ex) {
				return ht;
			}

			rsExtFuente.close();
			rsExtInicial.close();
			psFuente.close();
			psInicial.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return new Hashtable<String, Object>();
		}
		return ht;
	}

	public static Hashtable procesar(Connection cInicial, Connection cFuente,
			ResultSet rsInicial, ResultSet rsFuente) {
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.putAll(compararWindow(cInicial, cFuente, rsInicial, rsFuente));
		ht.putAll(compararTab(cInicial, cFuente, rsInicial, rsFuente));
		return ht;
	}
}
