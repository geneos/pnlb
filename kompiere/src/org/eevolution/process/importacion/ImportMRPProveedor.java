package org.eevolution.process.importacion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.X_C_BP_Customer_Acct;
import org.compiere.model.X_C_BPartner_Jurisdiccion;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author mariano Importacion de los proveedores desde la base de datos de
 *         produccion MRP a Integrado
 */

public class ImportMRPProveedor {

	// Conexión donde se van a insertar los datos (Destino)
	private Connection conexionDestino;

	// Coenxion de la BD desde donde se van a obtener los datos (Origen)
	private Connection conexionFuente;

	private String trxName;

	// variable utilizada para llevar la cantidad maxima
	private static int maxRegistro;

	/**
	 * 
	 * @return
	 */
	public Connection getConexionDestino() {
		return conexionDestino;
	}

	/**
	 * 
	 * @param conexionDestino
	 */
	public void setConexionDestino(Connection conexionDestino) {
		this.conexionDestino = conexionDestino;
	}

	public Connection getConexionFuente() {
		return conexionFuente;
	}

	/**
	 * 
	 * @param conexionFuente
	 */
	public void setConexionFuente(Connection conexionFuente) {
		this.conexionFuente = conexionFuente;
	}

	/**
	 * 
	 * @return
	 */
	public String getTrxName() {
		return trxName;
	}

