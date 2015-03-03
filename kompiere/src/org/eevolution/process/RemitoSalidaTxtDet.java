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

public class RemitoSalidaTxtDet extends SvrProcess {

	private Timestamp p_DateValue;
	
	private BigDecimal p_PuntoVenta = new BigDecimal(0);

	private final String siglas = new String("PAN");

	private int m_inout_id = 3;

	private final int lenghtsiglas = 3;

	private String nroRemito = null;

	private static int lenghtnroRemito = 10;

	private String categoriaCliente = null;

	private final int lenghtcategoriaCliente = 2;

	private String codigoDeProducto = null;

	private final int lenghtcodigoDeProducto = 15;

	private String cantUnidades;

	private final int lenghtCantUnidades = 7;

	private String lote = null;

	private final int lenghtLote = 15;

	private String nroLinea = null;;

	private final int lenghtnroLinea = 4;

	private String sucursal = null;

	private final int lenghtsucursal = 4;

	String fecha;

	private final String nombreArchivo = "Remito-Detalle-";

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
	private String getFechaFromTimeStamp(Timestamp ts,String separador) {
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
		String sql = "select * from RV_M_INOUT_HEADER_DET_TXT "
				+ "where  MOVEMENTDATE  =  to_date('"
				+ getFechaFromTimeStamp(p_DateValue,"/") + "', 'dd,mm,yyyy') "
				+ "and C_DOCTYPE_ID = " + p_PuntoVenta.intValue();

		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			limpiarVariables();
			m_inout_id = rs.getInt("M_INOUT_ID");
			nroRemito = new String(rs.getString("DOCUMENTNO"));
			if (rs.getString("CODIGO") != null)
				codigoDeProducto = rs.getString("CODIGO");
			if (rs.getString("LOTE") != null)
				lote = new String(rs.getString("LOTE"));
			if (rs.getString("NROLINEA") != null)
				nroLinea = rs.getString("NROLINEA");

			calcularLineas(m_inout_id, rs.getInt("M_INOUTLINE_ID") );
			crearArchivoSocios();
		}
		rs.close();
		ps.close();

		return "ok";

	}

	/**
	 * Metodo que calcula las lineas del remito pasado como parametro y suma las
	 * cantidades de cada linea
	 * 
	 * @param m_inout_id
	 *            Identificador del remito a buscar
	 * @throws Exception
	 */
	public void calcularLineas(int m_inout_id, int m_inoutline_id) throws Exception {
		try {
			String sqlCant = "select mil.qtyentered as CANTUNITS "
					+ "from M_InOut mio "
					+ "left join M_InOutLine MIL on (mio.m_inout_id = mil.m_inout_id ) "
					+ "where mio.m_inout_id=? " 
					+ "and mil.m_inoutline_id=?";
			PreparedStatement psCant = DB.prepareStatement(sqlCant, null);
			psCant.setInt(1, m_inout_id);
			psCant.setInt(2, m_inoutline_id);
			ResultSet rsCant = psCant.executeQuery();
			if (rsCant.next()) {
				cantUnidades = rsCant.getString("CANTUNITS");

			}
			rsCant.close();
			psCant.close();
		} catch (SQLException ex) {
			Logger.getLogger(RemitoSalidaTxtDet.class.getName()).log(
					Level.SEVERE, null, ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	private void limpiarVariables() {
		// TODO Auto-generated method stub
		nroRemito = null;
		codigoDeProducto = null;
		lote = null;
		nroLinea = null;


	}
	/**
	 * Creo al Archivo de Salida del informe.
	 * 
	 */
	public void inicializarArchivo() {
		FileWriter fw = null;
		try {
			/*SimpleDateFormat formateador = new SimpleDateFormat(
					"dd '-'MM'-'yyyy", new Locale("es_ES"));
			Date fechaDate = new Date();
			fecha = formateador.format(fechaDate);*/
			fw = new FileWriter(System.getenv("COMPIERE_HOME")//System.getProperty("user.home")
					+ System.getProperty("file.separator") + nombreArchivo
					+ getFechaFromTimeStamp(p_DateValue,"-") + ".txt");
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
					+ getFechaFromTimeStamp(p_DateValue,"-") + ".txt", true);
			// fw = new FileWriter("SociosDeNegocio.txt", true);
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

			if (codigoDeProducto != null)
				pw.print(codigoDeProducto
						+ rellenarEspaciosBlanco(lenghtcodigoDeProducto
								- codigoDeProducto.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtcodigoDeProducto));

			if (cantUnidades != null) {
				String[] unidades = cantUnidades.split("\\.");
				pw.print(unidades[0]
						+ rellenarEspaciosBlanco(lenghtCantUnidades
								- unidades[0].length()));
			} else
				pw.print(rellenarEspaciosBlanco(lenghtCantUnidades));

			if (lote != null)
				pw.print(lote
						+ rellenarEspaciosBlanco(lenghtLote - lote.length()));
			else
				pw.print(rellenarEspaciosBlanco(lenghtcodigoDeProducto));

			if (nroLinea != null) {
				pw.print(nroLinea
						+ rellenarEspaciosBlanco(lenghtnroLinea
								- nroLinea.length()));
			} else
				pw.print(rellenarEspaciosBlanco(lenghtnroLinea));

			pw.print(rellenarEspaciosBlanco(lenghtsucursal));

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
	 * Metodo empleado para formatear el NroRemito (Elimina los dos primeros digitos y el -)
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
