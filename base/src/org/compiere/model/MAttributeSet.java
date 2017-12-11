/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *  Product Attribute Set
 *
 *	@author Jorg Janke
 *	@version $Id: MAttributeSet.java,v 1.13 2005/12/17 19:55:33 jjanke Exp $
 */
public class MAttributeSet extends X_M_AttributeSet {

    /**
     * 	Get MAttributeSet from Cache
     *	@param ctx context
     *	@param M_AttributeSet_ID id
     *	@return MAttributeSet
     */
    public static MAttributeSet get(Properties ctx, int M_AttributeSet_ID) {
        Integer key = new Integer(M_AttributeSet_ID);
        MAttributeSet retValue = (MAttributeSet) s_cache.get(key);
        if (retValue != null) {
            return retValue;
        }
        retValue = new MAttributeSet(ctx, M_AttributeSet_ID, null);
        if (retValue.get_ID() != 0) {
            s_cache.put(key, retValue);
        }
        return retValue;
    }	//	get
    /**	Cache						*/
    private static CCache<Integer, MAttributeSet> s_cache = new CCache<Integer, MAttributeSet>("M_AttributeSet", 20);

    /**
     * 	Standard constructor
     *	@param ctx context
     *	@param M_AttributeSet_ID id
     */
    public MAttributeSet(Properties ctx, int M_AttributeSet_ID, String trxName) {
        super(ctx, M_AttributeSet_ID, trxName);
        if (M_AttributeSet_ID == 0) {
            //	setName (null);
            setIsGuaranteeDate(false);
            setIsGuaranteeDateMandatory(false);
            setIsLot(false);
            setIsLotMandatory(false);
            setIsSerNo(false);
            setIsSerNoMandatory(false);
            setIsInstanceAttribute(false);
            setMandatoryType(MANDATORYTYPE_NotMandatary);
        }
    }	//	MAttributeSet

