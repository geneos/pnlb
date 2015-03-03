CREATE TABLE compiere.mpc_order_workflow_trl
(
  mpc_order_workflow_id int4 NOT NULL,
  mpc_order_id int4 NOT NULL,
  ad_language varchar(6) NOT NULL,
  ad_client_id int4 NOT NULL,
  ad_org_id int4 NOT NULL,
  isactive char(1) NOT NULL DEFAULT 'Y'::bpchar,
  created timestamp NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  createdby int4 NOT NULL,
  updated timestamp NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby int4 NOT NULL,
  name varchar(60) NOT NULL,
  description varchar(255),
  help varchar(2000),
  istranslated char(1) NOT NULL DEFAULT 'N'::bpchar,
  CONSTRAINT mpc_order_workflow_trl_key PRIMARY KEY (ad_language, mpc_order_workflow_id),
  CONSTRAINT ad_language_workflowtrl FOREIGN KEY (ad_language) REFERENCES compiere.ad_language (ad_language) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mpc_order_workflowtrl FOREIGN KEY (mpc_order_workflow_id, mpc_order_id) REFERENCES compiere.mpc_order_workflow (mpc_order_workflow_id, mpc_order_id) ON UPDATE NO ACTION ON DELETE CASCADE
) 
WITH OIDS;