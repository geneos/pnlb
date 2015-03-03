/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;
import java.util.ArrayList;
import java.util.Properties;
import org.compiere.model.X_ZYN_VIEW_CALC;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *
 * @author José Fantasia - Zynnia
 */
public class MZYNVIEWCALC extends X_ZYN_VIEW_CALC {


	public MZYNVIEWCALC(Properties ctx, int ZYN_VIEW_CALC_ID, String trxName) {
		super (ctx, ZYN_VIEW_CALC_ID, trxName);
	}


}
