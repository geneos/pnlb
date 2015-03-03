/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zynnia.reports.enums;

import java.util.ArrayList;
import java.util.List;

public class EnumUtil {

	public static <I extends IdEnum> I getEnum(Class<I> type, int id) {
		I[] types = type.getEnumConstants();
		for (I t : types) {
			if (t.getId() == id) {
				return t;
			}
		}
		throw new AssertionError("Unmapped id: " + id);
	}

	public static <I extends IdEnum> List<I> getEnumList(Class<I> type, List<Integer> ids) {
		List<I> result = new ArrayList<I>();
		if (ids != null) {
			for (Integer id : ids) {
				result.add(getEnum(type, id));
			}
		}
		return result;
	}
}