/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/8/29 19:21:46                           */
/*==============================================================*/


drop index COL_CONFIG_UNIX1 on GEN_COLUMN_CONFIG;

drop table if exists GEN_COLUMN_CONFIG;

drop index SYS_CONFIG_UNIX1 on GEN_SYSTEM_CONFIG;

drop table if exists GEN_SYSTEM_CONFIG;

drop index TABLE_CONFIG_UNIX1 on GEN_TABLE_CONFIG;

drop table if exists GEN_TABLE_CONFIG;

/*==============================================================*/
/* Table: GEN_COLUMN_CONFIG                                     */
/*==============================================================*/
create table GEN_COLUMN_CONFIG
(
   DATASOURCE_ID        varchar(16) comment '����ԴID',
   COLUMN_NAME          varchar(32) comment '����',
   STRATEGY_CODE        varchar(16) comment '���Դ���',
   DEFAULT_VAL          varchar(1024) comment 'Ĭ��ֵ',
   BASE_VALUE           int comment '����ֵ',
   PREFIX               varchar(32) comment 'ǰ׺',
   SUFFIX               varchar(32) comment '��׺',
   STEP                 numeric(10,0) default 0 comment '����',
   QUERY_SQL            varchar(1024) default ' ' comment '��ѯSQL',
   QUERY_COL            varchar(32) default ' ' comment '��ѯ����',
   RANDOM_ELE           varchar(1024) default ' ' comment '���Ԫ���б�',
   DICT_COL_NAME        varchar(32) default ' ' comment '�ֵ�����'
);

alter table GEN_COLUMN_CONFIG comment '�����ñ�';

/*==============================================================*/
/* Index: COL_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index COL_CONFIG_UNIX1 on GEN_COLUMN_CONFIG
(
   DATASOURCE_ID,
   COLUMN_NAME
);

/*==============================================================*/
/* Table: GEN_SYSTEM_CONFIG                                     */
/*==============================================================*/
create table GEN_SYSTEM_CONFIG
(
   DATASOURCE_ID        varchar(16) comment '����ԴID',
   DATABASE_URL         varchar(16) comment '����ԴURL',
   DATABASE_USER        varchar(16) comment '����Դ�û���',
   DATABASE_PASSWORD    varchar(32) comment '����Դ����',
   LOAD_DICT_CACHE      numeric(1,0) default 0 comment '�Ƿ�����ֵ仺�� 1-���� 0-������',
   DICT_TABLE_NAME      varchar(32) default ' ' comment '�ֵ����',
   DICT_CODE_COL_NAME   varchar(32) default ' ' comment '�ֵ����ֵ��������',
   DICT_ITEM_COL_NAME   varchar(32) default ' ' comment '�ֵ����ֵ�������'
);

alter table GEN_SYSTEM_CONFIG comment 'ϵͳ���ñ�';

/*==============================================================*/
/* Index: SYS_CONFIG_UNIX1                                      */
/*==============================================================*/
create unique index SYS_CONFIG_UNIX1 on GEN_SYSTEM_CONFIG
(
   DATASOURCE_ID
);

/*==============================================================*/
/* Table: GEN_TABLE_CONFIG                                      */
/*==============================================================*/
create table GEN_TABLE_CONFIG
(
   DATASOURCE_ID        varchar(16) comment '����ԴID',
   TABLE_CODE           varchar(32) comment '���ɱ����',
   GEN_NUM              numeric(10,0) default 0 comment '��������',
   COLUMN_NAME          varchar(32) comment '����',
   STRATEGY_CODE        varchar(16) comment '���Դ���',
   DEFAULT_VAL          varchar(1024) comment 'Ĭ��ֵ',
   BASE_VALUE           int comment '����ֵ',
   PREFIX               varchar(32) comment 'ǰ׺',
   SUFFIX               varchar(32) comment '��׺',
   STEP                 numeric(10,0) default 0 comment '����',
   QUERY_SQL            varchar(1024) default ' ' comment '��ѯSQL',
   QUERY_COL            varchar(32) default ' ' comment '��ѯ����',
   RANDOM_ELE           varchar(1024) default ' ' comment '���Ԫ���б�',
   DICT_COL_NAME        varchar(32) default ' ' comment '�ֵ�����'
);

alter table GEN_TABLE_CONFIG comment '���������ñ�';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on GEN_TABLE_CONFIG
(
   DATASOURCE_ID,
   TABLE_CODE,
   COLUMN_NAME
);

