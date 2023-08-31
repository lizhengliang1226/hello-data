/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2023/8/31 13:11:58                           */
/*==============================================================*/


drop index COL_CONFIG_UNIX1;

drop table GEN_COLUMN_CONFIG cascade constraints;

drop index COL_DEFAULT_CONFIG_UNIX1;

drop table GEN_COLUMN_DEFAULT_CONFIG cascade constraints;

drop index IGNORE_COL_UNIX1;

drop table GEN_IGNORE_COL cascade constraints;

drop index JDBC_TYPE_UNIX1;

drop table GEN_JDBC_TYPE_DEFAULT_VAL cascade constraints;

drop index STRATEGY_TMPL_UNIX1;

drop table GEN_STRATEGY_TEMPLATE cascade constraints;

drop index SYS_CONFIG_UNIX1;

drop table GEN_SYSTEM_CONFIG cascade constraints;

drop index TAB_CONFIG_UNIX1;

drop table GEN_TABLE_CONFIG cascade constraints;

/*==============================================================*/
/* Table: GEN_COLUMN_CONFIG                                     */
/*==============================================================*/
create table GEN_COLUMN_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   TABLE_CODE           VARCHAR2(32),
   COLUMN_NAME          VARCHAR2(32),
   STRATEGY_TMPL_ID     VARCHAR2(16)
);

comment on table GEN_COLUMN_CONFIG is
'�����ñ�';

comment on column GEN_COLUMN_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_COLUMN_CONFIG.TABLE_CODE is
'���ɱ����';

comment on column GEN_COLUMN_CONFIG.COLUMN_NAME is
'����';

comment on column GEN_COLUMN_CONFIG.STRATEGY_TMPL_ID is
'����ģ��ID����Ϊ@ʱ������';

/*==============================================================*/
/* Index: COL_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index COL_CONFIG_UNIX1 on GEN_COLUMN_CONFIG (
   DATASOURCE_ID ASC,
   TABLE_CODE ASC,
   COLUMN_NAME ASC
);

/*==============================================================*/
/* Table: GEN_COLUMN_DEFAULT_CONFIG                             */
/*==============================================================*/
create table GEN_COLUMN_DEFAULT_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   COLUMN_NAME          VARCHAR2(32),
   STRATEGY_TMPL_ID     VARCHAR2(16),
   DEFAULT_VAL          VARCHAR2(1024)
);

comment on table GEN_COLUMN_DEFAULT_CONFIG is
'�����ñ�,��������ĳ�����ȫ��������';

comment on column GEN_COLUMN_DEFAULT_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_COLUMN_DEFAULT_CONFIG.COLUMN_NAME is
'����';

comment on column GEN_COLUMN_DEFAULT_CONFIG.STRATEGY_TMPL_ID is
'����ģ��ID����Ϊ@ʱ������';

comment on column GEN_COLUMN_DEFAULT_CONFIG.DEFAULT_VAL is
'Ĭ��ֵ';

/*==============================================================*/
/* Index: COL_DEFAULT_CONFIG_UNIX1                              */
/*==============================================================*/
create unique index COL_DEFAULT_CONFIG_UNIX1 on GEN_COLUMN_DEFAULT_CONFIG (
   DATASOURCE_ID ASC,
   COLUMN_NAME ASC
);

/*==============================================================*/
/* Table: GEN_IGNORE_COL                                        */
/*==============================================================*/
create table GEN_IGNORE_COL 
(
   DATASOURCE_ID        VARCHAR2(16),
   COLUMN_NAME          VARCHAR2(32)
);

comment on table GEN_IGNORE_COL is
'���Ե���';

comment on column GEN_IGNORE_COL.DATASOURCE_ID is
'����ԴID';

comment on column GEN_IGNORE_COL.COLUMN_NAME is
'����';

/*==============================================================*/
/* Index: IGNORE_COL_UNIX1                                      */
/*==============================================================*/
create unique index IGNORE_COL_UNIX1 on GEN_IGNORE_COL (
   DATASOURCE_ID ASC,
   COLUMN_NAME ASC
);

/*==============================================================*/
/* Table: GEN_JDBC_TYPE_DEFAULT_VAL                             */
/*==============================================================*/
create table GEN_JDBC_TYPE_DEFAULT_VAL 
(
   JDBC_TYPE            VARCHAR2(16),
   DEFAULT_VAL          VARCHAR2(1024)
);

