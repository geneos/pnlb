package org.zynnia.reports;

/**
 *
 * @author alejandro
 */
/*
 * BarCode.java
 *
 * Created on January 23, 2008, 10:38 AM
 */

//import com.lowagie.text.Chunk;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Element;
//import com.lowagie.text.Image;
//import com.lowagie.text.Phrase;
//import com.lowagie.text.Rectangle;
//import com.lowagie.text.pdf.Barcode128;
//import com.lowagie.text.pdf.PdfContentByte;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import java.awt.Color;
//import java.io.*;
//import java.net.*;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.servlet.*;
//import javax.servlet.http.*;

/**
 *
 * @author lmcantu
 * @version
 */
public class BarCode {
//
//    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
//     * @param request servlet request
//     * @param response servlet response
//     */
//    public void processRequest()
//    throws ServletException, IOException {
//
//        int number = 25; // recibimos los parametros que necesitamos.
//        String location = "AmbaryMarco";
//        String prior = "0";
//        String value = new String(""+System.currentTimeMillis());//tomamos el tiempo porque es un numero que no se repite.
//
//        File temp = File.createTempFile( location+value, ".tmp", new File( "/home/alejandro/Escritorio" ) ); //creamos un archivo temporal
//        //temp.deleteOnExit();// Arreglamos para que se borren al salir.
//
//        Document document = new Document();
//        //la escritura del pdf la haremos dentro de try para capturar errores.
//
//        try {
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(temp));
//            document.open();
//            PdfContentByte cb = writer.getDirectContent();
//            /* Empezamos a usar itext para crear una tabla. */
//            PdfPTable page = new PdfPTable(3);    //tres columnas
//            page.getDefaultCell().setPadding(0f); //sin espacios
//            page.getDefaultCell().setBorder(Rectangle.NO_BORDER);//sin bordos
//            page.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);//alineado de izquierda a derecha
//            page.setWidthPercentage(110f);
//            /* Ciclo para generar cada codigo de barras, sin repetir el contenido*/
//            int a = 0;
//            for(int i = 0; i<number;i++){
//
//            PdfPTable cell = new PdfPTable(1);//hacemos una tabla para el codigo que haremos
//            cell.getDefaultCell().setBorder(Rectangle.NO_BORDER);//sin borde
//            cell.getDefaultCell().setPadding(40f); //medidas solicitadas
//
//            Barcode128 shipBarCode = new Barcode128();//usamos la clase Barcode128 de iText para generar la imagen
//            shipBarCode.setX(1f);//puedes modificar estas medidas para que veas como queda tu codigo de barras (mas grande, mas ancho, etcetera)
//            shipBarCode.setN(0.5f);
//            shipBarCode.setChecksumText(true);
//            shipBarCode.setGenerateChecksum(true);
//            shipBarCode.setSize(5f);
//            shipBarCode.setTextAlignment(Element.ALIGN_CENTER);//alineado al centro
//            shipBarCode.setBaseline(9f);
//            value = new String(""+System.currentTimeMillis());
//            if(a >9){//cada 9 codigos generamos un consecutivo
//                String ax = new String(""+System.currentTimeMillis());
//                while(value.substring(1,value.length()-3).equals(ax.substring(1,ax.length()-3))){//nos ciclamos hasta que el tiempo cambie.
//                ax = new String(""+System.currentTimeMillis());
//                }
//                a = 0;
//                value = ax;
//            }
//            shipBarCode.setCode(location+value.substring(1,value.length()-3)+a);//este es el valor que tendra el codigo de barras.
//            a++;
//            shipBarCode.setBarHeight(40f);//altura del codigo de barras
//
//            Image imgShipBarCode = shipBarCode.createImageWithBarcode(cb, Color.black, Color.BLACK);// convertimos este codigo en una imagen
//            Chunk cbc = new Chunk(imgShipBarCode, 0, 0);//la imagen del codigo de barras la ponemos en un chunk
//
//            Phrase p = new Phrase(cbc);//este chunk lo ponemos en un phrase.
//
//            PdfPCell c = new PdfPCell(p); //creamos una celda que contenga la frase P
//
//            c.setPaddingTop(23f); //medidas necesarias
//            c.setPaddingBottom(3f);
//            c.setPaddingLeft(0f);
//            c.setPaddingRight(5f);
//            c.setBorder(Rectangle.NO_BORDER);
//            c.setVerticalAlignment(Element.ALIGN_TOP);
//            c.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.addCell(c);//acregamos la celda a la tabla
//            page.addCell(cell); //la tabla a la tabla principal
//            }//seguimos en el ciclo!
//            document.add(page);
//            document.close();
//
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (DocumentException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void main(String[] argv) {
//        BarCode bc = new BarCode();
//        try {
//            bc.processRequest();
//        } catch (ServletException ex) {
//            Logger.getLogger(BarCode.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(BarCode.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
}
