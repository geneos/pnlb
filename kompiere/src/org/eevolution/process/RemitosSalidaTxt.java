package org.eevolution.process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class RemitosSalidaTxt extends SvrProcess {

	private Timestamp p_DateValue;
	
	private BigDecimal p_PuntoVenta = new BigDecimal(0);

	private final String siglas = new String("PAN");

	private final int lenghtsiglas = 3;

	private String nroRemito = null;

	private static int lenghtnroRemito = 10;

	private String categoriaCliente = null;

	private final int lenghtcategoriaCliente = 2;

	private String clienteDeLaboratorio = null;

	private final int lenghtclienteDeLaboratorio = 10;

	private String nroTransferencia = null;

	private final int lenghtnroTransferencia = 6;

	private String anioAprobacion = null;

	private final int lenghtanioAprobacion = 4;

	private String mesAprobacion = null;

	private final int lenghtmesAprobcion = 2;

	private String diaAprobacion = null;

	private final int lenghtdiaAprobacion = 2;

	private String fechaPactada = null;

	private final int lenghtfechaPactada = 8;

	private String localidadDestino = null;

	private final int lenghtlocalidadDestino = 25;

	private String domicilio1 = null;

	private final int lenghtDomicilio1 = 25;

	private String domicilio2 = null;

	private final int lenghtDomicilio2 = 25;

	private String  nroPuerta = null;

	private final int lenghtNroPuerta = 6;

	private String piso = null;

	private final int lenghtPiso = 2;

	private String cp = null;

	private final int lenghtCP = 6;

	private String codigoMovLaboratorio = null;

	private final int lenghtcodigoMovLaboratorio = 3;

	private String marcaGestionCobranza;

	private final int lenghtmarcaGestionCobranza = 1;

	private String valorGestionCobranza;

	private final int lenghtValorGestionCobranza = 15;

	private String items = null;

	private final int lenghtItems = 3;

	private String cantUnidades = null;

	private final int lenghtCantUnidades = 7;

	private String zonaCliente = null;

	private final int lenghtZonaCliente = 4;

	private String razonSocial = null;

	private final int lenghtRazonSocial = 40;

	private String obserRto = null;

	private final int lenghtObserRto = 30;

	private String obserRt1 = null;

	private final int lenghtObserRt1 = 30;

	private String obserRt2 = null;

	private final int lenghtObserRt2 = 30;

	private String cporig = null;

	private final int lenghtCporig = 4;

	private String valorFactura = null;

	private final int lenghtValorFactura = 9;

	private String subCuenta = null;

	private final int lenghtSubCuenta = 4;

	private String sucursalCliente = null;

	private final int lenghtSucursalCliente = 3;

	private String nroCompDelPedido = null;

	private final int lenghtNroCompDelPedido = 8;

	private String categoriaPedido = null;

	private final int lenghtCategoriaPedido = 2;

	private int m_inout_id = 0;

	String fecha;

	private final String nombreArchivo = "Remito-Cabecera-";

	/**
	 * Prepare - e.g., get Parameters.
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("Datetrx"))
				p_DateValue = (Timestamp) para[i].getParameter();
			else if (name.equals("C_DocType_ID"))
				p_PuntoVenta = (BigDecimal) para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		inicializarArchivo();

	}

	/**
	 * 
	 * @param ts
	 * @return
	 */
	private String getFechaFromTimeStamp(Timestamp ts, String separador) {
		Calendar gc = new GregorianCalendar();
		gc.setTimeInMillis(ts.getTime());
		String fecha = "";
		int mes = gc.get(Calendar.MONTH) + 1;
		if (gc.get(Calendar.DAY_OF_MONTH) < 10)
			fecha = "0" + gc.get(Calendar.DAY_OF_MONTH);
		else
			fecha += gc.get(Calendar.DAY_OF_MONTH);
		fecha += separador;
		if (mes < 10)
			fecha += "0" + mes;
		else
			fecha += mes;
		fecha += separador;
		fecha += gc.get(Calendar.YEAR);
		return fecha;
	}

	/**
	 * Perrform process.
	 * 
	 * @return Message (clear text)
	 * @throws Exception
	 *             if not successful
	 */
	protected String doIt() throws Exception {

		String sql = "select * from RV_M_INOUT_HEADER_SALIDA_TXT "
				+ "where  MOVEMENTDATE  =  to_date('"
				+ getFechaFromTimeStamp(p_DateValue, "/") + "', 'dd,mm,yyyy') "
				+ "and C_DOCTYPE_ID = " + p_PuntoVenta.intValue();

		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			limpiarVariables();
			m_inout_id = rs.getInt("M_INOUT_ID");
			nroRemito = new String(rs.getString("DOCUMENTNO"));
			if (rs.getString("LOCALIDADDESTINO") != null)
				localidadDestino = new String(rs.getString("LOCALIDADDESTINO"));
			if (rs.getString("DOMICILIO") != null)
				domicilio1 = new String(rs.getString("DOMICILIO"));
			if (rs.getString("CP") != null)
				cp = new String(rs.getString("CP"));
			if (rs.getString("VALUE") != null)
				nroTransferencia = rs.getString("VALUE");
			if (rs.getString("RAZONSOCIAL") != null)
				razonSocial = new String(rs.getString("RAZONSOCIAL"));
			calcularLineas(m_inout_id);
			crearArchivoSocios();
		}
		rs.close();
		ps.close();

		return "ok";

	}

	private void limpiarVariables() {
		// TODO Auto-generated method stub

		nroRemito = null;

		categoriaCliente = null;
		
		clienteDeLaboratorio = null;

		nroTransferencia = null;

		anioAprobacion = null;

		mesAprobacion = null;

		diaAprobacion = null;

		fechaPactada = null;

		localidadDestino = null;

		domicilio1 = null;

		domicilio2 = null;

		nroPuerta=null;

		piso = null;;

		cp = null;

		codigoMovLaboratorio = null;;

		marcaGestionCobranza = null;
		
		valorGestionCobranza = null;

		items = null;

		cantUnidades = null;

		zonaCliente = null;

		razonSocial = null;

		obserRto = null;

		obserRt1 = null;

		obserRt2 = null;

		cporig = null;

		valorFactura = null;

		subCuenta = null;

		sucursalCliente = null;
		
		nroCompDelPedido = null;

		categoriaPedido = null;

		m_inout_id = 0;

	}

	/**
	 * Metodo que calcula las lineas del remito pasado como parametro y suma las
	 * cantidades de cada linea
	 * 
	 * @param m_inout_id
	 *            Identificador del remito a buscar
	 * @throws Exception
	 */
	public void calcularLineas(int m_inout_id) throws Exception {
		try {
			String sqlCant = "select count(*) as CANTITEMS,sum(mil.qtyentered) as CANTUNITS "
					+ "from M_InOut mio "
					+ "join M_InOutLine MIL on (mio.m_inout_id = mil.m_inout_id ) "
					+ "where mio.m_inout_id=? ";
			PreparedStatement psCant = DB.prepareStatement(sqlCant, null);
			psCant.setInt(1, m_inout_id);
			ResultSet rsCant = psCant.executeQuery();
			if (rsCant.next()) {
				if (rsCant.getString("CANTITEMS") != null)
					items = new String(rsCant.getString("CANTITEMS"));
				if (rsCant.getString("CANTUNITS") != null) {
					cantUnidades = new String(rsCant.getString("CANTUNITS"));
					String[] campos = cantUnidades.split(",");
					cantUnidades = campos[0];
				}

			}
			rsCant.close();
			psCant.close();
		} catch (SQLException ex) {
			Logger.getLogger(RemitosSalidaTxt.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Creo al Archivo de Salida del informe
	 * 
	 */
	public void inicializarArchivo() {
		FileWriter fw = null;
		try {
			/*
			 * SimpleDateFormat formateador = new SimpleDateFormat(
			 * "dd'-'MM'-'yyyy", new Locale("es_ES")); Date fechaDate = new
			 * Date(); fecha = formateador.format(fechaDate);
			 */
			fw = new FileWriter(System.getenv("COMPIERE_HOME")
					+ System.getProperty("file.separator") + nombreArchivo
					+ getFechaFromTimeStamp(p_DateValue, "-") + ".txt");
		} catch (java.io.IOException ioex) {
			System.out.println("se presento el error: " + ioex.toString());
		} finally {
			// En el finally cierro el fichero, para asegurarme
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fw) {
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 
	 */
	public void crearArchivoSocios() {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {

			fw = new FileWriter(System.getenv("COMPIERE_HOME")
					+ System.getProperty("file.separator") + nombreArchivo
					+ getFechaFromTimeStamp(p_DateValue, "-") + ".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			formatNroRemito();
			pw.print(siglas);

			if (nroRemito != null)
				pw.print(nroRemito
						+ rellenarEspaciosBlanco(lenghtnroRemito
								- nroRemito.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtnroRemito));

			pw.print(rellenarEspaciosBlanco(lenghtcategoriaCliente));

			pw.print(rellenarEspaciosBlanco(lenghtclienteDeLaboratorio));

			if (nroTransferencia != null)
				pw.print(nroTransferencia
						+ rellenarEspaciosBlanco(lenghtnroTransferencia
								- nroTransferencia.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtnroTransferencia));

			pw.print(rellenarEspaciosBlanco(lenghtanioAprobacion));

			pw.print(rellenarEspaciosBlanco(lenghtmesAprobcion));

			pw.print(rellenarEspaciosBlanco(lenghtdiaAprobacion));

			pw.print(rellenarEspaciosBlanco(lenghtfechaPactada));

			if (localidadDestino != null)
				pw.print(localidadDestino + 
						rellenarEspaciosBlanco(lenghtlocalidadDestino 
								- localidadDestino.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtlocalidadDestino));

			if (domicilio1 != null)
				pw.print(domicilio1 
						+ rellenarEspaciosBlanco(lenghtDomicilio1
						- domicilio1.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtDomicilio1));

			pw.print(rellenarEspaciosBlanco(lenghtDomicilio2));

			pw.print(rellenarEspaciosBlanco(lenghtNroPuerta));

			pw.print(rellenarEspaciosBlanco(lenghtPiso));

			if (cp != null)
				pw.print(cp
						+ rellenarEspaciosBlanco(lenghtCP
						- cp.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtCP));

			pw.print(rellenarEspaciosBlanco(lenghtcodigoMovLaboratorio));

			pw.print(rellenarEspaciosBlanco(lenghtmarcaGestionCobranza));

			pw.print(rellenarEspaciosBlanco(lenghtValorGestionCobranza));

			if (items != null)
				pw.print(items
						+ rellenarEspaciosBlanco(lenghtItems - items.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtItems));

			if (cantUnidades != null) {
				String[] unidades = cantUnidades.split("\\.");
				pw.print(unidades[0]
						+ rellenarEspaciosBlanco(lenghtCantUnidades
								- unidades[0].length()));
			} else
				pw.print(rellenarEspaciosBlanco(lenghtCantUnidades));

			pw.print(rellenarEspaciosBlanco(lenghtZonaCliente));

			if (razonSocial != null) {
				pw.print(razonSocial
						+ rellenarEspaciosBlanco(lenghtRazonSocial
								- razonSocial.length()));
			} else

				pw.print(rellenarEspaciosBlanco(lenghtRazonSocial));

			pw.print(rellenarEspaciosBlanco(lenghtObserRto));

			pw.print(rellenarEspaciosBlanco(lenghtObserRt1));

			pw.print(rellenarEspaciosBlanco(lenghtObserRt2));

			pw.print(rellenarEspaciosBlanco(lenghtCporig));

			pw.print(rellenarEspaciosBlanco(lenghtValorFactura));

			pw.print(rellenarEspaciosBlanco(lenghtSubCuenta));

			pw.print(rellenarEspaciosBlanco(lenghtSucursalCliente));

			pw.print(rellenarEspaciosBlanco(lenghtNroCompDelPedido));

			pw.print(rellenarEspaciosBlanco(lenghtCategoriaPedido));

			pw.println();

			pw.close();

		} catch (java.io.IOException ioex) {
			System.out.println("se presento el error: " + ioex.toString());
		} finally {
			// En el finally cierro el fichero, para asegurarme
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fw) {
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * Metodo empleado para formatear el NroRemito (Elimina los dos primeros
	 * digitos y el -)
	 * 
	 */
	public void formatNroRemito() {

		String comienzo = nroRemito.substring(2, 4);
		String fin = new String(nroRemito.substring(5, nroRemito.length()));
		nroRemito = comienzo + fin;
	}

	/**
	 * Completa con espacios en blanco los campos que devuelve vacia la consulta
	 * o que no se tienen en cuenta
	 * 
	 * @param cantidad
	 * @return
	 */
	public String rellenarEspaciosBlanco(int cantidad) {
		String valorRelleno = "";
		for (int i = 0; i < cantidad; i++) {
			valorRelleno = valorRelleno + " ";
		}
		return valorRelleno;

	}

}
