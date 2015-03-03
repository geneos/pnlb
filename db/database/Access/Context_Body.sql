CREATE OR REPLACE PACKAGE BODY Compiere_Context
AS
/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Context_Body.sql,v 1.4 2004/07/22 05:50:06 jjanke Exp $
 ***
 * Title:	 Security Procedure
 * Description:
 *	When logging in, the procedure requires access to login tables
 *	(AD_User, AD_User_Roles, AD_Role, AD_Client, AD_Org, M_Warehouse)
 *	The security policy includes all tables but these 
 *	(also exclused report views, temporary tables)
 *	All tables but AD_ have GRANTs to public
 *
 *	The user has to log in with UserName, Password and Role
 *		This sets ClientList, OrgList, Language
 *	Unrestricted access is obtained by loggin in as Accorto/Internal Server
 *		This sets Server to true
 ******************************************************************************/

	/******************************************************************************
	 *  Sets Context Client - ClientList, OrgList
	 */
	PROCEDURE Login
	(
		UserName	IN VARCHAR2,
		UserPwd		IN VARCHAR2,
		UserRole	IN VARCHAR2,
		UserLang	IN VARCHAR2	DEFAULT 'USAENG'
	)
	IS
		ClientList	VARCHAR2(60);
		OrgList		VARCHAR2(60);
	BEGIN
   		--	No Access Restrictions
		IF (UserName = 'Compiere' AND UserPwd = 'Internal' AND UserRole = 'Server') THEN
		    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'Server', 'true');
			RETURN;
		END IF;
		DBMS_SESSION.SET_CONTEXT('CompiereInc', 'Server', 'false');

		/*	Check Access
		SELECT 	r.ClientList, r.OrgList
		  INTO	ClientList, OrgList
		FROM	AD_User u, AD_User_Roles ur, AD_Role r
		WHERE u.AD_User_ID=ur.AD_User_ID
		  AND r.AD_Role_ID=ur.AD_Role_ID
		  AND u.IsActive='Y' AND r.IsActive='Y' AND ur.IsActive='Y'
		  AND u.Name=UserName AND u.Password=UserPwd AND r.Name=UserRole;
        */
		--	Check Values to include System
		IF (ClientList <> '0' AND SUBSTR(ClientList,1,2) <> '0,') THEN
			ClientList := '0,' || ClientList;
		END IF;
		IF (OrgList <> '0' AND SUBSTR(OrgList,1,2) <> '0,') THEN
			OrgList := '0,' || OrgList;
		END IF;

		--	Set Values
	    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'ClientList', ClientList);
	    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'OrgList', OrgList);
	    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'Language', UserLang);
		--
		EXCEPTION
			WHEN OTHERS THEN
			    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'ClientList', NULL);
			    DBMS_SESSION.SET_CONTEXT('CompiereInc', 'Login', NULL);
				RAISE_APPLICATION_ERROR(-20222, 'LoginFailure');

	END Login;


	/******************************************************************************
	 *	Set Environment Variables
	 */
	PROCEDURE SetEnv 
	(
		Name	 	IN VARCHAR2, 
		NewValue 	IN VARCHAR
	)
	IS
	BEGIN
		IF (Name <> 'ClientList' AND Name <> 'OrgList' AND Name <> 'Login') THEN
			DBMS_SESSION.SET_CONTEXT('CompiereInc', Name, NewValue);
		END IF;
	END SetEnv;


	/******************************************************************************
	 *  Return Security Predicates
	 */
	FUNCTION GetPredicate 
	(
	    ObjectSchema    VARCHAR2, 
	    ObjectName      VARCHAR2
	) 
	RETURN VARCHAR2
	IS
	    Predicate     VARCHAR2 (2000);
	BEGIN
		--	Unrestricted Access
	 	IF (SYS_CONTEXT('CompiereInc','Server') = 'true') THEN
			Predicate := '';

		--	No Access
		ELSIF (SYS_CONTEXT('CompiereInc','ClientList') IS NULL) THEN
			Predicate := '1=2';

		--	Translation
		ELSIF (ObjectName LIKE '%TRL' AND SYS_CONTEXT('CompiereInc','Language') <> 'USAENG') THEN
			Predicate := 'AD_Language=''' || SYS_CONTEXT('CompiereInc','Language') || '''';

		--	Standard Security
		ELSE
		    Predicate := 'AD_Client_ID IN (' || SYS_CONTEXT('CompiereInc','ClientList') 
				|| ') AND AD_Org_ID IN (' || SYS_CONTEXT('CompiereInc','OrgList') || ')';

		END IF;

	    RETURN Predicate;    
	END GetPredicate;

END Compiere_Context;
/
