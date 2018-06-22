package com.bizwell.echarts.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
// 系统启动时,用于将表中数据加载到内存中
@Component
public class MetaDataMap implements ApplicationRunner {
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;
	
	private static ConcurrentMap<Integer,ConcurrentMap<Integer,SheetMetaData>> META_DATA_MAP = new ConcurrentHashMap<Integer, ConcurrentMap<Integer, SheetMetaData>>();
	
	// 从map中获取数据
	public static SheetMetaData get(Integer userId, Integer id) {
		
		ConcurrentMap<Integer,SheetMetaData> map = META_DATA_MAP.get(userId);
		return map.get(id);
	}
	
	// 往map中存放数据
	public static void put(Integer userId, ConcurrentMap<Integer,SheetMetaData> map) {
		
		META_DATA_MAP.put(userId, map);
	}
	
	
	// 系统启动时自动执行一次
	@Override
	public void run(ApplicationArguments args) throws Exception {

		META_DATA_MAP = sheetMetaDataService.loadProperty();
	}

}
