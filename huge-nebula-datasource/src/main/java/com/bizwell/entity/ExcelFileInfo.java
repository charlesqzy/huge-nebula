package com.bizwell.entity;

/**
 * Created by liujian on 2017/9/25.
 */
public class ExcelFileInfo {
    private Integer id;
    private String fileCode;
    private String fileName;
    private String filePath;
    private Integer fileSize;
    private Integer fileRows;
    private Integer fileColumns;
    private Integer userId;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFileCode() {
        return fileCode;
    }
    
    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Integer getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    
    public Integer getFileRows() {
        return fileRows;
    }
    
    public void setFileRows(Integer fileRows) {
        this.fileRows = fileRows;
    }
    
    public Integer getFileColumns() {
        return fileColumns;
    }
    
    public void setFileColumns(Integer fileColumns) {
        this.fileColumns = fileColumns;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
