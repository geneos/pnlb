/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports;

import org.zynnia.reports.table.ReportsTableModel;
import biz.source_code.miniTemplator.MiniTemplator;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.compiere.report.JasperViewer;

public class InformePostgres {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		int pageWidth = 595;

		ArrayList<String> titles = new ArrayList<String>();
		titles.add("Ciudad");
		titles.add("Id");
		titles.add("Nombre");
		titles.add("Calle");

		try {

			Random rnd = new Random();
			MiniTemplator t = new MiniTemplator("D:\\workspaceZynnia\\propalma\\data\\InformePostgres.jrxml");
			int point = 0;
			for(String title : titles) {
				t.setVariable("name_title", title);
				t.setVariable("pointx", String.valueOf(point));
				point = point + 63;
				t.addBlock ("block_titles");
			}

			ArrayList<String> row = new ArrayList<String>();
			row.add("the_city");
			row.add("id");
			row.add("name");
			row.add("street");

			point = 0;
			for(String col : row) {
				t.setVariable("value_field", col);
				t.setVariable("pointx", String.valueOf(point));
				point = point + 63;
				t.addBlock ("block_col");
			}
			
			String fileName = "InformePostgresTmp_" + rnd.nextInt();
			String pathJasperTemplate = "D:\\workspaceZynnia\\propalma\\data\\" + fileName + ".jrxml";
			t.generateOutput(pathJasperTemplate);

			long start = System.currentTimeMillis();

			JasperReport report = JasperCompileManager.compileReport(pathJasperTemplate);

			//Preparing parameters
			Map parameters = new HashMap();
			parameters.put("ReportTitle", "Address Report");
			parameters.put("Fecha", new Date());

			JasperPrint print = JasperFillManager.fillReport(report, parameters,  new JRTableModelDataSource(new ReportsTableModel()));
			System.err.println("Filling time : " + (System.currentTimeMillis() - start));

			// Exporta el informe a PDF
			JasperExportManager.exportReportToPdfFile(print, "D:\\workspaceZynnia\\propalma\\data\\" + fileName + ".pdf");
			//Para visualizar el pdf directamente desde java
			JasperViewer.viewReport(print);

			new File(pathJasperTemplate).delete();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
