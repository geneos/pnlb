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

--------------------------------------------------------------------------------
--      ERP - Reintegro de Exportacion
--------------------------------------------------------------------------------

/*
        hay que agregar una sequencia para que autoincremente el documentno
*/

-- obtengo el prox id 
SELECT MAX(ad_sequence_id)+1 FROM ad_sequence;

-- inserto con ese id
Insert into AD_SEQUENCE 
(AD_SEQUENCE_ID,AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,CREATED,CREATEDBY,UPDATED,UPDATEDBY,NAME,DESCRIPTION,VFORMAT,ISAUTOSEQUENCE,INCREMENTNO,STARTNO,CURRENTNEXT,CURRENTNEXTSYS,ISAUDITED,ISTABLEID,PREFIX,SUFFIX,STARTNEWYEAR) 
values 
(5000018,1000002,0,'Y',to_date('04/11/2009','DD/MM/RRRR'),1000684,to_date('04/11/2011','DD/MM/RRRR'),1000684,'MV Reintegro de Exportación',null,null,'Y',1,1000000,1,100,'N','N',null,null,'N');

/*
    hay q insertar un nuevo doctype
*/
-- verifico los tipos de documentos existentes
select * from c_doctype;

-- Verifico los tipos de document q existen para movimientos de fondos
select * from c_doctype where c_doctype_id IN (
  select distinct c_doctype_id from c_movimientofondos 
);

-- chekeo q no exista el par de secuencia – tipo de documento
SELECT dt.C_DocType_ID, sq.CurrentNext
FROM C_DocType dt LEFT JOIN AD_SEQUENCE sq ON (dt.DocNoSequence_ID = sq.AD_Sequence_ID)
WHERE PrintName = 'MVF R';

-- Seobtine el valor para el siguiente tipo de documento
select max(c_doctype_id) from c_doctype;

-- insert el tipo de document
Insert into c_doctype(C_DOCTYPE_ID,AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,CREATED,CREATEDBY,UPDATED,UPDATEDBY,NAME,PRINTNAME,DESCRIPTION,DOCBASETYPE,ISSOTRX,DOCSUBTYPESO,HASPROFORMA,C_DOCTYPEPROFORMA_ID,C_DOCTYPESHIPMENT_ID,C_DOCTYPEINVOICE_ID,ISDOCNOCONTROLLED,DOCNOSEQUENCE_ID,GL_CATEGORY_ID,HASCHARGES,DOCUMENTNOTE,ISDEFAULT,DOCUMENTCOPIES,AD_PRINTFORMAT_ID,ISDEFAULTCOUNTERDOC,ISSHIPCONFIRM,ISPICKQACONFIRM,ISINTRANSIT,ISSPLITWHENDIFFERENCE,C_DOCTYPEDIFFERENCE_ID,ISCREATECOUNTER,C_REVDOCTYPE_ID) 
values 
(5000016,1000002,0,'Y',to_date('05/03/2010','DD/MM/RRRR'),1000684,to_date('05/03/2010','DD/MM/RRRR'),1000684,'Reintegro de Exportación','MVF R',null,'AMM','N',null,'N',null,null,null,'Y',5000018,1000021,'N',null,'N',1,null,'N','N','N','N','N',null,'Y',null);

-- chekeo q ahora si exista el par tipo de doc, secuencia
SELECT dt.C_DocType_ID, sq.CurrentNext
FROM C_DocType dt LEFT JOIN AD_SEQUENCE sq ON (dt.DocNoSequence_ID = sq.AD_Sequence_ID)
WHERE PrintName = 'MVF R';

-- confirmo los cambios
commit;

-- verifico si estan los hechos contables dados de alta
-- NOTA: cambiar Record_ID por uno existente
SELECT * FROM Fact_Acct WHERE Record_ID=5000609 AND AD_Table_ID=1000304;