    /**
     * 	Load constructor
     *	@param ctx context
     *	@param rs result set
     */
    public MAttributeSet(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MAttributeSet
    /**	Instance Attributes					*/
    private MAttribute[] m_instanceAttributes = null;
    /**	Instance Attributes					*/
    private MAttribute[] m_productAttributes = null;
    /** Entry Exclude						*/
    private X_M_AttributeSetExclude[] m_excludes = null;
    /** Lot create Exclude						*/
    private X_M_LotCtlExclude[] m_excludeLots = null;
    /** Serial No create Exclude				*/
    private X_M_SerNoCtlExclude[] m_excludeSerNos = null;

    /**
     * 	Get Attribute Array
     * 	@param instanceAttributes true if for instance
     *	@return instance or product attribute array
     */
    public MAttribute[] getMAttributes(boolean instanceAttributes) {
        if ((m_instanceAttributes == null && instanceAttributes)
                || m_productAttributes == null && !instanceAttributes) {
            String sql = "SELECT mau.M_Attribute_ID "
                    + "FROM M_AttributeUse mau"
                    + " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
                    + "WHERE mau.IsActive='Y' AND ma.IsActive='Y'"
                    + " AND mau.M_AttributeSet_ID=? AND ma.IsInstanceAttribute=? " //	#1,2
                    + "ORDER BY mau.SeqNo";
            ArrayList<MAttribute> list = new ArrayList<MAttribute>();
            PreparedStatement pstmt = null;
            try {
                pstmt = DB.prepareStatement(sql, get_TrxName());
                pstmt.setInt(1, getM_AttributeSet_ID());
                pstmt.setString(2, instanceAttributes ? "Y" : "N");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    MAttribute ma = new MAttribute(getCtx(), rs.getInt(1), get_TrxName());
                    list.add(ma);
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (SQLException ex) {
                log.log(Level.SEVERE, sql, ex);
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex1) {
            }
            pstmt = null;

            //	Differentiate attributes
            if (instanceAttributes) {
                m_instanceAttributes = new MAttribute[list.size()];
                list.toArray(m_instanceAttributes);
            } else {
                m_productAttributes = new MAttribute[list.size()];
                list.toArray(m_productAttributes);
            }
        }
        //
        if (instanceAttributes) {
            if (isInstanceAttribute() != m_instanceAttributes.length > 0) {
                setIsInstanceAttribute(m_instanceAttributes.length > 0);
            }
        }

        //	Return
        if (instanceAttributes) {
            return m_instanceAttributes;
        }
        return m_productAttributes;
    }	//	getMAttributes

    /**
     * 	Something is Mandatory
     *	@return true if something is mandatory
     */
    public boolean isMandatory() {
        return !MANDATORYTYPE_NotMandatary.equals(getMandatoryType())
                || isLotMandatory()
                || isSerNoMandatory()
                || isGuaranteeDateMandatory();
    }	//	isMandatory

    /**
     * 	Is always mandatory
     *	@return mandatory 
     */
    public boolean isMandatoryAlways() {
        return MANDATORYTYPE_AlwaysMandatory.equals(getMandatoryType());
    }	//	isMandatoryAlways

    /**
     * 	Is Mandatory when Shipping
     *	@return true if required for shipping
     */
    public boolean isMandatoryShipping() {
        return MANDATORYTYPE_WhenShipping.equals(getMandatoryType());
    }	//	isMandatoryShipping

    /**
     * 	Exclude entry
     *	@param AD_Column_ID column
     *	@param isSOTrx sales order
     *	@return true if excluded
     */
    public boolean excludeEntry(int AD_Column_ID, boolean isSOTrx) {
        if (m_excludes == null) {
            ArrayList<X_M_AttributeSetExclude> list = new ArrayList<X_M_AttributeSetExclude>();
            String sql = "SELECT * FROM M_AttributeSetExclude WHERE IsActive='Y' AND M_AttributeSet_ID=?";
            PreparedStatement pstmt = null;
            try {
                pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, getM_AttributeSet_ID());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(new X_M_AttributeSetExclude(getCtx(), rs, null));
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (Exception e) {
                log.log(Level.SEVERE, sql, e);
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                pstmt = null;
            }
            m_excludes = new X_M_AttributeSetExclude[list.size()];
            list.toArray(m_excludes);
        }
        //	Find it
        if (m_excludes != null && m_excludes.length > 0) {
            M_Column column = M_Column.get(getCtx(), AD_Column_ID);
            for (int i = 0; i < m_excludes.length; i++) {
                if (m_excludes[i].getAD_Table_ID() == column.getAD_Table_ID()
                        && m_excludes[i].isSOTrx() == isSOTrx) {
                    return true;
                }
            }
        }
        return false;
    }	//	excludeEntry

    /**
     * 	Exclude Lot creation
     *	@param AD_Column_ID column
     *	@param isSOTrx SO
     *	@return true if excluded
     */
    public boolean isExcludeLot(int AD_Column_ID, boolean isSOTrx) {
        if (getM_LotCtl_ID() == 0) {
            return true;
        }
        if (m_excludeLots == null) {
            ArrayList<X_M_LotCtlExclude> list = new ArrayList<X_M_LotCtlExclude>();
            String sql = "SELECT * FROM M_LotCtlExclude WHERE IsActive='Y' AND M_LotCtl_ID=?";
            PreparedStatement pstmt = null;
            try {
                pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, getM_LotCtl_ID());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(new X_M_LotCtlExclude(getCtx(), rs, null));
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (Exception e) {
                log.log(Level.SEVERE, sql, e);
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                pstmt = null;
            }
            m_excludeLots = new X_M_LotCtlExclude[list.size()];
            list.toArray(m_excludeLots);
        }
        //	Find it
        if (m_excludeLots != null && m_excludeLots.length > 0) {
            M_Column column = M_Column.get(getCtx(), AD_Column_ID);
            for (int i = 0; i < m_excludeLots.length; i++) {
                if (m_excludeLots[i].getAD_Table_ID() == column.getAD_Table_ID()
                        && m_excludeLots[i].isSOTrx() == isSOTrx) {
                    return true;
                }
            }
        }
        return false;
    }	//	isExcludeLot

    /**
     *	Exclude SerNo creation
     *	@param AD_Column_ID column
     *	@param isSOTrx SO
     *	@return true if excluded
     */
    public boolean isExcludeSerNo(int AD_Column_ID, boolean isSOTrx) {
        if (getM_SerNoCtl_ID() == 0) {
            return true;
        }
        if (m_excludeSerNos == null) {
            ArrayList<X_M_SerNoCtlExclude> list = new ArrayList<X_M_SerNoCtlExclude>();
            String sql = "SELECT * FROM M_SerNoCtlExclude WHERE IsActive='Y' AND M_SerNoCtl_ID=?";
            PreparedStatement pstmt = null;
            try {
                pstmt = DB.prepareStatement(sql, null);
                pstmt.setInt(1, getM_SerNoCtl_ID());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(new X_M_SerNoCtlExclude(getCtx(), rs, null));
                }
                rs.close();
                pstmt.close();
                pstmt = null;
            } catch (Exception e) {
                log.log(Level.SEVERE, sql, e);
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (Exception e) {
                pstmt = null;
            }
            m_excludeSerNos = new X_M_SerNoCtlExclude[list.size()];
            list.toArray(m_excludeSerNos);
        }
        //	Find it
        if (m_excludeSerNos != null && m_excludeSerNos.length > 0) {
            M_Column column = M_Column.get(getCtx(), AD_Column_ID);
            for (int i = 0; i < m_excludeSerNos.length; i++) {
                if (m_excludeSerNos[i].getAD_Table_ID() == column.getAD_Table_ID()
                        && m_excludeSerNos[i].isSOTrx() == isSOTrx) {
                    return true;
                }
            }
        }
        return false;
    }	//	isExcludeSerNo

    /**
     * 	Get Lot Char Start
     *	@return defined or \u00ab 
     */
    public String getLotCharStart() {
        String s = super.getLotCharSOverwrite();
        if (s != null && s.length() == 1 && !s.equals(" ")) {
            return s;
        }
        return "\u00ab";
    }	//	getLotCharStart

    /**
     * 	Get Lot Char End
     *	@return defined or \u00bb 
     */
    public String getLotCharEnd() {
        String s = super.getLotCharEOverwrite();
        if (s != null && s.length() == 1 && !s.equals(" ")) {
            return s;
        }
        return "\u00bb";
    }	//	getLotCharEnd

    /**
     * 	Get SerNo Char Start
     *	@return defined or #
     */
    public String getSerNoCharStart() {
        String s = super.getSerNoCharSOverwrite();
        if (s != null && s.length() == 1 && !s.equals(" ")) {
            return s;
        }
        return "#";
    }	//	getSerNoCharStart

    /**
     * 	Get SerNo Char End
     *	@return defined or none
     */
    public String getSerNoCharEnd() {
        String s = super.getSerNoCharEOverwrite();
        if (s != null && s.length() == 1 && !s.equals(" ")) {
            return s;
        }
        return "";
    }	//	getSerNoCharEnd

    /**
     * 	Before Save.
     * 	- set instance attribute flag
     *	@param newRecord new
     *	@return true
     */
    protected boolean beforeSave(boolean newRecord) {
        if (!isInstanceAttribute()
                && (isSerNo() || isLot() || isGuaranteeDate())) {
            setIsInstanceAttribute(true);
        }

        boolean check = false;
        String sql = "select 1 FROM  M_PRODUCT p where p.m_attributeset_id = " + getM_AttributeSet_ID()
                + " and p.m_product_category_id IN"
                + "(SELECT m_product_category_id from m_product_category where "
                + "name like'%Padre s/hijo%' "
                + "or name like 'Original' "
                + "or name like 'Granel' "
                + "or name like 'Producto Envasado' "
                + "or name like '%Material de Acondicionamiento%' "
                + "or name like '%Principio Activo%' "
                + "or name like '%Material de Empaque%' "
                + "or name like '%Material de Llenado%' "
                + "or name like '%FOLIA%' "
                + "or name like '%Excipientes%' "
                + "or name like '%Muestra%' "
                + "or name like '%Temporal%' )";
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException ex) {
        }

        if (check) {
            String err = "";
            if (!isGuaranteeDate()) {
                err += " El campo fecha de garantia es obligatorio \n";
            }
            if (!isGuaranteeDateMandatory()) {
                err += " El campo fecha de garantia obligatoria es obligatorio \n";
            }
            if (getGuaranteeDays() == 0) {
                err += " El campo cantidad de dias es obligatorio \n";
            }
            JOptionPane.showMessageDialog(null, err, "Error: Campos Obligatorios", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }	//	beforeSave

    /**
     * 	After Save.
     * 	- Verify Instance Attribute
     *	@param newRecord new
     *	@param success success
     *	@return success
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        //	Set Instance Attribute
        if (!isInstanceAttribute()) {
            String sql = "UPDATE M_AttributeSet mas"
                    + " SET IsInstanceAttribute='Y' "
                    + "WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID()
                    + " AND IsInstanceAttribute='N'"
                    + " AND (IsSerNo='Y' OR IsLot='Y' OR IsGuaranteeDate='Y'"
                    + " OR EXISTS (SELECT * FROM M_AttributeUse mau"
                    + " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
                    + "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
                    + " AND mau.IsActive='Y' AND ma.IsActive='Y'"
                    + " AND ma.IsInstanceAttribute='Y')"
                    + ")";
            int no = DB.executeUpdate(sql, get_TrxName());
            if (no != 0) {
                log.warning("Set Instance Attribute");
                setIsInstanceAttribute(true);
            }
        }
        //	Reset Instance Attribute
        if (isInstanceAttribute() && !isSerNo() && !isLot() && !isGuaranteeDate()) {
            String sql = "UPDATE M_AttributeSet mas"
                    + " SET IsInstanceAttribute='N' "
                    + "WHERE M_AttributeSet_ID=" + getM_AttributeSet_ID()
                    + " AND IsInstanceAttribute='Y'"
                    + "	AND IsSerNo='N' AND IsLot='N' AND IsGuaranteeDate='N'"
                    + " AND NOT EXISTS (SELECT * FROM M_AttributeUse mau"
                    + " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
                    + "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
                    + " AND mau.IsActive='Y' AND ma.IsActive='Y'"
                    + " AND ma.IsInstanceAttribute='Y')";
            int no = DB.executeUpdate(sql, get_TrxName());
            if (no != 0) {
                log.warning("Reset Instance Attribute");
                setIsInstanceAttribute(false);
            }
        }
        return success;
    }	//	afterSave
}	//	MAttributeSet
