create view compiere.mpc_order_receipt_issue as
select
	obl.mpc_order_bomline_id, 
	obl.iscritical, 
	p.value, 
	obl.m_product_id, 
	mos.name as productname, 
	mos.m_attributesetinstance_id, 
	asi.description as instancename,
	mos.c_uom_id, 
	u.name as uomname, 
	obl.qtyrequiered, 
	obl.qtyreserved as qtyreserved_order,
	mos.qtyonhand,
	mos.qtyreserved as qtyreserved_storage,
	mos.qtyavailable,
	mos.m_locator_id, 
	mos.m_warehouse_id, 
	w.name as warehousename, 
	mos.qtybom, 
	mos.isqtypercentage, 
	mos.qtybatch, 
	obl.ComponentType, 
	mos.QtyRequiered - obl.QtyDelivered AS QtyOpen, 
	obl.mpc_order_id
from compiere.rv_mpc_order_storage mos
inner join compiere.mpc_order_bomline obl on mos.mpc_order_bomline_id = obl.mpc_order_bomline_id
inner join compiere.m_attributesetinstance asi on mos.m_attributesetinstance_id = asi.m_attributesetinstance_id
inner join compiere.c_uom u on mos.c_uom_id = u.c_uom_id
inner join compiere.m_product p on mos.m_product_id = p.m_product_id 
inner join compiere.m_warehouse w on mos.m_warehouse_id = w.m_warehouse_id;