	/**
	 * 
	 * @param trxName
	 */
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}

	/**
	 * Elimina de la BD destino los socios de negocio que sean solo proveedores
	 * (si son proveedores y clientes no lo elimina)
	 * 
	 * @param rd
	 *            resulset de los socios de negocio que son solo proveedores
	 * 
	 */
	public void limpiarInicial(ResultSet rsInicial) throws Exception {
		MBPartner mbpartner = null;
		while (rsInicial.next()) {
			mbpartner = new MBPartner(Env.getCtx(), rsInicial.getInt("C_BPARTNER_ID"),null);
			mbpartner.delete(true);
			System.gc();
		}
	}

	/**
	 * Verifica los cinco casos posibles 1) Existe el id y existe el nombre en
	 * la misma ubicacion 2) Existe el id y existe el nombre pero en otra
	 * ubicacion 3) Existe el id y no existe el nombre 4) No existe el id pero
	 * si el nombre 5) No existe el id y no existe el nombre
	 * 
	 * 
	 * @param rsFuente
	 *            Contiene todos los proveedores de la BD de produccion MRP
	 * @throws Exception
	 *             Propaga la Excepcion en caso de ocurrir rsInicialId rsultset
	 *             empleado para realizar la consulta por id en la BD Integrado
	 *             rsInicialName rsultset empleado para realizar la consulta por
	 *             nombre en la BD Integrado
	 */

	protected boolean comparar(ResultSet rsFuente) throws Exception {
		try {
			while (rsFuente.next()) {
				// Consulta que me devuelve si existe el id sobre la BD
				// Integrado
				String sqlId = "select * " + "from c_bpartner "
						+ "where c_bpartner_id = ? " + "order by c_bpartner_id";
				PreparedStatement psId = conexionDestino.prepareStatement(
						sqlId, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				psId.setInt(1, rsFuente.getInt("C_BPARTNER_ID"));
				ResultSet rsInicialId = psId.executeQuery();

				// Consulta que me devuelve si existe el nombre sobre la BD
				// Integrado
				String sqlName = "select * " + "from c_bpartner "
						+ "where name = ? " + "order by c_bpartner_id";
				PreparedStatement psName = conexionDestino.prepareStatement(
						sqlName, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				psName.setString(1, rsFuente.getString("name"));
				ResultSet rsInicialName = psName.executeQuery();

				if (rsInicialId.next()) {
					if (rsInicialId.getString("C_BPARTNER_ID") != null) {
						if (rsInicialId.getString("name").equals(
								rsFuente.getString("name"))) {
							/*
							 * Existe el id y existe el nombre en la misma
							 * ubicacion (Ej: 1 A - 1 A) Hago corrimiento 1 A en
							 * integrado al final Agrego 1 A al listado
							 */
							// merge(rsInicialName, rsFuente);
							modificarId(rsInicialId.getInt("C_BPARTNER_ID"));
							insertarTupla(rsFuente.getInt("C_BPARTNER_ID"),
									rsFuente);
							generarListado(rsInicialName.getString("name"),
									maxRegistro - 1);
						} else {
							if (rsInicialName.next())
								if (rsInicialName.getString("name") != null)
									if (rsInicialName.getString("name").equals(
											(rsFuente.getString("name")))) {
										/*
										 * Existe el id y existe el nombre pero
										 * en otra ubicacion (Ej 1 A - 1 C,2 A)
										 * Corrimiento 1 C al final Inserto 1 A
										 * en integrado Agrego 1 A al listado
										 */
										modificarId(rsInicialId
												.getInt("C_BPARTNER_ID"));
										insertarTupla(rsFuente
												.getInt("C_BPARTNER_ID"),
												rsFuente);
										generarListado(
												rsInicialName.getString("name"),
												rsInicialName
														.getInt("C_BPARTNER_ID"));
										// merge(rsInicialName, rsFuente);
									} else {
										/*
										 * Existe el id y no existe el nombre
										 * Corrimiento Insertar tupla
										 */
										modificarId(rsInicialId
												.getInt("C_BPARTNER_ID"));
										insertarTupla(rsFuente
												.getInt("C_BPARTNER_ID"),
												rsFuente);
									}
						}
					}
				} else if (rsInicialId.next()) {
					if (rsInicialName.getString("name") != null)

						if (rsInicialName.getString("name").equals(
								(rsFuente.getString("name")))) {
							/*
							 * No existe el id pero si el nombre MERP (EJ 1 A 2
							 * A)-> insertar 1 A Agrego 1 A al listado
							 */
							insertarTupla(rsFuente.getInt("C_BPARTNER_ID"),
									rsFuente);
							generarListado(rsInicialName.getString("name"),
									rsInicialName.getInt("C_BPARTNER_ID"));
							// merge(rsInicialName, rsFuente);
						}
				} else {
					// No existe el id y no existe el nombre
					// insertar tupla
					insertarTupla(rsFuente.getInt("C_BPARTNER_ID"), rsFuente);
				}
				rsInicialId.close();
				rsInicialName.close();

			}
			rsFuente.close();
		} catch (SQLException ex) {
			Logger.getLogger(ImportMRPProveedor.class.getName()).log(
					Level.SEVERE, null, ex);
			return false;
		}
		return true;
	}

	/**
	 * Genera un archivo que contiene todos los socios de negocio que se
	 * encuentran repetidos
	 * 
	 * @param name
	 *            Nombre del socio de negocio
	 * @param id
	 *            identificador del socio de negocio
	 */
	public void generarListado(String name, int id) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(System.getProperty("user.home")+ System.getProperty("file.separator") +"SociosDeNegocio.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			pw.println("El identificador del socio es: " + id
					+ "      Nombre: " + name);

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

	public void crearArchivoSocios() {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(System.getProperty("user.home")+ System.getProperty("file.separator") +"SociosDeNegocio.txt");
			//fw = new FileWriter("SociosDeNegocio.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			pw.println("Socios de Negocio Repetidos");
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
	 * Modifico el id de C_BPartner por el que contiene maxregistro Luego
	 * Actualizo todas las referencias
	 * 
	 * @param id
	 */
	private void modificarId(int id) {
		// TODO Auto-generated method stub
		MBPartner mb = new MBPartner(Env.getCtx(), id, getTrxName());
		int idActualizarRef = mb.getC_BPartner_ID();
		mb.setC_BPartner_ID(maxRegistro);
		mb.save();
		try {
			actualizarReferencias(idActualizarRef);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxRegistro++;
	}

	/**
	 * Actualizo todas las referencias de las tablas C_BPartner_Location
	 * C_BPartner_Jurisdiccion c_bp_customer_acct
	 * 
	 * @param id
	 *            Es el id por el cual busco en las tablas asociadas, luego lo
	 *            modifico por maxregistro
	 * @throws Exception
	 */
	private void actualizarReferencias(int id) throws Exception {
		// A partir del id de C_BPartner instancio las variables de las
		// tablas involucradas
		// C_BPartner_Location
		String sql = "select * from  C_BPartner_Location "
				+ "where c_bpartner_id  = " + id;
		PreparedStatement psId = conexionDestino.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = psId.executeQuery();
		while (rs.next())
			if (rs.getString("C_BPartner_Location") != null) {
				MBPartnerLocation mbpartnerLocation = new MBPartnerLocation(Env
						.getCtx(), rs.getInt("C_BPartner_Location_id"),
						getTrxName());
				// Seteo el nuevo id
				mbpartnerLocation.setC_BPartner_Location_ID(maxRegistro);
				mbpartnerLocation.save();
			}
		rs.close();

		// C_BPartner_Jurisdiccion
		sql = "select * from  C_BPartner_Jurisdiccion "
				+ "where c_bpartner_id  = " + id;
		psId = conexionDestino.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = psId.executeQuery();
		while (rs.next())
			if (rs.getString("C_BPARTNER_JURISDICCION_ID") != null) {
				X_C_BPartner_Jurisdiccion cbPartnerJurisdiccion = new X_C_BPartner_Jurisdiccion(
						Env.getCtx(), rs.getInt(rs
								.getInt("C_BPARTNER_JURISDICCION_ID")),
						getTrxName());
				// Seteo el nuevo id
				cbPartnerJurisdiccion
						.setC_BPartner_Jurisdiccion_ID(maxRegistro);
				cbPartnerJurisdiccion.save();
			}
		rs.close();

		// c_bp_customer_acct
		sql = "select * from  c_bp_customer_acct " + "where c_bpartner_id  = "
				+ id;
		psId = conexionDestino.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = psId.executeQuery();
		if (rs.next())
			if (rs.getString("c_bp_customer_acct_id") != null) {
				X_C_BP_Customer_Acct bpCustomerAcct = new X_C_BP_Customer_Acct(
						Env.getCtx(), rs.getInt("c_bp_customer_acct_id"),
						getTrxName());
				// Seteo el nuevo id
				bpCustomerAcct.setC_AcctSchema_ID(maxRegistro);
				bpCustomerAcct.save();
			}
		rs.close();

	}

	public void merge(ResultSet rsInicial, ResultSet rsFuente) throws Exception {
		if (rsFuente.next() && rsInicial.next()) {
			MBPartner mbpartnerFuente = new MBPartner(Env.getCtx(), rsFuente
					.getInt("C_BPARTNER_ID"), getTrxName());
			MBPartner mbpartnerInicial = new MBPartner(Env.getCtx(), rsInicial
					.getInt("C_BPARTNER_ID"), getTrxName());
			int idActualizarRef = mbpartnerInicial.getC_BPartner_ID();
			// Seteo la Variable isvendor
			mbpartnerInicial.setIsVendor(mbpartnerFuente.isVendor());
			// Seteo la Variable isSalesRep
			mbpartnerInicial.setIsSalesRep(mbpartnerFuente.isSalesRep());
			// Seteo la Variable PaymentRulePO
			mbpartnerInicial.setPaymentRulePO(mbpartnerFuente
					.getPaymentRulePO());
			// Seteo la Variable getPO_PaymentTerm_ID
			mbpartnerInicial.setPO_PaymentTerm_ID(mbpartnerFuente
					.getPO_PaymentTerm_ID());
			// Seteo la Variable getPO_PriceList_ID
			mbpartnerInicial.setPO_PriceList_ID(mbpartnerFuente
					.getPO_PriceList_ID());
			// Seteo la Variable PO_DiscountSchema_ID
			mbpartnerInicial.setPO_DiscountSchema_ID(mbpartnerFuente
					.getPO_DiscountSchema_ID());
			// Seteo el id
			mbpartnerInicial.setC_BPartner_ID(mbpartnerFuente
					.getC_BPartner_ID());
			mbpartnerInicial.save();
			actualizarReferencias(idActualizarRef);
		}
	}

	/**
	 * 
	 * @param id
	 *            Lugar donde debo insertar el registro
	 */
	private void insertarTupla(int id, ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		if (rs.next()) {
			MBPartner mbpartner = new MBPartner(Env.getCtx(), 0, getTrxName());
			/*
			 * C_BPARTNER_ID AD_CLIENT_ID AD_ORG_ID ISACTIVE CREATED CREATEDBY
			 * UPDATED UPDATEDBY VALUE NAME NAME2 DESCRIPTION ISSUMMARY
			 * C_BP_GROUP_ID ISONETIME ISPROSPECT ISVENDOR ISCUSTOMER ISEMPLOYEE
			 * ISSALESREP REFERENCENO DUNS URL AD_LANGUAGE TAXID ISTAXEXEMPT
			 * C_INVOICESCHEDULE_ID RATING,SALESVOLUME NUMBEREMPLOYEES NAICS
			 * FIRSTSALE ACQUSITIONCOST POTENTIALLIFETIMEVALUE
			 * ACTUALLIFETIMEVALUE SHAREOFCUSTOMER PAYMENTRULE SO_CREDITLIMIT
			 * SO_CREDITUSED C_PAYMENTTERM_ID M_PRICELIST_ID M_DISCOUNTSCHEMA_ID
			 * C_DUNNING_ID ISDISCOUNTPRINTED SO_DESCRIPTION POREFERENCE
			 * PAYMENTRULEPO PO_PRICELIST_ID PO_DISCOUNTSCHEMA_ID
			 * PO_PAYMENTTERM_ID DOCUMENTCOPIES C_GREETING_ID INVOICERULE
			 * DELIVERYRULE FREIGHTCOSTRULE DELIVERYVIARULE SALESREP_ID
			 * SENDEMAIL BPARTNER_PARENT_ID INVOICE_PRINTFORMAT_ID
			 * SOCREDITSTATUS SHELFLIFEMINPCT AD_ORGBP_ID FLATDISCOUNT
			 * TOTALOPENBALANCE FACTURAM EMPLEADOR FACTURAHONORARIOS
			 * EXENCIONGANANCIAS EXENCIONIB EXENCIONIVA EXENCIONSUSS SUJETO IVA
			 * CONDICIONIVA_ID SUJETOPER EXENCIONPERIB EXENCIONPERIVA
			 * COEFICIENTEIVA CONDICIONGAN_ID CONVENIOMULTILATERAL COEFICIENTEIB
			 * BPEXTRANJERO NROEXTRANJERO
			 */

			mbpartner.setName(rs.getString("Name"));
			mbpartner.setAD_Org_ID(rs.getInt("AD_ORG_ID"));
			mbpartner.setIsActive(rs.getBoolean("ISACTIVE"));
			mbpartner.setValue(rs.getString("VALUE"));
			mbpartner.setName(rs.getString("NAME"));
			mbpartner.setName2(rs.getString("NAME2"));
			mbpartner.setDescription(rs.getString("DESCRIPTION"));
			mbpartner.setIsSummary(rs.getBoolean("ISSUMMARY"));
			mbpartner.setC_BP_Group_ID(rs.getInt("C_BP_GROUP_ID"));
			mbpartner.setIsOneTime(rs.getBoolean("ISONETIME"));
			mbpartner.setIsProspect(rs.getBoolean("ISPROSPECT"));
			mbpartner.setIsVendor(rs.getBoolean("ISVENDOR"));
			mbpartner.setIsCustomer(rs.getBoolean("ISCUSTOMER"));
			mbpartner.setIsEmployee(rs.getBoolean("ISEMPLOYEE"));
			mbpartner.setIsSalesRep(rs.getBoolean("ISSALESREP"));
			mbpartner.setReferenceNo(rs.getString("REFERENCENO"));
			mbpartner.setDUNS(rs.getString("DUNS"));
			mbpartner.setURL(rs.getString("URL"));
			mbpartner.setAD_Language(rs.getString("AD_LANGUAGE"));
			mbpartner.setTaxID(rs.getString("TAXID"));
			mbpartner.setIsTaxExempt(rs.getBoolean("ISTAXEXEMPT"));
			mbpartner
					.setC_InvoiceSchedule_ID(rs.getInt("C_INVOICESCHEDULE_ID"));
			mbpartner.setRating(rs.getString("RATING"));
			mbpartner.setSalesVolume(rs.getInt("SALESVOLUME"));
			mbpartner.setNumberEmployees(rs.getInt("NUMBEREMPLOYEES"));
			mbpartner.setNAICS(rs.getString("NAICS"));
			mbpartner.setFirstSale(rs.getTimestamp("FIRSTSALE"));
			mbpartner.setAcqusitionCost(rs.getBigDecimal("ACQUSITIONCOST"));
			mbpartner.setPotentialLifeTimeValue(rs
					.getBigDecimal("POTENTIALLIFETIMEVALUE"));
			mbpartner.setActualLifeTimeValue(rs
					.getBigDecimal("ACTUALLIFETIMEVALUE"));
			mbpartner.setShareOfCustomer(rs.getInt("SHAREOFCUSTOMER"));
			mbpartner.setPaymentRule(rs.getString("PAYMENTRULE"));
			mbpartner.setSO_CreditLimit(rs.getBigDecimal("SO_CREDITLIMIT"));
			mbpartner.setSO_CreditUsed(rs.getBigDecimal("SO_CREDITUSED"));
			mbpartner.setC_PaymentTerm_ID(rs.getInt("C_PAYMENTTERM_ID"));
			mbpartner.setM_PriceList_ID(rs.getInt("M_PRICELIST_ID"));
			mbpartner.setM_DiscountSchema_ID(rs.getInt("M_DISCOUNTSCHEMA_ID"));
			mbpartner.setC_Dunning_ID(rs.getInt("C_DUNNING_ID"));
			mbpartner.setIsDiscountPrinted(rs.getBoolean("ISDISCOUNTPRINTED"));
			mbpartner.setSO_Description(rs.getString("SO_DESCRIPTION"));
			mbpartner.setPOReference(rs.getString("POREFERENCE"));
			mbpartner.setPaymentRule(rs.getString("PAYMENTRULEPO"));
			mbpartner.setPO_PriceList_ID(rs.getInt("PO_PRICELIST_ID"));
			mbpartner
					.setPO_DiscountSchema_ID(rs.getInt("PO_DISCOUNTSCHEMA_ID"));
			mbpartner.setPO_PaymentTerm_ID(rs.getInt("PO_PAYMENTTERM_ID"));
			mbpartner.setDocumentCopies(rs.getInt("DOCUMENTCOPIES"));
			mbpartner.setC_Greeting_ID(rs.getInt("C_GREETING_ID"));
			mbpartner.setInvoiceRule(rs.getString("INVOICERULE"));
			mbpartner.setDeliveryRule(rs.getString("DELIVERYRULE"));
			mbpartner.setFreightCostRule(rs.getString("FREIGHTCOSTRULE"));
			mbpartner.setDeliveryViaRule(rs.getString("DELIVERYVIARULE"));
			mbpartner.setSalesRep_ID(rs.getInt("SALESREP_ID"));
			mbpartner.setSendEMail(rs.getBoolean("SENDEMAIL"));
			mbpartner.setBPartner_Parent_ID(rs.getInt("BPARTNER_PARENT_ID"));
			mbpartner.setInvoice_PrintFormat_ID(rs
					.getInt("INVOICE_PRINTFORMAT_ID"));
			mbpartner.setSOCreditStatus(rs.getString("SOCREDITSTATUS"));
			mbpartner.setShelfLifeMinPct(rs.getInt("SHELFLIFEMINPCT"));
			mbpartner.setAD_OrgBP_ID(rs.getInt("AD_ORGBP_ID"));
			mbpartner.setFlatDiscount(rs.getBigDecimal("FLATDISCOUNT"));
			mbpartner.setTotalOpenBalance(rs.getBigDecimal("TOTALOPENBALANCE"));
			mbpartner.setFACTURAM(rs.getBoolean("FACTURAM"));
			mbpartner.setEMPLEADOR(rs.getBoolean("EMPLEADOR"));
			mbpartner.setFACTURAHONORARIOS(rs.getBoolean("FACTURAHONORARIOS"));
			mbpartner.setEXENCIONGANANCIAS(rs.getBoolean("EXENCIONGANANCIAS"));
			mbpartner.setEXENCIONIB(rs.getBoolean("EXENCIONIB"));
			mbpartner.setEXENCIONIVA(rs.getBoolean("EXENCIONIVA"));
			mbpartner.setEXENCIONSUSS(rs.getBoolean("EXENCIONSUSS"));
			mbpartner.setSUJETO(rs.getBoolean("SUJETO"));
			mbpartner.setIVA(rs.getBigDecimal("IVA"));
			mbpartner.setCONDICIONIVA_ID(rs.getInt("CONDICIONIVA_ID"));
			mbpartner.setSUJETOPER(rs.getBoolean("SUJETOPER"));
			mbpartner.setEXENCIONPERIB(rs.getBoolean("EXENCIONPERIB"));
			mbpartner.setEXENCIONPERIVA(rs.getBoolean("EXENCIONPERIVA"));

			// Preguntar estas columnas no existen en MRP
			// mbpartner.setCONDICIONGAN_ID(rs.getInt("CONDICIONGAN_ID"));
			// mbpartner.setConvenioMultilateral(rs.getBoolean("CONVENIOMULTILATERAL"));
			// mbpartner.setNro_Extranjero(rs.getString("NROEXTRANJERO"));
			// mbpartner.setCoeficienteIVA(rs.getBigDecimal("COEFICIENTEIVA"));

			mbpartner.setCoeficienteIB(rs.getBigDecimal("COEFICIENTEIB"));

			/*
			 * AD_CLIENT_ID CREATED CREATEDBY UPDATED UPDATEDBY BPEXTRANJERO
			 */

			mbpartner.save();
			mbpartner.setC_BPartner_ID(id);
			mbpartner.save();
		}
	}

	public ImportMRPProveedor(Connection cd, Connection cf) throws Exception {
		setConexionFuente(cf);
		setConexionDestino(cd);
		String sql = "select max(C_BPARTNER_ID) " + "from c_bpartner";
		PreparedStatement ps = conexionDestino.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = ps.executeQuery();
		int destino = 0;
		if (rs.next())
			destino = rs.getInt(1);
		sql = "select max(C_BPARTNER_ID) " + "from c_bpartner";
		ps = conexionFuente.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = ps.executeQuery();
		int fuente = 0;
		if (rs.next())
			fuente = rs.getInt(1);
		maxRegistro = (destino > fuente) ? destino : fuente;
		rs.close();

	}

	public boolean procesar(ResultSet rsInicial, ResultSet rsFuente)
			throws Exception {
		limpiarInicial(rsInicial);
		//crearArchivoSocios();
		//return comparar(rsFuente);
		return true;
	}

}
