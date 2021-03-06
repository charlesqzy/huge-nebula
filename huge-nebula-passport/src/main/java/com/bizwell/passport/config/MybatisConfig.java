package com.bizwell.passport.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bizwell.passport.common.Constants;
import com.bizwell.passport.common.DataSourceHelp;
import com.bizwell.passport.common.DruidProperties;
import com.bizwell.passport.common.MybatisHelp;
import com.github.pagehelper.PageHelper;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// Mybatis配置类
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@MapperScan(basePackages = Constants.MAPPER_PATH, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class MybatisConfig {
	
	@Autowired
	private DruidProperties druidProperties;
	
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() {
		
		DataSource dataSource = DataSourceHelp.getDataSource(druidProperties);
		return dataSource;
	}
	
	@Bean(name = "sqlSessionFactory")
	@Primary
    public SqlSessionFactory sqlSessionFactoryBean() {
		
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setTypeAliasesPackage(Constants.DO_MAIN_PATH);

        //添加插件
        PageHelper pageHelper = MybatisHelp.getPageHelper();
        bean.setPlugins(new Interceptor[]{pageHelper});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources(Constants.MAPPER_XML_PATH));
            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	@Bean
	@Primary
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
//    @Bean
//    @Primary
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//    	return new SqlSessionTemplate(sqlSessionFactory);
//    }
	
}
