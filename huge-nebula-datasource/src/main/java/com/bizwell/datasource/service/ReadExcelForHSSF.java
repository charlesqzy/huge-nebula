package com.bizwell.datasource.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.Constants;
import com.bizwell.datasource.common.DateHelp;
import com.bizwell.datasource.common.JsonUtils;


/**
 * excel解析 Created by liujian on 2018/4/28.
 */
@Service
public class ReadExcelForHSSF {
	private static Logger logger = LoggerFactory.getLogger(ReadExcelForHSSF.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	NumberFormat nf = NumberFormat.getInstance();

	public XlsContent readExcel(String filePath, String fileName,boolean isCut) throws IOException {

		String fileFullName = filePath + fileName;
		
		FileInputStream fileInputStream = new FileInputStream(fileFullName);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

		Workbook wb = null;  
        if(fileFullName.indexOf(".xlsx")>-1){//判断excel版本
        	wb = new XSSFWorkbook(new FileInputStream(fileFullName));  
        } else {  
        	wb = new HSSFWorkbook(new FileInputStream(fileFullName));  
        } 

		int numberOfSheets = wb.getNumberOfSheets();
		logger.info("numberOfSheets=" + numberOfSheets);

		int fileRows = 0;
		int fileColumns = 0;
		SheetInfo[] sheets = new SheetInfo[numberOfSheets];

		// 循环每个sheet
		for (int s = 0; s < numberOfSheets; s++) {
			Sheet sheet =  wb.getSheetAt(s);
			String sheetName = sheet.getSheetName();
			int lastRowIndex = sheet.getLastRowNum();
			logger.info("sheetName== " + sheetName + "   lastRowIndex=" + lastRowIndex);
			fileRows += lastRowIndex;
			
			if(null != sheet.getRow(0)){
				fileColumns += sheet.getRow(0).getLastCellNum();
			}
			
			List<XLSHaderType> typeList = new ArrayList<XLSHaderType>();
			Map<String, String> rowCellValues;
			List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();

			for (int i = 0; i <= lastRowIndex; i++) {
				Row row = sheet.getRow(i);

				if (row == null) {
					break;
				}

				short lastCellNum = row.getLastCellNum();
				rowCellValues = new HashMap<String, String>();

				for (int j = 0; j < lastCellNum; j++) {

					if (i == 1) {// 取第二行数据类型
						String type = getCellValueByCell(row.getCell(j));
						typeList.add(new XLSHaderType(Constants.excelHader[j], type));
					}
					
					String value = "";
					
					Cell hssfCell = row.getCell(j);
			        //DecimalFormat df = new DecimalFormat("#");
			        if(hssfCell != null){
				        switch (row.getCell(j).getCellType()){
				        case HSSFCell.CELL_TYPE_NUMERIC:
				        	//value= String.valueOf(hssfCell.getNumericCellValue());
				        	value=nf.format(hssfCell.getNumericCellValue());
				            if(HSSFDateUtil.isCellDateFormatted(hssfCell)||isReserved(hssfCell.getCellStyle().getDataFormat())||isDateFormat(hssfCell.getCellStyle().getDataFormatString())){
				                value= sdf.format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
				            }
				            break;
				        case HSSFCell.CELL_TYPE_STRING:
				        	hssfCell.setCellType(Cell.CELL_TYPE_STRING);
				        	value= hssfCell.getStringCellValue();
				        	break;
				        case HSSFCell.CELL_TYPE_BLANK:
				        	value= "";
				        	break;
				        }
			        }

			        rowCellValues.put(Constants.excelHader[j], value);			
				}
				contentList.add(rowCellValues);
			}

			sheets[s] = new SheetInfo(sheetName, typeList, (isCut&&contentList.size()>100?contentList.subList(0, 100):contentList));
		}

		bufferedInputStream.close();

		XlsContent xslContent = new XlsContent();
		xslContent.setFileName(fileName);
		xslContent.setSheets(sheets);
		xslContent.setFileRows(fileRows);
		xslContent.setFileColumns(fileColumns);

		return xslContent;
	}

	// 获取单元格各类型值，返回字符串类型
	private static String getCellValueByCell(Cell cell) {
		String value = "";
		// 判断是否为null或空串
		if (cell == null || cell.toString().trim().equals("")) {
			return "";
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			// 如果为时间格式的内容
			if (DateUtil.isCellDateFormatted(cell)) {
				value = "3";break;
			} else {
				value = "1";
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			if(DateHelp.isRightDateStr(cell.getStringCellValue(),"yyyy-MM-dd")){
				value = "3";break;
			}
			if(DateHelp.isRightDateStr(cell.getStringCellValue(),"yyyy-MM-dd HH:mm:ss")){
				value = "3";break;
			}
			value = "2";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
			value = "4";
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			value = "5";
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空值
			value = "6";
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障
			value = "7";
			break;
		default:
			value = "7";
			break;
		}
		return value;
	}
	
	/**
    * 是否是日期格式保留字段 
    * @return booleantrue - 是保留字       false 不是 
    */  
   private boolean isReserved(short reserv)  
   {  
       if(reserv>=27&&reserv<=31)  
       {  
           return true;  
       }  
       return false;  
   }  
	
   
   /** 
    * 判断是否是中文日期格式 
    * @param isNotDate 
    * @return boolean true - 是日期格式      false不是 
    */  
   private boolean isDateFormat(String isNotDate)  
   {  
       if(isNotDate.contains("年")||isNotDate.contains("月")||isNotDate.contains("日"))  
       {  
           return true;  
       }  
       else if(isNotDate.contains("aaa;")||isNotDate.contains("AM")||isNotDate.contains("PM"))  
       {  
           return true;  
       }  
       return false;  
   }
   

	public static void main(String[] args) throws IOException {
		String filePath = "D:\\";
		String fileName = "predict.xlsx";

	
		XlsContent xlsContent = new ReadExcelForHSSF().readExcel(filePath, fileName,false);

		System.out.println(JsonUtils.toJson(xlsContent));
	}
	

}
