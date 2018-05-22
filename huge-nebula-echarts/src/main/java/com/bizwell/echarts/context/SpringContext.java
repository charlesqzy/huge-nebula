package com.bizwell.echarts.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhangjianjun
 * @date 2018年5月18日
 *
 */
@Component
public class SpringContext implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
		
	}
	
    public static Object getBean(Class<?> clazz){
        return getApplicationContext().getBean(clazz);
    }
	
}
