package com.bizwell.datasource.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.JsonUtils;

/**
 * excel解析
 * Created by liujian on 2018/4/28.
 */
@Service
public class ReadExcelForHSSF {
	private static Logger logger = LoggerFactory.getLogger(ReadExcelForHSSF.class);
	
	private static String excelHader[] = { "A", "B", "C", "D", "E", "F", "G" };
	

	

	public XlsContent readExcel(String filePath,String fileName) throws IOException {

		FileInputStream fileInputStream = new FileInputStream(filePath+fileName);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
		HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);

		int numberOfSheets = workbook.getNumberOfSheets();
		logger.info("numberOfSheets=" + numberOfSheets);

		int fileRows = 0;
		int fileColumns = 0;
		
		SheetInfo[] sheets = new SheetInfo[numberOfSheets];
		
		//循环每个sheet
		for (int s = 0; s < numberOfSheets; s++) {
			HSSFSheet sheet = workbook.getSheetAt(s);
			String sheetName = sheet.getSheetName();
			//HSSFSheet sheet = workbook.getSheet("Sheet1");
			int lastRowIndex = sheet.getLastRowNum();
			logger.info("sheetName== "+sheetName+"   lastRowIndex=" + lastRowIndex);
			fileRows += lastRowIndex;
			
			List<XLSHaderType> typeList = new ArrayList<XLSHaderType>();
			Map<String,String> rowCellValues;
			List<Map<String,String>> contentList = new ArrayList<Map<String,String>>();
			

			for (int i = 0; i <= lastRowIndex; i++) {
				HSSFRow row = sheet.getRow(i);

				if (row == null) {
					break;
				}

				short lastCellNum = row.getLastCellNum();
				rowCellValues = new HashMap<String,String>();
				//rowCellValues = new String[lastCellNum];
				fileColumns += lastCellNum;

				for (int j = 0; j < lastCellNum; j++) {

					if (i == 1) {// 取第二行数据类型
						String type = getCellValueByCell(row.getCell(j));
						//logger.info(" type=" + type);
						//cellType.put(excelHader[j], type);
						typeList.add(new XLSHaderType(excelHader[j],type));
					}

					// String cellValue = row.getCell(j).getStringCellValue();
					if (row.getCell(j) != null) {
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						String cellValue = row.getCell(j).getStringCellValue();
						rowCellValues.put(excelHader[j] , cellValue);
					
					}

				}
				contentList.add(rowCellValues);
			}
			
			sheets[s] = new SheetInfo(sheetName, typeList, contentList);
			
		}

		bufferedInputStream.close();

		
//		Map xslContent = new HashMap();		
//		xslContent.put("fileName", fileName);
//		xslContent.put("sheets", sheets);
//		xslContent.put("fileRows", fileRows);
//		xslContent.put("fileColumns", fileColumns);
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
				value = "date";
				break;
			} else {
				value = "numeric";				
//				System.out.println("aaa="+
//				 new DecimalFormat("0").format(cell.getNumericCellValue())
//				 );
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			value = "string";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
			value = "boolean";
			break;
		case HSSFCell.CELL_TYPE_FORMULA: // 公式
			value = "formula";
			break;
		case HSSFCell.CELL_TYPE_BLANK: // 空值
			value = "blank";
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障
			value = "非法字符";
			break;
		default:
			value = "未知类型";
			break;
		}
		return value;
	}

	// 动态创建表
	public String generateCreateTableSQL(List<XLSHaderType> typeList,List<Map<String, String>> contentList, String tableName) {

		
		StringBuffer createSql = new StringBuffer();
		createSql.append("create table ").append(tableName).append("(");		
		for(XLSHaderType type : typeList){			
			createSql.append(type.getProp());
			if("string".equals(type.getType())){
				createSql.append(" varchar(100) ,");
			}else if("numeric".equals(type.getType())){
				createSql.append(" double ,");
			}	
		}
		//删除最后一个逗号
		createSql.delete(createSql.lastIndexOf(","), createSql.lastIndexOf(",")+1);
		createSql.append(")");		
		logger.info("createSql ==== "+ createSql);
		
		return createSql.toString();
	}
	
	
	// 动态插入元数据
	public String generateMetadataSQL(List<XLSHaderType> typeList,List<Map<String, String>> contentList, Integer sheetId) {
		
		StringBuffer metadataSQL = new StringBuffer();
		Map<String,String> headerMap = contentList.get(0);
		metadataSQL.append("insert into ds_sheet_metadata(sheet_id,field_name_old,field_name_new,field_type,field_comment,is_visible) values ");
		for(int i = 0;i< headerMap.size();i++){
			metadataSQL.append("('"+sheetId+"','"+headerMap.get(excelHader[i])+"','field_name_new','1','field_comment','1'),");
		}		
		metadataSQL.delete(metadataSQL.lastIndexOf(","), metadataSQL.lastIndexOf(",")+1);
		logger.info("metadataSQL ==== "+ metadataSQL);
		
		return metadataSQL.toString();
	}
	
	
	
	// 动态插入数据
	public String generateInsertTableSQL(List<Map<String, String>> contentList, String tableName) {		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append(" insert into ").append(tableName);
		insertSql.append(" values  ");
		for (int k = 1; k < contentList.size(); k++) {
			Map<String,String> map = contentList.get(k);
			insertSql.append("(");
			for(int i = 0;i< map.size();i++){
				insertSql.append("'").append(map.get(excelHader[i])).append("',");
			}
			insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",")+1);
			insertSql.append("),");
		}		
		insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",")+1);
		logger.info("insertSql ==== "+ insertSql);
		
		return insertSql.toString();
	}
	
	
	
	
	
/*
	public static void main(String[] args) throws IOException {
		String filePath = "D:\\";
		String fileName = "test.xls";
		
		XlsContent xlsContent = new ReadExcelForHSSF().readExcel(filePath,fileName);
		
		//动态创建mysql表，插入数据		
		ReadExcelForHSSF.generateCreateTable(xlsContent.getSheets()[0].getTypeList(),
				xlsContent.getSheets()[0].getContentList(), filePath + fileName);
		
		System.out.println(JsonUtils.toJson(xlsContent));
	}*/
}