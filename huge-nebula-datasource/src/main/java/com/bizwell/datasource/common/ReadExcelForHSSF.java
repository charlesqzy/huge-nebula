package com.bizwell.datasource.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XLSHaderType;

/**
 * excel解析
 * Created by liujian on 2018/4/28.
 */
public class ReadExcelForHSSF {
	private static Logger logger = LoggerFactory.getLogger(ReadExcelForHSSF.class);

	private static String excelHader[] = { "A", "B", "C", "D", "E", "F", "G" };

	public static String readExcel(String filePath,String fileName) throws IOException {

		FileInputStream fileInputStream = new FileInputStream(filePath+fileName);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
		HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);

		int numberOfSheets = workbook.getNumberOfSheets();
		logger.info("numberOfSheets=" + numberOfSheets);	
		
		SheetInfo[] sheets = new SheetInfo[numberOfSheets];
		
		//循环每个sheet
		for (int s = 0; s < numberOfSheets; s++) {
			HSSFSheet sheet = workbook.getSheetAt(s);
			String sheetName = sheet.getSheetName();
			//HSSFSheet sheet = workbook.getSheet("Sheet1");
			int lastRowIndex = sheet.getLastRowNum();
			logger.info("lastRowIndex=" + lastRowIndex);
			
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

				for (int j = 0; j < lastCellNum; j++) {

					if (i == 1) {// 取第二行数据类型
						String type = getCellValueByCell(row.getCell(j));
						logger.info(" type=" + type);
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

		
		Map xslContent = new HashMap();		
		xslContent.put("fileName", fileName);
		xslContent.put("sheets", sheets);

		//generateCreateTable(typeList,contentList,filePath+fileName);
		

		return JsonUtils.toJson(xslContent);
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
	private static String generateCreateTable(List<XLSHaderType> typeList,List<Map<String, String>> contentList, String filePath) {
		//获取文件hash值
		String md5Hashcode = FileMD5Util.getFileMD5(new File(filePath));
		logger.info("filePath = "+filePath + "  md5Hashcode="+md5Hashcode);		
		String tableName = "xls_"+md5Hashcode;
		
		StringBuffer sql = new StringBuffer();
		sql.append("create table ").append(tableName).append("(");		
		for(XLSHaderType type : typeList){			
			sql.append(type.getProp());
			if("string".equals(type.getType())){
				sql.append(" varchar(100) ,");
			}else if("numeric".equals(type.getType())){
				sql.append(" double ,");
			}	
		}
		//删除最后一个都好
		sql.delete(sql.lastIndexOf(","), sql.lastIndexOf(",")+1);
		sql.append(")");
		
		logger.info("create table sql ==== "+ sql);
		return "";
	}

	public static void main(String[] args) throws IOException {
		String result = new ReadExcelForHSSF().readExcel("D:\\","test.xls");
		System.out.println(result);
	}
}
