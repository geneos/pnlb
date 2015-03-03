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

import org.compiere.util.*;

/**
 * 	BOM Product/Component Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MBOMProduct.java,v 1.3 2005/05/18 05:57:27 jjanke Exp $
 */
public class MBOMProduct extends X_M_BOMProduct
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_BOMProduct_ID id
	 *	@param trxName trx
	 */
	public MBOMProduct (Properties ctx, int M_BOMProduct_ID, String trxName)
	{
		super (ctx, M_BOMProduct_ID, trxName);
		if (M_BOMProduct_ID == 0)
		{
		//	setM_BOM_ID (0);
			setBOMProductType (BOMPRODUCTTYPE_StandardProduct);	// S
			setBOMQty (Env.ONE);
			setIsPhantom (false);
			setLeadTimeOffset (0);
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_BOMProduct WHERE M_BOM_ID=@M_BOM_ID@
		}
	}	//	MBOMProduct

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MBOMProduct (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MBOMProduct

	/**	BOM Parent				*/
	private MBOM		m_bom = null;
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	private MBOM getBOM()
	{
		if (m_bom == null && getM_BOM_ID() != 0)
			m_bom = MBOM.get(getCtx(), getM_BOM_ID());
		return m_bom;
	}	//	getBOM
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Product
		if (getBOMProductType().equals(BOMPRODUCTTYPE_OutsideProcessing))
		{
			if (getM_ProductBOM_ID() != 0)
				setM_ProductBOM_ID(0);
		}
		else if (getM_ProductBOM_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @M_ProductBOM_ID@"));
			return false;
		}
		//	Operation
		if (getM_ProductOperation_ID() == 0)
		{
			if (getSeqNo() != 0)
				setSeqNo(0);
		}
		else if (getSeqNo() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @SeqNo@"));
			return false;
		}
		//	Product Attribute Instance
		if (getM_AttributeSetInstance_ID() != 0)
		{
			getBOM();
			if (m_bom != null 
				&& MBOM.BOMTYPE_Make_To_Order.equals(m_bom.getBOMType()))
				;
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), 
					"Reset @M_AttributeSetInstance_ID@: Not Make-to-Order"));
				setM_AttributeSetInstance_ID(0);
				return false;
			}
		}
		//	Alternate
		if ((getBOMProductType().equals(BOMPRODUCTTYPE_Alternative)
			|| getBOMProductType().equals(BOMPRODUCTTYPE_AlternativeDefault))
			&& getM_BOMAlternative_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @M_BOMAlternative_ID@"));
			return false;
		}
		//	Operation
		if (getM_ProductOperation_ID() != 0)
		{
			if (getSeqNo() == 0)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @SeqNo@"));
				return false;
			}
		}
		else	//	no op
		{
			if (getSeqNo() != 0)
				setSeqNo(0);
			if (getLeadTimeOffset() != 0)
				setLeadTimeOffset(0);
		}
		
		//	Set Line Number
		if (getLine() == 0)
		{
			String sql = "SELECT NVL(MAX(Line),0)+10 FROM M_BOMProduct WHERE M_BOM_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getM_BOM_ID());
			setLine (ii);
		}

		return true;
	}	//	beforeSave
	
	
}	//	MBOMProduct
