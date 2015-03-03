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
package org.compiere.sqlj;

import java.math.*;
import java.sql.*;


/**
 *	SQLJ Product related Functions
 *	
 *  @author Jorg Janke
 *  @version $Id: Product.java,v 1.9 2005/10/11 02:26:14 jjanke Exp $
 */
public class Product
{
	/**
	 * 	Get Product Attribute Instance Name.
	 * 	Previously:  M_Attribute_Name - Now: productAttribute
	 * 	Test:
	 	    SELECT M_Attribute_Name (M_AttributeSetInstance_ID) 
		    FROM M_InOutLine WHERE M_AttributeSetInstance_ID > 0
		    --
		    SELECT p.Name
		    FROM C_InvoiceLine il LEFT OUTER JOIN M_Product p ON (il.M_Product_ID=p.M_Product_ID);
		    SELECT p.Name || M_Attribute_Name (il.M_AttributeSetInstance_ID) 
		    FROM C_InvoiceLine il LEFT OUTER JOIN M_Product p ON (il.M_Product_ID=p.M_Product_ID);
	 *	@param p_M_AttributeSetInstance_ID instance
	 *	@return Name or ""
	 */
	public static String attributeName (int p_M_AttributeSetInstance_ID)
		throws SQLException
	{
		if (p_M_AttributeSetInstance_ID == 0)
			return "";
		//
		StringBuffer sb = new StringBuffer();
		//	Get Base Info
		String sql = "SELECT asi.Lot, asi.SerNo, asi.GuaranteeDate "
			+ "FROM M_AttributeSetInstance asi "
			+ "WHERE asi.M_AttributeSetInstance_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_AttributeSetInstance_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			String lot = rs.getString(1);
			if (lot != null && lot.length() > 0)
				sb.append(lot).append(" ");
			String serNo = rs.getString(2);
			if (serNo != null && serNo.length() > 0)
				sb.append("#").append(serNo).append(" ");
			Date guarantee = rs.getDate(3);
			if (guarantee != null)
				sb.append(guarantee).append(" ");
		}
		rs.close();
		pstmt.close();

