package com.bizwell.echarts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.mapper.EchartsMapper;
import com.bizwell.echarts.mapper.SheetMetaDataMapper;
import com.bizwell.echarts.sql.QueryBulider;

/**
 * @author zhangjianjun
 * @date 2018年5月23日
 *
 */
// 抽象类,抽象出图表的执行流程
public abstract class AbstractReportService implements ReportService {
	
	@Autowired
	private EchartsMapper echartsMapper;


	
	@Override
	public ResultData selectEcharts(String data, Integer userId) {

		// 创建sql
		//String sql = QueryBulider.getSql(data, userId);
		String sql = QueryBulider.getQueryString(data);
		System.out.println("selectEcharts.sql="+sql);
		// 查询出数据
		List<Map<String,Object>> list = echartsMapper.selectBySql(sql);
		// 封装各图表所需数据,为抽象方法,需要各个模板类自己实现,封装各自所需的数据格式
		return this.setupData(list, data, userId);
	}
	
	

	
	protected abstract ResultData setupData(List<Map<String,Object>> list, String data, Integer userId);

}
