package org.compiere.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Percepcion {

	List<BigDecimal> montos;
	List<BigDecimal> alicuotas;
	List<Boolean> otros;
	
	public Percepcion(){
		montos = new ArrayList<BigDecimal>();
		alicuotas = new ArrayList<BigDecimal>();
		otros = new ArrayList<Boolean>();
	}
	
	public void setPercepcion(BigDecimal monto, BigDecimal alicuota, boolean otro)
	{
		boolean is = false;
		
		for (int i=0; i<alicuotas.size(); i++)
		{
			if (alicuotas.get(i).equals(alicuota) && otros.get(i).booleanValue()==otro)
			{
				is = true;
				montos.set(i,montos.get(i).add(monto));
			}
		}
		
		if (is==false)
		{
			montos.add(monto);
			alicuotas.add(alicuota);
			otros.add(new Boolean(otro));
		}
	}
	
	public BigDecimal getTotalPercepcionIB()
	{
		BigDecimal total = BigDecimal.ZERO;
		
		for (int i=0; i<montos.size(); i++)
			total = total.add(montos.get(i));
		
		return total; 
	}
	
	public BigDecimal getMonto(int i)
	{
		return montos.get(i);
	}
	
	public BigDecimal getAlicuota(int i)
	{
		return alicuotas.get(i);
	}
	
	public boolean getOtro(int i)
	{
		return (otros.get(i)).booleanValue();
	}
	
	public int getSize()
	{
		return otros.size();
	}
}
