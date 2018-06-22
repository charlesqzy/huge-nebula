package com.bizwell.echarts.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.QueryBulider;
import com.bizwell.echarts.mapper.EchartsMapper;

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
		String sql = QueryBulider.getSql(data, userId);
		// 查询出数据
		List<Map<String,Object>> list = echartsMapper.selectBySql(sql);
		// 封装各图表所需数据,为抽象方法,需要各个模板类自己实现,封装各自所需的数据格式
		return this.setupData(list, data, userId);
	}
	
	protected abstract ResultData setupData(List<Map<String,Object>> list, String data, Integer userId);

}
