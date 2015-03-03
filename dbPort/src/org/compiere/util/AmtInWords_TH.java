/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.util;


/**
 *      Amount in Words for Thai
 *
 *  @author Sureeraya Limpaibul - http://www.rgagnon.com/javadetails/java-0426.html
 *  @version $Id: AmtInWords_TH.java,v 1.3 2005/09/16 00:49:04 jjanke Exp $
 */
public class AmtInWords_TH implements AmtInWords
{
        /**
         *      AmtInWords_TH
		 *
		 *	0.23 = ศูนย์ 23/100
		 *	1.23 = หนึ่งบาท 23/100
		 *	11.45 = สิบเอ็ดบาท 45/100
		 *	121.45 = หนึ่งร้อยยี่สิบเอ็ดบาท 45/100
		 *	1231.56 = หนึ่งพันสองร้อยสามสิบเอ็ดบาท 56/100
		 *	12341.78 = หนึ่งหมื่นสองพันสามร้อยสี่สิบเอ็ดบาท 78/100
		 *	123451.89 = หนึ่งแสนสองหมื่นสามพันสี่ร้อยห้าสิบเอ็ดบาท 89/100
		 *	12234571.90 = สิบสองล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	123234571.90 = หนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	1987234571.90 = หนึ่งพันเก้าร้อยแปดสิบเจ็ดล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	11123234571.90 = หนึ่งหมื่นหนึ่งพันหนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	123123234571.90 = หนึ่งแสนสองหมื่นสามพันหนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	2123123234571.90 = สองล้าน หนึ่งแสนสองหมื่นสามพันหนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	23,123,123,234,571.90 = ยี่สิบสามล้าน หนึ่งแสนสองหมื่นสามพันหนึ่งร้อยยี่สิบสามล้าน สองแสนสามหมื่นสี่พันห้าร้อยเจ็ดสิบเอ็ดบาท 90/100
		 *	100,000,000,000,000.90 = หนึ่งร้อยล้านล้าน บาท 90/100
		 *	0.00 = ศูนย์ 00/100
		 *
         */
        public AmtInWords_TH ()
        {
                super ();
        }       //      AmtInWords_TH

