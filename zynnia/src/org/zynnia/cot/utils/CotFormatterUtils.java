/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.cot.utils;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Alejandro Scott
 */
public final class CotFormatterUtils {

    public static String COT_SEPARATOR = "|";

    public static String getFormattedDate(Date sourceDate) {
        long time = sourceDate.getTime();
        java.util.Date dateToReturn = new java.util.Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
        String dateToReturnStr = format.format(dateToReturn);
        return dateToReturnStr;
    }

    public static String getFormattedComment(String comment, int size) {
        if (comment.length() < size) {
            return comment;
        }
        return comment.substring(0, size);
    }

    public static String getFormattedAmount(double value) {
        DecimalFormat myFormatter = new DecimalFormat("#.##");
        String output = myFormatter.format(value);
        if (!output.contains(",")) {
            output = output.concat("00");
        } else {
            output = output.replaceAll(",", "");
        }
        return output;

    }
}
