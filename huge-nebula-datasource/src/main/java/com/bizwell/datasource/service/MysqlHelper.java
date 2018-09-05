package com.bizwell.datasource.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.execchain.MainClientExec;
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
	public static String generateCreateTableSQL(List<XLSHaderType> typeList, String tableName) {

		if (typeList.size() == 0) {
			return "select 1";
		}

		StringBuffer createSql = new StringBuffer();
		createSql.append("create table ").append(tableName).append("(");
		for (XLSHaderType type : typeList) {
			createSql.append(type.getProp());
			if  ("3".equals(type.getType())) {
				createSql.append(" datetime ,");
			} else if ("1".equals(type.getType())) {
				createSql.append(" double ,");
			}else{//("2".equals(type.getType())) {
				createSql.append(" varchar(200) ,");
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
	 * 
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
			Integer sheetId, String tableName, Integer userId) {

		if (typeList.size() == 0) {
			return "select 1";
		}
		if (contentList.size() == 0) {
			return "select 1";
		}

		int fieldType = 2;

		StringBuffer metadataSQL = new StringBuffer();
		Map<String, String> headerMap = contentList.get(0);
		metadataSQL.append(
				"insert into ds_sheet_metadata(sheet_id,table_name,field_column,field_name_old,field_name_new,field_type,field_comment,is_visible,user_id) values ");
		for (int i = 0; i < headerMap.size(); i++) {

			if ("2".equals(typeList.get(i).getType())) {
				fieldType = 2;
			} else if ("3".equals(typeList.get(i).getType())) {
				fieldType = 3;
			} else if ("1".equals(typeList.get(i).getType())) {
				fieldType = 1;
			}

			metadataSQL.append("('" + sheetId + "','" + tableName + "','" + Constants.excelHader[i] + "','"
					+ headerMap.get(Constants.excelHader[i]) + "','" + headerMap.get(Constants.excelHader[i]) + "',"
					+ fieldType + ",'','1','" + userId + "'),");
		}
		metadataSQL.delete(metadataSQL.lastIndexOf(","), metadataSQL.lastIndexOf(",") + 1);
		logger.info("metadataSQL ==== " + metadataSQL);

		return metadataSQL.toString();
	}

	// 动态插入数据
	public static String[] generateInsertTableSQL(List<Map<String, String>> contentList, String tableName) {

		
		//数据量过大时，需要分批插入
		int pageSize=5000;
		//int pageCount=size/pageSize+1;
		
		List<List<Map<String, String>>> averageAssign = splitList(contentList.subList(1, contentList.size()), pageSize);
		
		
		String[] insertSqls = new String[averageAssign.size()];
		
		StringBuffer insertSql;
		
		int s = 0;
		for(List<Map<String, String>> content: averageAssign){
			insertSql = new StringBuffer();
			if (content.size() == 0) {
				insertSql.append("select 1");
			}else{
				insertSql.append(" insert into ").append(tableName);
				insertSql.append(" values  ");
				for (int k = 0; k < content.size(); k++) {
					Map<String, String> map = content.get(k);
					insertSql.append("(");
					for (int i = 0; i < map.size(); i++) {
						String value = map.get(Constants.excelHader[i]);
						if (StringUtils.isEmpty(value)) {
							insertSql.append("null,");
						} else {
							insertSql.append("'").append(value).append("',");
						}
					}
					if(insertSql.lastIndexOf(",")>0)
					insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
					insertSql.append("),");
				}
				if(insertSql.lastIndexOf(",")>0)				
				insertSql.delete(insertSql.lastIndexOf(","), insertSql.lastIndexOf(",") + 1);
				logger.info("insertSql ====2== " + insertSql.toString());
			}
			insertSqls[s++] = insertSql.toString();			
		}
		


		return insertSqls;
	}
	
	
	

     public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        
        int listSize = list.size();                                                           //list的大小
        int page = (listSize + (pageSize-1))/ pageSize;                      //页数
        
        List<List<T>> listArray = new ArrayList<List<T>>();              //创建list数组 ,用来保存分割后的list
        for(int i=0;i<page;i++) {                                                         //按照数组大小遍历
            List<T> subList = new ArrayList<T>();                               //数组每一位放入一个分割后的list
            for(int j=0;j<listSize;j++) {                                                 //遍历待分割的list
                int pageIndex = ( (j + 1) + (pageSize-1) ) / pageSize;   //当前记录的页码(第几页)
                if(pageIndex == (i + 1)) {                                               //当前记录的页码等于要放入的页码时
                    subList.add(list.get(j));                                               //放入list中的元素到分割后的list(subList)
                }
                
                if( (j + 1) == ((j + 1) * pageSize) ) {                               //当放满一页时退出当前循环
                    break;
                }
            }
            listArray.add(subList);                                                         //将分割后的list放入对应的数组的位中
        }
        return listArray;
    }
	

	public static void main(String[] args) {
		List<Integer> dataList = new ArrayList<Integer>();
		for (int i = 0; i < 102; i++)
			dataList.add(i);
		
		
		
		int n = 50;
		List<List<Integer>> averageAssign = splitList(dataList,n);
	
		System.out.println(averageAssign.size());
		
		
		for (List<Integer> list : averageAssign) {
			System.out.println("----------------");
			for (Integer v : list) {
				System.out.println(v);
			}
			
		}

	}

}