        private static final String[]   majorNames      = {
                "",
                "\u0e25\u0e49\u0e32\u0e19 ",
                };
        private static final String[]   hundredThousandNames    = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e32\u0e21\u0e41\u0e2a\u0e19",
                "\u0e2a\u0e35\u0e48\u0e41\u0e2a\u0e19",
                "\u0e2b\u0e49\u0e32\u0e41\u0e2a\u0e19",
                "\u0e2b\u0e01\u0e41\u0e2a\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e41\u0e2a\u0e19",
                "\u0e41\u0e1b\u0e14\u0e41\u0e2a\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e41\u0e2a\u0e19"
        };

        private static final String[]   tenThousandNames        = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e32\u0e21\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2a\u0e35\u0e48\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2b\u0e49\u0e32\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e2b\u0e01\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e41\u0e1b\u0e14\u0e2b\u0e21\u0e37\u0e48\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e2b\u0e21\u0e37\u0e48\u0e19"
       };

       private static final String[]    thousandNames   = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e2d\u0e07\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e32\u0e21\u0e1e\u0e31\u0e19",
                "\u0e2a\u0e35\u0e48\u0e1e\u0e31\u0e19",
                "\u0e2b\u0e49\u0e32\u0e1e\u0e31\u0e19",
                "\u0e2b\u0e01\u0e1e\u0e31\u0e19",
                "\u0e40\u0e08\u0e47\u0e14\u0e1e\u0e31\u0e19",
                "\u0e41\u0e1b\u0e14\u0e1e\u0e31\u0e19",
                "\u0e40\u0e01\u0e49\u0e32\u0e1e\u0e31\u0e19"
       };

       private static final String[]    hundredNames    = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e2d\u0e07\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e32\u0e21\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2a\u0e35\u0e48\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2b\u0e49\u0e32\u0e23\u0e49\u0e2d\u0e22",
                "\u0e2b\u0e01\u0e23\u0e49\u0e2d\u0e22",
                "\u0e40\u0e08\u0e47\u0e14\u0e23\u0e49\u0e2d\u0e22",
                "\u0e41\u0e1b\u0e14\u0e23\u0e49\u0e2d\u0e22",
                "\u0e40\u0e01\u0e49\u0e32\u0e23\u0e49\u0e2d\u0e22"
                };

       private static final String[]    tensNames       = {
                "",
                "\u0e2a\u0e34\u0e1a",
                "\u0e22\u0e35\u0e48\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e32\u0e21\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e35\u0e48\u0e2a\u0e34\u0e1a",
                "\u0e2b\u0e49\u0e32\u0e2a\u0e34\u0e1a",
                "\u0e2b\u0e01\u0e2a\u0e34\u0e1a",
                "\u0e40\u0e08\u0e47\u0e14\u0e2a\u0e34\u0e1a",
                "\u0e41\u0e1b\u0e14\u0e2a\u0e34\u0e1a",
                "\u0e40\u0e01\u0e49\u0e32\u0e2a\u0e34\u0e1a"
        };

        private static final String[]   numNames        = {
                "",
                "\u0e2b\u0e19\u0e36\u0e48\u0e07",
                "\u0e2a\u0e2d\u0e07",
                "\u0e2a\u0e32\u0e21",
                "\u0e2a\u0e35\u0e48",
                "\u0e2b\u0e49\u0e32",
                "\u0e2b\u0e01",
                "\u0e40\u0e08\u0e47\u0e14",
                "\u0e41\u0e1b\u0e14",
                "\u0e40\u0e01\u0e49\u0e32",
                "\u0e2a\u0e34\u0e1a",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e2d\u0e47\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e2d\u0e07",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e32\u0e21",
                "\u0e2a\u0e34\u0e1a\u0e2a\u0e35\u0e48",
                "\u0e2a\u0e34\u0e1a\u0e2b\u0e49\u0e32",
                "\u0e2a\u0e34\u0e1a\u0e2b\u0e01",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e08\u0e47\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e41\u0e1b\u0e14",
                "\u0e2a\u0e34\u0e1a\u0e40\u0e01\u0e49\u0e32"
                };

        /**
         *      Convert Less Than One Thousand
         *      @param number
         *      @return amt
         */
        private String convertLessThanOneMillion (int number)
        {
                String soFar;
                // Esta dentro de los 1os. diecinueve?? ISCAP
                if (number % 100 < 20)
                {
                        soFar = numNames[(number % 100)];
                        number /= 100;
                }
                else
                {
                        soFar = numNames[number % 10];
                        if (number != 1 && soFar.equals("\u0e2b\u0e19\u0e36\u0e48\u0e07")) {
                              soFar = "\u0e40\u0e2d\u0e47\u0e14";
                        }
                        number /= 10;

                        soFar = tensNames[number % 10] + soFar;
                        number /= 10;

                        soFar = hundredNames[number % 10] + soFar;
                        number /= 10;

                        soFar = thousandNames[number % 10] + soFar;
                        number /= 10;

                        soFar = tenThousandNames[number % 10] + soFar;
                        number /= 10;

                        soFar = hundredThousandNames[number % 10] + soFar;
                        number /= 10;

                }
                if (number == 0)
                        return soFar;

                return numNames[number] + "\u0e23\u0e49\u0e2d\u0e22\u0e25\u0e49\u0e32\u0e19" + soFar;
        }       //      convertLessThanOneThousand

        /**
         *      Convert
         *      @param number
         *      @return amt
         */
        private String convert (double number)
        {
                /* special case */
                if (number == 0)
                {
                        return "\u0e28\u0e39\u0e19\u0e22\u0e4c";
                }
                String prefix = "";
                String subfix = "\u0e1a\u0e32\u0e17";

                if (number < 0)
                {
                        number = -number;
                        prefix = "\u0e25\u0e1a ";
                }
                String soFar = "";
                int place = 0;
                do
                {
                        double d =  number % 1000000;
                        int n =  (int) d;
                        if (n != 0)
                        {
                                String s = convertLessThanOneMillion (n);
                                place = place > 0 ? 1 : 0;

                                soFar = s + majorNames[place] + soFar;
                        }
                        place++;
                        number /= 1000000d;
                } while (number > 0);

                return (prefix + soFar + subfix).trim ();
        }       //      convert


        /***********************************************************************
***
         *      Get Amount in Words
         *      @param amount numeric amount (352.80)
         *      @return amount in words (three*five*two 80/100)
         */
        public String getAmtInWords (String amount) throws Exception
        {
                if (amount == null)
                        return amount;
                //
                StringBuffer sb = new StringBuffer ();
                double pos = (double)amount.lastIndexOf ('.');
                double pos2 = (double)amount.lastIndexOf (',');
                if (pos2 > pos)
                        pos = pos2;
                String oldamt = amount;
                amount = amount.replaceAll (",", "");
                int newpos = amount.lastIndexOf ('.');
                double pesos = Double.parseDouble (amount.substring (0, newpos))
;
                sb.append (convert (pesos));
                for (int i = 0; i < oldamt.length (); i++)
                {
                        if (pos == i) //        we are done
                        {
                                String cents = oldamt.substring (i + 1);
                                sb.append (' ').append (cents).append ("/100");
                                break;
                        }
                }
                return sb.toString ();
        }       //      getAmtInWords

        /**
         *      Test Print
         *      @param amt amount
         */
        private void print (String amt)
        {
                try
                {
                        System.out.println(amt + " = " + getAmtInWords(amt));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }       //      print

        /**
         *      Test
         *      @param args ignored
         */
        public static void main (String[] args)
        {
                AmtInWords_TH aiw = new AmtInWords_TH();
        //      aiw.print (".23");      Error
                aiw.print ("0.23");
                aiw.print ("1.23");
                aiw.print ("11.45");
                aiw.print ("121.45");
                aiw.print ("1231.56");
                aiw.print ("12341.78");
                aiw.print ("123451.89");
                aiw.print ("12234571.90");
                aiw.print ("123234571.90");
                aiw.print ("1987234571.90");
                aiw.print ("11123234571.90");
                aiw.print ("123123234571.90");
                aiw.print ("2123123234571.90");
                aiw.print ("23,123,123,234,571.90");
                aiw.print ("100,000,000,000,000.90");
                aiw.print ("0.00");
        }       //      main

}       //      AmtInWords_TH