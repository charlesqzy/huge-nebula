package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.MysqlConnConf;
import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.mapper.MysqlConnConfMapper;
//import com.bizwell.echarts.common.MetaDataMap;
import com.bizwell.echarts.mapper.SheetMetaDataMapper;
import com.bizwell.echarts.service.JDBCService;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
// 表格源数据service
@Service
public class SheetMetaDataServiceImpl implements SheetMetaDataService {

	@Autowired
	private SheetMetaDataMapper sheetMetaDataMapper;
	
	@Autowired
	private MysqlConnConfMapper mysqlConnConfMapper;
	
	@Autowired
	private JDBCService jdbcService;
	
	
	public List<SheetMetaData> getFields(String data, String field, String metadataId) {
		
		
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		SheetMetaData meteData=null;
		
		
		int dataSourceType = JsonUtils.getInteger(data, "dataSourceType");
		
		//dataSourceType  1  文件   2 mysql
		if(dataSourceType==1){
			List<Integer> ids = new ArrayList<Integer>();
			JSONObject jsonObject = JSONObject.parseObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray(field);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				Integer id = object.getIntValue(metadataId);
				ids.add(id);
			}
			
			if(ids.size()>0){
				list = sheetMetaDataMapper.selectByIds(ids);
			}
		}else if(dataSourceType==2){
			
			JSONObject jsonObject = JSONObject.parseObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray(field);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				
				String connId = object.getString("connId");
				String databaseName = object.getString("databaseName");
				String tableName = object.getString("tableName");
				String fieldColumn = object.getString("fieldColumn");
				
				
				MysqlConnConf connConf = new MysqlConnConf();
				connConf.setId(3);
				List<MysqlConnConf> connList = mysqlConnConfMapper.select(connConf);
				if(connList.size()>0){
					MysqlConnConf conf = connList.get(0);
					meteData=jdbcService.getMysqlTableMetaData(conf.getDbUrl(), conf.getUsername(), conf.getPassword(), connId,databaseName,tableName,fieldColumn);
					list.add(meteData);
				}
				
			}
			
			
			
	
		}
		

		
		return list;
	}
	
	
}
