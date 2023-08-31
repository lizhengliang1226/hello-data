/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2023/8/29 18:49:22                           */
/*==============================================================*/


drop index COL_CONFIG_UNIX1;

drop table GEN_COLUMN_CONFIG cascade constraints;

drop index SYS_CONFIG_UNIX1;

drop table GEN_SYSTEM_CONFIG cascade constraints;

drop index TABLE_CONFIG_UNIX1;

drop table GEN_TABLE_CONFIG cascade constraints;

/*==============================================================*/
/* Table: GEN_COLUMN_CONFIG                                     */
/*==============================================================*/
create table GEN_COLUMN_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   COLUMN_NAME          VARCHAR2(32),
   STRATEGY_CODE        VARCHAR2(16),
   DEFAULT_VAL          VARCHAR2(1024),
   BASE_VALUE           INTEGER,
   PREFIX               VARCHAR2(32),
   SUFFIX               VARCHAR2(32),
   STEP                 NUMBER(10)           default 0,
   QUERY_SQL            VARCHAR2(1024)       default ' ',
   QUERY_COL            VARCHAR2(32)         default ' ',
   RANDOM_ELE           VARCHAR2(1024)       default ' ',
   DICT_COL_NAME        VARCHAR2(32)         default ' '
);

comment on table GEN_COLUMN_CONFIG is
'�����ñ�';

comment on column GEN_COLUMN_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_COLUMN_CONFIG.COLUMN_NAME is
'����';

comment on column GEN_COLUMN_CONFIG.STRATEGY_CODE is
'���Դ���';

comment on column GEN_COLUMN_CONFIG.DEFAULT_VAL is
'Ĭ��ֵ';

comment on column GEN_COLUMN_CONFIG.BASE_VALUE is
'����ֵ';

comment on column GEN_COLUMN_CONFIG.PREFIX is
'ǰ׺';

comment on column GEN_COLUMN_CONFIG.SUFFIX is
'��׺';

comment on column GEN_COLUMN_CONFIG.STEP is
'����';

comment on column GEN_COLUMN_CONFIG.QUERY_SQL is
'��ѯSQL';

comment on column GEN_COLUMN_CONFIG.QUERY_COL is
'��ѯ����';

comment on column GEN_COLUMN_CONFIG.RANDOM_ELE is
'���Ԫ���б�';

comment on column GEN_COLUMN_CONFIG.DICT_COL_NAME is
'�ֵ�����';

/*==============================================================*/
/* Index: COL_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index COL_CONFIG_UNIX1 on GEN_COLUMN_CONFIG (
   DATASOURCE_ID ASC,
   COLUMN_NAME ASC
);

/*==============================================================*/
/* Table: GEN_SYSTEM_CONFIG                                     */
/*==============================================================*/
create table GEN_SYSTEM_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   DATABASE_URL         VARCHAR2(16),
   DATABASE_USER        VARCHAR2(16),
   DATABASE_PASSWORD    VARCHAR2(32),
   LOAD_DICT_CACHE      NUMBER(1)            default 0,
   DICT_TABLE_NAME      VARCHAR2(32)         default ' ',
   DICT_CODE_COL_NAME   VARCHAR2(32)         default ' ',
   DICT_ITEM_COL_NAME   VARCHAR2(32)         default ' '
);

comment on table GEN_SYSTEM_CONFIG is
'ϵͳ���ñ�';

comment on column GEN_SYSTEM_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_SYSTEM_CONFIG.DATABASE_URL is
'����ԴURL';

comment on column GEN_SYSTEM_CONFIG.DATABASE_USER is
'����Դ�û���';

comment on column GEN_SYSTEM_CONFIG.DATABASE_PASSWORD is
'����Դ����';

comment on column GEN_SYSTEM_CONFIG.LOAD_DICT_CACHE is
'�Ƿ�����ֵ仺�� 1-���� 0-������';

comment on column GEN_SYSTEM_CONFIG.DICT_TABLE_NAME is
'�ֵ����';

comment on column GEN_SYSTEM_CONFIG.DICT_CODE_COL_NAME is
'�ֵ����ֵ��������';

comment on column GEN_SYSTEM_CONFIG.DICT_ITEM_COL_NAME is
'�ֵ����ֵ�������';

/*==============================================================*/
/* Index: SYS_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index SYS_CONFIG_UNIX1 on GEN_SYSTEM_CONFIG (
   DATASOURCE_ID ASC
);

/*==============================================================*/
/* Table: GEN_TABLE_CONFIG                                      */
/*==============================================================*/
create table GEN_TABLE_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   TABLE_CODE           VARCHAR2(32),
   GEN_NUM              NUMBER(10)           default 0,
   COLUMN_NAME          VARCHAR2(32),
   STRATEGY_CODE        VARCHAR2(16),
   DEFAULT_VAL          VARCHAR2(1024),
   BASE_VALUE           INTEGER,
   PREFIX               VARCHAR2(32),
   SUFFIX               VARCHAR2(32),
   STEP                 NUMBER(10)           default 0,
   QUERY_SQL            VARCHAR2(1024)       default ' ',
   QUERY_COL            VARCHAR2(32)         default ' ',
   RANDOM_ELE           VARCHAR2(1024)       default ' ',
   DICT_COL_NAME        VARCHAR2(32)         default ' '
);

comment on table GEN_TABLE_CONFIG is
'���������ñ�';

comment on column GEN_TABLE_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_TABLE_CONFIG.TABLE_CODE is
'���ɱ����';

comment on column GEN_TABLE_CONFIG.GEN_NUM is
'��������';

comment on column GEN_TABLE_CONFIG.COLUMN_NAME is
'����';

comment on column GEN_TABLE_CONFIG.STRATEGY_CODE is
'���Դ���';

comment on column GEN_TABLE_CONFIG.DEFAULT_VAL is
'Ĭ��ֵ';

comment on column GEN_TABLE_CONFIG.BASE_VALUE is
'����ֵ';

comment on column GEN_TABLE_CONFIG.PREFIX is
'ǰ׺';

comment on column GEN_TABLE_CONFIG.SUFFIX is
'��׺';

comment on column GEN_TABLE_CONFIG.STEP is
'����';

comment on column GEN_TABLE_CONFIG.QUERY_SQL is
'��ѯSQL';

comment on column GEN_TABLE_CONFIG.QUERY_COL is
'��ѯ����';

comment on column GEN_TABLE_CONFIG.RANDOM_ELE is
'���Ԫ���б�';

comment on column GEN_TABLE_CONFIG.DICT_COL_NAME is
'�ֵ�����';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on GEN_TABLE_CONFIG (
   DATASOURCE_ID ASC,
   TABLE_CODE ASC,
   COLUMN_NAME ASC
);

