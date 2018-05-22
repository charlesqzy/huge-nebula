package com.bizwell.echarts.common;

import java.util.HashMap;
import java.util.Map;

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
	
	private static Map<Integer, SheetMetaData> META_DATA_MAP = new HashMap<Integer, SheetMetaData>();

	public static SheetMetaData get(Integer id) {
		
		return META_DATA_MAP.get(id);		
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		META_DATA_MAP = sheetMetaDataService.loadProperty();
	}

}
