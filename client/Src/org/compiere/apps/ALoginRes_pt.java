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
package org.compiere.apps;

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@author 	Jesse Jr
 * 	@version 	$Id: ALoginRes_pt.java,v 1.4 2005/12/19 01:16:51 jjanke Exp $
 */
public final class ALoginRes_pt extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Conexão" },
	{ "Defaults",           "Padrões" },
	{ "Login",              "Compiere Login" },
	{ "File",               "Arquivo" },
	{ "Exit",               "Sair" },
	{ "Help",               "Ajuda" },
	{ "About",              "Sobre" },
	{ "Host",               "Servidor" },
	{ "Database",           "Banco de Dados" },
	{ "User",               "ID Usuário" },
	{ "EnterUser",          "Entre com o ID Usuário da Aplicação" },
	{ "Password",           "Senha" },
	{ "EnterPassword",      "Entre com a Senha da Aplicação" },
	{ "Language",           "Idioma" },
	{ "SelectLanguage",     "Selecione o idioma" },
	{ "Role",               "Regra" },
	{ "Client",             "Cliente" },
	{ "Organization",       "Organização" },
	{ "Date",               "Data" },
	{ "Warehouse",          "Depósito" },
	{ "Printer",            "Impressora" },
	{ "Connected",          "Conectado" },
	{ "NotConnected",       "Não conectado" },
	{ "DatabaseNotFound",   "Banco de Dados não encontrado" },
	{ "UserPwdError",       "Usuário/Senha inválidos" },
	{ "RoleNotFound",       "Regra não encontrada/incorreta" },
	{ "Authorized",         "Autorizado" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Cancelar" },
	{ "VersionConflict",    "Conflito de Versões:" },
	{ "VersionInfo",        "Servidor <> Cliente" },
	{ "PleaseUpgrade",      "Favor executar o programa de atualização" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_pt
