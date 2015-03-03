/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * created by Victor Perez are Copyright (C) e-Evolution,SC. All Rights Reserved.
 * Contributor(s): Victor Perez
 *****************************************************************************/

package org.eevolution.form;


import java.math.BigDecimal;
import java.text.MessageFormat;
import org.eevolution.model.MMPCMRP;
import org.eevolution.model.MMPCOrderNode;
import org.eevolution.tools.DateTimeUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.resources.JFreeChartResources;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.ui.ApplicationFrame;
//import org.jfree.ui.RefineryUtilities;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.text.DateFormat;
import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.compiere.apps.*;
import org.compiere.impexp.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.print.*;
import org.compiere.db.*;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLocator;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VPAttribute;
import org.compiere.model.M_Table;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;

import org.eevolution.model.MMPCOrder;
//import org.eevolution.plaf.*;

public class VCRP extends CPanel
implements FormPanel, ActionListener
{
	// begin vpj
	private static CLogger log = CLogger.getCLogger(VCRP.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info( "VCRP.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			fillPicks();
			jbInit();
			/*dynInit();*/
			frame.getContentPane().add(northPanel, BorderLayout.NORTH);
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
			frame.pack();
			//frame.m_maximize=true;
			//frame.setMaximize(true);

		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VCRP.init", e);
		}
	}	//	init


	private CPanel northPanel = new CPanel();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private Hashtable hash = new Hashtable();

    private VLookup resource = null;
    private CLabel resourceLabel =  new CLabel();

    private VDate dateFrom = new VDate("DateFrom", true, false, true, DisplayType.Date, "DateFrom");
    private CLabel dateFromLabel =  new CLabel();
    private int AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
    //private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    //private JFreeChart chart;
    private ChartPanel chartPanel = new ChartPanel(createChart(new DefaultCategoryDataset(), "", null));
    //private CPanel chart = new CPanel();

	private void jbInit() throws Exception
	{

		northPanel.setLayout(new java.awt.GridBagLayout());

        resourceLabel.setText(Msg.translate(Env.getCtx(), "S_Resource_ID"));

        northPanel.add(resourceLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(resource ,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));

        northPanel.add(dateFromLabel,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        northPanel.add(dateFrom,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        chartPanel.setPreferredSize(new Dimension(750, 550));
		centerPanel.add(chartPanel, BorderLayout.CENTER);
        confirmPanel.addActionListener(this);
	}

	/**
	 *	Fill Picks
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void fillPicks() throws Exception
	{

                Properties ctx = Env.getCtx();
                //createChart(dataset);
                MLookup resourceL = MLookupFactory.get (ctx, m_WindowNo, 0, 1000148, DisplayType.TableDir);
                resource = new VLookup ("S_Resource_ID", false, false, true, resourceL);

	}	//	fillPicks

	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{

			 Timestamp date = (Timestamp) dateFrom.getValue();
			 int S_Resource_ID = ((Integer)resource.getValue()).intValue();
			 System.out.println("ConfirmPanel.A_OK");
			 System.out.println("date" + date + " S_Resource_ID " + S_Resource_ID);

			 if (date != null && S_Resource_ID != 0)
			 {
			 	System.out.println("Call createDataset(date,S_Resource_ID)");
			 	 MResource r = new MResource (Env.getCtx(), S_Resource_ID, null);
//	Ge�ndert Anfang 04.08.2005
			 	 int uom_id = r.getResourceType().getC_UOM_ID();
			 	 MUOM uom = new MUOM(Env.getCtx(),uom_id,null);

			 	 CategoryDataset dataset = null;
			 	 if(uom.isHour()) {
			 	 	System.out.println("\n ->is Hour<- \n");
			 	 	dataset = createDataset(date,r);
			 	 }
			 	 else {
			 	 	System.out.println("\n ->is not Hour<- \n");
			 	 	dataset = createWeightDataset(date,r);
			 	 }
//	Ge�ndert Ende 04.08.2005

			 	 //CategoryDataset dataset = createDataset();
			 	System.out.println("dataset.getRowCount:" +dataset.getRowCount());
			 	String title = r.getName() != null ? r.getName() : "";
			 	title = title +  " " + r.getDescription() != null ? r.getDescription() : "";
			 	JFreeChart jfreechart = createChart(dataset, title, uom);
			 	centerPanel.removeAll();
			 	chartPanel = new ChartPanel(jfreechart, false);
			 	centerPanel.add(chartPanel, BorderLayout.CENTER);
			 	centerPanel.setVisible(true);
			 	m_frame.pack();

			 }
		}
        if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
        {
             dispose();
        }
	}

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose





	private JFreeChart  createChart(CategoryDataset dataset  , String title, MUOM uom)
	{
		JFreeChart chart = ChartFactory.createBarChart3D(title," "," ",dataset,PlotOrientation.VERTICAL,true,true,false);

		//Hinzugef�gt Begin 05.08.2005
		if(uom == null || uom.isHour())
		{
			chart = ChartFactory.createBarChart3D
        ( title ,
          Msg.translate(Env.getCtx(), "Days"),            // X-Axis label
          Msg.translate(Env.getCtx(), "Hours"),             // Y-Axis label
          dataset,         // Dataset
		  PlotOrientation.VERTICAL, // orientation
          true,                     // include legend
          true,                     // tooltips?
          false                     // URLs?
        );



        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        //chart.setBackgroundPaint(Color.white);

		}
		//Ge�ndert 05.08.2005 Anfang
		else
		{
			chart = ChartFactory.createBarChart3D
	        ( title ,
	          Msg.translate(Env.getCtx(), "Days"),            // X-Axis label
	          Msg.translate(Env.getCtx(), "Kilo"),             // Y-Axis label
	          dataset,         // Dataset
			  PlotOrientation.VERTICAL, // orientation
	          true,                     // include legend
	          true,                     // tooltips?
	          false                     // URLs?
	        );

	        //chart.setBackgroundPaint(Color.white);


		}

		//Ge�ndert 05.08.2005 Ende

		/*
        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue,
            0.0f, 0.0f, new Color(0, 0, 64)
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green,
            0.0f, 0.0f, new Color(0, 64, 0)
        );
        GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red,
            0.0f, 0.0f, new Color(64, 0, 0)
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );*/
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }


// Added at 05.08.2005
// Begin
	public CategoryDataset createWeightDataset(Timestamp start, MResource r) {

		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTimeInMillis(start.getTime());
		gc1.clear(Calendar.MILLISECOND);
 		gc1.clear(Calendar.SECOND);
 		gc1.clear(Calendar.MINUTE);
 		gc1.clear(Calendar.HOUR_OF_DAY);

 		 String namecapacity = Msg.translate(Env.getCtx(), "Capacity");
 		 String nameload = Msg.translate(Env.getCtx(), "Load");
 		 String namesummary = Msg.translate(Env.getCtx(), "Summary");
 		 String namepossiblecapacity = "Possible Capacity";

 		 MResourceType t = new MResourceType(Env.getCtx(),r.getS_ResourceType_ID(),null);

		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		 double currentweight = DB.getSQLValue(null, "Select SUM( (mo.qtyordered-mo.qtydelivered)*(Select mp.weight From m_product mp Where mo.m_product_id=mp.m_product_id )  )From mpc_order mo Where ad_client_id=?", r.getAD_Client_ID());
		 // fjviejo e-evolution machineqty capacidad por el numero de maquinas
                // double dailyCapacity = DB.getSQLValue(null,"Select dailycapacity From s_resource Where s_resource_id=?",r.getS_Resource_ID());
                  double dailyCapacity = DB.getSQLValue(null,"Select dailycapacity*MachineQty From s_resource Where s_resource_id=?",r.getS_Resource_ID());
                    System.out.println("***** Capacidad diaria " +dailyCapacity);
                 // e-evolution end
		 double utilization = DB.getSQLValue(null, "Select percentutillization From s_resource Where s_resource_id=?", r.getS_Resource_ID());

	     double summary = 0;

	     int day = 0;

             /*
              *     Vit4B Modificado para que tome 28 dias y
              *
              *
              */


	     while(day < 29) {

 		 		day++;


 		 		switch(gc1.get(Calendar.DAY_OF_WEEK)) {

					case Calendar.SUNDAY:

						if (t.isOnSunday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.MONDAY:

						if (t.isOnMonday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.TUESDAY:

						if (t.isOnTuesday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.WEDNESDAY:

						if (t.isOnWednesday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.THURSDAY:

						if (t.isOnThursday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.FRIDAY:

						if (t.isOnFriday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;

					case Calendar.SATURDAY:

						if (t.isOnSaturday()) {

							currentweight -= (dailyCapacity*utilization)/100;
					        summary += ((dailyCapacity*utilization)/100);

							dataset.addValue(dailyCapacity ,namepossiblecapacity, new Integer(day));
							dataset.addValue((dailyCapacity*utilization)/100, namecapacity, new Integer(day) );
						}
						else {

							dataset.addValue(0,namepossiblecapacity, new Integer(day) );
							dataset.addValue(0, namecapacity, new Integer(day) );
						}

		 		 		break;
				}

 		 		dataset.addValue(currentweight, nameload, new Integer(day));
				dataset.addValue(summary, namesummary, new Integer(day) );

				gc1.add(Calendar.DATE, 1);
 		 }

	     return dataset;
 	}
// End

	public CategoryDataset createDataset(Timestamp start ,MResource r)
 	{
		 //System.out.println("Create new data set");
		 GregorianCalendar gc1 = new GregorianCalendar();
		 gc1.setTimeInMillis(start.getTime());
		 gc1.clear(Calendar.MILLISECOND);
 		 gc1.clear(Calendar.SECOND);
 		 gc1.clear(Calendar.MINUTE);
 		 gc1.clear(Calendar.HOUR_OF_DAY);

 		 Timestamp date = start;
 		 String namecapacity = Msg.translate(Env.getCtx(), "Capacity");
 		 System.out.println("\n Namecapacity :"+namecapacity);
 		 String nameload = Msg.translate(Env.getCtx(), "Load");
 		 System.out.println("\n Nameload :"+nameload);
 		 String namesummary = Msg.translate(Env.getCtx(), "Summary");
 		 System.out.println("\n Namesummary :"+namesummary);
		 MResourceType t = new MResourceType(Env.getCtx(),r.getS_ResourceType_ID(),null);
		 System.out.println("\n Resourcetype "+t);
		 int days = 1;
	     long hours = 0;

		 if (t.isTimeSlot())
                 {

                    hours = MMPCMRP.getHoursAvailable(t.getTimeSlotStart(),t.getTimeSlotEnd());

                 }
 		 else
                 {
                  //fjviejo e-evolution MachineQty
                    if (r.getDailyCapacity().multiply(r.getMachineQty()).compareTo(Env.ZERO) != 0)
                         hours = r.getDailyCapacity().multiply(r.getMachineQty()).longValue();
                    else
                  //fjviejo e-evolution end
 		 	hours  = 24;
                 }
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		 //		Long Hours = new Long(hours);
		 int C_UOM_ID = DB.getSQLValue(null,"SELECT C_UOM_ID FROM M_Product WHERE S_Resource_ID = ? " , r.getS_Resource_ID());
		 MUOM uom = new MUOM(Env.getCtx(),C_UOM_ID,null);
		 System.out.println("\n uom1 "+uom+"\n");
		 //System.out.println("um.isHour()"+ uom.isHour() );
	     if (!uom.isHour())
 	     {
	     	System.out.println("\n uom2 "+uom+"\n");
 			return dataset;
 		 }
	     System.out.println("\n Dataset "+dataset+"\n");
	     int summary = 0;
             //int cont = 1;
	     DateFormat formatter = DateFormat.getDateInstance();


             /*
              *     Vit4B Modificado para que tome 28 dias y
              *
              *
              */


             while(days < 29)
 		 {
                                /*
                                 *      VIT4B MODIFICACION PARA ACUMULADO DE DIAS
                                 *
                                 *
                                 *


 		 		//System.out.println("Day Number" + days);
 		 		String strday = formatter.format(date);
                                //String day = new String(new Integer (date.getDate()).toString());
                                String day = new String(formatter.format(date));
                                //day = day.substring(0,5);

                                int index = day.indexOf("/");

                                String str = day.substring(0,index);
                                System.out.println("str: " + str + " index de / " + index);

                                String str2 = day.substring(index+1,day.length());
                                System.out.println("str2 resto: " + str2);

                                index = str2.indexOf("/");
                                System.out.println(index);

                                String str3 = str2.substring(0,index);
                                System.out.println("str3: " + str3 + " index de / " + index);

                                String str4 = str2.substring(index+1,str2.length());
                                System.out.println(str4);

                                System.out.println("dia: " + str);
                                System.out.println("mes: " + str3);
                                System.out.println("a�o: " + str4);

                                //day = str + str3;

                                day = new String(str + str3);


                                //day = day.substring(0,day.lastIndexOf("/"));
                                //day =day.substring(0,3);

                                //cont++;

                                System.out.println("r.getS_Resource_ID()" + r.getS_Resource_ID());
                                System.out.println("Date: "  +  date + " day: "  +  day);
 		 		int seconds = getLoad(r.getS_Resource_ID(),date ,date);
 		 		Long Hours = new Long(hours);
 		 		System.out.println("Summary "+ summary);
                                System.out.println("Capacity "+ hours);
                                System.out.println("Load "+ seconds);

                                /*
                                 *
                                 *      VIT4B MODIFICACION PARA ACUMULADO DE DIAS
                                 *
                                 *
                                 */





                                /*
                                 *      ORIGINAL COMPIERE
                                 */


                                String day = new String(new Integer (date.getDate()).toString());
                                System.out.println("r.getS_Resource_ID()" + r.getS_Resource_ID());
                                System.out.println("Date:"  +  date);


                                /*
                                 *      VIT4B - Modificaci�n para calcular como se calcula el reporte CCRP
                                 *      en funcion de la carga parcial
                                 *
                                 *
                                 */


                                int seconds = getLoad(r.getS_Resource_ID(),date ,date);

                                //BigDecimal value = calculateLoad(date, r, null);
                                //int seconds = value.intValue();

                                Long Hours = new Long(hours);
 		 		System.out.println("Summary "+ summary);
                                System.out.println("Load "+ seconds);



                                /*
                                 *      FIN ORIGINAL COMPIERE
                                 *
                                 */


 		 		switch(gc1.get(Calendar.DAY_OF_WEEK))
				{
					case Calendar.SUNDAY:
						days ++;
						if (t.isOnSunday())
						{	//System.out.println("si Sunday");
							 //Msg.translate(Env.getCtx(), "OnSunday");
							dataset.addValue(hours, namecapacity, day );
							dataset.addValue(seconds / 3600,nameload, day );
					        dataset.addValue(summary, namesummary, day );
					        summary = summary + Hours.intValue() - (seconds / 3600); //+ (Hours.intValue() - ((seconds / 3600)));
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{	//System.out.println("no Sunday");
							//String day = Msg.translate(Env.getCtx(), "OnSunday") ;
							dataset.addValue(0, namecapacity, day );
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day );
							summary = summary - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
					case Calendar.MONDAY:
						days ++;
						if (t.isOnMonday())
						{		//System.out.println("si Monday");
								//String day = Msg.translate(Env.getCtx(), "OnMonday") ;
								dataset.addValue(hours, namecapacity, day );
								dataset.addValue(seconds / 3600, nameload, day );
						        dataset.addValue(summary, namesummary, day);
						        summary = summary + Hours.intValue() - (seconds / 3600);
						        gc1.add(Calendar.DATE, 1);
				 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
				 		 		break;
						}
						else
						{
								//System.out.println("no Monday");
								//String day = Msg.translate(Env.getCtx(), "OnMonday")  ;
								dataset.addValue(0, namecapacity, day );
								dataset.addValue(seconds / 3600, nameload, day );
								dataset.addValue(summary, namesummary, day );
								summary = summary  - (seconds / 3600);
								gc1.add(Calendar.DATE, 1);
				 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
				 		 		break;
						}
					case Calendar.TUESDAY:
						days ++;
						if (t.isOnTuesday())
						{	//System.out.println("si TuesDay");
							//String day = Msg.translate(Env.getCtx(), "OnTuesday");
							dataset.addValue(hours, namecapacity, day );
							dataset.addValue(seconds / 3600, nameload, day );
					        dataset.addValue(summary, namesummary, day );
					        summary = summary + Hours.intValue() - (seconds / 3600);
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{
							//System.out.println("no TuesDay");
							//String day = Msg.translate(Env.getCtx(), "OnTuesday");
							dataset.addValue(0, namecapacity, day );
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
					case Calendar.WEDNESDAY:
						days ++;
						if (t.isOnWednesday())
						{
							//String day = Msg.translate(Env.getCtx(), "OnWednesday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (seconds / 3600);
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{

							//String day = Msg.translate(Env.getCtx(), "OnWednesday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
					case Calendar.THURSDAY:
						days ++;
						if (t.isOnThursday())
						{
							//String day = Msg.translate(Env.getCtx(), "OnThursday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (seconds / 3600);
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{

							//String day = Msg.translate(Env.getCtx(), "OnThursday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary  - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
					case Calendar.FRIDAY:
						days ++;
						if (t.isOnFriday())
						{
							//String day = Msg.translate(Env.getCtx(), "OnFriday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
					        dataset.addValue(summary, namesummary, day);
					        summary = summary + Hours.intValue() - (seconds / 3600);
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{

							//String day = Msg.translate(Env.getCtx(), "OnFriday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
					case Calendar.SATURDAY:
						days ++;
						if (t.isOnSaturday())
						{
							//String day = Msg.translate(Env.getCtx(), "OnSaturday");
							dataset.addValue(hours, namecapacity, day);
							dataset.addValue(seconds / 3600,nameload, day);
					        dataset.addValue(summary,namesummary, day);
					        summary = summary + Hours.intValue() - (seconds / 3600);
					        gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
						else
						{
							//String day = Msg.translate(Env.getCtx(), "OnSaturday");
							dataset.addValue(0, namecapacity, day);
							dataset.addValue(seconds / 3600, nameload, day);
							dataset.addValue(summary, namesummary, day);
							summary = summary - (seconds / 3600);
							gc1.add(Calendar.DATE, 1);
			 		 		date = org.compiere.util.TimeUtil.addDays(date,1);
			 		 		break;
						}
				}

 		 }
 		return dataset;
 	}

	int getLoad(int S_Resource_ID, Timestamp start, Timestamp end)
 	{
 		int load = 0;

                String sql = "SELECT SUM( CASE WHEN ow.DurationUnit = 's'  THEN 1 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'm' THEN 60 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'h'  THEN 3600 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'Y'  THEN 31536000 *  (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'M' THEN 2592000 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'D' THEN 86400 END ) AS Load FROM MPC_Order_Node onode INNER JOIN MPC_Order_Workflow ow ON (ow.MPC_Order_Workflow_ID =  onode.MPC_Order_Workflow_ID) INNER JOIN MPC_Order o ON (o.MPC_Order_ID = onode.MPC_Order_ID)  WHERE onode.S_Resource_ID = ?  AND onode.AD_Client_ID = ? AND  trunc(?) BETWEEN trunc(onode.DateStartSchedule) AND trunc(onode.DateFinishSchedule)" ;

                String sql2 = "SELECT SUM( CASE WHEN ow.DurationUnit = 's'  THEN 1 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'm' THEN 60 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'h'  THEN 3600 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'Y'  THEN 31536000 *  (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'M' THEN 2592000 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'D' THEN 86400 END ) AS Load FROM MPC_Order_Node onode INNER JOIN MPC_Order_Workflow ow ON (ow.MPC_Order_Workflow_ID =  onode.MPC_Order_Workflow_ID) INNER JOIN MPC_Order o ON (o.MPC_Order_ID = onode.MPC_Order_ID)  WHERE onode.S_Resource_ID = ?  AND onode.AD_Client_ID = ? AND  trunc('" + start + "') BETWEEN trunc(onode.DateStartSchedule) AND trunc(onode.DateFinishSchedule)" ;


 		//String sql = "SELECT SUM( CASE WHEN ow.DurationUnit = 's'  THEN 1 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'm' THEN 60 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'h'  THEN 3600 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'Y'  THEN 31536000 *  (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'M' THEN 2592000 * (onode.QueuingTime + onode.SetupTime + (onode.Duration * (o.QtyOrdered - o.QtyDelivered - o.QtyScrap)) + onode.MovingTime + onode.WaitingTime) WHEN ow.DurationUnit = 'D' THEN 86400 END ) AS Load FROM MPC_Order_Node onode INNER JOIN MPC_Order_Workflow ow ON (ow.MPC_Order_Workflow_ID =  onode.MPC_Order_Workflow_ID) INNER JOIN MPC_Order o ON (o.MPC_Order_ID = onode.MPC_Order_ID)  WHERE onode.S_Resource_ID = ?  AND onode.AD_Client_ID = ? AND  trunc(onode.DateStartSchedule) = ?" ;
 		System.out.println("SQL SUM :" + sql2);
        try
		{
                PreparedStatement pstmt = null;
                pstmt = DB.prepareStatement (sql);
                pstmt.setInt(1, S_Resource_ID);
                pstmt.setInt(2, AD_Client_ID);
                pstmt.setTimestamp(3, start);
                System.out.println("Params SQL :" + S_Resource_ID + " " + AD_Client_ID + " " + start);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
	 			 load = rs.getInt(1);
                }
	 		    rs.close();
	            pstmt.close();
	            return load;
			}
	        catch (Exception e)
			{
	        	log.log(Level.SEVERE,"doIt - " + sql, e);
			}
	 			return 0;
 	}


        /*
         *
         *
         *
         */


        public boolean isResourceAvailable(Timestamp dateTime, MResource r) {

            MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);

            return ( checkResourceAvailability(dateTime, r) && checkResourceTypeAvailability(dateTime, t) );
        }


        public boolean checkResourceAvailability(Timestamp dateTime, MResource r) {

		int[] ids = PO.getAllIDs("S_ResourceUnAvailable", "S_Resource_ID = "+r.get_ID() + " AND AD_Client_ID = " + r.getAD_Client_ID(), null);

		Timestamp dateFrom = null;
		Timestamp dateTo = null;
		Timestamp dateActual = null;

		MResourceUnAvailable rua = null;
		for(int i = 0; i < ids.length; i++) {

			rua = new MResourceUnAvailable(Env.getCtx(), ids[i], null);

			dateFrom = DateTimeUtil.getDayBorder(rua.getDateFrom(), null, false);
			dateTo = DateTimeUtil.getDayBorder(rua.getDateTo(), null, true);
			dateActual = DateTimeUtil.getDayBorder(dateTime, null, false);

			if(dateFrom.compareTo(dateActual) <= 0 && dateTo.compareTo(dateActual) >= 0 ) {

				return false;
			}
		}

		return true;
	}

        public boolean checkResourceTypeAvailability(Timestamp dateTime, MResourceType t) {

		if(!t.isDateSlot()) {

			return true;
		}

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(dateTime.getTime());

		boolean retValue = false;
	 	switch(gc.get(Calendar.DAY_OF_WEEK)) {

	 		case Calendar.SUNDAY:
	 			retValue = t.isOnSunday();
	 			break;

			case Calendar.MONDAY:
				retValue = t.isOnMonday();
				break;

			case Calendar.TUESDAY:
				retValue = t.isOnTuesday();
				break;

			case Calendar.WEDNESDAY:
				retValue = t.isOnWednesday();
				break;

			case Calendar.THURSDAY:
				retValue = t.isOnThursday();
				break;

			case Calendar.FRIDAY:
				retValue = t.isOnFriday();
				break;

			case Calendar.SATURDAY:
				retValue = t.isOnSaturday();
				break;
			}

	 	return retValue;
	}



	/**
	 * All the below cases expect exactly two parameters: The (1) begin and the (2) end of a day
	 */

	/**
	 * Case 1: The time dependent process has already begun and ends at this day.
	 */
	public static final String RESTRICTION_DAY_CASE_1 =
		"(datestartschedule<=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 2: The time dependent process begins and ends at this day.
	 */
	public static final String RESTRICTION_DAY_CASE_2 =
		"(datestartschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datestartschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 3: The time dependent process begins at this day and ends few days later.
	 */
	public static final String RESTRICTION_DAY_CASE_3 =
		"(datestartschedule>=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datestartschedule<=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

	/**
	 * Case 4: The time dependent process has already begun and ends few days later.
	 */
	public static final String RESTRICTION_DAY_CASE_4 =
		"(datestartschedule<=to_timestamp(''{0}'',''YYYY-MM-DD HH24:MI:SSXFF'') AND datefinishschedule>=to_timestamp(''{1}'',''YYYY-MM-DD HH24:MI:SSXFF''))";

        public Timestamp getBorderDayMin(Timestamp dateTime, MResource r) {

            MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
            Timestamp dMin = null;
            return (t.isTimeSlot()) ?
                            DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotStart(), false) :
                            DateTimeUtil.getDayBorder(dateTime, null, false);
        }

        public Timestamp getBorderDayMax(Timestamp dateTime, MResource r) {

            MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
            Timestamp dMin = null;
            return (t.isTimeSlot()) ?
                            DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotEnd(), true) :
                            DateTimeUtil.getDayBorder(dateTime, null, true);
        }



	private String getDayRestriction(Timestamp dateTime, MResource r) {

		Object[] params = { getBorderDayMin(dateTime, r).toString(), getBorderDayMax(dateTime, r).toString() };

		return
			MessageFormat.format(RESTRICTION_DAY_CASE_1, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_2, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_3, params)+
			" OR "+MessageFormat.format(RESTRICTION_DAY_CASE_4, params);
	}


        public MMPCOrderNode[] getMPCOrderNodes(Timestamp dateTime, MResource r) {

            if(!isResourceAvailable(dateTime, r)) {

                    return new MMPCOrderNode[0];
            }

            String where =
                     "s_resource_id = "+r.get_ID()
                     +" AND (" + getDayRestriction(dateTime, r)+") AND AD_Client_ID = " + r.getAD_Client_ID();
             log.log(Level.FINE,"getMPCOrderNodes --> Where:" + where);

             System.out.println("WHERE MPC_Order_Node " + where);

             int[] ids = PO.getAllIDs("MPC_Order_Node", where, null);

             MMPCOrderNode[] nodes = new MMPCOrderNode[ids.length];
             for(int i = 0; i < ids.length; i++) {

                     nodes[i] = new MMPCOrderNode(Env.getCtx(), ids[i], null);
             }

             return nodes;
        }


	protected BigDecimal calculateLoad(Timestamp dateTime, MResource r, String docStatus) {

		MResourceType t = new MResourceType(Env.getCtx(), r.getS_ResourceType_ID(), null);
		MMPCOrderNode[] nodes = getMPCOrderNodes(dateTime, r);
		MUOM uom = new MUOM(Env.getCtx(), t.getC_UOM_ID(), null);

		MMPCOrder o = null;
		BigDecimal qtyOpen;
		long millis = 0l;
		for(int i = 0; i < nodes.length; i++) {
			o = new MMPCOrder(Env.getCtx(), nodes[i].getMPC_Order_ID(), null);
			if(docStatus != null && !o.getDocStatus().equals(docStatus)) {

				continue;
			}

			millis += calculateMillisForDay(dateTime, nodes[i], t);
		}

		// Pre-converts to minutes, because its the lowest time unit of compiere
		BigDecimal scale = new BigDecimal(1000*60);
		BigDecimal minutes = new BigDecimal(millis).divide(scale, 2, BigDecimal.ROUND_HALF_UP);
		//return convert(minutes);
                return minutes;
	}

	protected Timestamp[] getDayBorders(Timestamp dateTime, MMPCOrderNode node, MResourceType t) {

		Timestamp endDayTime = null;
 		// The theoretical latest time on a day, where the work ends, dependent on
		// the resource type's time slot value
		if(t.isTimeSlot()) {

 			endDayTime = DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotEnd(), true);
 		}
 		else {

 			endDayTime = DateTimeUtil.getDayBorder(dateTime, null, true);
 		}

		// Initialize the end time to the present, if the work ends at this day. Otherwise
		// the earliest possible start time for a day is set.
		endDayTime = (endDayTime.before(node.getDateFinishSchedule())) ? endDayTime : node.getDateFinishSchedule();

		Timestamp startDayTime = null;
 		// The theoretical earliest time on a day, where the work begins, dependent on
		// the resource type's time slot value
		if(t.isTimeSlot()) {

 			startDayTime = DateTimeUtil.getDayBorder(dateTime, t.getTimeSlotStart(), false);
 		}
 		else {

 			startDayTime = DateTimeUtil.getDayBorder(dateTime, null, false);
 		}

		// Initialize the end time to the present, if the work begins at this day. Otherwise
		// the earliest possible start time for a day is set.
		startDayTime = (startDayTime.after(node.getDateStartSchedule())) ? startDayTime : node.getDateStartSchedule();

		return new Timestamp[] {startDayTime, endDayTime};
	}

	protected long calculateMillisForDay(Timestamp dateTime, MMPCOrderNode node, MResourceType t) {

		Timestamp[] borders = getDayBorders(dateTime, node, t);
		return DateTimeUtil.getTimeDifference(borders[0], borders[1]);
	}







}
