/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.compiere.model;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.compiere.util.*;
import org.compiere.util.QueryDB;

/**
 *
 * @author BISION - Santiago Iba�ez - 23/09/2008
 * @version 1.0
 */
public class MActionPermission extends X_AD_Action_Permission {

/**
	 * 	Get Action permissions from Cache
	 *	@param ctx context
	 *	@param AD_Action_Permission id
	 *	@return MActionPermission
	 */

	public static  MActionPermission get (Properties ctx, int AD_Action_Permission_ID)
	{
		Integer key = new Integer (AD_Action_Permission_ID);
		MActionPermission retValue = (MActionPermission) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MActionPermission (ctx, AD_Action_Permission_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MActionPermission>	s_cache	= new CCache<Integer,MActionPermission>("AD_Action_Permission", 10);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Action_Permission_ID id
	 */
	public MActionPermission (Properties ctx, int AD_Action_Permission_ID, String trxName)
	{
		super (ctx, AD_Action_Permission_ID, trxName);
	}	//	MActionPermission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MActionPermission (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MActionPermission

	/**
         * M�todo que comprueba qu no existan previamente permisos iguales.
         * Contempla que sea un nuevo registro o que se est� modificando uno
         * existente. 
         * @param newRecord
         * @return
         */
        public boolean existePermiso(boolean newRecord){
            /**
             * si es nuevo registro entonces si encuentra un registro en la BD
             * sino busca al menos 2, por que uno es el que se est� modificando
             */      
            int size = newRecord == true ? 0 : 1;
            int AD_Org_ID = ((Integer) get_Value("AD_Org_ID")).intValue();
            int AD_Role_ID = ((Integer) get_Value("AD_Role_ID")).intValue();
            String AD_Workflow_ID = get_Value("AD_Workflow_ID").toString();
            String tipo = get_Value("TIPOPRODUCTO").toString();
            int Action_ID = getAction_ID();
            int M_Warehouse_ID = (Integer) get_Value("M_Warehouse_ID") == null ? 0 : ((Integer) get_Value("M_Warehouse_ID")).intValue();
            QueryDB  query = new QueryDB("org.compiere.model.X_AD_Action_Permission");
            String filter = "AD_Role_ID = " + AD_Role_ID +
                         " AND AD_Workflow_ID = " + AD_Workflow_ID +
                         " AND Action_ID = " + Action_ID +
                         " AND AD_Org_ID = " + AD_Org_ID +
                         " AND (TipoProducto = '" + tipo +"' OR TipoProducto = 'T')"+
                         " AND M_Warehouse_ID = " + M_Warehouse_ID;
             java.util.List results = query.execute(filter);
             Iterator select = results.iterator();
             if (results.size()>size)
             //while (select.hasNext())
             {
                JOptionPane.showMessageDialog(null,"Ya existe un permiso asociado", "Informaci�n", JOptionPane.INFORMATION_MESSAGE);
                return true;
             }
             return false;
        }
        
        /** Controla que no exista ya el permiso
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord){
            int AD_Org_ID = ((Integer) get_Value("AD_Org_ID")).intValue();
            int AD_Role_ID = ((Integer) get_Value("AD_Role_ID")).intValue();
            String AD_Workflow_ID = get_Value("AD_Workflow_ID").toString();
            int Action_ID = getAction_ID();
            Integer i = (Integer)get_Value("NIVEL");
            if (i==null)
                set_Value("NIVEL",-1);
            //si es un registro nuevo chequeo que no exista un permiso similar
            if (is_new()){
                 if (!existePermiso(true)&&!conflictoInclusion(AD_Org_ID,AD_Role_ID,AD_Workflow_ID,Action_ID))
                     return true;
                 else
                     return false;
            }
            //si no es un registro nuevo entonces solo busco
            else{
                if (!existePermiso(false))
                     return true;
                 else
                     return false;
            } 
	}	//	beforeSave

	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	}	//	afterDelete
        
        /**
         * M�todo que retorna si ya existen permisos para el rol dentro de la
         * organizaci�n (AD_Org_ID) para ejecutar la acci�n dada (Action_ID)
         * durante el procedimiento (AD_Workflow_ID) 
         * @param AD_Org_ID 
         * @param AD_Role_ID
         * @param AD_Workflow_ID
         * @param Action_ID
         * @return boolean
         */
        public static boolean hasRolePermission(int AD_Org_ID , int AD_Role_ID, String AD_Workflow_ID,int Action_ID,String tipo,int level,String AD_Window_ID){
         if (AD_Workflow_ID.equals(""))
             return false;
         
         QueryDB  query = new QueryDB("org.compiere.model.X_AD_Action_Permission");
         String filter = "AD_Role_ID = " + AD_Role_ID +
                         " AND Action_ID = " + Action_ID +
                         " AND AD_Org_ID = " + AD_Org_ID;
                         
         /** BISion - 11/03/2009 - Santiago Ibañez
          * Modificacion realizada para considerar Workflows o Ventanas (pueden ser nulls)
          */
         if (AD_Workflow_ID!=null&&!AD_Workflow_ID.equals(""))
        	 filter+=" AND AD_Workflow_ID = " + AD_Workflow_ID;
         if (AD_Window_ID!=null&&!AD_Window_ID.equals(""))
        	 filter+=" AND AD_Window_ID = " + AD_Window_ID;
         filter += " AND (";
         /**
          * si es insumo productivo los permisos para productos comprados
          * tambi�n los incluyen
          */
         //si es insumo productivo considero adem�s los comprados
         if (tipo.equals(Origen_Producto_InsumoProductivo))
             filter += " TipoProducto = '" + Origen_Producto_Comprado +"' OR ";
         //si es insumo no productivo considero adem�s los comprados
         else if (tipo.equals(Origen_Producto_InsumoNoProductivo))
             filter += " TipoProducto = '"+ Origen_Producto_Comprado +"' OR ";
         //si es elaborado cosidero los niveles (o no)
         else if (tipo.equals(Origen_Producto_Elaborado))             
            filter+="  (NIVEL = "+ level + " AND TipoProducto = '"+ Origen_Producto_ElaboradoNivel + "') OR ";
         filter += "TipoProducto = '" + Origen_Producto_Todos + "' OR TipoProducto = '" + tipo + "')";
         
         java.util.List results = query.execute(filter);
         Iterator select = results.iterator();
         while (select.hasNext())
         {
          return true;
         }
         return false;
         }

        
        /**
         * M�todo que retorna las ubicaciones de almacenes desde los cuales 
         * se debe considerar o no (seg�n sugiera "excluir") a la hora de 
         * seleccionar una partida.
         * @param excluir
         * @param AD_Org_ID
         * @param AD_Role_ID
         * @param tipo
         * @return
         */
        public static Integer[] getWarehouse(boolean excluir,int AD_Org_ID,int AD_Role_ID,String AD_Workflow_ID,String trx){
                //si se excluyen los almacenes la accion va a ser la n�mero 6
                int Action_ID = excluir == true ? 6:7;
                //hago la consulta para obtener las categor�as de alamacenes
                String sql = "SELECT M_Category_ID FROM AD_Action_Permission " +
                             "WHERE AD_Org_ID=? AND AD_Role_ID=? AND AD_Workflow_ID =? AND ACTION_ID=?";
                ArrayList<Integer> list = new ArrayList<Integer>();
                PreparedStatement pstmt = null;
                try{    
                    pstmt = DB.prepareStatement (sql, trx);
                    pstmt.setInt (1, AD_Org_ID);
                    pstmt.setInt (2, AD_Role_ID);
                    pstmt.setString (3, AD_Workflow_ID);
                    pstmt.setInt (4, Action_ID);
                    ResultSet rs = pstmt.executeQuery ();
                    while (rs.next ()){   
                        list.add (new Integer(rs.getInt(1)));
                    }
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
                    if (list.size()==0)
                        return null;
                    //Coloco las categor�as en un arreglo
                    Integer[] categorias = new Integer[list.size()];
                    list.toArray(categorias); 
                    //Obtengo los almacenes
                    sql = "SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE_CATEGORY WHERE ";
                    //Armo la consulta con cada una de las categor�as obtenidas
                    sql += "M_CATEGORY_ID = ?";
                    for (int i=1; i<categorias.length;i++){
                        sql+= " OR M_CATEGORY_ID = ?";
                    }
                    pstmt = DB.prepareStatement (sql, trx);
                    //seteo los parametros                    
                    for (int i=0; i<categorias.length;i++){
                        pstmt.setInt(i+1, categorias[i]);
                    }
                    //agrego los almacenes al vector
                    list = new ArrayList<Integer>();
                    rs = pstmt.executeQuery ();
                    while (rs.next ()){   
                        list.add (new Integer(rs.getInt(1)));
                    }
                    //Coloco los almacenes en un arreglo
                    Integer[] almacenes = new Integer[list.size()];
                    list.toArray(almacenes);
                    //Obtengo las ubicaciones de los alamacenes
                    sql = "SELECT M_LOCATOR_ID FROM M_LOCATOR WHERE ";
                    //Armo la consulta con cada una de las categor�as obtenidas
                    sql += "M_WAREHOUSE_ID = ?";
                    for (int i=1; i<almacenes.length;i++){
                        sql+= " OR M_WAREHOUSE_ID = ?";
                    }
                    pstmt = DB.prepareStatement (sql, trx);
                    //seteo los parametros                    
                    for (int i=0; i<almacenes.length;i++){
                        pstmt.setInt(i+1, almacenes[i]);
                    }
                    //agrego los almacenes al vector
                    list = new ArrayList<Integer>();
                    rs = pstmt.executeQuery ();
                    while (rs.next ()){   
                        list.add (new Integer(rs.getInt(1)));
                    }
                    
                    rs.close ();
                    pstmt.close ();
                    pstmt = null;
                    Integer[] ubic = new Integer[list.size()];
                    list.toArray(ubic);
                    return ubic;
                }
                catch (Exception ex)
                {

                }            
                return null;
        }
        
        
        /** BISion - 03/10/2008 - Santiago Iba�ez
         * Metodo que hace la comprobacion para evitar que existan permisos de
         * "seleccion de partida incluyendo almacen" en simultaneo con 
         * "seleccion de partida excluyendo almacen" para los parametros dados
         * @param AD_Org_ID : organizacion
         * @param AD_Role_ID : rol
         * @param AD_Workflow_ID : procedimiento Panalab
         * @param Action_ID: accion: 1 o 6.
         * @return
         */
        private boolean conflictoInclusion(int AD_Org_ID,int AD_Role_ID,String AD_Workflow_ID,int Action_ID){
            /**
             * Si la accion es de "selecci�n de partidas incluyendo almac�n",
             * entonces busco que no existan "acciones de seleccion de partidas
             * excluyendo almacen".
             * De forma an�loga sucede si la acci�n es de selecci�n de partidas
             * excluyendo almac�n.
             */         
            int accion = Action_ID == Accion_Partidas_Seleccionar_excluyendo_Almacen ? 
                         Accion_Partidas_Seleccionar_incluyendo_Almacen : 
                         Accion_Partidas_Seleccionar_excluyendo_Almacen;
            //Busco permisos para seleccionar partidas exluyendo/incluyendo almacen
            QueryDB  query = new QueryDB("org.compiere.model.X_AD_Action_Permission");
            String filter = "AD_Role_ID = " + AD_Role_ID +
                     " AND AD_Workflow_ID = " + AD_Workflow_ID +
                     " AND Action_ID = " + accion +
                     " AND AD_Org_ID = " + AD_Org_ID;
            java.util.List results = query.execute(filter);
            Iterator select = results.iterator();
            while (select.hasNext()){
                if (accion==Accion_Partidas_Seleccionar_incluyendo_Almacen)
                    JOptionPane.showMessageDialog(null,"Ya existe un permiso de seleccion de partidas incluyendo almacen.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,"Ya existe un permiso de seleccion de partidas excluyendo almacen.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            return false;
        }
                
	
}	//	MRegimRetenPercepRecib

