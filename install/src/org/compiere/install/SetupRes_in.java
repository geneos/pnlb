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
 * Contributor(s): PT. RFID INDONESIA (info@rfid-indonesia.com)____________.
 *****************************************************************************/
package org.compiere.install;

import java.util.*;

/**
 *	Setup Resources
 *
 * 	@author 	Halim Englen (halim@rfid-indonesia.com)
 * 	@version 	$Id: SetupRes_in.java,v 1.2 2005/11/09 17:39:34 jjanke Exp $
 */
public class SetupRes_in extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Setup Compiere Server" },
	{ "Ok", 					"Ok" },
	{ "File", 					"File" },
	{ "Exit", 					"Keluar" },
	{ "Help", 					"Bantuan" },
	{ "PleaseCheck", 			"Mohon diperiksa" },
	{ "UnableToConnect", 		"Koneksi gagal. Saat ini bantuan tidak bisa didapatkan dari situs web Compiere" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home adalah direktori utama" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Port Web (HTML)" },
	{ "WebPort", 				"Port Web" },
	{ "AppsServerInfo", 		"Nama Server Aplikasi" },
	{ "AppsServer", 			"Server Aplikasi" },
	{ "DatabaseTypeInfo", 		"Tipe Database" },
	{ "DatabaseType", 			"Tipe Database" },
	{ "DatabaseNameInfo", 		"Nama Database (Service)" },
	{ "DatabaseName", 			"Nama Database" },
	{ "DatabasePortInfo", 		"Port Database Listener" },
	{ "DatabasePort", 			"Port Database" },
	{ "DatabaseUserInfo", 		"ID Pengguna untuk database Compiere" },
	{ "DatabaseUser", 			"Pengguna Database" },
	{ "DatabasePasswordInfo", 	"Kata sandi pengguna untuk database Compiere" },
	{ "DatabasePassword", 		"Kata Sandi Database" },
	{ "TNSNameInfo", 			"Database-database yang ditemukan" },
	{ "TNSName", 				"Pilih Database" },
	{ "SystemPasswordInfo", 	"Kata Sandi Pengguna Sistem" },
	{ "SystemPassword", 		"Kata Sandi Sistem" },
	{ "MailServerInfo", 		"Server Mail" },
	{ "MailServer", 			"Server Mail" },
	{ "AdminEMailInfo", 		"Email Compiere Administrator" },
	{ "AdminEMail", 			"EMail Admin" },
	{ "DatabaseServerInfo", 	"Nama Database Server" },
	{ "DatabaseServer", 		"Server Database" },
	{ "JavaHomeInfo", 			"Java Home Folder" },
	{ "JavaHome", 				"Java Home" },
	{ "JNPPortInfo", 			"Port JNP untuk Server Aplikasi" },
	{ "JNPPort", 				"Port JNP" },
	{ "MailUserInfo", 			"Pengguna Compiere Mail" },
	{ "MailUser", 				"Pengguna Mail" },
	{ "MailPasswordInfo", 		"Compiere Mail User Password" },
	{ "MailPassword", 			"Kata Sandi Mail" },
	{ "KeyStorePassword",		"Kata Sandi KeyStore" },
	{ "KeyStorePasswordInfo",	"Kata Sandi untuk SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Vendor Java VM"},
	{ "AppsType",				"Tipe Server"},
	{ "AppsTypeInfo",			"Tipe Server Aplikasi J2EE"},
	{ "DeployDir",				"Direktori"},
	{ "DeployDirInfo",			"Direktori sebar aplikasi J2EE"},
	{ "ErrorDeployDir",			"Error direktori sebar aplikasi J2EE"},
	//
	{ "TestInfo", 				"Uji Setup" },
	{ "Test", 					"Uji" },
	{ "SaveInfo", 				"Simpan Setup" },
	{ "Save", 					"Simpan" },
	{ "HelpInfo", 				"Cari Bantuan" },
	//
	{ "ServerError", 			"Error Setup Server" },
	{ "ErrorJavaHome", 			"Error Java Home" },
	{ "ErrorCompiereHome", 		"Error Compiere Home" },
	{ "ErrorAppsServer", 		"Error Server Aplikasi (jangan gunakan localhost)" },
	{ "ErrorWebPort", 			"Error Web Port" },
	{ "ErrorJNPPort", 			"Error JNP Port" },
	{ "ErrorDatabaseServer", 	"Error Database Server (jangan gunakan localhost)" },
	{ "ErrorDatabasePort", 		"Error Port Database" },
	{ "ErrorJDBC", 				"Error Koneksi JDBC" },
	{ "ErrorTNS", 				"Error Koneksi TNS" },
	{ "ErrorMailServer", 		"Error Mail Server (jangan gunakan localhost)" },
	{ "ErrorMail", 				"Error Mail" },
	{ "ErrorSave", 				"Error Simpan File" },

	{ "EnvironmentSaved", 		"File environment tersimpan .... Penyebaran dimulai\n"
		+ "Anda dapat me-restart Server Aplikasi setelah program selesai.\n"
		+ "Mohon periksa layar trace untuk error.\n" }

	};

	/**
	 * 	Get Contents
	 * 	@return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}	//	getContents

}	//	SerupRes
