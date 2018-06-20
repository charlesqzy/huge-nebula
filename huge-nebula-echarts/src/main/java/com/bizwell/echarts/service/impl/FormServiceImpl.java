package com.bizwell.echarts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.common.QueryBulider;
import com.bizwell.echarts.mapper.EchartsMapper;
import com.bizwell.echarts.service.FormService;

/**
 * @author zhangjianjun
 * @date 2018年6月13日
 *
 */
@Service
public class FormServiceImpl implements FormService {
	
	@Autowired
	private EchartsMapper echartsMapper;

	@Override
	public List<Map<String, Object>> selectList(String data, Integer userId) {
		
		String sql = QueryBulider.getSql(data, userId);
		List<Map<String,Object>> list = echartsMapper.selectBySql(sql);
		return list;
	}

	@Override
	public Integer selectCnt(String data, Integer userId) {
		
		String sql = parseSql(QueryBulider.getSql(data, userId));
		Integer cnt = echartsMapper.selectCntBySql(sql);
		return cnt;
	}
	
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
		System.out.println(resultSql);
		return resultSql;
	}
	
}
