package com.bizwell.echarts.bean.domain;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
public class SheetMetaData {
    
	private Integer id;

    private Integer sheetId;

    private String tableName;

    private String fieldColumn;

    private String fieldNameOld;

    private String fieldNameNew;

    private Integer fieldType;

    private String fieldComment;

    private Integer isVisible;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSheetId() {
        return sheetId;
    }

    public void setSheetId(Integer sheetId) {
        this.sheetId = sheetId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getFieldColumn() {
        return fieldColumn;
    }

    public void setFieldColumn(String fieldColumn) {
        this.fieldColumn = fieldColumn == null ? null : fieldColumn.trim();
    }

    public String getFieldNameOld() {
        return fieldNameOld;
    }

    public void setFieldNameOld(String fieldNameOld) {
        this.fieldNameOld = fieldNameOld == null ? null : fieldNameOld.trim();
    }

    public String getFieldNameNew() {
        return fieldNameNew;
    }

    public void setFieldNameNew(String fieldNameNew) {
        this.fieldNameNew = fieldNameNew == null ? null : fieldNameNew.trim();
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment == null ? null : fieldComment.trim();
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

	@Override
	public String toString() {
		return String.format(
				"SheetMetaData [id=%s, sheetId=%s, tableName=%s, fieldColumn=%s, fieldNameOld=%s, fieldNameNew=%s, fieldType=%s, fieldComment=%s, isVisible=%s]",
				id, sheetId, tableName, fieldColumn, fieldNameOld, fieldNameNew, fieldType, fieldComment, isVisible);
	}
    
}