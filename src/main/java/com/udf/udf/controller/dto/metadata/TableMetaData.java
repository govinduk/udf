package com.udf.udf.controller.dto.metadata;

import java.io.Serializable;
import java.util.List;

public class TableMetaData implements Serializable {
    private  String tableName;
    private List<ColumnMetadata> columnMetadataList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnMetadata> getColumnMetadataList() {
        return columnMetadataList;
    }

    public void setColumnMetadataList(List<ColumnMetadata> columnMetadataList) {
        this.columnMetadataList = columnMetadataList;
    }
}
