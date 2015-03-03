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

import javax.swing.*;

import javax.swing.event.*;

import java.awt.*;

import qs.*;
import java.io.*;

import java.net.URL;

import java.awt.event.*;

import java.sql.*;

import java.math.*;

import java.text.*;

import java.util.*;

import org.compiere.util.*;

import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.apps.search.*;

import org.compiere.grid.ed.*;

import org.compiere.minigrid.*;

import org.compiere.model.*;

import org.compiere.swing.*;

import org.compiere.grid.ed.VLookup;
import org.compiere.grid.*;


/**
 * use as java simplesolver [params] prob_file
 *       for paremeters see usage routine 
 *      needs qsopt.jar on classpath 
 * 
 * @author Monika Mevenkamp, All Rights Reserved 
 */
class simplesolver {

   // the LP or MPS format file defining a problem 
   private String fname;
   
   // the file format is LP (default)
   private boolean isLpFile; 
   
   // use primal simplex algorithm to solve problem (default) 
   private boolean solvePrimal; 
   
   // print variable values (default optimal val only)
   private boolean printX=true; 
   
   // print a complete solution report
   private boolean printReport; 
   
   // turn on simplex tracing (default false)
   private boolean traceSimplex;
   
   // pricing strategy DANTZIG is default; 
   private int price; 
   private int DANTZIG = 0;
   private int STEEP = 1;
   private int DEVEX = 2;
   private   StringBuffer pba = new StringBuffer("");
   private String pba0 = "";
   public simplesolver() {
      fname = null;
      isLpFile = true;
      solvePrimal = true;
      printX = false;
      price = DANTZIG;
      traceSimplex = false;
   }

   public simplesolver(String av[]) throws QSException {
      this();
      getargs(av);
   }

   public StringBuffer solveProblem() throws QSException, IOException {
      int status = QS.LP_UNSOLVED;
 Problem prob = Problem.read(fname, !isLpFile);
//      Problem prob = new Problem(); //.read(fname, !isLpFile);
//      String pl ="problem \n p \n Minimize \n obj: \n 1x +2y \n subject to: \n c1: x+y >=5 \n End";
  //    Reader rd = "";
      
   //  rd.close();
    //  prob.read_lp(rd,pl);
      if (prob == null) {
         throw new QSException("Could not parse problem.");
      }

      if (traceSimplex) // trace simplex actions 
         prob.setparam(QS.PARAM_SIMPLEX_DISPLAY, 1); 

      if (solvePrimal) {
         if (price == DANTZIG) {
            prob.setparam(QS.PARAM_PRIMAL_PRICING_I, QS.PRIMAL_DANTZIG);
         } else {
            if (price == DEVEX) {
               prob.setparam(QS.PARAM_PRIMAL_PRICING_I, QS.PRIMAL_DEVEX);
            } else {
               prob.setparam(QS.PARAM_PRIMAL_PRICING_I, QS.PRIMAL_STEEP);
            }
         }
         prob.setparam(QS.PARAM_PRIMAL_PRICING_II,
                       prob.getparam(QS.PARAM_PRIMAL_PRICING_I));
         prob.opt_primal();
      } else { // solve dual 
         if (price == DANTZIG) {
            prob.setparam(QS.PARAM_DUAL_PRICING_I, QS.PRIMAL_DANTZIG);
         } else {
            if (price == DEVEX) {
               prob.setparam(QS.PARAM_DUAL_PRICING_I, QS.PRIMAL_DEVEX);
            } else {
               prob.setparam(QS.PARAM_DUAL_PRICING_I, QS.PRIMAL_STEEP);
            }
         }
         prob.setparam(QS.PARAM_DUAL_PRICING_II, 
                       prob.getparam(QS.PARAM_DUAL_PRICING_I));
         prob.opt_dual();
      }

      status = prob.get_status();
      if (status == QS.LP_OPTIMAL) {
//         System.out.println("Optimal\n\t" + prob.get_objname() +
//                            " = " + prob.get_objval());
//       
//         pba0 =pba0.valueOf(prob.get_objval());
//         pba.append(pba0);
//         System.out.println("pba " +pba);
        
         
        // prob.get_solution(double[] a,double[] b, double[] c, double[] d);

         if (printX) {
           // System.out.println("\nVariable Values (non zero only):");
         //   prob.print_x(new Reporter(System.out), true, 6);
            
            int nrows = prob.get_rowcount();
            double pi[] = new double[nrows];
            double slack[] = new double[nrows];
            String rows[] = new String[nrows];
            
            int ncols = prob.get_colcount();
            double x[] = new double[ncols];
            double rc[] = new double[ncols];

            String cols[] = new String[ncols];
            prob.get_rownames(rows);
            prob.get_colnames(cols);
            prob.get_solution(x, pi, slack, rc);
            
            
            for (int j = 0; j < ncols; j++) {
               if (x[j] != 0.0 || rc[j] != 0.0) {
                   
//                   String pba2="";
//                   String pba3="";
//                   pba3 = pba3.valueOf(cols[j]);
//                   pba2 = pba2.valueOf(x[j]);
//                   pba.append("," +pba3);
//                   pba.append("," +pba2);
//                  // pbaarray.add(0,pba2);
//                   System.out.println("ings "+pba2);
//                  System.out.println("\t" + cols[j] +
//                                    ": x = " + x[j] + "; " +
//                                     "rc = " + rc[j]);
               }
            }
           
         } 

         if (printReport) {
            int nrows = prob.get_rowcount();
            double pi[] = new double[nrows];
            double slack[] = new double[nrows];
            String rows[] = new String[nrows];
            
            int ncols = prob.get_colcount();
            double x[] = new double[ncols];
            double rc[] = new double[ncols];

            String cols[] = new String[ncols];
            prob.get_rownames(rows);
            prob.get_colnames(cols);
            prob.get_solution(x, pi, slack, rc);

           // System.out.println("Row information");
            for (int i = 0; i < nrows; i++) {
               if ((pi[i] != 0.0) || (slack[i] != 0.0)) {
//                  System.out.println("\t" + rows[i] + 
//                                     ": pi = " + pi[i] + "; " +
//                                     "slack = " + slack[i]);
               }
            }
         //   System.out.println("Column information");
            for (int j = 0; j < ncols; j++) {
               if (x[j] != 0.0 || rc[j] != 0.0) {
//                  System.out.println("\t" + cols[j] +
//                                     ": x = " + x[j] + "; " +
//                                     "rc = " + rc[j]);
               }
            }
         }
      }
               pba0 =pba0.valueOf(prob.get_objval());
         pba.append(pba0);
        // System.out.println("pba " +pba);
      
       int nrows = prob.get_rowcount();
            double pi[] = new double[nrows];
            double slack[] = new double[nrows];
            String rows[] = new String[nrows];
            
            int ncols = prob.get_colcount();
            double x[] = new double[ncols];
            double rc[] = new double[ncols];

            String cols[] = new String[ncols];
            prob.get_rownames(rows);
            prob.get_colnames(cols);
            prob.get_solution(x, pi, slack, rc);
            for (int j = 0; j < ncols; j++) {
               if (x[j] != 0.0 || rc[j] != 0.0) {
                   
                   String pba2="";
                   String pba3="";
                   pba3 = pba3.valueOf(cols[j]);
                   pba2 = pba2.valueOf(x[j]);
                   pba.append("," +pba3);
                   pba.append("," +pba2);
                  // pbaarray.add(0,pba2);
//                  System.out.println("ings "+pba2);
//                  System.out.println("\t" + cols[j] +
//                                    ": x = " + x[j] + "; " +
//                                     "rc = " + rc[j]);
              }
            }

      return pba;
   }

