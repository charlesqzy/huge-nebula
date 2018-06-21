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
@Component
public class MetaDataMap implements ApplicationRunner {
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;
	
	private static ConcurrentMap<Integer,ConcurrentMap<Integer,SheetMetaData>> META_DATA_MAP = new ConcurrentHashMap<Integer, ConcurrentMap<Integer, SheetMetaData>>();
	
	public static SheetMetaData get(Integer userId, Integer id) {
		
		ConcurrentMap<Integer,SheetMetaData> map = META_DATA_MAP.get(userId);
		return map.get(id);
	}
	
	public static void put(Integer userId, ConcurrentMap<Integer,SheetMetaData> map) {
		
		META_DATA_MAP.put(userId, map);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		META_DATA_MAP = sheetMetaDataService.loadProperty();
	}

}
