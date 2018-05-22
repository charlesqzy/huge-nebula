package com.bizwell.echarts.service;

import java.util.Map;

import com.bizwell.echarts.bean.domain.SheetMetaData;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
public interface SheetMetaDataService {
	
	public Map<Integer, SheetMetaData> loadProperty();

}
