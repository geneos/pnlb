CREATE OR REPLACE FUNCTION compiere.bomqtyreserved(int4, int4, int4, int4)
RETURNS "numeric" AS
'org.compiere.sqlj.Product.bomQtyReserved(int,int,int,int)'
LANGUAGE 'java' VOLATILE;
ALTER FUNCTION compiere.bomqtyreserved(int4, int4, int4, int4) OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyavailable(int4, int4, int4, int4)
RETURNS "numeric" AS
'org.compiere.sqlj.Product.bomQtyAvailable(int,int,int,int)'
LANGUAGE 'java' VOLATILE;
ALTER FUNCTION compiere.bomqtyavailable(int4, int4, int4, int4) OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyonhand(int4, int4, int4, int4)
  RETURNS "numeric" AS
'org.compiere.sqlj.Product.bomQtyOnHand(int,int,int,int)'
LANGUAGE 'java' VOLATILE;
ALTER FUNCTION compiere.bomqtyonhand(int4, int4, int4, int4) OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyordered(int4, int4, int4, int4)
RETURNS "numeric" AS
'org.compiere.sqlj.Product.bomQtyOrdered(int,int,int,int)'
LANGUAGE 'java' VOLATILE;
ALTER FUNCTION compiere.bomqtyordered(int4, int4, int4, int4) OWNER TO compiere;


CREATE OR REPLACE FUNCTION compiere.bomqtyavailable("numeric", "numeric", "numeric", "numeric")
  RETURNS "numeric" AS
$BODY$
BEGIN
    RETURN bomQtyOnHand(ID(M_Product_ID), ID(M_AttributeSetInstance_ID),ID(M_Warehouse_ID),ID(M_Locator_ID));
END;    
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION compiere.bomqtyavailable("numeric", "numeric", "numeric", "numeric") OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyonhand("numeric", "numeric", "numeric", "numeric")
  RETURNS "numeric" AS
$BODY$
BEGIN
    RETURN bomQtyOnHand(ID(M_Product_ID), ID(M_AttributeSetInstance_ID),ID(M_Warehouse_ID),ID(M_Locator_ID));
END;    
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION compiere.bomqtyonhand("numeric", "numeric", "numeric", "numeric") OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyordered("numeric", "numeric", "numeric", "numeric")
  RETURNS "numeric" AS
$BODY$
BEGIN
    RETURN bomQtyOnHand(ID(M_Product_ID), ID(M_AttributeSetInstance_ID),ID(M_Warehouse_ID),ID(M_Locator_ID));
END;    
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION compiere.bomqtyordered("numeric", "numeric", "numeric", "numeric") OWNER TO compiere;

CREATE OR REPLACE FUNCTION compiere.bomqtyreserved("numeric", "numeric", "numeric", "numeric")
  RETURNS "numeric" AS
$BODY$
BEGIN
    RETURN bomQtyOnHand(ID(M_Product_ID), ID(M_AttributeSetInstance_ID),ID(M_Warehouse_ID),ID(M_Locator_ID));
END;    
$BODY$
LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION compiere.bomqtyreserved("numeric", "numeric", "numeric", "numeric") OWNER TO compiere;