/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import javax.jnlp.*;
import javax.swing.*;
import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.util.*;
import com.qoppa.pdf.*;
import org.zynnia.utils.Version;

/**
 *  Compiere Control Class
 *
 *  @author Jorg Janke
 *  @version $Id: Compiere.java,v 1.96 2005/12/31 06:33:21 jjanke Exp $
 */
public final class Compiere {

    /** Timestamp                   */
    static public final String ID = "$Id: Compiere.java,v 1.96 2005/12/31 06:33:21 jjanke Exp $";
    /** Main Version String         */
    static public final String MAIN_VERSION = "Release 3.3.0.0";
    /** Main Version Numeric         */
    static public final String MAIN_CLIENT_VERSION = "3.5.0.23";
    /** Detail Version as date      Used for Client/Server		*/
    static public final String DATE_VERSION = "2005-12-30";
    /** Database Version as date    Compared with AD_System		*/
    static public final String DB_VERSION = "2005-12-30";
    /** Product Name            */
    static public final String NAME = "Compiere\u00AE";
    /** URL of Product          */
    static public final String URL = "www.compiere.org";
    /** 16*16 Product Image.
    /** Removing/modifying the Compiere logo is a violation of the license	*/
    static private final String s_File16x16 = "images/C16.gif";
    /** 32*32 Product Image.
    /** Removing/modifying the Compiere logo is a violation of the license	*/
    static private final String s_file32x32 = "images/C32.gif";
    /** 100*30 Product Image.
    /** Removing/modifying the Compiere logo is a violation of the license	*/
    static private final String s_file100x30 = "images/C10030.png";
    /** 48*15 Product Image.
    /** Removing/modifying the Compiere logo is a violation of the license	*/
    static private final String s_file48x15 = "images/Compiere.png";
    /** Support Email           */
    static private String s_supportEmail = "";
    /** Subtitle                */
    static public final String SUB_TITLE = " Smart ERP, CRM & MRP ";
    /** Subtitle                */
    //static public final String	SUB_TITLE		= " Smart ERP, & CRM ";
    /** Compiere is a wordwide registered Trademark
     *  - Don't modify this - Program will fail unexpectedly	*/
    static public final String COMPIERE_R = "Compiere\u00AE";
    /** Copyright Notice   - Don't modify this - Program will fail unexpectedly
     *  it also violates the license and you'll be held liable for any damage claims */
    //static public final String	COPYRIGHT		= "\u00A9 1999-2005 Compiere \u00AE";
    static public final String COPYRIGHT = "\u00A9 1999-2009 Compiere \u00AE";
    static private String s_ImplementationVersion = null;
    static private String s_ImplementationVendor = null;
    static private Image s_image16;
    static private Image s_image48x15;
    static private Image s_imageLogo;
    static private ImageIcon s_imageIcon32;
    static private ImageIcon s_imageIconLogo;
    /**	Logging								*/
    private static CLogger log = null;

    /**
     *  Get Product Name
     *  @return Application Name
     */
    public static String getName() {
        return NAME;
    }   //  getName

    /**
     *  Get Product Version
     *  @return Application Version
     */
    public static String getVersion() {
        return MAIN_VERSION + " - " + DATE_VERSION;
    }   //  getVersion

    /**
     *  Get Product Version
     *  @return Application Version
     */
    public static Version getClientVersion() {
        return Version.parse(MAIN_CLIENT_VERSION);
    }   //  getVersion

    /**
     *	Short Summary (Windows)
     *  @return summary
     */
    public static String getSum() {
        StringBuilder sb = new StringBuilder();
        sb.append(NAME).append(" ").append(MAIN_VERSION).append(SUB_TITLE);
        return sb.toString();
    }	//	getSum

