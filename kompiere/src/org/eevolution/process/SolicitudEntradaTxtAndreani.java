package org.eevolution.process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SolicitudEntradaTxtAndreani extends SvrProcess {
	
    private final String c01 = "c01";
    private final String c02 = "c02";
    private final String c03 = "c03";
    private final String c04 = "c04";
    private final String c05 = "c05";
    private final String c06 = "c06";
    private final String c07 = "c07";
    private final String c08 = "c08";
    private final String c09 = "c09";
    
    
    private String p_fecha = null;
    private String p_fecha_to = null;

    private final String nombreArchivo = "PCLT01PF";

    private BigDecimal MPC_Order_ID = null;
    
    /**
     * Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (name.equals("FECHA")) {
                p_fecha = para[i].getInfo();
                p_fecha_to = para[i].getInfo_To();
            }
            else if (name.equals("MPC_Order_ID")) {
                MPC_Order_ID = ((BigDecimal)para[i].getParameter());
            }
            else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
            
        }
        inicializarArchivo();

    }



    /**
     * Perrform process.
     * 
     * @return Message (clear text)
     * @throws Exception
     *             if not successful
     */
    protected String doIt() throws Exception {
            
        String sql = "select o.documentno,p.Value, obl.qtyrequired"
            + "from mpc_order_bomline obl "
            + "inner join mpc_order o "
                + "on (o.mpc_order_id = obl.mpc_order_id) "
            + "inner join M_Product p "
                + "on (p.M_Product_ID = obl.M_Product_ID) "
            + "where  o.docstatus =  'CO'";
        
        if(p_fecha != null)        
            sql += "and order.datestartschedule  >=  to_date('" + p_fecha + "', 'dd/mm/yyyy') ";
        if(p_fecha_to != null)        
            sql += "and order.datestartschedule  <=  to_date('" + p_fecha_to + "', 'dd/mm/yyyy') ";
        if(MPC_Order_ID != null) 
            sql += "and order.MPC_Order_ID = " + MPC_Order_ID;

        PreparedStatement ps = DB.prepareStatement(sql, null);
        ResultSet rs = ps.executeQuery();

        String prod_txt = "";
        String ord_txt = "";
        String qty_txt = "";
        
        FileWriter fw = null;
	PrintWriter pw = null;
        BufferedWriter bw = null;
        
        
        try {
        
            fw = new FileWriter(System.getProperty("user.home")
                + System.getProperty("file.separator") + nombreArchivo
                + ".csv");
        
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);      

            while (rs.next()) {

                    ord_txt = rs.getString(1) != null ? rs.getString(1) : "";
                    prod_txt = rs.getString(2) != null ? rs.getString(2) : "";
                    qty_txt = rs.getString(3) != null ? rs.getString(3) : "";

                    pw.print(c01+";"+c02+";"+c03+";"+c04+";"+c05+";"+c06+";"+c07+";"+c08+";"+c09
                    +";"+ord_txt+prod_txt+qty_txt);
                    pw.println();
                    
            }
            pw.close();
        } catch (java.io.IOException ioex) {
                System.out.println("se presento el error: " + ioex.toString());
                rs.close();
                ps.close();
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
                        rs.close();
                        ps.close();

                }
        }        
        
        rs.close();
        ps.close();

        return "ok";

    }

    /**
     * Creo al Archivo de Salida del informe.
     * 
     */
    public void inicializarArchivo() {
            FileWriter fw = null;
            try {
                fw = new FileWriter(System.getenv("COMPIERE_HOME")//System.getProperty("user.home")
                    + System.getProperty("file.separator") + nombreArchivo
                    + p_fecha + "-" + p_fecha_to +".csv");
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
