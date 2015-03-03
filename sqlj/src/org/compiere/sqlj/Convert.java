package org.compiere.sqlj;

public class Convert {

	public static String num_to_word(Integer num){
        return doThings(num.intValue());
    }
     
    private static String doThings(int _counter)
    {
        //Limite
        if(_counter >2000000)
            return "DOS MILLONES";
         
        switch(_counter){
            case 0: return "CERO";
            case 1: return "UN"; //UNO
            case 2: return "DOS";
            case 3: return "TRES";
            case 4: return "CUATRO";
            case 5: return "CINCO";
            case 6: return "SEIS";
            case 7: return "SIETE";
            case 8: return "OCHO";
            case 9: return "NUEVE";
            case 10: return "DIEZ";
            case 11: return "ONCE";
            case 12: return "DOCE";
            case 13: return "TRECE";
            case 14: return "CATORCE";
            case 15: return "QUINCE";
            case 20: return "VEINTE";
            case 30: return "TREINTA";
            case 40: return "CUARENTA";
            case 50: return "CINCUENTA";
            case 60: return "SESENTA";
            case 70: return "SETENTA";
            case 80: return "OCHENTA";
            case 90: return "NOVENTA";
            case 100: return "CIEN";
            case 200: return "DOSCIENTOS";
            case 300: return "TRESCIENTOS";
            case 400: return "CUATROCIENTOS";
            case 500: return "QUINIENTOS";
            case 600: return "SEISCIENTOS";
            case 700: return "SETECIENTOS";
            case 800: return "OCHOCIENTOS";
            case 900: return "NOVECIENTOS";
             
            case 1000: return "MIL";
         
            case 1000000: return "UN MILLON";
            case 2000000: return "DOS MILLONES";
        }
        if(_counter<20){
            //System.out.println(">15");
            return "DIECI"+ doThings(_counter-10);
        }
        if(_counter<30){
            //System.out.println(">20");
            return "VEINTI" + doThings(_counter-20);
        }
        if(_counter<100){
            //System.out.println("<100");
            return doThings( (int)(_counter/10)*10 ) + " Y " + doThings(_counter%10);
        }       
        if(_counter<200){
        	//System.out.println("<200");
            return "CIENTO " + doThings( _counter - 100 );
        }        
        if(_counter<1000){
            //System.out.println("<1000");
            return doThings( (int)(_counter/100)*100 ) + " " + doThings(_counter%100);
        }
        if(_counter<2000){
            //System.out.println("<2000");
            return "MIL " + doThings( _counter % 1000 );
        }
        if(_counter<1000000){
            String var="";
            //System.out.println("<1000000");
            var = doThings((int)(_counter/1000)) + " MIL" ;
            if(_counter % 1000!=0){
                //System.out.println(var);
                var += " " + doThings(_counter % 1000);
            }
            return var;
        }
        if(_counter<2000000){
            return "UN MILLON " + doThings( _counter % 1000000 );
        }
        return "";
    }
}
