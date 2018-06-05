package com.bizwell.datasource.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.common.Constants;

public class MysqlHelper {
	private static Logger logger = LoggerFactory.getLogger(MysqlHelper.class);
	
	
	// drop表
	public static String generateDropTableSQL(String tableName) {
		StringBuffer dropSql = new StringBuffer();
		dropSql.append("DROP TABLE IF EXISTS ").append(tableName);
		logger.info("dropSql ==== " + dropSql);
		return dropSql.toString();
	}
	
	// truncate表
	public static String generateTruncateTableSQL(String tableName) {
		StringBuffer truncateSql = new StringBuffer();
		truncateSql.append("TRUNCATE TABLE ").append(tableName);
		logger.info("truncateSql ==== " + truncateSql);
		return truncateSql.toString();
	}

	// 动态创建表
	public static String generateCreateTableSQL(List<XLSHaderType> typeList,String tableName) {
		
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
				//createSql.append(" varchar(20) ,");
				createSql.append(" datetime ,");
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
	
	
	/**
	 * 删除元数据
	 * @param tableName
	 * @return
	 */
	public static String generateDeleteMetadataSQL(String tableName) {
		StringBuffer deleteSql = new StringBuffer();
		deleteSql.append("delete from ds_sheet_metadata where table_name='").append(tableName).append("';");
		logger.info("deleteSql ==== " + deleteSql);
		return deleteSql.toString();
	}


	// 动态插入元数据
	public static String generateInsertMetadataSQL(List<XLSHaderType> typeList, List<Map<String, String>> contentList,
			Integer sheetId, String tableName,Integer userId) {
		
		if(typeList.size()==0){
			return "select 1";
		}		
		if(contentList.size()==0){
			return "select 1";
		}
		

		int fieldType=2;
		
		StringBuffer metadataSQL = new StringBuffer();
		Map<String, String> headerMap = contentList.get(0);
		metadataSQL.append(
				"insert into ds_sheet_metadata(sheet_id,table_name,field_column,field_name_old,field_name_new,field_type,field_comment,is_visible,user_id) values ");
		for (int i = 0; i < headerMap.size(); i++) {
			
			
			if ("string".equals(typeList.get(i).getType())) {
				fieldType =2;
			} else if ("date".equals(typeList.get(i).getType())) {
				fieldType =3;
			} else if ("numeric".equals(typeList.get(i).getType())) {
				fieldType = 1;
			}
			
			metadataSQL.append("('" + sheetId + "','"+tableName+"','"+Constants.excelHader[i]+"','" + headerMap.get(Constants.excelHader[i]) + "','" + headerMap.get(Constants.excelHader[i]) + "',"+fieldType+",'','1','"+userId+"'),");
		}
		metadataSQL.delete(metadataSQL.lastIndexOf(","), metadataSQL.lastIndexOf(",") + 1);
		logger.info("metadataSQL ==== " + metadataSQL);

		return metadataSQL.toString();
	}

	// 动态插入数据
	public static String generateInsertTableSQL(List<Map<String, String>> contentList, String tableName) {
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
				insertSql.append("'").append(map.get(Constants.excelHader[i])).append("',");
			}
			insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
			insertSql.append("),");
		}
		insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
		logger.info("insertSql ==== " + insertSql);

		return insertSql.toString();
	}
	
}