   public static void main(String av[]) {
      simplesolver solver = null;
        String [] problems = new String[] {"-X","/lp.txt"};
        
      try {
         solver = new simplesolver(problems);
      //   System.out.println("SOLVER: " + solver);
         solver.solveProblem();
      //   if (solver.solveProblem() == QS.LP_OPTIMAL) {
      //       funcpba();
//         pba =pba.valueOf(prob.get_objval());
//            System.out.println("prueba 2" +pba );
      //   } else {
         //   System.out.println("Problem maybe unbounded or infeasible.\n");
       //  }
      } catch (QSException e) {
         System.err.println(e.toString());
      } catch (IOException e) {
         System.err.println("Could not read file \"" + 
                            solver.fname + "\".\n");
         System.err.println(e);
      }
   }

   public static void usage() throws QSException {
      String msg;
      msg = "Usage: java simplesolver [- below -] prob_file\n";
      msg += "   -M     input file is in MPS format " +
                   "(default: LP format)\n";
      msg += "   -d     use dual simplex " + 
                   "(default: primal simplex)\n";
      msg += "   -X     print variable values " + 
                   "(default optimal value only)\n";
      msg += "   -R     print a solution report " + 
                   "(default no report)\n";
      msg += "   -T     trace simplex progress " + 
                   "(default no traces)\n";
      msg += "   -a     use DANTZIG pricing\n";
      msg += "   -s     use STEEP pricing in simplex " + 
                   "(default DANTZIG)\n";
      msg += "   -x     use DEVEX pricing in simplex " + 
                   "(default DANTZIG)\n";
      msg += "\n";
      msg += "  prob_file   contains the lp definition";
      throw new QSException(msg);
   }

   public void getargs(String av[]) throws QSException {
      try {
         int i = 0;
         while (av[i].charAt(0) == '-') {
            switch (av[i].charAt(1)) {
               case 'M' :
                  isLpFile = false;
                  break;
               case 'R' :
                  printReport = true;
                  break;
               case 'X' :
                  printX = true;
                  break;
               case 'd' :
                  solvePrimal = false;
                  break;
               case 'T' :
                  traceSimplex = true;
                  break;
               case 'a' :
                  price = DANTZIG;
                  break;
               case 's' :
                  price = STEEP;
                  break;
               case 'x' :
                  price = DEVEX;
                  break;
               default :
                  usage();
            }
            i++;
         }
         fname = av[i];
         if (fname == null) {
            usage();
         }
      } catch (ArrayIndexOutOfBoundsException e) {
         usage();
      }
   }


   public String toString() {
      String s = "File \"" + fname + "\"";
      if (!isLpFile)
         s += " MPS format;";
      if (!solvePrimal)
         s += " opt_dual;";
      if (!printX)
         s += " print var values; ";
      if (!printReport)
         s += " print sol report; ";
      if (price == DANTZIG)
         s += " DANTZIG;";
      if (price == DEVEX)
         s += " DEVEX;";
      if (price == STEEP)
         s += " STEEP;";
      return s;
   }
}
