package com.bizwell.echarts.test;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bizwell.echarts.Application;
import com.bizwell.echarts.bean.domain.FolderInfo;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.mapper.FolderInfoMapper;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = Application.class)
public class TestDemo {
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	private JDBCService JDBCService;
	
	@Autowired
	private FolderInfoMapper folderInfoMapper;
	
	@Test
	public void testJDBC() throws SQLException {
		System.out.println("连接池 : " + dataSource);
		System.out.println("连接 : " + dataSource.getConnection());
		
	}
	
	@Test
	public void testUser() throws Exception {
		
//		JDBCService.getUser();
		
	}
	
	@Test
	public void testFolder() {
		
		List<FolderInfo> list = folderInfoMapper.selectFolder(2);
		for (FolderInfo folderInfo : list) {
			System.out.println(folderInfo);
		}
		String json = JsonUtils.toJson(list);
		System.out.println(json);
		
	}
	

}
