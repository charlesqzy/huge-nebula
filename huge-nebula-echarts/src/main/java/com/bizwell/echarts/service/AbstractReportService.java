package com.bizwell.echarts.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.QueryBulider;
import com.bizwell.echarts.mapper.EchartsMapper;

public abstract class AbstractReportService implements ReportService {
	
	@Autowired
	private EchartsMapper echartsMapper;

	@Override
	public ResultData selectEcharts(String data) {

		String sql = QueryBulider.getSql(data);
//		String sql = "SELECT SUBSTR(t.A,1,10) AS A, SUM(t.I) AS I, SUM(t.K) AS K FROM xls_571bebf42840428bb73393264dd4d793_sheet_1 t GROUP BY t.A ORDER BY t.A ASC";
		
		List<Map<String,Object>> list = echartsMapper.selectBySql(sql);
		return this.setupData(list, data);
	}
	
	protected abstract ResultData setupData(List<Map<String,Object>> list, String data);

}
