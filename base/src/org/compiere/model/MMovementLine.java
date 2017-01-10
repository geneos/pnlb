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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;

/**
 *	Inventory Move Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLine.java,v 1.2 2006/11/21 18:42:23 SIGArg-01 Exp $
 */
public class MMovementLine extends X_M_MovementLine
{
	/**
	 * 	Standard Cosntructor
	 *	@param ctx context
	 *	@param M_MovementLine_ID id
	 */
	public MMovementLine (Properties ctx, int M_MovementLine_ID, String trxName)
	{
		super (ctx, M_MovementLine_ID, trxName);
		if (M_MovementLine_ID == 0)
		{
		//	setM_LocatorTo_ID (0);	// @M_LocatorTo_ID@
		//	setM_Locator_ID (0);	// @M_Locator_ID@
		//	setM_MovementLine_ID (0);			
		//	setLine (0);	
		//	setM_Product_ID (0);
			setM_AttributeSetInstance_ID(0);	//	ID
			setMovementQty (Env.ZERO);	// 1
			setTargetQty (Env.ZERO);	// 0
			setScrappedQty(Env.ZERO);
			setConfirmedQty(Env.ZERO);
			setProcessed (false);
		}	
	}	//	MMovementLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MMovementLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MMovementLine

	/**
	 * 	Parent constructor
	 *	@param parent parent
	 */
	public MMovementLine (MMovement parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_Movement_ID(parent.getM_Movement_ID());
	}	//	MMovementLine
	
	/**
	 * 	Get AttributeSetInstance To
	 *	@return ASI
	 */
	public int getM_AttributeSetInstanceTo_ID ()
	{
		int M_AttributeSetInstanceTo_ID = super.getM_AttributeSetInstanceTo_ID();
		if (M_AttributeSetInstanceTo_ID == 0)
			M_AttributeSetInstanceTo_ID = super.getM_AttributeSetInstance_ID();
		return M_AttributeSetInstanceTo_ID;
	}	//	getM_AttributeSetInstanceTo_ID
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Set Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_MovementLine WHERE M_Movement_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getM_Movement_ID());
			setLine (ii);
		}
		
		if (getM_Locator_ID() == getM_LocatorTo_ID())
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@M_Locator_ID@ == @M_LocatorTo_ID@"));
			return false;
		}

		if (getMovementQty().signum() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "MovementQty"));
			return false;
		}


                MMovement move = new MMovement (getCtx(), getM_Movement_ID(), get_TrxName());
                MMovementLine line = new MMovementLine(move);

                //int attributeSetInstance_ID = line.getM_AttributeSetInstance_ID();
                int attributeSetInstance_ID = getM_AttributeSetInstance_ID();

                
                /*
                 *  Vit4b - 05/07/2007
                 *  Correccio�n para manejar el caso de productos que no tienen instancias y
                 *  debe controlarse el stock de movimiento. 
                 *
                 *
                 */
                

                int product_ID = this.getM_Product_ID();
                MProduct product = MProduct.get(getCtx(),product_ID);
                
                int isM_AttributeSet = product.getM_AttributeSet_ID();
                if(isM_AttributeSet == 0)
                {
                    
                    
                    String sql = "SELECT QTYONHAND FROM M_STORAGE WHERE M_PRODUCT_ID = " + product_ID + " AND M_LOCATOR_ID = " + getM_Locator_ID() + " AND M_ATTRIBUTESETINSTANCE_ID <> 0";
                    int qty = 0;

                    try {
                        PreparedStatement pstmt1 = DB.prepareStatement(sql,get_TrxName());
                        ResultSet rs1 = pstmt1.executeQuery();

                        while(rs1.next())
                        {
                            qty += rs1.getFloat(1);          
                        }
                        rs1.close();
                        pstmt1.close();
                    } catch(SQLException obl) {
                    }
                    
                    
                    float qtyMovement = getMovementQty().floatValue();
                    
                    
                                       
                                        
                    if(qty < qtyMovement)
                    {
                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Cantidad superior a la existencia"), "Info" , JOptionPane.INFORMATION_MESSAGE);    
                        // Vit4B - 14/06/2007
                        // Agregado para que no deje guardar la linea en el caso que la cantidad sea mayor a la existente
                        //

                        return false;
                    }

                    
                
                }
                else
                {
                
                
                
                    String sql = "SELECT QTYONHAND FROM M_STORAGE WHERE M_PRODUCT_ID = " + getM_Product_ID() + " AND M_ATTRIBUTESETINSTANCE_ID = " + attributeSetInstance_ID + " AND M_LOCATOR_ID = " + getM_Locator_ID();
                    BigDecimal qty = Env.ZERO;

                    try {
                        PreparedStatement pstmt1 = DB.prepareStatement(sql,get_TrxName());
                        ResultSet rs1 = pstmt1.executeQuery();

                        if(rs1.next())
                        {
                            qty = rs1.getBigDecimal(1);
                        }
                        rs1.close();
                        pstmt1.close();
                    } catch(SQLException obl) {
                    }


                    BigDecimal qtyMovement = getMovementQty();


                    if(qty.subtract(qtyMovement).compareTo(Env.ZERO) == -1)
                    {
                        JOptionPane.showMessageDialog(null,Msg.getMsg(Env.getCtx(),"Cantidad superior a la existencia"), "Info" , JOptionPane.INFORMATION_MESSAGE);    
                        // Vit4B - 14/06/2007
                        // Agregado para que no deje guardar la linea en el caso que la cantidad sea mayor a la existente
                        //

                        return false;
                    }

                
                
                }

                /** BISion - 05/06/2009 - Santiago Ibañez
                 * Se chequea que esta linea tenga la misma ubicacion de
                 * deposito que la primer linea del movimiento
                 */
                 //Obtengo el movimiento al que pertenece esta linea
                /*MMovement mov = new MMovement(p_ctx, this.getM_Movement_ID(), get_TrxName());
                //obtengo las lineas
                MMovementLine[] lineas = mov.getLines(true);
                for (int i=0;i<lineas.length;i++){
                    //Busco la primer linea activa que haya
                    if (!lineas[i].isActive())
                        continue;
                    //el deposito de esta linea (que no sea la primera) debe coincidir con el de la primer linea activa
                    if (lineas.length >0 && lineas[i].getM_LocatorTo_ID()!= this.getM_LocatorTo_ID()){
                       //Si esta NO es la primer linea activa
                       if (lineas[i].getM_MovementLine_ID()!=this.getM_MovementLine_ID()){
                            JOptionPane.showMessageDialog(null,"Todas las ubicaciones destino de las lineas deben coincidir en el almacen destino de la primer linea.", "Info" , JOptionPane.INFORMATION_MESSAGE);
                            return false;
                       }
                       //Si esta es la primer linea activa y ademas existen otras líneas
                       else if (lineas.length>1){
                           JOptionPane.showMessageDialog(null,"No se puede cambiar la ubicacion destino de la primer linea activa ya que hay otras lineas en este movimiento.", "Info" , JOptionPane.INFORMATION_MESSAGE);
                           return false;
                       }
                    }
                }*/
                //fin modificacion BISion
                
		return true;
	}	//	beforeSave

	
}	//	MMovementLine
