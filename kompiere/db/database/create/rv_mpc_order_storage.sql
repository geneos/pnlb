DROP VIEW compiere.rv_mpc_order_storage;
create view compiere.rv_mpc_order_storage as
SELECT 
	obl.ad_client_id, 
	obl.ad_org_id, 
	obl.createdby, 
	obl.updatedby, 
	obl.updated, 
	obl.created, 
	obl.isactive,
	obl.mpc_order_bom_id, 
	obl.mpc_order_bomline_id, 
	obl.mpc_order_id, 
	obl.iscritical,
	obl.m_product_id, 
	p.name, 
	obl.c_uom_id,  
	round(obl.qtyrequiered, 4) AS qtyrequiered, 
	round(compiere.bomqtyonhand(obl.m_product_id, obl.m_attributesetinstance_id, obl.m_warehouse_id,0),4) AS qtyonhand, 
        CASE
            WHEN o.qtybatchs = 0::numeric THEN 1::numeric
            ELSE round(obl.qtyrequiered / o.qtybatchs, 4)
        END AS qtybatchsize, 
	round(compiere.bomqtyreserved(obl.m_product_id, obl.m_attributesetinstance_id, obl.m_warehouse_id,0),4) AS qtyreserved, 
	round(compiere.bomqtyavailable(obl.m_product_id, obl.m_attributesetinstance_id, obl.m_warehouse_id,0),4) AS qtyavailable, 
	obl.m_warehouse_id, 
	obl.qtybom, 
	obl.isqtypercentage, 
	round(obl.qtybatch, 4) AS qtybatch, 
	l.m_locator_id, 
	l.x, 
	l.y, 
	l.z, 
	obl.m_attributesetinstance_id
   
FROM compiere.mpc_order_bomline obl
INNER JOIN compiere.m_product p ON obl.m_product_id = p.m_product_id 
INNER JOIN compiere.mpc_order o ON obl.mpc_order_id = o.mpc_order_id
INNER JOIN compiere.m_locator l ON obl.m_warehouse_id = l.m_warehouse_id
ORDER BY obl.m_product_id;