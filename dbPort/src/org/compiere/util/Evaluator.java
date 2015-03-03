/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial Developer of the Original Code is Jorg
 * Janke. Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s): ______________________________________.
 * ***************************************************************************
 */
package org.compiere.util;

import java.math.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;

/**
 * Expression Evaluator
 *
 * @author Jorg Janke
 * @version $Id: Evaluator.java,v 1.4 2005/12/09 05:19:09 jjanke Exp $
 */
public class Evaluator {

    /**
     * Static Logger
     */
    private static CLogger s_log = CLogger.getCLogger(Evaluator.class);

    /**
     * Check if All Variables are Defined
     *
     * @param source source
     * @param logic logic info
     * @return true if fully defined
     */
    public static boolean isAllVariablesDefined(Evaluatee source, String logic) {
        if (logic == null || logic.length() == 0) {
            return true;
        }
        //
        int pos = 0;
        while (pos < logic.length()) {
            int first = logic.indexOf('@', pos);
            if (first == -1) {
                return true;
            }
            int second = logic.indexOf('@', first + 1);
            if (second == -1) {
                s_log.severe("No second @ in Logic: " + logic);
                return false;
            }
            String variable = logic.substring(first + 1, second - 1);
            String eval = source.get_ValueAsString(variable);
            s_log.finest(variable + "=" + eval);
            if (eval == null || eval.length() == 0) {
                return false;
            }
            //
            pos = second + 1;
        }
        return true;
    }	//	isAllVariablesDefined

    /**
     * Evaluate Logic.
     * <code>
     *	format		:= <expression> [<logic> <expression>]
     *	expression	:=
     *
     * @<context>@<exLogic><value> logic	:= <|> | <&> exLogic	:= <=> | <!> | <^> | <<> | <>>
     *
     * context	:= any global or window context value	:= strings can be with ' or " logic operators	:= AND or OR with the
     * prevoius result from left to right
     *
     * Example	'
     * @AD_Table@=Test |
     * @Language@=GERGER
     * </code>
     * @param logic logic string
     * @return locic result
     */
    public static boolean evaluateLogic(Evaluatee source, String logic) {
        //	Conditional
        StringTokenizer st = new StringTokenizer(logic.trim(), "&|", true);
        int it = st.countTokens();
        if (((it / 2) - ((it + 1) / 2)) == 0) //	only uneven arguments
        {
            s_log.severe("Logic does not comply with format "
                         + "'<expression> [<logic> <expression>]' => " + logic);
            return false;
        }

        boolean retValue = evaluateLogicTuple(source, st.nextToken());
        while (st.hasMoreTokens()) {
            String logOp = st.nextToken().trim();
            boolean temp = evaluateLogicTuple(source, st.nextToken());
            if (logOp.equals("&")) {
                retValue = retValue & temp;
            } else if (logOp.equals("|")) {
                retValue = retValue | temp;
            } else {
                s_log.log(Level.SEVERE, "Logic operant '|' or '&' expected => " + logic);
                return false;
            }
        }	// hasMoreTokens
        return retValue;
    }   //  evaluateLogic

