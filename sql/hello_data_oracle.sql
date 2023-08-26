/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2023/8/27 3:14:08                            */
/*==============================================================*/


drop index COLUMN_CONFIG_UNIX1;

drop table COLUMN_CONFIG cascade constraints;

drop index TABLE_CONFIG_UNIX1;

drop table TABLE_CONFIG cascade constraints;

/*==============================================================*/
/* Table: COLUMN_CONFIG                                         */
/*==============================================================*/
create table COLUMN_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   COLUMN_NAME          VARCHAR2(32),
   DEFAULT_VAL          VARCHAR2(1024),
   STRATEGY_NAME        VARCHAR2(16),
   BASE_VALUE           VARCHAR2(1024),
   PREFIX               VARCHAR2(32),
   SUFFIX               VARCHAR2(32),
   STEP                 NUMBER(10,0)         default 0,
   QUERY_SQL            VARCHAR2(1024),
   QUERY_COL            VARCHAR2(32),
   RANDOM_ELE           VARCHAR2(1024),
   DICT_COL_NAME        VARCHAR2(32),
   FIXED_VALUE          VARCHAR2(1024)
);

comment on table COLUMN_CONFIG is
'列生成配置表';

comment on column COLUMN_CONFIG.DATASOURCE_ID is
'数据源ID';

comment on column COLUMN_CONFIG.COLUMN_NAME is
'列名';

comment on column COLUMN_CONFIG.DEFAULT_VAL is
'默认值';

comment on column COLUMN_CONFIG.STRATEGY_NAME is
'策略名';

comment on column COLUMN_CONFIG.BASE_VALUE is
'基础值';

comment on column COLUMN_CONFIG.PREFIX is
'前缀';

comment on column COLUMN_CONFIG.SUFFIX is
'后缀';

comment on column COLUMN_CONFIG.STEP is
'步进';

comment on column COLUMN_CONFIG.QUERY_SQL is
'查询SQL';

comment on column COLUMN_CONFIG.QUERY_COL is
'查询的列';

comment on column COLUMN_CONFIG.RANDOM_ELE is
'随机元素列表';

comment on column COLUMN_CONFIG.DICT_COL_NAME is
'字典列名';

comment on column COLUMN_CONFIG.FIXED_VALUE is
'固定值';

/*==============================================================*/
/* Index: COLUMN_CONFIG_UNIX1                                   */
/*==============================================================*/
create index COLUMN_CONFIG_UNIX1 on COLUMN_CONFIG (
   DATASOURCE_ID ASC,
   COLUMN_NAME ASC
);

/*==============================================================*/
/* Table: TABLE_CONFIG                                          */
/*==============================================================*/
create table TABLE_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   TABLE_CODE           VARCHAR2(32),
   GEN_NUM              NUMBER(10,0)         default 0,
   LOAD_DICT_CACHE      NUMBER(1,0)          default 0,
   DICT_TABLE_NAME      VARCHAR2(32)         default ' ',
   DICT_CODE_COL_NAME   VARCHAR2(32)         default ' ',
   DICT_ITEM_COL_NAME   VARCHAR2(32)         default ' '
);

comment on table TABLE_CONFIG is
'生成配置表';

comment on column TABLE_CONFIG.DATASOURCE_ID is
'数据源ID';

comment on column TABLE_CONFIG.TABLE_CODE is
'生成表代码';

comment on column TABLE_CONFIG.GEN_NUM is
'生成数量';

comment on column TABLE_CONFIG.LOAD_DICT_CACHE is
'是否加载字典缓存 1-加载 0-不加载';

comment on column TABLE_CONFIG.DICT_TABLE_NAME is
'字典表名';

comment on column TABLE_CONFIG.DICT_CODE_COL_NAME is
'字典表的字典代码列名';

comment on column TABLE_CONFIG.DICT_ITEM_COL_NAME is
'字典表的字典项列名';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on TABLE_CONFIG (
   DATASOURCE_ID ASC,
   TABLE_CODE ASC
);

