package com.bizwell.datasource.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.Constants;
import com.bizwell.datasource.common.DateHelp;
import com.bizwell.datasource.common.JsonUtils;
import com.csvreader.CsvReader;

public class ReadCSVUtil {

	public static XlsContent readCSV(String filePath, String fileName, boolean isCut) throws IOException {

		int fileRows = 0;
		int fileColumns = 0;
		SheetInfo[] sheets = new SheetInfo[1];

		// 创建CSV读对象
		CsvReader csvReader = new CsvReader(filePath + fileName, ',', Charset.forName("utf-8"));

		List<XLSHaderType> typeList = new ArrayList<XLSHaderType>();
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> rowCellValues = null;

		String[] headers;
		if(csvReader.readHeaders()){
			rowCellValues = new HashMap<String, String>();
			headers = csvReader.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				rowCellValues.put(Constants.excelHader[i], headers[i]);
			}
			contentList.add(rowCellValues);
		}
		
		
		String type;
		int s = 0;
		while (csvReader.readRecord()) {

			if (s == 0) {// 取第一行的数据类型
				fileColumns = csvReader.getColumnCount();
				for (int j = 0; j < fileColumns; j++) {
					String value = csvReader.get(j);

					type = "";
					if (DateHelp.isRightDateStr(value, "yyyy-MM-dd")) {
						type = "3";
					} else if (DateHelp.isRightDateStr(value, "yyyy-MM-dd HH:mm:ss")) {
						type = "3";
					} else if (isNumericzidai(value)) {
						type = "1";
					} else {
						type = "2";
					}

			
					XLSHaderType xlsHaderType = new XLSHaderType();
					xlsHaderType.setProp(Constants.excelHader[j]);
					xlsHaderType.setType(type);
					xlsHaderType.setLabel(value);
					typeList.add(xlsHaderType);
					

				}
				rowCellValues = new HashMap<String, String>();
				for (int j = 0; j < fileColumns; j++) {
					String value = csvReader.get(j);
					rowCellValues.put(Constants.excelHader[j], value);
				}
				contentList.add(rowCellValues);
			}else{
				rowCellValues = new HashMap<String, String>();
				for (int j = 0; j < fileColumns; j++) {
					String value = csvReader.get(j);
					rowCellValues.put(Constants.excelHader[j], value);
				}
				contentList.add(rowCellValues);
			}
			


			fileRows += 1;
			s++;
		}

		sheets[0] = new SheetInfo(fileName, typeList,
				(isCut && contentList.size() > 100 ? contentList.subList(0, 100) : contentList));

		XlsContent xslContent = new XlsContent();
		xslContent.setFileName(fileName);
		xslContent.setSheets(sheets);
		xslContent.setFileRows(fileRows);
		xslContent.setFileColumns(fileColumns);
		return xslContent;
	}

	public static boolean isNumericzidai(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {

		String path = "D:\\";
		String fileName = "position.csv";
		
		XlsContent xlsContent = ReadCSVUtil.readCSV(path, fileName, false);

		System.out.println(JsonUtils.toJson(xlsContent));
	}

}
