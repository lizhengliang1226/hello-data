/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/8/27 14:51:47                           */
/*==============================================================*/


drop index COLUMN_CONFIG_UNIX1 on COLUMN_CONFIG;

drop table if exists COLUMN_CONFIG;

drop index STRATEGY_UNIX1 on STRATEGY;

drop table if exists STRATEGY;

drop index TABLE_CONFIG_UNIX1 on TABLE_CONFIG;

drop table if exists TABLE_CONFIG;

/*==============================================================*/
/* Table: COLUMN_CONFIG                                         */
/*==============================================================*/
create table COLUMN_CONFIG
(
   DATASOURCE_ID        varchar(16) not null comment '����ԴID',
   COLUMN_NAME          varchar(32) not null comment '����',
   DEFAULT_VAL          varchar(1024) comment 'Ĭ��ֵ',
   STRATEGY_NAME        varchar(16) not null comment '������',
   BASE_VALUE           bigint comment '����ֵ',
   PREFIX               varchar(32) comment 'ǰ׺',
   SUFFIX               varchar(32) comment '��׺',
   STEP                 numeric(10,0) default 0 comment '����',
   QUERY_SQL            varchar(1024) comment '��ѯSQL',
   QUERY_COL            varchar(32) comment '��ѯ����',
   RANDOM_ELE           varchar(1024) comment '���Ԫ���б�',
   DICT_COL_NAME        varchar(32) comment '�ֵ�����',
   FIXED_VALUE          varchar(1024) comment '�̶�ֵ'
);

alter table COLUMN_CONFIG comment '���������ñ�';

/*==============================================================*/
/* Index: COLUMN_CONFIG_UNIX1                                   */
/*==============================================================*/
create unique index COLUMN_CONFIG_UNIX1 on COLUMN_CONFIG
(
   DATASOURCE_ID,
   COLUMN_NAME
);

/*==============================================================*/
/* Table: STRATEGY                                              */
/*==============================================================*/
create table STRATEGY
(
   STRATEGY_ID          varchar(16) not null comment '����ID',
   STRATEGY_CODE        varchar(16) not null comment '���Դ���',
   STRATEGY_NAME        varchar(16) not null comment '������'
);

alter table STRATEGY comment '���Ա�';

/*==============================================================*/
/* Index: STRATEGY_UNIX1                                        */
/*==============================================================*/
create unique index STRATEGY_UNIX1 on STRATEGY
(
   STRATEGY_ID
);

/*==============================================================*/
/* Table: TABLE_CONFIG                                          */
/*==============================================================*/
create table TABLE_CONFIG
(
   DATASOURCE_ID        varchar(16) not null comment '����ԴID',
   TABLE_CODE           varchar(32) not null comment '���ɱ����',
   GEN_NUM              numeric(10,0) not null default 0 comment '��������',
   LOAD_DICT_CACHE      numeric(1,0) not null default 0 comment '�Ƿ�����ֵ仺�� 1-���� 0-������',
   DICT_TABLE_NAME      varchar(32) not null default ' ' comment '�ֵ����',
   DICT_CODE_COL_NAME   varchar(32) not null default ' ' comment '�ֵ����ֵ��������',
   DICT_ITEM_COL_NAME   varchar(32) not null default ' ' comment '�ֵ����ֵ�������'
);

alter table TABLE_CONFIG comment '���������ñ�';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on TABLE_CONFIG
(
   DATASOURCE_ID,
   TABLE_CODE
);

