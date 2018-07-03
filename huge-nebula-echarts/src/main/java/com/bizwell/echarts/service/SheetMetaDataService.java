package com.bizwell.echarts.service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.bizwell.echarts.bean.domain.SheetMetaData;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
public interface SheetMetaDataService {
	
	public List<SheetMetaData> getFields(String data, String field, String metadataId);
	
	public ConcurrentMap<Integer,ConcurrentMap<Integer,SheetMetaData>> loadProperty();

	public void refresh(Integer userId);
}
