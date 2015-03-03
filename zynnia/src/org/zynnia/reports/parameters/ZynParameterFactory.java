/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.parameters;

import java.util.logging.Level;
import org.compiere.model.MColumn;
import org.compiere.model.MZYNMODELCOLUMN;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zynnia.reports.enums.EnumUtil;

public class ZynParameterFactory {

	private static CLogger log = CLogger.getCLogger(ZynParameterFactory.class);

	public static ZynParameter getParameterTypeInstance(int m_WindowNo, MZYNMODELCOLUMN column) {

		ParameterType type = EnumUtil.getEnum(ParameterType.class, column.getZYN_PARAMETER_TYPE_ID());
		ZynParameter ret = null;
                MColumn mcolumn = new MColumn(Env.getCtx(), column.getAD_Column_ID(), null);

		switch (type) {
			case TYPE_TEXT:
				ret = new ZynParameterText(column);
				break;
			case TYPE_DATE:
				ret = new ZynParameterDate(column);
				break;
			case TYPE_SEARCH:
				ret = new ZynParameterSearch(m_WindowNo, column);
				break;
			case TYPE_SEARCH_RANGE:
				ret = new ZynParameterSearchRange(m_WindowNo, column);
				break;
			case TYPE_DATE_RANGE:
				ret = new ZynParameterDateRange(column);
				break;
			case TYPE_LIST:
				ret = new ZynParameterList(m_WindowNo, column);
				break;
//            case TYPE_LISTFIX:
//				ret = new ZynParameterListFix(m_WindowNo, column, mcolumn);
//				break;
			case TYPE_LIST_RANGE:
				ret = new ZynParameterListRange(m_WindowNo, column);
				break;
			case TYPE_AMOUNT:
				ret = new ZynParameterAmount(column);
				break;
			case TYPE_AMOUNT_RANGE:
				ret = new ZynParameterAmountRange(column);
				break;
			case TYPE_SELECT:
				ret = new ZynParameterSelect(m_WindowNo, column);
				break;
			default:
				log.log(Level.SEVERE, "Unmapped type {0} in {1}.{2} whith id {3}", new Object[]{column.getZYN_PARAMETER_TYPE_ID(), column.getAD_TABLE_NAME(), column.getAD_COLUMN_NAME(), column.getZYN_MODEL_COLUMN_ID()});
				break;
		}
		return ret;
	}
}
