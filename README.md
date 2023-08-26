# 数据库数据生成器
简介：基于配置的数据库数据生成器，使用YML文件配置表的数据生成策略即可快速生成数据

配置文件：
- db.setting:数据库连接配置
- generate.yml:表数据生成配置
- startup.bat：启动脚本
使用说明：
1.在db.setting文件配置数据源，支持多数据源配置，使用[]分隔多个数据源组，内部使用的是hutool的数据库工具操作数据库
2.配置generate.yml
```yml
generate: kbssoptsett # 需要生成的数据源ID，多个以逗号分隔，只会根据这个读取对应的数据源配置生成数据，配置**ALL**则生成全部
jdbcTypeDefaultValue: # 类型默认值配置，配置JdbcType的类型默认值，内置变量$sysdate代表当前时间
  VARCHAR: 1
  CHAR: 1
  NUMERIC: 1
  TIMESTAMP: $sysdate
  INTEGER: 1
  BIGINT: 1
  SMALLINT: 1
dataConfigList: # 数据配置列表，所有的数据配置在这个列表下
  - dataSourceId: mysql #数据源ID，对应db.setting配置的数据源ID，在generate配置填写后会生成此配置下的所有表数据
    columnConfig: # 列配置列表，对于每个列的数据生成策略进行配置
      - colName: id # 列名
      	# 生成策略，有6种：
      	# default(默认自增，从0开始)
      	# auto-inc(自增策略，可配置步进，前缀，后缀，基础值，基于此值开始自增)
      	# dict-value(字典值，随机一个数据字典的值，可指定用哪个数据字典，不指定默认列名的数据字典)
      	# fixed-value(固定值，已废弃)
      	# rand-ele(随机数组元素，可以指定从一个数组元素随机得到)
      	# rand-table-ele(随机表元素，指定查询的sql和取的列值，从查询结果中随机取值)
        strategy: default
        fixedValue: 1 # 固定值策略的属性，在strategy=fixed-value生效
        baseValue: # 自增策略的属性，在strategy=auto-inc生效
        randomEle: ["1","2","3"] # 随机元素的属性，在strategy=rand-ele生效
        querySql: select A from T_A # rand-table-ele策略的查询sql，只允许标量查询，返回单列值
        queryCol: A # 与querySql连用，指定查询的单列是哪个
        dictColName: 
        prefix:
        suffix:
        step:
    tableConfig:
      - tableName: a
        genNum: 0
  - dataSourceId: cur
    columnConfig:
      - colName: STK_CODE,REC_SN,FEE_TEMPLATE_ID,INT_ORG,FEE_CFG_SN,FEE_CFG_ID,FEE_TEMPLATE_CFG_SN,ORIGIN_TMPL_SN
        strategy: default
      - colName: ABN_REASON
        strategy: dict-value
      - colName: OPT_NUM
        strategy: auto-inc
        baseValue: 600000
      - colName: CUACCT_CODE
        strategy: auto-inc
        baseValue: 2200000000
      - colName: TRDACCT
        strategy: auto-inc
        baseValue: 56200000
      - colName: COMB_NUM
        strategy: auto-inc
        baseValue: 1100000
      - colName: LEG1_TYPE,LEG2_TYPE,LEG3_TYPE,LEG4_TYPE
        strategy: dict-value
        dictColName: OPT_TYPE
      - colName: LEG1_SIDE,LEG2_SIDE,LEG3_SIDE,LEG4_SIDE
        strategy: dict-value
        dictColName: OPT_SIDE
      - colName: LEG1_NUM,LEG2_NUM,LEG3_NUM,LEG4_NUM
        strategy: rand-table-ele
        querySql: "select OPT_NUM  from OPT_INFO"
        queryCol: OPT_NUM
      - colName: CURRENCY
        strategy: rand-ele
        randomEle: ["0","1","2"]
      - colName: CONFIRM_FLAG
        strategy: rand-ele
        randomEle: ["0","1"]
      - colName: CUST_CODE
        strategy: auto-inc
        baseValue: 1200000000
      - colName: CHANNELES
        strategy: dict-value
        dictColName: CHANNEL
      - colName: CUACCT_LVLS
        strategy: dict-value
        dictColName: CUACCT_LVL
      - colName: CUACCT_CLSES
        strategy: dict-value
        dictColName: CUACCT_CLS
      - colName: CUACCT_GRPS
        strategy: dict-value
        dictColName: CUACCT_GRP
      - colName: FEE_TEMPLATE_NAME
        strategy: auto-inc
        baseValue: 0
        prefix: 模板名称
      - colName: FEE_TEMPLATE_DESC
        strategy: auto-inc
        baseValue: 0
        prefix: 模板描述
      - colName: FEE_ID
        strategy: rand-table-ele
        querySql: "select FEE_ID from FEE_DEFINE"
        queryCol: FEE_ID
#      - colName: FEE_CAL_AMT
#        strategy: auto-inc
#        querySql: "select FEE_ID from FEE_DEFINE"
#        queryCol: FEE_ID
      - colName: STK_CLSES
        strategy: dict-value
        dictColName: STK_CLS
      - colName: BIZ_CODES
        strategy: dict-value
        dictColName: BIZ_CODE
      - colName: COMMISION_MODE
        strategy: rand-ele
        randomEle: ["0","1","2"]
      - colName: TMPL_ORIGIN
        strategy: rand-ele
        randomEle: ["0","1","2","3"]
      - colName: CHARGE_TYPE
        strategy: rand-ele
        randomEle: ["0","1"]
    tableConfig:
      # 佣金模板配置
      - tableName: COMM_TEMPLATE
        genNum: 0
      # 机构配置
      - tableName: BRANCH_ORG_CFG
        genNum: 0
      # 资金账号配置
      - tableName: CUACCT_FEE_CFG
        genNum: 0
      # 费用模板声明
      - tableName: FEE_TEMPLATE_DEFINE
        genNum: 0
      # 按证券类别的费用配置
      - tableName: STKCLS_STDFEE_RATE
        genNum: 0
      # 按证券代码的费用配置
      - tableName: STKCODE_STDFEE_RATE
        genNum: 0
      # 分段配置
      - tableName: SUB_CAL_CFG
        genNum: 0
      - tableName: CUST_SETT
        genNum: 0
      - tableName: OPT_INFO
        genNum: 0
      - tableName: STKCLS_STDFEE_RATE
        genNum: 0
      - tableName: OPT_CLOSE_PRICE
        genNum: 0
      - tableName: OPT_ADJUST_INFO
        genNum: 0
      - tableName: OPT_ASSET
        genNum: 0
      - tableName: Q_CUACCT_FUND
        genNum: 0
      - tableName: OPT_CUACCT_FUND
        genNum: 0
      - tableName: OPT_COMB_ASSET
        genNum: 0
    dictConfig:
      dictTableName: UPM_DICT_ITEMS
      dictCodeColName: DICT_CODE
      dictItemColName: DICT_ITEM
    loadDictCache: true
    colDefaultValue:
      MAX_FEE: 500
      MIN_FEE: 0
      SETT_DATE: 20230404
      TRD_DATE: 20230404
      MARKET: 1
      BOARD: 15
      SUB_CUACCT_CODE: 0
      FEE_CAL_AMT: 100
      FEE_MAX: 100
      FEE_MIN: 0
      FEE_RATE: 1
    ignoreCol: PARTITION_FIELD
  - dataSourceId: his
    columnConfig:
      - colName: STK_CODE
        strategy: default
      - colName: ABN_REASON
        strategy: dict-value
      - colName: OPT_NUM
        strategy: auto-inc
        baseValue: 600000
      - colName: FEE_CFG_SN
        strategy: default
      - colName: CUACCT_CODE
        strategy: auto-inc
        baseValue: 2200000000
      - colName: TRDACCT
        strategy: auto-inc
        baseValue: 56200000
      - colName: COMB_NUM
        strategy: auto-inc
        baseValue: 1100000
      - colName: LEG1_TYPE,LEG2_TYPE,LEG3_TYPE,LEG4_TYPE
        strategy: dict-value
        dictColName: OPT_TYPE
      - colName: LEG1_SIDE,LEG2_SIDE,LEG3_SIDE,LEG4_SIDE
        strategy: dict-value
        dictColName: OPT_SIDE
      - colName: LEG1_NUM,LEG2_NUM,LEG3_NUM,LEG4_NUM
        strategy: rand-table-ele
        querySql: "select OPT_NUM  from OPT_INFO"
        queryCol: OPT_NUM
      - colName: CURRENCY
        strategy: rand-ele
        randomEle: ["0","1","2"]
      - colName: CONFIRM_FLAG
        strategy: rand-ele
        randomEle: ["0","1"]
      - colName: CUST_CODE
        strategy: auto-inc
        baseValue: 1200000000
    tableConfig:
      - tableName: CUST_SETT
        genNum: 0
      - tableName: OPT_INFO
        genNum: 0
      - tableName: STKCLS_STDFEE_RATE
        genNum: 0
      - tableName: OPT_CLOSE_PRICE
        genNum: 0
      - tableName: OPT_ADJUST_INFO
        genNum: 0
      - tableName: OPT_ASSET
        genNum: 0
      - tableName: Q_CUACCT_FUND
        genNum: 0
      - tableName: OPT_CUACCT_FUND
        genNum: 0
      - tableName: OPT_COMB_ASSET
        genNum: 0
    colDefaultValue:
      SETT_DATE: 20230404
      TRD_DATE: 20230404
      MARKET: 1
      BOARD: 15
      SUB_CUACCT_CODE: 0
  - dataSourceId: kbssoptsett
    columnConfig:
      - colName: USER_CODE,CUST_CODE
        strategy: auto-inc
        baseValue: 124100000000
      - colName: TRDACCT
        strategy: auto-inc
        baseValue: 13100000000
        prefix: A
      - colName: INT_ORG
        strategy: rand-table-ele
        querySql: "select ORG_CODE from ORG"
        queryCol: ORG_CODE
    tableConfig:
      - tableName: USERS
        genNum: 100
      - tableName: USER_BASIC_INFO
        genNum: 100
      - tableName: CUSTOMER
        genNum: 100
      - tableName: CUST_OTHER_INFO
        genNum: 100
      - tableName: OPT_TRDACCT
        genNum: 100
    dictConfig:
      dictTableName: SYS_DD_ITEM
      dictCodeColName: DD_ID
      dictItemColName: DD_ITEM
    loadDictCache: true
    colDefaultValue:
      MAX_FEE: 500
      MIN_FEE: 0
      SETT_DATE: 20230404
      TRD_DATE: 20230404
      MARKET: 1
      BOARD: 15
      SUB_CUACCT_CODE: 0
      FEE_CAL_AMT: 100
      FEE_MAX: 100
      FEE_MIN: 0
      FEE_RATE: 1
      STKBD: 15
    ignoreCol: PARTITION_FIELD
```