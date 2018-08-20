package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.ChartConfig;
import com.bizwell.echarts.bean.dto.ChartConfigParam;
import com.bizwell.echarts.bean.vo.ChartConfigVo;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.bean.vo.ResultLocation;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.ReportManager;
import com.bizwell.echarts.mapper.ChartConfigMapper;
import com.bizwell.echarts.service.ChartConfigService;
import com.bizwell.echarts.service.ReportService;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
// 图表配置信息service
@Service
public class ChartConfigServiceImpl implements ChartConfigService {
	
	@Autowired
	private ChartConfigMapper chartConfigMapper;

	// 保存图表信息
	@Transactional
	@Override
	public void save(ChartConfigParam param) {
		
		// 查询出改用户新建的仪表盘条数
		List<ChartConfig> list = chartConfigMapper.selectChartConfig(param.getPanelId(),param.getPanelUuid());
		Integer size = list.size();
		
		ChartConfig chartConfig = new ChartConfig();
		BeanUtils.copyProperties(param, chartConfig);
		chartConfig.setCreateTime(new Date());
		chartConfig.setUpdateTime(new Date());
		chartConfigMapper.insertSelective(chartConfig);
		
		Integer id = chartConfig.getId();
		// 设置位置信息		
		String location = getLocation(size, id);
		ChartConfig chartConfig1 = new ChartConfig();
		chartConfig1.setId(id);
		chartConfig1.setLocation(location);
		chartConfigMapper.updateByPrimaryKeySelective(chartConfig1);
	}

	// 查询位置信息
	@Override
	public ResultLocation selectLocation(Integer panelId,String panelUuid) {

		List<Object> resultList = new ArrayList<Object>();
		String status = null;
		String shareRemarks = null;
		Integer isHeaderShow =null;
		// 查询出改仪表盘下面所有配置信息
		List<ChartConfig> list = chartConfigMapper.selectChartConfig(panelId,panelUuid);
		for (ChartConfig chartConfig : list) {
			if (StringUtils.isEmpty(status)) {
				status = chartConfig.getReserved1();
			}
			if (StringUtils.isEmpty(shareRemarks)) {
				shareRemarks = chartConfig.getReserved2();
			}
			if(null==isHeaderShow){
				isHeaderShow=chartConfig.getIsHeaderShow();
			}
			
			JSONObject jsonObject = JSONObject.parseObject(chartConfig.getLocation());
			if (null != jsonObject) {
				String sqlConfig = chartConfig.getSqlConfig();
				String echartType = JsonUtils.getString(sqlConfig, "echartType");
				//JSONArray inChartFilter = JsonUtils.getJSONArray(sqlConfig, "inChartFilter");
				String chatData = getData(chartConfig.getSqlConfig(), chartConfig.getUserId());
				jsonObject.put("panelUuid", chartConfig.getPanelUuid());
				jsonObject.put("chartName", chartConfig.getChartName());
				jsonObject.put("chartRemarks", chartConfig.getChartRemarks());
				jsonObject.put("chatData", chatData);
				jsonObject.put("echartType", echartType);
				jsonObject.put("sqlConfig", JSONObject.parseObject(sqlConfig));
				jsonObject.put("connId", chartConfig.getConnId());
				jsonObject.put("databaseName", chartConfig.getDatabaseName());
				jsonObject.put("tableName", chartConfig.getTableName());
				resultList.add(jsonObject);				
			}
		}
		
		ResultLocation resultLocation = new ResultLocation();
		resultLocation.setStatus(status);
		resultLocation.setShareRemarks(shareRemarks);
		resultLocation.setLocations(resultList);
		resultLocation.setIsHeaderShow(isHeaderShow);
		return resultLocation;
	}

	// 获取单条配置信息
	@Override
	public ChartConfigVo getOne(Integer id) {

		ChartConfig chartConfig = chartConfigMapper.selectByPrimaryKey(id);
		ChartConfigVo chartConfigVo = new ChartConfigVo();
		BeanUtils.copyProperties(chartConfig, chartConfigVo);
		chartConfigVo.setType(chartConfig.getReserved1());
		return chartConfigVo;
	}

	// 更新配置信息
	@Transactional
	@Override
	public void update(ChartConfigParam param) {

		ChartConfig chartConfig = new ChartConfig();
		BeanUtils.copyProperties(param, chartConfig);
		chartConfig.setUpdateTime(new Date());
		chartConfigMapper.updateByPrimaryKeySelective(chartConfig);
	}
	
	// 更新位置信息
	@Transactional
	@Override
	public void updateLocation(List<String> locations) {
		
		for (String location : locations) {
			Integer id = JsonUtils.getInteger(location, "id");
			ChartConfig chartConfig = new ChartConfig();
			chartConfig.setId(id);
			chartConfig.setLocation(location);
			chartConfig.setUpdateTime(new Date());
			chartConfigMapper.updateByPrimaryKeySelective(chartConfig);
		}
	}
	
	// 删除配置信息
	@Override
	public void delete(Integer id) {
		
		chartConfigMapper.deleteByPrimaryKey(id);
	}
	
	// 获取数据
	private String getData(String json, Integer userId) {
		
		String code = JsonUtils.getString(json, "moduleType");
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		ReportService reportService = ReportManager.getService(code);
		ResultData resultData = reportService.selectEcharts(json, userId);
		String jsonString = JSON.toJSONString(resultData);
		return jsonString;
	}
	
	// 拼装位置坐标
	private String getLocation(Integer i, Integer id) {
		
		/*
			{"x":0,"y":0,"w":4,"h":2},   0
			{"x":4,"y":0,"w":4,"h":2},   1
			{"x":0,"y":2,"w":4,"h":2},   2
			{"x":4,"y":2,"w":4,"h":2},   3
			{"x":0,"y":4,"w":4,"h":2},   4
			{"x":4,"y":4,"w":4,"h":2},   5
		*/
		Integer x;
		if (i % 2 == 0) {
			x = 0;
		} else {
			x = 4;
		}
		Integer y = (i/2) * 2;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("'x':").append(x);
		sb.append(",'y':").append(y);
		sb.append(",'w':").append(4);
		sb.append(",'h':").append(2);
		sb.append(",'i':'").append(id);
		sb.append("','id':").append(id);
		sb.append("}");
		return sb.toString();
	}

}
