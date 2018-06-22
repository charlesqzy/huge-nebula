package com.bizwell.echarts.service;

import com.bizwell.echarts.bean.vo.ResultData;

/**
 * @author zhangjianjun
 * @date 2018年5月18日
 *
 */
// 图表的顶层接口,定义查询图表数据的方法
public interface ReportService {
	
	public ResultData selectEcharts(String data, Integer userId);

}
