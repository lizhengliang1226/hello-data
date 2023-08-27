package entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 表生成配置表(TableConfig)实体类
 *
 * @author makejava
 * @since 2023-08-27 14:35:08
 */
@Data
public class TableConfig implements Serializable {
    private static final long serialVersionUID = 863538340352524661L;
    /**
     * 数据源ID
     */
    private String datasourceId;
    /**
     * 生成表代码
     */
    private String tableCode;
    /**
     * 生成数量
     */
    private Long genNum;
    /**
     * 是否加载字典缓存 1-加载 0-不加载
     */
    private Integer loadDictCache;
    /**
     * 字典表名
     */
    private String dictTableName;
    /**
     * 字典表的字典代码列名
     */
    private String dictCodeColName;
    /**
     * 字典表的字典项列名
     */
    private String dictItemColName;

}

