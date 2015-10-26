package org.eevolution.process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class RemitoSalidaTxtAndreani extends SvrProcess {
	
    private String locia = "";
    private String losku = "";
    private String lolot = "";
    private String loesto = "";
    private String loesin = "";
    private String loven = "";
    private String lofec = "";
    private String locmnt = "";
    
    private String p_fecha = "";
    private String p_fecha_to = "";

    private final String nombreArchivo = "PCLT01PF";
    
    // Id del tipo de movimiento.
    
    private String tipoMovimientoValido = "5000045";
    
    // Identificación de origen y destino
    
    private String origenMovimiento = "CUA";
    private String destinoMovimiento = "DIS";
        
    
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
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
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
            
        String sql = "select prod.Value, masi.created, masi.guaranteedate, masi.lot, mov.description "
            + "from M_Movement mov "
            + "inner join M_MovementLine movline "
                + "on (movline.M_Movement_ID = mov.M_Movement_ID) "
            + "inner join M_AttributeSetInstance masi "
                + "on (masi.M_AttributeSetInstance_ID = movline.M_AttributeSetInstance_ID) "
            + "inner join M_Product prod "
                + "on (prod.M_Product_ID = movline.M_Product_ID) "
            + "where  mov.MOVEMENTDATE  >=  to_date('" + p_fecha + "', 'dd/mm/yyyy') ";
        
        if(p_fecha_to != "")        
            sql += "and mov.MOVEMENTDATE  <=  to_date('" + p_fecha_to + "', 'dd/mm/yyyy') ";
        
        sql += "and mov.C_DOCTYPE_ID = " + tipoMovimientoValido;

        PreparedStatement ps = DB.prepareStatement(sql, null);
        ResultSet rs = ps.executeQuery();

        String prod_txt = "";
        String crea_txt = "";
        String venc_txt = "";
        String lote_txt = "";
        
        FileWriter fw = null;
	PrintWriter pw = null;
        BufferedWriter bw = null;
        
        
        try {
        
            fw = new FileWriter(System.getProperty("user.home")
                + System.getProperty("file.separator") + nombreArchivo
                + ".txt");
        
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            String [] crea_date = null;
            String [] venc_date = null;
            String crea_dd = "";
            String crea_mm = "";
            String crea_yy = "";
            String venc_dd = "";
            String venc_mm = "";
            String venc_yy = "";
            String desc = "";
            

            while (rs.next()) {

                    prod_txt = rs.getString(1);
                    
                    crea_txt = rs.getDate(2).toString();
                    crea_date = crea_txt.split("-");
                    crea_dd = crea_date[2];
                    crea_mm = crea_date[1];
                    crea_yy = crea_date[0].substring(2,4);

                    venc_txt = rs.getDate(3).toString();
                    venc_date = venc_txt.split("-");
                    venc_dd = venc_date[2];
                    venc_mm = venc_date[1];
                    venc_yy = venc_date[0].substring(2,4);

                    System.out.println(venc_date[1]);
                    System.out.println(venc_date[2]);
                    lote_txt = rs.getString(4);
                    
                    
                    if(rs.getString(4).length()>25)
                        desc = rs.getString(5).substring(0, 24);
                    else
                        desc = rs.getString(5);
                    
                    pw.print("PMP"+","+prod_txt+","+lote_txt+","+"CUA"+","+"DIS"
                    +","+venc_dd+","+venc_mm+","+venc_yy
                    +","+crea_dd+","+crea_mm+","+crea_yy
                    +","+ desc);
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
                    + p_fecha + "-" + p_fecha_to +".txt");
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
