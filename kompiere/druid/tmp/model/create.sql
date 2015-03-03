DROP TABLE  QM_Specification;
CREATE TABLE QM_Specification
  (
    QM_Specification_ID  NUMBER(10,0)     NOT NULL ,
    AD_Client_ID         NUMBER(10,0)     NOT NULL  ,
    AD_Org_ID            NUMBER(10,0)     NOT NULL  ,
    IsActive             CHAR(1)          DEFAULT 'Y' NOT NULL ,
    Created              DATE             DEFAULT SYSDATE NOT NULL ,
    CreatedBy            NUMBER(10,0)     NOT NULL ,
    Updated              DATE             DEFAULT SYSDATE NOT NULL,
    UpdatedBy            NUMBER(10,0)     NOT NULL  ,
    Value                NVARCHAR2(40)     ,
    Name                 NVARCHAR2(60)     ,
    Description          NVARCHAR2(255)   ,
    M_Product_ID         NUMBER(10,0)     NOT NULL ,
    MPC_Product_BOM_ID   NUMBER(10,0)     NOT NULL  ,
    AD_Workflow_ID       NUMBER(10,0)     NOT NULL  ,
    M_AttributeSet_ID    NUMBER(10,0)     NOT NULL ,
    ValidFrom            DATE              ,
    ValidTo              DATE              ,
    CONSTRAINT QM_Specification_Key primary key (QM_Specification_ID),
    CHECK(IsActive IN ('Y', 'N'))
  );


DROP TABLE QM_SpecificationLine;

CREATE TABLE QM_SpecificationLine
  (
    QM_SpecificationLine_ID  NUMBER(10,0)    NOT NULL  ,
    AD_Client_ID             NUMBER(10,0)    NOT NULL  ,
    AD_Org_ID                NUMBER(10,0)    NOT NULL  ,
    IsActive                 CHAR(1)         DEFAULT 'Y' NOT NULL ,
    Created                  DATE            DEFAULT SYSDATE NOT NULL  ,
    CreatedBy                NUMBER(10,0)    NOT NULL  ,
    Updated                  DATE            DEFAULT SYSDATE NOT NULL  ,
    UpdatedBy                NUMBER(10,0)    NOT NULL  ,
    SeqNo                    NUMBER(22)      ,
    M_Attribute_ID           NUMBER(10,0)    NOT NULL  ,
    ValidFrom                DATE            ,
    ValidTo                  DATE             ,
    AndOr                    CHAR(1)         DEFAULT 'O' NOT NULL ,
    Value                    NVARCHAR2(40)   ,
    Operation                CHAR(2)         NOT NULL,
    Value2                   NVARCHAR2(40)   ,
    QM_Specification_ID      NUMBER(10,0),
    CONSTRAINT QM_SpecificationLine_Key primary key(QM_SpecificationLine_ID),
   -- foreign key(QM_SpecificationLine) references QM_Specification(QM_Specification_ID) on update CASCADE,
    CHECK(IsActive IN ('Y', 'N'))
  );

ALTER TABLE QM_SpecificationLine ADD CONSTRAINT  QMSpecificationQMSLine
    FOREIGN KEY (QM_Specification_ID)
    REFERENCES QM_Specification(QM_Specification_ID);
;

