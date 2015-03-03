/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.process;

import javax.swing.JOptionPane;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 * This process performs an update on Payment Values with type PC Banking
 * Can change the payment date, release date, or check number
 * If check number is changed, then the account facts for PC Banking payment value
 * is updated too, following the current format Banco_Número_Fecha Vto
 *
 * @author Ezequiel Scott @ Zynnia
 */
public class UnLockCtaCte extends SvrProcess{


    protected String doIt() throws Exception{
        
        JOptionPane.showMessageDialog(null,"Cierre las aplicaciones que generaron el bloqueo","Aviso",JOptionPane.INFORMATION_MESSAGE);
        
        String sql = "UPDATE T_BLOQUEOS SET BLOQUEADO = 'N' WHERE AD_PROCESS_ID = 1000235";
        DB.executeUpdate(sql, get_TrxName());
        
        return "Proceso Desbloqueado";
        
    }

    @Override
    protected void prepare() {
        
    }

   
}
