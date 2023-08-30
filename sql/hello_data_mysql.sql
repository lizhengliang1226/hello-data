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
   DATASOURCE_ID        varchar(16) comment '数据源ID',
   COLUMN_NAME          varchar(32) comment '列名',
   STRATEGY_CODE        varchar(16) comment '策略代码',
   DEFAULT_VAL          varchar(1024) comment '默认值',
   BASE_VALUE           int comment '基础值',
   PREFIX               varchar(32) comment '前缀',
   SUFFIX               varchar(32) comment '后缀',
   STEP                 numeric(10,0) default 0 comment '步进',
   QUERY_SQL            varchar(1024) default ' ' comment '查询SQL',
   QUERY_COL            varchar(32) default ' ' comment '查询的列',
   RANDOM_ELE           varchar(1024) default ' ' comment '随机元素列表',
   DICT_COL_NAME        varchar(32) default ' ' comment '字典列名'
);

alter table GEN_COLUMN_CONFIG comment '列配置表';

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
   DATASOURCE_ID        varchar(16) comment '数据源ID',
   DATABASE_URL         varchar(16) comment '数据源URL',
   DATABASE_USER        varchar(16) comment '数据源用户名',
   DATABASE_PASSWORD    varchar(32) comment '数据源密码',
   LOAD_DICT_CACHE      numeric(1,0) default 0 comment '是否加载字典缓存 1-加载 0-不加载',
   DICT_TABLE_NAME      varchar(32) default ' ' comment '字典表名',
   DICT_CODE_COL_NAME   varchar(32) default ' ' comment '字典表的字典代码列名',
   DICT_ITEM_COL_NAME   varchar(32) default ' ' comment '字典表的字典项列名'
);

alter table GEN_SYSTEM_CONFIG comment '系统配置表';

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
   DATASOURCE_ID        varchar(16) comment '数据源ID',
   TABLE_CODE           varchar(32) comment '生成表代码',
   GEN_NUM              numeric(10,0) default 0 comment '生成数量',
   COLUMN_NAME          varchar(32) comment '列名',
   STRATEGY_CODE        varchar(16) comment '策略代码',
   DEFAULT_VAL          varchar(1024) comment '默认值',
   BASE_VALUE           int comment '基础值',
   PREFIX               varchar(32) comment '前缀',
   SUFFIX               varchar(32) comment '后缀',
   STEP                 numeric(10,0) default 0 comment '步进',
   QUERY_SQL            varchar(1024) default ' ' comment '查询SQL',
   QUERY_COL            varchar(32) default ' ' comment '查询的列',
   RANDOM_ELE           varchar(1024) default ' ' comment '随机元素列表',
   DICT_COL_NAME        varchar(32) default ' ' comment '字典列名'
);

alter table GEN_TABLE_CONFIG comment '表生成配置表';

/*==============================================================*/
/* Index: TABLE_CONFIG_UNIX1                                    */
/*==============================================================*/
create unique index TABLE_CONFIG_UNIX1 on GEN_TABLE_CONFIG
(
   DATASOURCE_ID,
   TABLE_CODE,
   COLUMN_NAME
);

