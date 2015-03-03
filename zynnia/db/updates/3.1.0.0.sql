CREATE TABLE "COMPIERE"."ZYN_USER_VERSION"  (
 "ZYN_USER_VERSION_ID"   NUMBER(10,0) NOT NULL ENABLE,
 "AD_CLIENT_ID" NUMBER(10,0) NOT NULL ENABLE,
 "AD_ORG_ID"    NUMBER(10,0) NOT NULL ENABLE,
 "ISACTIVE"     CHAR(1 BYTE) DEFAULT 'Y' NOT NULL ENABLE,
 "CREATED" DATE DEFAULT SYSDATE NOT NULL ENABLE,
 "CREATEDBY" NUMBER(10,0) NOT NULL ENABLE,
 "UPDATED" DATE DEFAULT SYSDATE NOT NULL ENABLE,
 "UPDATEDBY"    NUMBER(10,0) NOT NULL ENABLE,
 "ISTRANSLATED" CHAR(1 BYTE) DEFAULT 'N' NOT NULL ENABLE,
 "HOSTNAME"     VARCHAR2(200 CHAR),
 "USER_ADDRESS"     VARCHAR2(50 BYTE) NOT NULL   ENABLE,
 "CLIENT_VERSION"   VARCHAR2(20 BYTE),
 "LAST_CHECK" DATE,
 CONSTRAINT "ZYN_USER_VERSION_PK" PRIMARY KEY ("ZYN_USER_VERSION_ID")
USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536
NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0
FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS"
ENABLE)
 PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE
 ( INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT )
 TABLESPACE "USERS" ;

ALTER TABLE "COMPIERE"."AD_SYSTEM" ADD (CLIENT_VERSION VARCHAR2(20 CHAR));

UPDATE "COMPIERE"."AD_SYSTEM" SET CLIENT_VERSION = '3.1.0.0' 



