package org.zynnia.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Vector;
import org.compiere.util.NamePair;

/**
 *  This class allows comparation between two Vectors, based in only one field. 
 * 
 *  @author Ezequiel Scott at Zynnia
 */
public class RowComparator implements Comparator {

    private int index = -1;
    private boolean asc = true;
    private int multiplier = 1;
    
    /*
     *  Construct RowComparator based on the field given by columnIndex. Default order is ascending.
     *  @param columnIndex   the index for based comparation
     */
    public RowComparator(int columnIndex){
        index = columnIndex;
    }
    
    /*
     *  Construct RowComparator based on the field given by columnIndex in the given order.
     *  @param columnIndex   the index for based comparation
     *  @param asc   boolean that determine ascending order
     */
    public RowComparator(int columnIndex, boolean asc){
        index = columnIndex;
        this.asc = asc;
        if (!asc)
            multiplier = -1;
    }
    
    /*
     *  Compare to Object, in this case Objects are row as Vectors
     *  @param o1    a row to compare as Vector
     *  @param o2    a row to compare as Vector
     */
    public int compare(Object o1, Object o2) {
        Object cmp1 = ((Vector) o1).elementAt(index);
        Object cmp2 = ((Vector) o2).elementAt(index);      
        
        //	Comparing Null values
        if (cmp1 == null)
            cmp1 = new String("");
        if (cmp2 == null)
            cmp2 = new String("");
        
        /**
		 *	compare different data types
		 */

        //	Get Objects to compare
		if (cmp1 instanceof NamePair)
			cmp1 = ((NamePair)cmp1).getName();
		if (cmp2 instanceof NamePair)
			cmp2 = ((NamePair)cmp2).getName();
        // System.out.println("Comparing " + cmp1 + " " + cmp2 );
        
		//	String
		if (cmp1 instanceof String && cmp2 instanceof String)
		{
			String s = (String)cmp1;
			return s.compareTo((String)cmp2) * multiplier;
		}
		//	Date
		else if (cmp1 instanceof Timestamp && cmp2 instanceof Timestamp)
		{
			Timestamp t = (Timestamp)cmp1;
			return t.compareTo((Timestamp)cmp2) * multiplier;
		}
		//	BigDecimal
		else if (cmp1 instanceof BigDecimal && cmp2 instanceof BigDecimal)
		{
			BigDecimal d = (BigDecimal)cmp1;
			return d.compareTo((BigDecimal)cmp2) * multiplier;
		}
		//	Integer
		else if (cmp1 instanceof Integer && cmp2 instanceof Integer)
		{
			Integer d = (Integer)cmp1;
			return d.compareTo((Integer)cmp2) * multiplier;
		}

		//  String value
		String s = cmp1.toString();
		return s.compareTo(cmp2.toString()) * multiplier;
    }
    
}
