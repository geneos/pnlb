package org.eevolution.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.compiere.model.MPAYMENTVALORES;

public class ListadoCobranzasMang {

	List<String> cuentas;
	List<String> retenciones;
	List<String> monedas;
	BigDecimal efectivo = BigDecimal.ZERO;
	BigDecimal cheques = BigDecimal.ZERO;
	Hashtable<String, BigDecimal> subtotales;
	
	public ListadoCobranzasMang()	{
		cuentas = new ArrayList<String>();
		retenciones = new ArrayList<String>();
		monedas = new ArrayList<String>();
		subtotales = new Hashtable<String, BigDecimal>();
	}
	
	public static String RETENCION = "RET";
	
	public void add(String type, String name, BigDecimal monto)	{
		
		if (type.equals(MPAYMENTVALORES.EFECTIVO))
			efectivo = efectivo.add(monto);
		
		if (type.equals(MPAYMENTVALORES.CHEQUE))
			cheques = cheques.add(monto);
		
		if (type.equals(MPAYMENTVALORES.BANCO))
		{
			BigDecimal subtotal = (BigDecimal)subtotales.get(name);
			if (subtotal!=null)
				subtotales.put(name,((BigDecimal)subtotales.get(name)).add(monto));
			else
			{
				cuentas.add(name);
				subtotales.put(name,monto);
			}
		}
		
		if (type.equals(MPAYMENTVALORES.MEXT))
		{
			BigDecimal subtotal = (BigDecimal)subtotales.get(name);
			if (subtotal!=null)
				subtotales.put(name,((BigDecimal)subtotales.get(name)).add(monto));
			else
			{
				monedas.add(name);
				subtotales.put(name,monto);
			}
		}
		
		if (type.equals(RETENCION))
		{
			BigDecimal subtotal = (BigDecimal)subtotales.get(name);
			if (subtotal!=null)
				subtotales.put(name,((BigDecimal)subtotales.get(name)).add(monto));
			else
			{
				retenciones.add(name);
				subtotales.put(name,monto);
			}
		}
	}
	
	public List<ListadoCobranzasUtil> getAll()	{
		List<ListadoCobranzasUtil> totales = new ArrayList<ListadoCobranzasUtil>();
		
		ListadoCobranzasUtil elem;
		if (!efectivo.equals(BigDecimal.ZERO))
		{	elem = new ListadoCobranzasUtil("Efectivo", "", efectivo);
			totales.add(elem);
		}
		if (!cheques.equals(BigDecimal.ZERO))
		{	elem = new ListadoCobranzasUtil("Cheq. de Terc.", "", cheques);
			totales.add(elem);
		}
		
		for (int i=0; i<monedas.size();i++)
		{
			elem = new ListadoCobranzasUtil("Moneda Ext.", monedas.get(i), subtotales.get(monedas.get(i)));
			totales.add(elem);
		}
		
		for (int i=0; i<cuentas.size();i++)
		{
			elem = new ListadoCobranzasUtil("Cta. Bancaria", cuentas.get(i), subtotales.get(cuentas.get(i)));
			totales.add(elem);
		}
		
		for (int i=0; i<retenciones.size();i++)
		{
			elem = new ListadoCobranzasUtil("Reg. Especial", retenciones.get(i), subtotales.get(retenciones.get(i)));
			totales.add(elem);
		}
		
		return totales;
	}
	
}
