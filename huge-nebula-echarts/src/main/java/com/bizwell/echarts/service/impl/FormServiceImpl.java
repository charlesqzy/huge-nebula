package com.bizwell.echarts.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.MysqlConnConf;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.mapper.EchartsMapper;
import com.bizwell.echarts.mapper.MysqlConnConfMapper;
import com.bizwell.echarts.service.AbstractReportService;
import com.bizwell.echarts.service.FormService;
import com.bizwell.echarts.service.JDBCService;
import com.bizwell.echarts.sql.QueryBulider;

/**
 * @author zhangjianjun
 * @date 2018年6月13日
 *
 */
// 表格service
@Service
public class FormServiceImpl implements FormService {
	
	private static Logger logger = LoggerFactory.getLogger(FormServiceImpl.class);

	
	@Autowired
	private EchartsMapper echartsMapper;
	@Autowired
	private MysqlConnConfMapper mysqlConnConfMapper;
	@Autowired
	private JDBCService jdbcService;

	// 查询出数据
	@Override
	public List<Map<String, Object>> selectList(String data,int start,int end) {
		List<Map<String,Object>> list = null;
		
		
		String sql = QueryBulider.getQueryString(data);
		logger.info("data=="+data + "\n selectEcharts.sql="+sql);
		
		
		int dataSourceType = JsonUtils.getInteger(data, "dataSourceType");
		if(dataSourceType==1){
			list = echartsMapper.selectBySql(sql + " LIMIT "+start+","+end);
		}else if(dataSourceType==2){
			MysqlConnConf connConf = new MysqlConnConf();
			connConf.setId(3);
			List<MysqlConnConf> connList = mysqlConnConfMapper.select(connConf);
			if(connList.size()>0){
				MysqlConnConf conf = connList.get(0);
				list=jdbcService.getMysqlTableData(conf.getDbUrl(), conf.getUsername(), conf.getPassword(), sql);
			}
		}
	
		return list;
	}

	// 查询总条数
	@Override
	public Integer selectCnt(String data) {
		
		Integer cnt = 0;
		
		String sql = QueryBulider.getQueryString(data);
		
		int dataSourceType = JsonUtils.getInteger(data, "dataSourceType");
		if(dataSourceType==1){
			cnt = echartsMapper.selectCntBySql(sql);
		}else if(dataSourceType==2){
			MysqlConnConf connConf = new MysqlConnConf();
			connConf.setId(3);
			List<MysqlConnConf> connList = mysqlConnConfMapper.select(connConf);
			if(connList.size()>0){
				MysqlConnConf conf = connList.get(0);
				cnt=jdbcService.getMysqlTableDataCount(conf.getDbUrl(), conf.getUsername(), conf.getPassword(), sql);
			}
		}
		
		return cnt;
	}
	
	// 解析sql
	private String parseSql(String sql) {
		
//		String sql = "SELECT DATE_FORMAT(C,'%Y-%m-%d') AS C, B, D, SUM(D) AS D, SUM(A) AS A "
//				+ "FROM xls_16d506e966a257c240adaed164fdbdcc_u16_s01 GROUP BY DATE_FORMAT(C, '%Y-%m-%d'), B, D";
		
		String[] split = sql.split("FROM");

		String str = new String();
		String[] split1 = split[0].trim().split(", ");
		for (int i = 0; i < split1.length; i++) {
			if (split1[i].contains("AS")) {
				split1[i] = split1[i] + "_bak";
			}
			str = str + split1[i] + ",";
		}
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		
		String resultSql = str + " FROM " + split[1].trim();
		//System.out.println(resultSql);
		return resultSql;
	}
	
}
