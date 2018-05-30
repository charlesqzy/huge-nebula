package com.bizwell.echarts.service;

import java.util.List;

import com.bizwell.echarts.bean.dto.ChartConfigParam;
import com.bizwell.echarts.bean.vo.ChartConfigVo;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
public interface ChartConfigService {
	
	public void save(ChartConfigParam param);
	
	public List<Object> selectLocation(Integer userId, Integer panelId);
	
	public ChartConfigVo getOne(Integer id);
	
	public void update(ChartConfigParam param);
	
	public void updateLocation(List<String> locations);
	
	public void delete(Integer id);
	
}
