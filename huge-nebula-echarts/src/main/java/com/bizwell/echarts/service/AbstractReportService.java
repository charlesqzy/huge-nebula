package com.bizwell.echarts.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizwell.echarts.bean.MysqlConnConf;
import com.bizwell.echarts.bean.MysqlTableConf;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.mapper.EchartsMapper;
import com.bizwell.echarts.mapper.MysqlConnConfMapper;
import com.bizwell.echarts.mapper.MysqlTableConfMapper;
import com.bizwell.echarts.sql.QueryBulider;

/**
 * @author zhangjianjun
 * @date 2018年5月23日
 *
 */
// 抽象类,抽象出图表的执行流程
public abstract class AbstractReportService implements ReportService {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractReportService.class);

	
	@Autowired
	private EchartsMapper echartsMapper;
	
	@Autowired
	private MysqlConnConfMapper mysqlConnConfMapper;
	
	@Autowired
	private JDBCService jdbcService;


	
	@Override
	public ResultData selectEcharts(String data, Integer userId) {

		// 创建sql
		String sql = QueryBulider.getQueryString(data);
		logger.info("data=="+data + "\n selectEcharts.sql="+sql);
		
		
		int dataSourceType = JsonUtils.getInteger(data, "dataSourceType");
		
		// 查询出数据
		List<Map<String,Object>> list = null;
		
		//dataSourceType  1  文件   2 mysql
		if(dataSourceType==1){
			list = echartsMapper.selectBySql(sql);// 查询出数据
		}else if(dataSourceType==2){
			int connId = JsonUtils.getInteger(data, "connId");
			
			MysqlConnConf connConf = new MysqlConnConf();
			connConf.setId(connId);
			List<MysqlConnConf> connList = mysqlConnConfMapper.select(connConf);
			if(connList.size()>0){
				MysqlConnConf conf = connList.get(0);
				list=jdbcService.getMysqlTableData(conf.getDbUrl(), conf.getUsername(), conf.getPassword(), sql);
			}
		}
		
		// 封装各图表所需数据,为抽象方法,需要各个模板类自己实现,封装各自所需的数据格式
		return this.setupData(list, data, userId);
	}
	
	

	
	protected abstract ResultData setupData(List<Map<String,Object>> list, String data, Integer userId);

}
