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
package org.compiere.db;

import java.util.*;

/**
 *  Connection Resource Strings
 *
 *  @author     Jesse Jr
 *  @version    $Id: DBRes_pt.java,v 1.6 2005/11/20 22:40:26 jjanke Exp $
 */
public class DBRes_pt extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]
	{
	{ "CConnectionDialog",  "Compiere Conexão" },
	{ "Name",               "Nome" },
	{ "AppsHost",           "Servidor de Aplicação" },
	{ "AppsPort",           "Porta TCP da Aplicação" },
	{ "TestApps",           "Testar Aplicação" },
	{ "DBHost",             "Servidor do Banco de Dado" },
	{ "DBPort",             "Porta TCP do Banco de Dados" },
	{ "DBName",             "Nome do Banco de Dados" },
	{ "DBUidPwd",           "Usuário / Senha" },
	{ "ViaFirewall",        "via Firewall" },
	{ "FWHost",             "Servidor de Firewall" },
	{ "FWPort",             "Porta TCP do Firewall" },
	{ "TestConnection",     "Testar Banco de Dados" },
	{ "Type",               "Tipo de Banco de Dados" },
	{ "BequeathConnection", "Conexão Bequeath" },
	{ "Overwrite",          "Sobrescrever" },
	{ "ConnectionProfile",	"Connection" },
	{ "LAN",		 		"LAN" },
	{ "TerminalServer",		"Terminal Server" },
	{ "VPN",		 		"VPN" },
	{ "WAN", 				"WAN" },
	{ "ConnectionError",    "Erro de Conexão" },
	{ "ServerNotActive",    "Servidor não Ativo" }
	};

	/**
	 * Get Contsnts
	 * @return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContent
}   //  Res
