package com.bizwell.common;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by liujian on 2018/4/28.
 */
public class ReadExcelForHSSF {
    public static void readExcel() throws IOException {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/template.xls";
        
        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        
        int lastRowIndex = sheet.getLastRowNum();
        
        
        System.out.println("lastRowIndex="+lastRowIndex);
        for (int i = 0; i <= lastRowIndex; i++) {
            HSSFRow row = sheet.getRow(i);
            
            if (row == null) { break; }
            
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                //String cellValue = row.getCell(j).getStringCellValue();
                if(row.getCell(j)!=null){
                    row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    String cellValue = row.getCell(j).getStringCellValue();
                    System.out.println(cellValue);
                }
                
                
            }
        }
        
        
        bufferedInputStream.close();
    }
    
    public static void main(String[] args) throws IOException {
        new ReadExcelForHSSF().readExcel();
    }
}
