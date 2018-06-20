package com.bizwell.echarts.test;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bizwell.echarts.Application;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.context.SpringContext;
import com.bizwell.echarts.mapper.EchartsMapper;
import com.bizwell.echarts.service.ReportService;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = Application.class)
public class TestTable {
	
	@Autowired
	private EchartsMapper echartsMapper;
	
	@Test
	public void testTable() {
		
//		String sql = "select t.company_name from gb_user t group by t.company_name";
//		String sql = "select t.* from gb_user t order by t.create_time asc";
		String sql = "SELECT SUBSTR(t.A,1,10) AS A, SUM(t.I) AS I, SUM(t.K) AS K FROM xls_571bebf42840428bb73393264dd4d793_sheet_1 t GROUP BY t.A ORDER BY t.A ASC";
		
//		List<Map<String,Object>> list = echartsMapper.selectBySql(sql);
//		for (Map<String, Object> map : list) {
//			System.out.println(map);			
//		}
		
	}
	
	@Test
	public void testSql() {
		 String jsonString = "{" +
		            "\"dimension\": [{\"metadataId\": 827,\"dateLevel\": \"week\"}]," +
		            "\"measure1\": [{\"metadataId\": 835,\"aggregate\": \"sum\",\"color\": \"red\"}, " +
		            "{\"metadataId\": 837,\"aggregate\": \"count\",\"color\": \"green\"}]}";
		 
//		 ReportService reportService = (ReportService) SpringContext.getBean("01Service");
//		 ResultData data = reportService.selectEcharts(jsonString);
//		 System.out.println(data);
	}
	
	
	

}