		//	Get Instance Info
		sql = "SELECT ai.Value, a.Name "
			+ "FROM M_AttributeInstance ai"
			+ " INNER JOIN M_Attribute a ON (ai.M_Attribute_ID=a.M_Attribute_ID AND a.IsInstanceAttribute='Y') "
			+ "WHERE ai.M_AttributeSetInstance_ID=?";
		pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_AttributeSetInstance_ID);
		rs = pstmt.executeQuery();
		while (rs.next())
		{
			sb.append(rs.getString(1))					//	value
				.append(":").append(rs.getString(2))	//	name
				.append(" ");
		}
		rs.close();
		pstmt.close();
		
		if (sb.length() == 0)
			return "";
		sb.insert(0, " (");
		sb.append(")");
		return sb.toString();
	}	//	getAttributeName

	
	/**************************************************************************
	 * 	Get BOM Price Limit
	 * 	Previously:  BOM_PriceLimit - Now: bomPriceLimit
	 *	@param p_M_Product_ID
	 *	@param p_M_PriceList_Version_ID
	 *	@return Price Limit
	 */
	public static BigDecimal bomPriceLimit (int p_M_Product_ID, int p_M_PriceList_Version_ID) 
		throws SQLException
	{
		return bomPrice(p_M_Product_ID, p_M_PriceList_Version_ID, "PriceLimit");
	}	//	bomPriceLimit
	
	/**
	 * 	Get BOM Price List
	 * 	Previously:  BOM_PriceList - Now: bomPriceList
	 *	@param p_M_Product_ID
	 *	@param p_M_PriceList_Version_ID
	 *	@return Price List
	 */
	public static BigDecimal bomPriceList (int p_M_Product_ID, int p_M_PriceList_Version_ID) 
		throws SQLException
	{
		return bomPrice(p_M_Product_ID, p_M_PriceList_Version_ID, "PriceList");
	}	//	bomPriceList
	
	/**
	 * 	Get BOM Price Std
	 * 	Previously:  BOM_PriceStd - Now: bomPriceStd
	 *	@param p_M_Product_ID
	 *	@param p_M_PriceList_Version_ID
	 *	@return Price Std
	 */
	public static BigDecimal bomPriceStd (int p_M_Product_ID, int p_M_PriceList_Version_ID) 
		throws SQLException
	{
		return bomPrice(p_M_Product_ID, p_M_PriceList_Version_ID, "PriceStd");
	}	//	bomPriceStd

	/**
	 * 	Get BOM Price
	 *	@param p_M_Product_ID
	 *	@param p_M_PriceList_Version_ID
	 *	@param p_what variable name
	 *	@return Price
	 */
	static BigDecimal bomPrice (int p_M_Product_ID, int p_M_PriceList_Version_ID, String p_what) 
		throws SQLException
	{
		BigDecimal price = null;
		//	Try to get price from PriceList directly
		String sql = "SELECT " + p_what
			+ " FROM M_ProductPrice "
			+ "WHERE M_PriceList_Version_ID=? AND M_Product_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_PriceList_Version_ID);
		pstmt.setInt(2, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			price = rs.getBigDecimal(1);
		rs.close();
		pstmt.close();
		//	Loop through BOM
		if (price == null || price.signum() == 0)
		{
			price = Compiere.ZERO;
			sql = "SELECT b.M_ProductBOM_ID, b.BOMQty, p.IsBOM "
				+ "FROM M_Product_BOM b, M_Product p "
				+ "WHERE b.M_ProductBOM_ID=p.M_Product_ID"
				+ " AND b.M_Product_ID=?";
			pstmt = Compiere.prepareStatement(sql);
			pstmt.setInt(1, p_M_Product_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int M_ProductBOM_ID = rs.getInt(1);
				BigDecimal qty = rs.getBigDecimal(2);
				BigDecimal productPrice = bomPrice(M_ProductBOM_ID, p_M_PriceList_Version_ID, p_what);
				productPrice = productPrice.multiply(qty);
				price = price.add(productPrice);
			}
			rs.close();
			pstmt.close();
		}
		return price;
	}	//	bomPrice

	
	/**************************************************************************
	 * 	Get BOM Quantity Available 
	 * 	Previously:  BOM_Qty_Available - Now: bomQtyAvailable
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@return Quantity Available
	 */
	public static BigDecimal bomQtyAvailable (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID) 
		throws SQLException
	{
		return bomQty(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOnHand")
			.subtract(bomQty(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyReserved"));
	}	//	bomQtyAvailable
	
	/**
	 * 	Get BOM Quantity OnHand 
	 * 	Previously:  BOM_Qty_OnHand - Now: bomQtyOnHand
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@return Quantity Available
	 */
	public static BigDecimal bomQtyOnHand (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID) 
		throws SQLException
	{
		return bomQty(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOnHand");
	}	//	bomQtyOnHand
        
        /**
	 * 	Get BOM Quantity OnHand 
	 * 	Previously:  BOM_Qty_OnHand - Now: bomQtyOnHand
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@return Quantity Available
	 */
	public static BigDecimal bomQtyOnHandActive (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID) 
		throws SQLException
	{
		return bomQtyActive(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOnHand");
	}	//	bomQtyOnHand
	
	/**
	 * 	Get BOM Quantity Ordered 
	 * 	Previously:  BOM_Qty_Ordered - Now: bomQtyOrdered
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@return Quantity Ordered
	 */
	public static BigDecimal bomQtyOrdered (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID) 
		throws SQLException
	{
		return bomQty(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOrdered");
	}	//	bomQtyOrdered
	
	/**
	 * 	Get BOM Quantity Reserved 
	 * 	Previously:  BOM_Qty_Reserved - Now: bomQtyReserved
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@return Qyantity Reserved
	 */
	public static BigDecimal bomQtyReserved (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID) 
		throws SQLException
	{
		return bomQty(p_M_Product_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyReserved");
	}	//	bomQtyReserved
	
	/**
	 * 	Get BOM Quantity
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return Quantity
	 */
	static BigDecimal bomQty (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID, String p_what) 
		throws SQLException
	{
		//	Check Parameters
		int M_Warehouse_ID = p_M_Warehouse_ID;
		if (M_Warehouse_ID == 0)
		{
			if (p_M_Locator_ID == 0)
				return Compiere.ZERO;
			else
			{
				String sql = "SELECT M_Warehouse_ID "
					+ "FROM M_Locator "
					+ "WHERE M_Locator_ID=?";
				M_Warehouse_ID = Compiere.getSQLValue(sql, p_M_Locator_ID);
			}
		}
		if (M_Warehouse_ID == 0)
			return Compiere.ZERO;
		
		//	Check, if product exists and if it is stocked
		boolean isBOM = false;
		String ProductType = null;
		boolean isStocked = false;
		String sql = "SELECT IsBOM, ProductType, IsStocked "
			+ "FROM M_Product "
			+ "WHERE M_Product_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			isBOM = "Y".equals(rs.getString(1));
			ProductType = rs.getString(2);
			isStocked = "Y".equals(rs.getString(3));
		}
		rs.close();
		pstmt.close();
		//	No Product
		if (ProductType == null)
			return Compiere.ZERO;
		//	Unlimited capacity if no item
		if (!isBOM && (!ProductType.equals("I") || !isStocked))
			return UNLIMITED;
		//	Get Qty
		if (isStocked)
			return getStorageQty(p_M_Product_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
		
		//	Go through BOM
		BigDecimal quantity = UNLIMITED;
		BigDecimal productQuantity = null;
		sql = "SELECT b.M_ProductBOM_ID, b.BOMQty, p.IsBOM, p.IsStocked, p.ProductType "
			+ "FROM M_Product_BOM b, M_Product p "
			+ "WHERE b.M_ProductBOM_ID=p.M_Product_ID"
			+ " AND b.M_Product_ID=?";
		pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		rs = pstmt.executeQuery();
		while (rs.next())
		{
			int M_ProductBOM_ID = rs.getInt(1);
			BigDecimal bomQty = rs.getBigDecimal(2);
			isBOM = "Y".equals(rs.getString(3));
			isStocked = "Y".equals(rs.getString(4)); 
			ProductType = rs.getString(5);
			
			//	Stocked Items "leaf node"
			if (ProductType.equals("I") && isStocked)
			{
				//	Get ProductQty
				productQuantity = getStorageQty(M_ProductBOM_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	Get Rounding Precision
				int uomPrecision = getUOMPrecision(M_ProductBOM_ID);
				//	How much can we make with this product
				productQuantity = productQuantity.setScale(uomPrecision)
					.divide(bomQty, uomPrecision, BigDecimal.ROUND_HALF_UP);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
			else if (isBOM)	//	Another BOM
			{
				productQuantity = bomQty (M_ProductBOM_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
		}
		rs.close();
		pstmt.close();
		
		if (quantity.signum() != 0)
		{
			int uomPrecision = getUOMPrecision(p_M_Product_ID);
			return quantity.setScale(uomPrecision, BigDecimal.ROUND_HALF_UP);
		}
		return Compiere.ZERO;
	}	//	bomQtyOnHand
        
        /**
	 * 	Get BOM Quantity
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return Quantity
	 */
	static BigDecimal bomQtyActive (int p_M_Product_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID, String p_what) 
		throws SQLException
	{
		//	Check Parameters
		int M_Warehouse_ID = p_M_Warehouse_ID;
		if (M_Warehouse_ID == 0)
		{
			if (p_M_Locator_ID == 0)
				return Compiere.ZERO;
			else
			{
				String sql = "SELECT M_Warehouse_ID "
					+ "FROM M_Locator "
					+ "WHERE M_Locator_ID=?";
				M_Warehouse_ID = Compiere.getSQLValue(sql, p_M_Locator_ID);
			}
		}
		if (M_Warehouse_ID == 0)
			return Compiere.ZERO;
		
		//	Check, if product exists and if it is stocked
		boolean isBOM = false;
		String ProductType = null;
		boolean isStocked = false;
		String sql = "SELECT IsBOM, ProductType, IsStocked "
			+ "FROM M_Product "
			+ "WHERE M_Product_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			isBOM = "Y".equals(rs.getString(1));
			ProductType = rs.getString(2);
			isStocked = "Y".equals(rs.getString(3));
		}
		rs.close();
		pstmt.close();
		//	No Product
		if (ProductType == null)
			return Compiere.ZERO;
		//	Unlimited capacity if no item
		if (!isBOM && (!ProductType.equals("I") || !isStocked))
			return UNLIMITED;
		//	Get Qty
		if (isStocked)
			return getStorageQtyActive(p_M_Product_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
		
		//	Go through BOM
		BigDecimal quantity = UNLIMITED;
		BigDecimal productQuantity = null;
		sql = "SELECT b.M_ProductBOM_ID, b.BOMQty, p.IsBOM, p.IsStocked, p.ProductType "
			+ "FROM M_Product_BOM b, M_Product p "
			+ "WHERE b.M_ProductBOM_ID=p.M_Product_ID"
			+ " AND b.M_Product_ID=?";
		pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		rs = pstmt.executeQuery();
		while (rs.next())
		{
			int M_ProductBOM_ID = rs.getInt(1);
			BigDecimal bomQty = rs.getBigDecimal(2);
			isBOM = "Y".equals(rs.getString(3));
			isStocked = "Y".equals(rs.getString(4)); 
			ProductType = rs.getString(5);
			
			//	Stocked Items "leaf node"
			if (ProductType.equals("I") && isStocked)
			{
				//	Get ProductQty
				productQuantity = getStorageQtyActive(M_ProductBOM_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	Get Rounding Precision
				int uomPrecision = getUOMPrecision(M_ProductBOM_ID);
				//	How much can we make with this product
				productQuantity = productQuantity.setScale(uomPrecision)
					.divide(bomQty, uomPrecision, BigDecimal.ROUND_HALF_UP);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
			else if (isBOM)	//	Another BOM
			{
				productQuantity = bomQty (M_ProductBOM_ID, M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
		}
		rs.close();
		pstmt.close();
		
		if (quantity.signum() != 0)
		{
			int uomPrecision = getUOMPrecision(p_M_Product_ID);
			return quantity.setScale(uomPrecision, BigDecimal.ROUND_HALF_UP);
		}
		return Compiere.ZERO;
	}	//	bomQtyOnHand
	
	/** Unlimited Quantity			*/
	private static final BigDecimal UNLIMITED = new BigDecimal((double)99999.0);
	
	/**
	 * 	Get Storage Qty
	 *	@param p_M_Product_ID product
	 *	@param M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return quantity or zero
	 *	@throws SQLException
	 */
	static BigDecimal getStorageQty (int p_M_Product_ID, 
		int M_Warehouse_ID, int p_M_Locator_ID, String p_what)
		throws SQLException
	{
		BigDecimal quantity = null;
		String sql = "SELECT SUM(" + p_what + ") "
			+ "FROM M_Storage s "
			+ "WHERE M_Product_ID=?";
                if (p_M_Locator_ID != 0)
			sql += " AND s.M_Locator_ID=?";
		else
			sql += " AND EXISTS (SELECT * FROM M_Locator l WHERE s.M_Locator_ID=l.M_Locator_ID"
				+ " AND l.M_Warehouse_ID=?)";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		if (p_M_Locator_ID != 0)
			pstmt.setInt(2, p_M_Locator_ID);
		else
			pstmt.setInt(2, M_Warehouse_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			quantity = rs.getBigDecimal(1);
		rs.close();
		pstmt.close();
		//	Not found
		if (quantity == null)
			return Compiere.ZERO;
		return quantity;
	}	//	getStorageQty
        
        /**
	 * 	Get Storage Qty
	 *	@param p_M_Product_ID product
	 *	@param M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return quantity or zero
	 *	@throws SQLException
	 */
	static BigDecimal getStorageQtyActive (int p_M_Product_ID, 
		int M_Warehouse_ID, int p_M_Locator_ID, String p_what)
		throws SQLException
	{
		BigDecimal quantity = null;
		String sql = "SELECT SUM(" + p_what + ") "
			+ "FROM M_Storage s "
			+ "WHERE M_Product_ID=?";
		sql +=" AND s.isactive = 'Y'";
                if (p_M_Locator_ID != 0)
			sql += " AND s.M_Locator_ID=?";
		else
			sql += " AND EXISTS (SELECT * FROM M_Locator l WHERE s.M_Locator_ID=l.M_Locator_ID"
				+ " AND l.M_Warehouse_ID=?)";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		if (p_M_Locator_ID != 0)
			pstmt.setInt(2, p_M_Locator_ID);
		else
			pstmt.setInt(2, M_Warehouse_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			quantity = rs.getBigDecimal(1);
		rs.close();
		pstmt.close();
		//	Not found
		if (quantity == null)
			return Compiere.ZERO;
		return quantity;
	}	//	getStorageQty
	
	/**
	 * 	Get UOM Precision for Product
	 *	@param p_M_Product_ID product
	 *	@return precision or 0
	 */
	static int getUOMPrecision (int p_M_Product_ID) throws SQLException
	{
		int precision = 0;
		String sql = "SELECT u.StdPrecision "
			+ "FROM C_UOM u"
			+ " INNER JOIN M_Product p ON (u.C_UOM_ID=p.C_UOM_ID) "
			+ "WHERE p.M_Product_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			precision = rs.getInt(1);
		rs.close();
		pstmt.close();
		return precision;
	}	//	getStdPrecision

	/**
	 * 	Test
	 *	@param args
	 *
	public static void main (String[] args)
	{
		
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Compiere.s_type = Compiere.TYPE_ORACLE;
			Compiere.s_url = "jdbc:oracle:thin:@//dev1:1521/dev1.compiere.org";
			Compiere.s_uid = "compiere";
			Compiere.s_pwd = "compiere";
	//		System.out.println(Product.bomQtyOnHand(p_M_Product_ID, 0, p_M_Locator_ID));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}	//	main	/* */
	
	//begin vpj-cd e-evolution 18 ENE 2006
	
	public static BigDecimal bomQtyAvailable (int p_M_Product_ID, int p_M_AttributeSetInstance_ID, 
			int p_M_Warehouse_ID, int p_M_Locator_ID) 
			throws SQLException
		{
			return bomQtyOnHand(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID)
			.subtract(bomQtyReserved(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID));
		}	//	bomQtyAvailable
	
	public static BigDecimal bomQtyOnHand (int p_M_Product_ID, int p_M_AttributeSetInstance_ID, 
			int p_M_Warehouse_ID, int p_M_Locator_ID) 
			throws SQLException
		{
			return bomQty(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOnHand");
		}	//	bomQtyOnHand
	
	public static BigDecimal bomQtyOrdered (int p_M_Product_ID, int p_M_AttributeSetInstance_ID,
			int p_M_Warehouse_ID, int p_M_Locator_ID) 
			throws SQLException
		{
			return bomQty(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyOrdered");
		}	//	bomQtyOrdered
		
	public static BigDecimal bomQtyReserved (int p_M_Product_ID, int p_M_AttributeSetInstance_ID, 
			int p_M_Warehouse_ID, int p_M_Locator_ID) 
			throws SQLException
    {
		return bomQty(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, "QtyReserved");
	}	//	bomQtyReserved
	
	/**
	 * 	Get BOM Quantity
	 *	@param p_M_Product_ID product
	 *	@param p_M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return Quantity
	 */
	static BigDecimal bomQty (int p_M_Product_ID, int p_M_AttributeSetInstance_ID, 
		int p_M_Warehouse_ID, int p_M_Locator_ID, String p_what) 
		throws SQLException
	{
		//	Check Parameters
		/*
		int M_Warehouse_ID = p_M_Warehouse_ID;
		if (M_Warehouse_ID == 0)
		{
			if (p_M_Locator_ID == 0)
				return Compiere.ZERO;
			else
			{
				String sql = "SELECT M_Warehouse_ID "
					+ "FROM M_Locator "
					+ "WHERE M_Locator_ID=" + p_M_Locator_ID;
				M_Warehouse_ID = Compiere.getSQLValue(sql, p_M_Locator_ID);
			}
		}
		if (M_Warehouse_ID == 0)
			return Compiere.ZERO;
		*/
		//	Check, if product exists and if it is stocked
		boolean isBOM = false;
		String ProductType = null;
		boolean isStocked = false;
		String sql = "SELECT IsBOM, ProductType, IsStocked "
			+ "FROM M_Product "
			+ "WHERE M_Product_ID=?";
		PreparedStatement pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			isBOM = "Y".equals(rs.getString(1));
			ProductType = rs.getString(2);
			isStocked = "Y".equals(rs.getString(3));
		}
		rs.close();
		pstmt.close();
		//	No Product
		if (ProductType == null)
			return Compiere.ZERO;
		//	Unlimited capacity if no item
		if (!isBOM && (!ProductType.equals("I") || !isStocked))
			return UNLIMITED;
		//	Get Qty
		if (isStocked) {
			
			return getStorageQty(p_M_Product_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, p_what);
		}
		//	Go through BOM
		BigDecimal quantity = UNLIMITED;
		BigDecimal productQuantity = null;
		sql = "SELECT b.M_ProductBOM_ID, b.BOMQty, p.IsBOM, p.IsStocked, p.ProductType "
			+ "FROM M_Product_BOM b, M_Product p "
			+ "WHERE b.M_ProductBOM_ID=p.M_Product_ID"
			+ " AND b.M_Product_ID=?";
		pstmt = Compiere.prepareStatement(sql);
		pstmt.setInt(1, p_M_Product_ID);
		rs = pstmt.executeQuery();
		while (rs.next())
		{
			int M_ProductBOM_ID = rs.getInt(1);
			BigDecimal bomQty = rs.getBigDecimal(2);
			isBOM = "Y".equals(rs.getString(3));
			isStocked = "Y".equals(rs.getString(4)); 
			ProductType = rs.getString(5);
			
			//	Stocked Items "leaf node"
			if (ProductType.equals("I") && isStocked)
			{
				//	Get ProductQty
				productQuantity = getStorageQty(M_ProductBOM_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	Get Rounding Precision
				int StdPrecision = getUOMPrecision(M_ProductBOM_ID);
				//	How much can we make with this product
				productQuantity = productQuantity.setScale(StdPrecision)
					.divide(bomQty, BigDecimal.ROUND_HALF_UP);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
			else if (isBOM)	//	Another BOM
			{
				productQuantity = bomQty (M_ProductBOM_ID, p_M_AttributeSetInstance_ID, p_M_Warehouse_ID, p_M_Locator_ID, p_what);
				//	How much can we make overall
				if (productQuantity.compareTo(quantity) < 0)
					quantity = productQuantity;
			}
		}
		rs.close();
		pstmt.close();
		
		if (quantity.signum() > 0)
		{
			int StdPrecision = getUOMPrecision(p_M_Product_ID);
			return quantity.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		}
		return Compiere.ZERO;
	}	//	bomQtyOnHand
	
	/**
	 * 	Get Storage Qty
	 *	@param p_M_Product_ID product
	 *	@param M_Warehouse_ID warehouse
	 *	@param p_M_Locator_ID locator
	 *	@param p_what variable name
	 *	@return quantity or zero
	 *	@throws SQLException
	 */
	static BigDecimal getStorageQty (int p_M_Product_ID, int p_M_AttributeSetInstance_ID,
			int M_Warehouse_ID, int p_M_Locator_ID, String p_what)
			throws SQLException
		{
			BigDecimal quantity = null;

			String sql = "SELECT SUM(" + p_what + ") "
						+ "FROM M_Storage s "
						+ "WHERE M_Product_ID=?";
                        if(p_M_AttributeSetInstance_ID != 0) {
				sql +=" AND s.M_AttributeSetInstance_ID = ?";
			}
			if (p_M_Locator_ID != 0) {
				sql += " AND s.M_Locator_ID=?";
			}
			else if(M_Warehouse_ID != 0) {
				sql += " AND EXISTS (SELECT * FROM M_Locator l WHERE s.M_Locator_ID=l.M_Locator_ID"
					+ " AND l.M_Warehouse_ID=?)";
			}
			
			int index=1;
			PreparedStatement pstmt = Compiere.prepareStatement(sql);
			pstmt.setInt(index++, p_M_Product_ID);
			if(p_M_AttributeSetInstance_ID != 0) {
				pstmt.setInt(index++, p_M_AttributeSetInstance_ID);
			}
			if (p_M_Locator_ID != 0) {
				pstmt.setInt(index++, p_M_Locator_ID);
			}
			else if(M_Warehouse_ID != 0) {
				pstmt.setInt(index++, M_Warehouse_ID);
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				quantity = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();
			//	Not found
			if (quantity == null)
				return Compiere.ZERO;
			return quantity;
		}	//	getStorageQty	
	//end vpj-cd e-evolution 18 ENE 2006

}	//	Product
