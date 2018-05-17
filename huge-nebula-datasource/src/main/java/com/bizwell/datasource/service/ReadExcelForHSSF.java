package com.bizwell.datasource.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.bean.XlsContent;


/**
 * excel解析 Created by liujian on 2018/4/28.
 */
@Service
public class ReadExcelForHSSF {
	private static Logger logger = LoggerFactory.getLogger(ReadExcelForHSSF.class);

	private static String excelHader[] = { "A", "B", "C", "D", "E", "F", "G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z" };
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	

	public XlsContent readExcel(String filePath, String fileName,boolean isCut) throws IOException {

		FileInputStream fileInputStream = new FileInputStream(filePath + fileName);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
		HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);

		int numberOfSheets = workbook.getNumberOfSheets();
		logger.info("numberOfSheets=" + numberOfSheets);

		int fileRows = 0;
		int fileColumns = 0;

		SheetInfo[] sheets = new SheetInfo[numberOfSheets];

		// 循环每个sheet
		for (int s = 0; s < numberOfSheets; s++) {
			HSSFSheet sheet = workbook.getSheetAt(s);
			String sheetName = sheet.getSheetName();
			int lastRowIndex = sheet.getLastRowNum();
			logger.info("sheetName== " + sheetName + "   lastRowIndex=" + lastRowIndex);
			fileRows += lastRowIndex;

			List<XLSHaderType> typeList = new ArrayList<XLSHaderType>();
			Map<String, String> rowCellValues;
			List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();

			for (int i = 0; i <= lastRowIndex; i++) {
				HSSFRow row = sheet.getRow(i);

				if (row == null) {
					break;
				}

				short lastCellNum = row.getLastCellNum();
				rowCellValues = new HashMap<String, String>();
				fileColumns += lastCellNum;

				for (int j = 0; j < lastCellNum; j++) {

					if (i == 1) {// 取第二行数据类型
						String type = getCellValueByCell(row.getCell(j));
						typeList.add(new XLSHaderType(excelHader[j], type));
					}
					
					
					String value = "";
					
					HSSFCell hssfCell = row.getCell(j);
			        DecimalFormat df = new DecimalFormat("#");
			        if(hssfCell != null){
				        switch (row.getCell(j).getCellType()){
				        case HSSFCell.CELL_TYPE_NUMERIC:
				        	value= df.format(hssfCell.getNumericCellValue());
				        	
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

			        rowCellValues.put(excelHader[j], value);			
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
				value = "date";break;
			} else {
				value = "numeric";
			}
			break;
		case HSSFCell.CELL_TYPE_STRING: // 字符串
			String sss = cell.getStringCellValue();
			if(isRightDateStr(cell.getStringCellValue(),"yyyy-MM-dd")){
				value = "date";break;
			}
			if(isRightDateStr(cell.getStringCellValue(),"yyyy-MM-dd HH:mm:ss")){
				value = "date";break;
			}
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
   
   
   /**
    * 判断是否是对应的格式的日期字符串
    * @param dateStr
    * @param datePattern
    * @return
    */
   public static boolean isRightDateStr(String dateStr,String datePattern){
       DateFormat dateFormat  = new SimpleDateFormat(datePattern);
       try {
           //采用严格的解析方式，防止类似 “2017-05-35” 类型的字符串通过
           dateFormat.setLenient(false);
           dateFormat.parse(dateStr);
           Date date = (Date)dateFormat.parse(dateStr);
           //重复比对一下，防止类似 “2017-5-15” 类型的字符串通过
           String newDateStr = dateFormat.format(date);
           if(dateStr.equals(newDateStr)){
               return true;
           }else {
               //logger.error("字符串dateStr:{}， 不是严格的 datePattern:{} 格式的字符串",dateStr,datePattern);
               return false;
           }
       } catch (ParseException e) {
    	   //logger.error("字符串dateStr:{}，不能按照 datePattern:{} 样式转换",dateStr,datePattern);
           return false;
       }
   }
   
	
	
	// drop表
	public String generateDropTableSQL(String tableName) {
		StringBuffer dropSql = new StringBuffer();
		dropSql.append("DROP TABLE IF EXISTS ").append(tableName);
		logger.info("dropSql ==== " + dropSql);
		return dropSql.toString();
	}
	
	// truncate表
	public String generateTruncateTableSQL(String tableName) {
		StringBuffer truncateSql = new StringBuffer();
		truncateSql.append("TRUNCATE TABLE ").append(tableName);
		logger.info("truncateSql ==== " + truncateSql);
		return truncateSql.toString();
	}

	// 动态创建表
	public String generateCreateTableSQL(List<XLSHaderType> typeList,String tableName) {
		
		if(typeList.size()==0){
			return "select 1";
		}

		StringBuffer createSql = new StringBuffer();
		createSql.append("create table ").append(tableName).append("(");
		for (XLSHaderType type : typeList) {
			createSql.append(type.getProp());
			if ("string".equals(type.getType())) {
				createSql.append(" varchar(200) ,");
			} else if ("date".equals(type.getType())) {
				createSql.append(" varchar(20) ,");
				//createSql.append(" timestamp ,");
			} else if ("numeric".equals(type.getType())) {
				createSql.append(" double ,");
			}
		}
		
			
		
		// 删除最后一个逗号
		createSql.delete(createSql.lastIndexOf(","), createSql.lastIndexOf(",") + 1);
		createSql.append(")");
		
		logger.info("createSql ==== " + createSql);

		return createSql.toString();
	}

	// 动态插入元数据
	public String generateMetadataSQL(List<XLSHaderType> typeList, List<Map<String, String>> contentList,
			Integer sheetId, String tableName) {
		
		if(typeList.size()==0){
			return "select 1";
		}		
		if(contentList.size()==0){
			return "select 1";
		}
		

		int fieldType=2;
		for (XLSHaderType type : typeList) {
			
			if ("string".equals(type.getType())) {
				fieldType =2;
			} else if ("date".equals(type.getType())) {
				fieldType =3;
			} else if ("numeric".equals(type.getType())) {
				fieldType = 1;
			}
		}
		
		StringBuffer metadataSQL = new StringBuffer();
		Map<String, String> headerMap = contentList.get(0);
		metadataSQL.append(
				"insert into ds_sheet_metadata(sheet_id,table_name,field_column,field_name_old,field_name_new,field_type,field_comment,is_visible) values ");
		for (int i = 0; i < headerMap.size(); i++) {
			metadataSQL.append("('" + sheetId + "','"+tableName+"','"+excelHader[i]+"','" + headerMap.get(excelHader[i]) + "','" + headerMap.get(excelHader[i]) + "',"+fieldType+",'','1'),");
		}
		metadataSQL.delete(metadataSQL.lastIndexOf(","), metadataSQL.lastIndexOf(",") + 1);
		logger.info("metadataSQL ==== " + metadataSQL);

		return metadataSQL.toString();
	}

	// 动态插入数据
	public String generateInsertTableSQL(List<Map<String, String>> contentList, String tableName) {
		if(contentList.size()==0){
			return "select 1";
		}
		
		StringBuffer insertSql = new StringBuffer();
		insertSql.append(" insert into ").append(tableName);
		insertSql.append(" values  ");
		for (int k = 1; k < contentList.size(); k++) {
			Map<String, String> map = contentList.get(k);
			insertSql.append("(");
			for (int i = 0; i < map.size(); i++) {
				insertSql.append("'").append(map.get(excelHader[i])).append("',");
			}
			insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
			insertSql.append("),");
		}
		insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
		logger.info("insertSql ==== " + insertSql);

		return insertSql.toString();
	}

	/*public static void main(String[] args) throws IOException {
		String filePath = "D:\\";
		String fileName = "test3.xls";

		XlsContent xlsContent = new ReadExcelForHSSF().readExcel(filePath, fileName);

		System.out.println(JsonUtils.toJson(xlsContent));
	}*/

}
