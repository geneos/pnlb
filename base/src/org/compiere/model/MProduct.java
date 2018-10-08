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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 * 	Product Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProduct.java,v 1.36 2005/11/09 17:38:39 jjanke Exp $
 */
public class MProduct extends X_M_Product {

    public static final int GRANEL = 1;
    public static final int SEMIELABORADO = 2;
    public static final int TERMINADO = 3;

    /**
     * 	Get MProduct from Cache
     *	@param ctx context
     *	@param M_Product_ID id
     *	@return MProduct
     */
    public static MProduct get(Properties ctx, int M_Product_ID) {
        Integer key = new Integer(M_Product_ID);
        MProduct retValue = (MProduct) s_cache.get(key);
        if (retValue != null) {
            return retValue;
        }
        retValue = new MProduct(ctx, M_Product_ID, null);
        if (retValue.get_ID() != 0) {
            s_cache.put(key, retValue);
        }
        return retValue;
    }	//	get

    /**
     * 	Get MProduct from Cache
     *	@param ctx context
     *	@param whereClause sql where clause
     *	@param trxName trx
     *	@return MProduct
     */
    public static MProduct[] get(Properties ctx, String whereClause, String trxName) {
        String sql = "SELECT * FROM M_Product";
        if (whereClause != null && whereClause.length() > 0) {
            sql += " WHERE AD_Client_ID=? AND " + whereClause;
        }
        ArrayList<MProduct> list = new ArrayList<MProduct>();
        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, AD_Client_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MProduct(ctx, rs, trxName));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MProduct[] retValue = new MProduct[list.size()];
        list.toArray(retValue);
        return retValue;
    }	//	get

    /**
     * 	Is Product Stocked
     *	@param M_Product_ID id
     *	@return true if found and stocked - false otherwise
     */
    public static boolean isProductStocked(Properties ctx, int M_Product_ID) {
        boolean retValue = false;
        MProduct product = get(ctx, M_Product_ID);
        return product.isStocked();
    }	//	isProductStocked
    /**	Cache						*/
    private static CCache<Integer, MProduct> s_cache = new CCache<Integer, MProduct>("M_Product", 40, 5);	//	5 minutes
    /**	Static Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MProduct.class);

    /**************************************************************************
     * 	Standard Constructor
     *	@param ctx context
     *	@param M_Product_ID id
     */
    public MProduct(Properties ctx, int M_Product_ID, String trxName) {
        super(ctx, M_Product_ID, trxName);
        if (M_Product_ID == 0) {
            //	setValue (null);
            //	setName (null);
            //	setM_Product_Category_ID (0);
            //	setC_TaxCategory_ID (0);
            //	setC_UOM_ID (0);
            //
            setProductType(PRODUCTTYPE_Item);	// I
            setIsBOM(false);	// N
            setIsInvoicePrintDetails(false);
            setIsPickListPrintDetails(false);
            setIsPurchased(true);	// Y
            setIsSold(true);	// Y
            setIsStocked(true);	// Y
            setIsSummary(false);
            setIsVerified(false);	// N
            setIsWebStoreFeatured(false);
            setIsSelfService(true);
            setIsExcludeAutoDelivery(false);
            setProcessing(false);	// N
        }
    }	//	MProduct

    /**
     * 	Load constructor
     *	@param ctx context
     *	@param rs result set
     */
    public MProduct(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }	//	MProduct

    /**
     * 	Parent Constructor
     *	@param et parent
     */
    public MProduct(MExpenseType et) {
        this(et.getCtx(), 0, et.get_TrxName());
        setProductType(MProduct.PRODUCTTYPE_ExpenseType);
        setExpenseType(et);
    }	//	MProduct

    /**
     * 	Parent Constructor
     *	@param resource parent
     */
    public MProduct(MResource resource, MResourceType resourceType) {
        this(resource.getCtx(), 0, resource.get_TrxName());
        setProductType(MProduct.PRODUCTTYPE_Resource);
        setResource(resource);
        setResource(resourceType);
    }	//	MProduct

    /**
     * 	Import Constructor
     *	@param impP import
     */
    public MProduct(X_I_Product impP) {
        this(impP.getCtx(), 0, impP.get_TrxName());
        setClientOrg(impP);
        setUpdatedBy(impP.getUpdatedBy());
        //
        setValue(impP.getValue());
        setName(impP.getName());
        setDescription(impP.getDescription());
        setDocumentNote(impP.getDocumentNote());
        setHelp(impP.getHelp());
        setUPC(impP.getUPC());
        setSKU(impP.getSKU());
        setC_UOM_ID(impP.getC_UOM_ID());
        setM_Product_Category_ID(impP.getM_Product_Category_ID());
        setProductType(impP.getProductType());
        setImageURL(impP.getImageURL());
        setDescriptionURL(impP.getDescriptionURL());
    }	//	MProduct
    /** Additional Downloads				*/
    private MProductDownload[] m_downloads = null;

    /**
     * 	Set Expense Type
     *	@param parent expense type
     *	@return true if changed
     */
    public boolean setExpenseType(MExpenseType parent) {
        boolean changed = false;
        if (!PRODUCTTYPE_ExpenseType.equals(getProductType())) {
            setProductType(PRODUCTTYPE_ExpenseType);
            changed = true;
        }
        if (parent.getS_ExpenseType_ID() != getS_ExpenseType_ID()) {
            setS_ExpenseType_ID(parent.getS_ExpenseType_ID());
            changed = true;
        }
        if (parent.isActive() != isActive()) {
            setIsActive(parent.isActive());
            changed = true;
        }
        //
        if (!parent.getValue().equals(getValue())) {
            setValue(parent.getValue());
            changed = true;
        }
        if (!parent.getName().equals(getName())) {
            setName(parent.getName());
            changed = true;
        }
        if ((parent.getDescription() == null && getDescription() != null)
                || (parent.getDescription() != null && !parent.getDescription().equals(getDescription()))) {
            setDescription(parent.getDescription());
            changed = true;
        }
        if (parent.getC_UOM_ID() != getC_UOM_ID()) {
            setC_UOM_ID(parent.getC_UOM_ID());
            changed = true;
        }
        if (parent.getM_Product_Category_ID() != getM_Product_Category_ID()) {
            setM_Product_Category_ID(parent.getM_Product_Category_ID());
            changed = true;
        }
        if (parent.getC_TaxCategory_ID() != getC_TaxCategory_ID()) {
            setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
            changed = true;
        }
        //
        return changed;
    }	//	setExpenseType

    /**
     * 	Set Resource
     *	@param parent resource
     *	@return true if changed
     */
    public boolean setResource(MResource parent) {
        boolean changed = false;
        if (!PRODUCTTYPE_Resource.equals(getProductType())) {
            setProductType(PRODUCTTYPE_Resource);
            changed = true;
        }
        if (parent.getS_Resource_ID() != getS_Resource_ID()) {
            setS_Resource_ID(parent.getS_Resource_ID());
            changed = true;
        }
        if (parent.isActive() != isActive()) {
            setIsActive(parent.isActive());
            changed = true;
        }
        //
        if (!parent.getValue().equals(getValue())) {
            setValue(parent.getValue());
            changed = true;
        }
        if (!parent.getName().equals(getName())) {
            setName(parent.getName());
            changed = true;
        }
        if ((parent.getDescription() == null && getDescription() != null)
                || (parent.getDescription() != null && !parent.getDescription().equals(getDescription()))) {
            setDescription(parent.getDescription());
            changed = true;
        }
        //
        return changed;
    }	//	setResource

    /**
     * 	Set Resource Type
     *	@param parent resource type
     *	@return true if changed
     */
    public boolean setResource(MResourceType parent) {
        boolean changed = false;
        if (PRODUCTTYPE_Resource.equals(getProductType())) {
            setProductType(PRODUCTTYPE_Resource);
            changed = true;
        }
        //
        if (parent.getC_UOM_ID() != getC_UOM_ID()) {
            setC_UOM_ID(parent.getC_UOM_ID());
            changed = true;
        }
        if (parent.getM_Product_Category_ID() != getM_Product_Category_ID()) {
            setM_Product_Category_ID(parent.getM_Product_Category_ID());
            changed = true;
        }
        if (parent.getC_TaxCategory_ID() != getC_TaxCategory_ID()) {
            setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
            changed = true;
        }
        //
        return changed;
    }	//	setResource
    /**	UOM Precision			*/
    private Integer m_precision = null;

    /**
     * 	Get UOM Standard Precision
     *	@return UOM Standard Precision
     */
    public int getStandardPrecision() {
        if (m_precision == null) {
            MUOM uom = MUOM.get(getCtx(), getC_UOM_ID());
            m_precision = new Integer(uom.getStdPrecision());
        }
        return m_precision.intValue();
    }	//	getStandardPrecision

    /**
     * 	Create Asset Group for this product
     *	@return asset group id
     */
    public int getA_Asset_Group_ID() {
        MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
        return pc.getA_Asset_Group_ID();
    }	//	getA_Asset_Group_ID

    /**
     * 	Create Asset for this product
     *	@return true if asset is created
     */
    public boolean isCreateAsset() {
        MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
        return pc.getA_Asset_Group_ID() != 0;
    }	//	isCreated

    /**
     * 	Get Attribute Set
     *	@return set or null
     */
    public MAttributeSet getAttributeSet() {
        if (getM_AttributeSet_ID() != 0) {
            return MAttributeSet.get(getCtx(), getM_AttributeSet_ID());
        }
        return null;
    }	//	getAttributeSet

    /**
     * 	Has the Product Instance Attribute
     *	@return true if instance attributes
     */
    public boolean isInstanceAttribute() {
        if (getM_AttributeSet_ID() == 0) {
            return false;
        }
        MAttributeSet mas = MAttributeSet.get(getCtx(), getM_AttributeSet_ID());
        return mas.isInstanceAttribute();
    }	//	isInstanceAttribute

    /**
     * 	Create One Asset Per UOM
     *	@return individual asset
     */
    public boolean isOneAssetPerUOM() {
        MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
        if (pc.getA_Asset_Group_ID() == 0) {
            return false;
        }
        MAssetGroup ag = MAssetGroup.get(getCtx(), pc.getA_Asset_Group_ID());
        return ag.isOneAssetPerUOM();
    }	//	isOneAssetPerUOM

    /**
     * 	Product is Item
     *	@return true if item
     */
    public boolean isItem() {
        String eltype = getProductType();
        return PRODUCTTYPE_Item.equals(getProductType());
    }	//	isItem

    /**
     * 	Product is an Item and Stocked
     *	@return true if stocked and item
     */
    public boolean isStocked() {
        return super.isStocked() && isItem();
    }	//	isStocked

    /**
     * 	Is Service
     *	@return true if service (resource, online)
     */
    public boolean isService() {
        //	PRODUCTTYPE_Service, PRODUCTTYPE_Resource, PRODUCTTYPE_Online
        return !isItem();	//	
    }	//	isService

    /**
     * 	Get UOM Symbol
     *	@return UOM Sumbol
     */
    public String getUOMSymbol() {
        return MUOM.get(getCtx(), getC_UOM_ID()).getUOMSymbol();
    }	//	getUOMSymbol

    /**
     * 	Get Active(!) Product Downloads
     *	@return array of downloads
     */
    public MProductDownload[] getProductDownloads(boolean requery) {
        if (m_downloads != null && !requery) {
            return m_downloads;
        }
        //
        ArrayList<MProductDownload> list = new ArrayList<MProductDownload>();
        String sql = "SELECT * FROM M_ProductDownload "
                + "WHERE M_Product_ID=? AND IsActive='Y' ORDER BY Name";
        //
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getM_Product_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MProductDownload(getCtx(), rs, get_TrxName()));
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
        //
        m_downloads = new MProductDownload[list.size()];
        list.toArray(m_downloads);
        return m_downloads;
    }	//	getProductDownloads

    /**
     * 	Does the product have downloads
     *	@return true if downloads exists
     */
    public boolean hasDownloads() {
        getProductDownloads(false);
        return m_downloads != null && m_downloads.length > 0;
    }	//	hasDownloads

    /**
     * 	String Representation
     *	@return info
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MProduct[");
        sb.append(get_ID()).append("-").append(getValue()).append("]");
        return sb.toString();
    }	//	toString

    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true
     */
    protected boolean beforeSave(boolean newRecord) {
        //	Check Storage
        if (!newRecord && //	
                ((is_ValueChanged("IsActive") && !isActive()) //	now not active 
                || (is_ValueChanged("IsStocked") && !isStocked()) //	now not stocked
                || (is_ValueChanged("ProductType") //	from Item
                && PRODUCTTYPE_Item.equals(get_ValueOld("ProductType"))))) {
            MStorage[] storages = MStorage.getOfProduct(getCtx(), get_ID(), get_TrxName());
            BigDecimal OnHand = Env.ZERO;
            BigDecimal Ordered = Env.ZERO;
            BigDecimal Reserved = Env.ZERO;
            for (int i = 0; i < storages.length; i++) {
                OnHand = OnHand.add(storages[i].getQtyOnHand());
                Ordered = OnHand.add(storages[i].getQtyOrdered());
                Reserved = OnHand.add(storages[i].getQtyReserved());
            }
            String errMsg = "";
            if (OnHand.signum() != 0) {
                errMsg = "@QtyOnHand@ = " + OnHand;
            }
            if (Ordered.signum() != 0) {
                errMsg += " - @QtyOrdered@ = " + Ordered;
            }
            if (Reserved.signum() != 0) {
                errMsg += " - @QtyReserved@" + Reserved;
            }
            if (errMsg.length() > 0) {
                log.saveError("Error", Msg.parseTranslation(getCtx(), errMsg));
                return false;
            }

        }

        MAttributeSet att = new MAttributeSet(getCtx(), getM_AttributeSet_ID(), get_TrxName());
        boolean check = false;
        String sql = "SELECT m_product_category_id from m_product_category where "
                + "( name like'%Padre s/hijo%' "
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
                + "or name like '%Temporal%' ) "
                + "and m_product_category_id = " + getM_Product_Category_ID();
        PreparedStatement ps = DB.prepareStatement(sql, null);
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException ex) {
        }
        /*
        if (check) {
        String err = "";
        if ( !att.isGuaranteeDate())
        err+=" El conjunto de atributos es invalido: El campo fecha de garantia es obligatorio \n";
        if ( !att.isGuaranteeDateMandatory())
        err+=" El conjunto de atributos es invalido: El campo fecha de garantia obligatoria es obligatorio \n";
        if ( att.getGuaranteeDays() == 0)
        err+=" El conjunto de atributos es invalido: El campo cantidad de dias es obligatorio \n";
        JOptionPane.showMessageDialog(null,err, "Error: El conjunto de atributos es invalido", JOptionPane.ERROR_MESSAGE);      
        }
         */

        //	Reset Stocked if not Item
        if (isStocked() && !PRODUCTTYPE_Item.equals(getProductType())) {
            setIsStocked(false);
        }

        return true;
    }	//	beforeSave

    /**
     * 	After Save
     *	@param newRecord new
     *	@param success success
     *	@return success
     */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!success) {
            return success;
        }

        //	Value/Name change in Account
        if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name"))) {
            MAccount.updateValueDescription(getCtx(), "M_Product_ID=" + getM_Product_ID(), get_TrxName());
        }

        //	Name/Description Change in Asset	MAsset.setValueNameDescription
        if (!newRecord && (is_ValueChanged("Name") || is_ValueChanged("Description"))) {
            String sql = "UPDATE A_Asset a "
                    + "SET (Name, Description)="
                    + "(SELECT SUBSTR(bp.Name || ' - ' || p.Name,1,60), p.Description "
                    + "FROM M_Product p, C_BPartner bp "
                    + "WHERE p.M_Product_ID=a.M_Product_ID AND bp.C_BPartner_ID=a.C_BPartner_ID) "
                    + "WHERE IsActive='Y'"
                    //	+ " AND GuaranteeDate > SysDate"
                    + "  AND M_Product_ID=" + getM_Product_ID();
            int no = DB.executeUpdate(sql, get_TrxName());
            log.fine("Asset Description updated #" + no);
        }

        //	New - Acct, Tree, Costing
        if (newRecord & success) {
            insert_Accounting("M_Product_Acct", "M_Product_Category_Acct",
                    "p.M_Product_Category_ID=" + getM_Product_Category_ID());
            insert_Tree(MTree_Base.TREETYPE_Product);
            MAcctSchema[] mass = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID(), get_TrxName());
            for (int i = 0; i < mass.length; i++) {
                MProductCosting pc = new MProductCosting(this, mass[i].getC_AcctSchema_ID());
                pc.save();
            }
        }

        return success;
    }	//	afterSave

    /**
     * 	Before Delete
     *	@return true if it can be deleted
     */
    protected boolean beforeDelete() {
        //	Check Storage
        if (isStocked() || PRODUCTTYPE_Item.equals(getProductType())) {
            MStorage[] storages = MStorage.getOfProduct(getCtx(), get_ID(), get_TrxName());
            BigDecimal OnHand = Env.ZERO;
            BigDecimal Ordered = Env.ZERO;
            BigDecimal Reserved = Env.ZERO;
            for (int i = 0; i < storages.length; i++) {
                OnHand = OnHand.add(storages[i].getQtyOnHand());
                Ordered = OnHand.add(storages[i].getQtyOrdered());
                Reserved = OnHand.add(storages[i].getQtyReserved());
            }
            String errMsg = "";
            if (OnHand.signum() != 0) {
                errMsg = "@QtyOnHand@ = " + OnHand;
            }
            if (Ordered.signum() != 0) {
                errMsg += " - @QtyOrdered@ = " + Ordered;
            }
            if (Reserved.signum() != 0) {
                errMsg += " - @QtyReserved@" + Reserved;
            }
            if (errMsg.length() > 0) {
                log.saveError("Error", Msg.parseTranslation(getCtx(), errMsg));
                return false;
            }

        }
        //	delete costing
        MProductCosting[] costings = MProductCosting.getOfProduct(getCtx(), get_ID(), get_TrxName());
        for (int i = 0; i < costings.length; i++) {
            costings[i].delete(true, get_TrxName());
        }
        //
        return delete_Accounting("M_Product_Acct");
    }	//	beforeDelete

    /**
     * 	After Delete
     *	@param success
     *	@return deleted
     */
    protected boolean afterDelete(boolean success) {
        if (success) {
            delete_Tree(MTree_Base.TREETYPE_Product);
        }
        return success;
    }	//	afterDelete

    public boolean isGranel() {
        if (getM_Product_Category_ID() == getCategoriaGranel()) {
            return true;
        }
        return false;
    }

    public boolean isFinal() {
        MProductCategory category = new MProductCategory(getCtx(), getM_Product_Category_ID(), get_TrxName());
        return category.isFinal();
    }

    public int getCategoriaGranel() {
        String sql =
                "SELECT m_product_category_id from M_product_category where name = 'Granel'";
        int m_product_category_id = 0;
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                m_product_category_id = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "getCategoriaGranel - " + sql, e);
        }
        return m_product_category_id;
    }
}	//	MProduct
