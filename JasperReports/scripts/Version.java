package JasperReports.scripts;

import org.compiere.*;


class Version
{
	public static void  main(String args[])
	{
		int i=0;
		String version[];
		version = org.compiere.Compiere.MAIN_VERSION.split(" ");
		System.out.println(version[1]); 
	}
}
