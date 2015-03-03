DROP TABLE compiere.MPC_Order_Node_Trl;
CREATE TABLE compiere.MPC_Order_Node_Trl
(
  ad_language varchar(6) NOT NULL,
  mpc_order_node_id int4 NOT NULL,
  mpc_order_id int4 NOT NULL,
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
  CONSTRAINT mpc_order_node_trl_key PRIMARY KEY (ad_language, mpc_order_node_id),
  CONSTRAINT ad_language_ordernodetrl FOREIGN KEY (ad_language) REFERENCES compiere.ad_language (ad_language) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mpc_ordernodetrl FOREIGN KEY (mpc_order_node_id, mpc_order_id) REFERENCES compiere.mpc_order_node (mpc_order_node_id, mpc_order_id) ON UPDATE NO ACTION ON DELETE CASCADE
) 
WITH OIDS;