    /**
     *	Summary (Windows).
     * 	Removing/modifying the Compiere copyright notice is a violation of the license
     *	Compiere(tm) Version 2.5.1a_2004-03-15 - Smart ERP & CRM - Copyright (c) 1999-2005 Jorg Janke; Implementation: 2.5.1a 20040417-0243 - (C) 1999-2005 Jorg Janke, ComPiere Inc. USA
     *  @return Summary in Windows character set
     */
    public static String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(NAME).append(" ").append(MAIN_VERSION).append("_").append(DATE_VERSION).append(" -").append(SUB_TITLE).append("- ").append(COPYRIGHT).append("; Implementation: ").append(getImplementationVersion()).append(" - ").append(getImplementationVendor());
        return sb.toString();
    }	//	getSummary

    /**
     * 	Set Package Info
     */
    private static void setPackageInfo() {
        if (s_ImplementationVendor != null) {
            return;
        }

        Package compierePackage = Package.getPackage("org.compiere");
        s_ImplementationVendor = compierePackage.getImplementationVendor();
        s_ImplementationVersion = compierePackage.getImplementationVersion();
        if (s_ImplementationVendor == null) {
            s_ImplementationVendor = "not supported";
            s_ImplementationVersion = "unknown";
        }
    }	//	setPackageInfo

    /**
     * 	Get Jar Implementation Version
     * 	@return Implementation-Version
     */
    public static String getImplementationVersion() {
        if (s_ImplementationVersion == null) {
            setPackageInfo();
        }
        return s_ImplementationVersion;
    }	//	getImplementationVersion

    /**
     * 	Get Jar Implementation Vendor
     * 	@return Implementation-Vendor
     */
    public static String getImplementationVendor() {
        if (s_ImplementationVendor == null) {
            setPackageInfo();
        }
        return s_ImplementationVendor;
    }	//	getImplementationVendor

    /**
     *  Get Checksum
     *  @return checksum
     */
    public static int getCheckSum() {
        return getSum().hashCode();
    }   //  getCheckSum

    /**
     *	Summary in ASCII
     *  @return Summary in ASCII
     */
    public static String getSummaryAscii() {
        String retValue = getSummary();
        //  Registered Trademark
        retValue = Util.replace(retValue, "\u00AE", "(r)");
        //  Trademark
        retValue = Util.replace(retValue, "\u2122", "(tm)");
        //  Copyright
        retValue = Util.replace(retValue, "\u00A9", "(c)");
        //  Cr
        retValue = Util.replace(retValue, Env.NL, " ");
        retValue = Util.replace(retValue, "\n", " ");
        return retValue;
    }	//	getSummaryAscii

    /**
     * 	Get Java VM Info
     *	@return VM info
     */
    public static String getJavaInfo() {
        return System.getProperty("java.vm.name")
                + " " + System.getProperty("java.vm.version");
    }	//	getJavaInfo

    /**
     * 	Get Operating System Info
     *	@return OS info
     */
    public static String getOSInfo() {
        return System.getProperty("os.name") + " "
                + System.getProperty("os.version") + " "
                + System.getProperty("sun.os.patch.level");
    }	//	getJavaInfo

    /**
     *  Get full URL
     *  @return URL
     */
    public static String getURL() {
        return "http://" + URL;
    }   //  getURL

    /**
     *  Get Sub Title
     *  @return Subtitle
     */
    public static String getSubtitle() {
        return SUB_TITLE;
    }   //  getSubitle

    /**
     *  Get 16x16 Image.
     *	Removing/modifying the Compiere logo is a violation of the license
     *  @return Image Icon
     */
    public static Image getImage16() {
        if (s_image16 == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            URL url = org.compiere.Compiere.class.getResource(s_File16x16);
            //	System.out.println(url);
            if (url == null) {
                return null;
            }
            s_image16 = tk.getImage(url);
        }
        return s_image16;
    }   //  getImage16

    /**
     *  Get 28*15 Logo Image.
     *	Removing/modifying the Compiere logo is a violation of the license
     *  @return Image Icon
     */
    public static Image getImageLogoSmall() {
        if (s_image48x15 == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            URL url = org.compiere.Compiere.class.getResource(s_file48x15);
            //	System.out.println(url);
            if (url == null) {
                return null;
            }
            s_image48x15 = tk.getImage(url);
        }
        return s_image48x15;
    }   //  getImageLogoSmall

    /**
     *  Get Logo Image.
     *	Removing/modifying the Compiere logo is a violation of the license
     *  @return Image Logo
     */
    public static Image getImageLogo() {
        if (s_imageLogo == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            URL url = org.compiere.Compiere.class.getResource(s_file100x30);
            //	System.out.println(url);
            if (url == null) {
                return null;
            }
            s_imageLogo = tk.getImage(url);
        }
        return s_imageLogo;
    }   //  getImageLogo

    /**
     *  Get 32x32 ImageIcon.
     *	Removing/modifying the Compiere logo is a violation of the license
     *  @return Image Icon
     */
    public static ImageIcon getImageIcon32() {
        if (s_imageIcon32 == null) {
            URL url = org.compiere.Compiere.class.getResource(s_file32x32);
            //	System.out.println(url);
            if (url == null) {
                return null;
            }
            s_imageIcon32 = new ImageIcon(url);
        }
        return s_imageIcon32;
    }   //  getImageIcon32

    /**
     *  Get 100x30 ImageIcon.
     *	Removing/modifying the Compiere logo is a violation of the license
     *  @return Image Icon
     */
    public static ImageIcon getImageIconLogo() {
        if (s_imageIconLogo == null) {
            URL url = org.compiere.Compiere.class.getResource(s_file100x30);
            //	System.out.println(url);
            if (url == null) {
                return null;
            }
            s_imageIconLogo = new ImageIcon(url);
        }
        return s_imageIconLogo;
    }   //  getImageIconLogo

    /**
     *  Get default (Home) directory
     *  @return Home directory
     */
    public static String getCompiereHome() {
        //  Try Environment
        String retValue = Ini.getCompiereHome();
        //	Look in current Directory
        if (retValue == null && System.getProperty("user.dir").indexOf("Compiere2") != -1) {
            retValue = System.getProperty("user.dir");
            int pos = retValue.indexOf("Compiere2");
            retValue = retValue.substring(pos + 9);
        }
        if (retValue == null) {
            retValue = File.separator + "Compiere2";
        }
        return retValue;
    }   //  getHome

    /**
     *  Get Support Email
     *  @return Support mail address
     */
    public static String getSupportEMail() {
        return s_supportEmail;
    }   //  getSupportEMail

    /**
     *  Set Support Email
     *  @param email Support mail address
     */
    public static void setSupportEMail(String email) {
        s_supportEmail = email;
    }   //  setSupportEMail

    /**
     * 	Get JNLP CodeBase
     *	@return code base or null
     */
    public static URL getCodeBase() {
        try {
            BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
            URL url = bs.getCodeBase();
            return url;
        } catch (UnavailableServiceException ue) {
            return null;
        }
    }	//	getCodeBase

    /**
     * 	Get JNLP CodeBase Host
     *	@return code base or null
     */
    public static String getCodeBaseHost() {
        URL url = getCodeBase();
        if (url == null) {
            return null;
        }
        return url.getHost();
    }	//	getCodeBase

    /*************************************************************************
     *  Startup Client/Server.
     *  - Print greeting,
     *  - Check Java version and
     *  - load ini parameters
     *  If it is a client, load/set PLAF and exit if error.
     *  If Client, you need to call startupEnvironment explicitly!
     * 	For testing call method startupEnvironment
     *	@param isClient true for client
     *  @return successful startup
     */
    public static synchronized boolean startup(boolean isClient) {
        //	Already started
        if (log != null) {
            return true;
        }

        //	Check Version
        if (!Login.isJavaOK(isClient) && isClient) {
            System.exit(1);
        }

        CLogMgt.initialize(isClient);
        Ini.setClient(isClient);		//	Ini requires Logging
        //	Init Log
        log = CLogger.getCLogger(Compiere.class);
        //	Greeting
        log.info(getSummaryAscii());
        log.log(Level.INFO, "{0} - {1} - {2}", new Object[]{getCompiereHome(), getJavaInfo(), getOSInfo()});

        //  Load System environment
        //	EnvLoader.load(Ini.ENV_PREFIX);

        //  System properties
        Ini.loadProperties(false);

        //	Set up Log
        CLogMgt.setLevel(Ini.getProperty(Ini.P_TRACELEVEL));
        if (isClient && Ini.isPropertyBool(Ini.P_TRACEFILE)
                && CLogFile.get(false, null, isClient) == null) {
            CLogMgt.addHandler(CLogFile.get(true, Ini.findCompiereHome(), isClient));
        }

        //	Set UI
        if (isClient) {
            CompiereTheme.load();
            CompierePLAF.setPLAF(null);
        }

        //  Set Default Database Connection from Ini
        DB.setDBTarget(CConnection.get(getCodeBaseHost()));

        //	don't test connection
        if (isClient) {
            return false;	//	need to call
        }
        return startupEnvironment(isClient);
    }   //  startup

    /**
     * 	Startup Compiere Environment.
     * 	Automatically called for Server connections
     * 	For testing call this method.
     *	@param isClient true if client connection
     *  @return successful startup
     */
    public static boolean startupEnvironment(boolean isClient) {
        startup(isClient);		//	returns if already initiated
        if (!DB.isConnected()) {
            log.severe("No Database");
            System.exit(1);
        }
        //	Initialize main cached Singletons
        ModelValidationEngine.get();
        try {
            MSystem system = MSystem.get(Env.getCtx());	//	Initializes Base Context too
            String className = system.getEncryptionKey();
            if (className == null || className.length() == 0) {
                className = System.getProperty(SecureInterface.COMPIERE_SECURE);
                if (className != null && className.length() > 0
                        && !className.equals(SecureInterface.COMPIERE_SECURE_DEFAULT)) {
                    SecureEngine.init(className);	//	test it
                    system.setEncryptionKey(className);
                    system.save();
                }
            }
            SecureEngine.init(className);

            //
            if (isClient) {
                MClient.get(Env.getCtx(), 0);			//	Login Client loaded later
            } else {
                MClient.getAll(Env.getCtx());
            }
            Document.setKey(system.getSummary());
        } catch (Exception e) {
            log.log(Level.WARNING, "Environment problems: {0}", e.toString());
        }

        //	Start Workflow Document Manager (in other package) for PO
        String className = null;
        try {
            className = "org.compiere.wf.DocWorkflowManager";
            Class.forName(className);
            //	Initialize Archive Engine
            className = "org.compiere.print.ArchiveEngine";
            Class.forName(className);
        } catch (Exception e) {
            log.log(Level.WARNING, "Not started: {0} - {1}", new Object[]{className, e.getMessage()});
        }

        if (!isClient) {
            DB.updateMail();
        }
        return true;
    }	//	startupEnvironment

    /**
     *  Main Method
     *
     *  @param args optional start class
     */
    public static void main(String[] args) {
        Splash.getSplash();
        startup(true);     //  error exit and initUI

        //  Start with class as argument - or if nothing provided with Client
        String className = "org.compiere.apps.AMenu";
        for (int i = 0; i < args.length; i++) {
            if (!args[i].equals("-debug")) { //  ignore -debug
                className = args[i];
                break;
            }
        }
        //
        try {
            Class startClass = Class.forName(className);
            startClass.newInstance();
        } catch (Exception e) {
            System.err.println("Compiere starting: " + className + " - " + e.toString());
        }
    }   //  main
}	//	Compiere