comment on table GEN_JDBC_TYPE_DEFAULT_VAL is
'jdbc����Ĭ��ֵ��';

comment on column GEN_JDBC_TYPE_DEFAULT_VAL.JDBC_TYPE is
'jdbc����';

comment on column GEN_JDBC_TYPE_DEFAULT_VAL.DEFAULT_VAL is
'Ĭ��ֵ';

/*==============================================================*/
/* Index: JDBC_TYPE_UNIX1                                       */
/*==============================================================*/
create unique index JDBC_TYPE_UNIX1 on GEN_JDBC_TYPE_DEFAULT_VAL (
   JDBC_TYPE ASC
);

/*==============================================================*/
/* Table: GEN_STRATEGY_TEMPLATE                                 */
/*==============================================================*/
create table GEN_STRATEGY_TEMPLATE 
(
   STRATEGY_TMPL_ID     VARCHAR2(16),
   STRATEGY_TMPL_NAME   VARCHAR2(32),
   STRATEGY_CODE        VARCHAR2(16),
   BASE_VALUE           INTEGER,
   PREFIX               VARCHAR2(32),
   SUFFIX               VARCHAR2(32),
   STEP                 NUMBER(10)           default 1,
   QUERY_SQL            VARCHAR2(1024)       default ' ',
   QUERY_COL            VARCHAR2(32)         default ' ',
   RANDOM_ELE           VARCHAR2(1024)       default ' ',
   DICT_COL_NAME        VARCHAR2(32)         default ' '
);

comment on table GEN_STRATEGY_TEMPLATE is
'����ģ��';

comment on column GEN_STRATEGY_TEMPLATE.STRATEGY_TMPL_ID is
'����ģ��ID����Ϊ@ʱ������';

comment on column GEN_STRATEGY_TEMPLATE.STRATEGY_TMPL_NAME is
'����ģ������';

comment on column GEN_STRATEGY_TEMPLATE.STRATEGY_CODE is
'���Դ���';

comment on column GEN_STRATEGY_TEMPLATE.BASE_VALUE is
'����ֵ';

comment on column GEN_STRATEGY_TEMPLATE.PREFIX is
'ǰ׺';

comment on column GEN_STRATEGY_TEMPLATE.SUFFIX is
'��׺';

comment on column GEN_STRATEGY_TEMPLATE.STEP is
'����';

comment on column GEN_STRATEGY_TEMPLATE.QUERY_SQL is
'��ѯSQL';

comment on column GEN_STRATEGY_TEMPLATE.QUERY_COL is
'��ѯ����';

comment on column GEN_STRATEGY_TEMPLATE.RANDOM_ELE is
'���Ԫ���б�';

comment on column GEN_STRATEGY_TEMPLATE.DICT_COL_NAME is
'�ֵ�����';

/*==============================================================*/
/* Index: STRATEGY_TMPL_UNIX1                                   */
/*==============================================================*/
create unique index STRATEGY_TMPL_UNIX1 on GEN_STRATEGY_TEMPLATE (
   STRATEGY_TMPL_ID ASC
);

/*==============================================================*/
/* Table: GEN_SYSTEM_CONFIG                                     */
/*==============================================================*/
create table GEN_SYSTEM_CONFIG 
(
   DATASOURCE_ID        VARCHAR2(16),
   DATABASE_URL         VARCHAR2(512),
   DATABASE_USER        VARCHAR2(512),
   DATABASE_PASSWORD    VARCHAR2(512),
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
   GEN_NUM              NUMBER(10)           default 0
);

comment on table GEN_TABLE_CONFIG is
'���������ñ�';

comment on column GEN_TABLE_CONFIG.DATASOURCE_ID is
'����ԴID';

comment on column GEN_TABLE_CONFIG.TABLE_CODE is
'���ɱ����';

comment on column GEN_TABLE_CONFIG.GEN_NUM is
'��������';

/*==============================================================*/
/* Index: TAB_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index TAB_CONFIG_UNIX1 on GEN_TABLE_CONFIG (
   DATASOURCE_ID ASC,
   TABLE_CODE ASC
);