    /**
     * Evaluate
     *
     * @context@=value or
     * @context@!value or
     * @context@^value.      <pre>
     *	value: strips ' and " always (no escape or mid stream)
     *  value: can also be a context variable
     * </pre>
     * @param logic logic tuple
     * @return	true or false
     */
    private static boolean evaluateLogicTuple(Evaluatee source, String logic) {
        StringTokenizer st = new StringTokenizer(logic.trim(), "!=^><", true);
        if (!logic.contains("@SQL=")
            && st.countTokens() != 3) {
            s_log.log(Level.SEVERE, "Logic tuple does not comply with format "
                                    + "'@context@=value' where operand could be one of '=!^><' => " + logic);
            return false;
        }

        //	First Part
        String first = st.nextToken().trim();					//	get '@tag@'
        String firstEval = first.trim();
        if (first.indexOf('@') != -1) //	variable
        {
            first = first.replace('@', ' ').trim(); 			//	strip 'tag'
            firstEval = source.get_ValueAsString(first);		//	replace with it's value
        }
        firstEval = firstEval.replace('\'', ' ').replace('"', ' ').trim();	//	strip ' and "

        //	Comperator
        String operand = st.nextToken();

        //	Second Part
        String second = st.nextToken(); //	get value
        String secondEval = second.trim();
        if (second.trim().equals("@SQL") || second.trim().startsWith("@SQL")) {
            int posSqlTag = logic.indexOf("@SQL");
            posSqlTag = logic.indexOf("=", posSqlTag);

            String sql = logic.substring(posSqlTag + 1);			//	w/o tag
            sql = replaceVarsInSql(source, sql);
            return evaluateLogicListTuple(firstEval, operand, sql);

        } else if (second.indexOf('@') != -1) {		//	variable
            second = second.replace('@', ' ').trim();			// strip tag
            secondEval = source.get_ValueAsString(second);		//	replace with it's value
        }
        // For the case when second params isn't a system var an
        secondEval = secondEval.replace('\'', ' ').replace('"', ' ').trim();	//	strip ' and "

        //	Handling of ID compare (null => 0)
        if (first.indexOf("_ID") != -1 && firstEval.length() == 0) {
            firstEval = "0";
        }
        if (second.indexOf("_ID") != -1 && secondEval.length() == 0) {
            secondEval = "0";
        }

        //	Logical Comparison
        boolean result = evaluateLogicTuple(firstEval, operand, secondEval);
        //
        if (CLogMgt.isLevelFinest()) {
            s_log.log(Level.FINEST, "{0} => \"{1}\" {2} \"{3}\" => {4}", new Object[]{logic, firstEval, operand, secondEval, result});
        }
        //
        return result;
    }	//	evaluateLogicTuple

    /**
     * Evaluate Logic Tuple
     *
     * @param value1 value
     * @param operand operand = ~ ^ ! > <
     * @param value2
     * @return evaluation
     */
    private static boolean evaluateLogicListTuple(String value1, String operand, String sql) {
        if (value1 == null || operand == null || sql == null) {
            return false;
        }

        String value2 = "";
        if (sql.equals("")) {
            s_log.log(Level.SEVERE, "(ERROR) - Default logic error : {0}", sql);
        } else {
            try {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    value2 = rs.getString(1);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                s_log.log(Level.SEVERE, "(ERROR) - Default logic error: " + sql, e);
            }
        }
        return evaluateLogicTuple(value1, operand, value2);
    }	//	evaluateLogicTuple

    private static boolean evaluateLogicTuple(String value1, String operand, String value2) {
        if (value1 == null || operand == null || value2 == null) {
            return false;
        }

        BigDecimal value1bd = null;
        BigDecimal value2bd = null;
        try {
            if (!value1.startsWith("'")) {
                value1bd = new BigDecimal(value1);
            }
            if (!value2.startsWith("'")) {
                value2bd = new BigDecimal(value2);
            }
        } catch (Exception e) {
            value1bd = null;
            value2bd = null;
        }
        //
        if (operand.equals("=")) {
            if (value1bd != null && value2bd != null) {
                return value1bd.compareTo(value2bd) == 0;
            }
            return value1.compareTo(value2) == 0;
        } else if (operand.equals("<")) {
            if (value1bd != null && value2bd != null) {
                return value1bd.compareTo(value2bd) < 0;
            }
            return value1.compareTo(value2) < 0;
        } else if (operand.equals(">")) {
            if (value1bd != null && value2bd != null) {
                return value1bd.compareTo(value2bd) > 0;
            }
            return value1.compareTo(value2) > 0;
        } else //	interpreted as not
        {
            if (value1bd != null && value2bd != null) {
                return value1bd.compareTo(value2bd) != 0;
            }
            return value1.compareTo(value2) != 0;
        }
    }

    private static String replaceVarsInSql(Evaluatee source, String value) {
        if (value == null || value.length() == 0) {
            return "";
        }

		String token;
		String inStr = value;
		StringBuilder outStr = new StringBuilder();

		int i = inStr.indexOf('@');
		while (i != -1)
		{
			outStr.append(inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('@');						// next @
			if (j < 0)
			{
				s_log.log(Level.SEVERE, "No second tag: " + inStr);
				return "";						//	no second tag
			}

			token = inStr.substring(0, j).trim();
			outStr.append(source.get_ValueAsString(token));				// replace context with Context

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf('@');
		}
		outStr.append(inStr);						// add the rest of the string

		return outStr.toString();
    }
}	//	Evaluator
