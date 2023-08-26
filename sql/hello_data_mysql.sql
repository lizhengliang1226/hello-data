/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/8/27 3:15:18                            */
/*==============================================================*/


drop index COLUMN_CONFIG_UNIX1 on COLUMN_CONFIG;

drop table if exists COLUMN_CONFIG;

drop index TABLE_CONFIG_UNIX1 on TABLE_CONFIG;

drop table if exists TABLE_CONFIG;

/*==============================================================*/
/* Table: COLUMN_CONFIG                                         */
/*==============================================================*/
create table COLUMN_CONFIG
(
   DATASOURCE_ID        varchar(16) comment '数据源ID',
   COLUMN_NAME          varchar(32) comment '列名',
   DEFAULT_VAL          varchar(1024) comment '默认值',
   STRATEGY_NAME        varchar(16) comment '策略名',
   BASE_VALUE           varchar(1024) comment '基础值',
   PREFIX               varchar(32) comment '前缀',
   SUFFIX               varchar(32) comment '后缀',
   STEP                 numeric(10,0) default 0 comment '步进',
   QUERY_SQL            varchar(1024) comment '查询SQL',
   QUERY_COL            varchar(32) comment '查询的列',
   RANDOM_ELE           varchar(1024) comment '随机元素列表',
   DICT_COL_NAME        varchar(32) comment '字典列名',
   FIXED_VALUE          varchar(1024) comment '固定值'
);

alter table COLUMN_CONFIG comment '列生成配置表';

/*==============================================================*/
/* Index: COLUMN_CONFIG_UNIX1                                   */
/*==============================================================*/
create index COLUMN_CONFIG_UNIX1 on COLUMN_CONFIG
(
   DATASOURCE_ID,
   COLUMN_NAME
);

/*==============================================================*/
/* Table: TABLE_CONFIG                                          */
/*==============================================================*/
create table TABLE_CONFIG
(
   DATASOURCE_ID        varchar(16) comment '数据源ID',
   TABLE_CODE           varchar(32) comment '生成表代码',
   GEN_NUM              numeric(10,0) default 0 comment '生成数量',
   LOAD_DICT_CACHE      numeric(1,0) default 0 comment '是否加载字典缓存 1-加载 0-不加载',
   DICT_TABLE_NAME      varchar(32) default ' ' comment '字典表名',
   DICT_CODE_COL_NAME   varchar(32) default ' ' comment '字典表的字典代码列名',
   DICT_ITEM_COL_NAME   varchar(32) default ' ' comment '字典表的字典项列名'
);

alter table TABLE_CONFIG comment '生成配置表';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on TABLE_CONFIG
(
   DATASOURCE_ID,
   TABLE_CODE
);

